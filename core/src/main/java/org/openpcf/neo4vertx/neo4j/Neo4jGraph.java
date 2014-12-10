package org.openpcf.neo4vertx.neo4j;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.graph.neo4j.Configuration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.Bootstrapper;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.server.rest.repr.BadInputException;
import org.neo4j.server.rest.repr.CypherResultRepresentation;
import org.neo4j.server.rest.repr.OutputFormat;
import org.neo4j.server.rest.repr.formats.JsonFormat;
import org.openpcf.neo4vertx.Graph;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * The Neo4jGraph object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/jotschi[Johannes Schüth]
 * @author https://github.com/fraik[Freek Alleman]
 */
public class Neo4jGraph implements Graph {

    public final static String DEFAULT_MODE = "default";
    public final static String CLUSTER_MODE = "cluster";
    public final static String REMOTE_MODE = "remote";
    public final static String GUI_MODE = "gui";

    private Bootstrapper bootStrapper;
    private GraphDatabaseService graphDatabaseService;
    private String restUrl;
    private Configuration configuration;

    /**
     * Create a Neo4j graph instance.
     *
     * @param conf
     *            configuration for the graph.
     * @throws Exception
     */
    public Neo4jGraph(Configuration conf) throws Exception {
        configuration = conf;
        initialize();
    }

    /**
     * Initialize the graph using the given configuration settings. The customServiceFactory will be utilized when specified.
     * 
     * @throws Exception
     */
    private void initialize() throws Exception {
        final String mode = configuration.getMode();
        this.restUrl = configuration.getRestUrl();

        switch (mode) {
        case DEFAULT_MODE:
            graphDatabaseService = new Neo4jGraphDatabaseServiceFactory().create(configuration);
            break;
        case CLUSTER_MODE:
            graphDatabaseService = new Neo4jGraphDatabaseHAServiceFactory().create(configuration);
            break;
        case GUI_MODE:
            graphDatabaseService = new Neo4jGraphDatabaseServiceFactory().create(configuration);
            bootStrapper = new WrappingNeoServerBootstrapper((GraphDatabaseAPI) graphDatabaseService);
            bootStrapper.start();
            break;
        case REMOTE_MODE:
            break;
        default:
            throw new Exception("Invalid mode " + mode + " specified");
        }
    }

    @Override
    public JsonObject query(JsonObject request) throws Exception {
        final String mode = configuration.getMode();
        switch (mode) {
        case DEFAULT_MODE:
        case CLUSTER_MODE:
        case GUI_MODE:
            return internalQuery(request);
        case REMOTE_MODE:
            return remoteQuery(request);
        default:
            throw new Exception("Invalid mode " + mode + " specified");
        }
    }

    /**
     * Query to the embedded neo4j database.
     * 
     * @param request
     * @return
     * @throws Exception
     */
    private JsonObject internalQuery(JsonObject request) throws Exception {
        ExecutionEngine engine = new ExecutionEngine(graphDatabaseService);
        ExecutionResult result;
        try (Transaction tx = graphDatabaseService.beginTx()) {
            result = engine.execute(request.getString("query"));
            String json = transformExecutionResult(result);
            JsonObject response = new JsonObject(json);
            //sanitizeJson(response);

            tx.success();
            return response;
        } catch (Exception e) {
            throw new Exception("Error while evaluating query: {" + request.getString("query") + "}", e);
        }
    }

    /**
     * Removes various bogus attributes which are not useful when executing queries in internal mode.
     * 
     * @param response
     */
    private void sanitizeJson(JsonObject response) {
        List<String> keysToBeRemoved = new ArrayList<String>();
        for (String key : response.getJsonArray("data").getJsonArray(0).getJsonObject(0).fieldNames()) {
            if ("data".equalsIgnoreCase(key)) {
                continue;
            } else {
                keysToBeRemoved.add(key);
            }
        }
        for (String key : keysToBeRemoved) {
            response.getJsonArray("data").getJsonArray(0).getJsonObject(0).remove(key);
        }

    }

    private String transformExecutionResult(ExecutionResult result) throws JsonProcessingException, BadInputException, URISyntaxException {
        CypherResultRepresentation repr = new CypherResultRepresentation(result, false, false);
        OutputFormat format = new OutputFormat(new JsonFormat(), new URI("http://localhost:8000"), null);
        return format.assemble(repr);
    }

    /**
     * Remote query the neo4j using the restUrl parameter
     *
     * @param request
     *            The JSON request object (the Cypher REST query in JSON format).
     */
    private JsonObject remoteQuery(JsonObject request) throws Exception {

        URL obj = new URL(restUrl);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/1.0");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(request.toString());
        wr.flush();
        wr.close();

        int responseCode = connection.getResponseCode();

        // TODO fix logging
        // System.out.println("\nSending 'POST' request to URL : " + restUrl);
        // System.out.println("Post parameters : " + request.toString());
        // System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JsonObject(response.toString());
    }

    /**
     * Returns the neo4j database service instance
     * @return
     */
    public GraphDatabaseService getGraphDatabaseService() {
        return graphDatabaseService;
    }

    @Override
    public void shutdown() {
        if (bootStrapper != null) {
            bootStrapper.stop();
        }

        if (graphDatabaseService != null) {
            graphDatabaseService.shutdown();
        }
    }
}
