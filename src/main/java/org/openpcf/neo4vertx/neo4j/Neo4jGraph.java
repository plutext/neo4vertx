package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.*;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.Bootstrapper;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.openpcf.neo4vertx.Graph;
import org.openpcf.neo4vertx.Handler;
import org.vertx.java.busmods.graph.neo4j.Configuration;
import org.vertx.java.core.json.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The Neo4jGraph object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/jotschi[Johannes Schüth]
 */
public class Neo4jGraph implements Graph {

    public final static String DEFAULT_MODE = "default";
    public final static String CLUSTER_MODE = "cluster";
    public final static String REMOTE_MODE = "remote";

    private Bootstrapper bootStrapper;
    private GraphDatabaseService graphDatabaseService;
    private String restUrl;

    /**
     * Create a Neo4j graph instance.
     *
     * @param conf configuration for the graph.
     */
    public Neo4jGraph(Configuration conf) {
        initialize(conf);
    }

    /**
     * Initialize the graph using the given configuration settings.
     * The customServiceFactory will be utilized when specified.
     *
     * @param conf neo4j module configuration.
     */
    private void initialize(Configuration conf) {
        final String mode = conf.getMode();
        this.restUrl = conf.getRestUrl();

        try {
            switch (mode) {
                case DEFAULT_MODE:
                    graphDatabaseService = new Neo4jGraphDatabaseServiceFactory().create(conf);
                    bootStrapper = new WrappingNeoServerBootstrapper((GraphDatabaseAPI) graphDatabaseService);
                    bootStrapper.start();
                    break;
                case CLUSTER_MODE:
                    graphDatabaseService = new Neo4jGraphDatabaseHAServiceFactory().create(conf);
                    bootStrapper = new WrappingNeoServerBootstrapper((GraphDatabaseAPI) graphDatabaseService);
                    bootStrapper.start();
                    break;
                case REMOTE_MODE:
                    break;
                default:
                    throw new Exception("Invalid mode " + mode + " specified");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * The query method.
     *
     * @param request The JSON request object (the Cypher REST query in JSON format).
     * @param handler The response handler.
     */
    @Override
    public void query(JsonObject request, Handler<JsonObject> handler) throws Exception {

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

        System.out.println("\nSending 'POST' request to URL : " + restUrl);
        System.out.println("Post parameters : " + request.toString());
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        handler.handle(new JsonObject(response.toString()));
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
