package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.*;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.Bootstrapper;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.tooling.GlobalGraphOperations;
import org.openpcf.neo4vertx.Complex;
import org.openpcf.neo4vertx.Graph;
import org.openpcf.neo4vertx.Handler;
import org.openpcf.neo4vertx.Nodes;
import org.openpcf.neo4vertx.Relationships;
import org.vertx.java.busmods.graph.neo4j.Configuration;
import org.vertx.java.core.json.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * The Neo4jGraph object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public class Neo4jGraph implements Graph {

    public final static String EMBEDDED_MODE = "embedded";
    public final static String EMBEDDED_HA_MODE = "embedded-ha";
    public final static String REMOTE_MODE = "remote";
    public final static String EMBEDDED_GUI_MODE = "embedded-with-gui";

    private GraphDatabaseService graphDatabaseService;
    private Bootstrapper bootStrapper;
    private Nodes nodes;
    private Relationships relationships;
    private Complex complex;
    private ExecutionEngine executionEngine;

    /**
     * Create an embedded Neo4j graph instance
     *
     * @param configuration
     *            configuration for the graph
     */
    public Neo4jGraph(Configuration configuration) {
        initialize(configuration, null);
    }

    /**
     * Create an embedded Neo4j graph instance
     *
     * @param configuration
     *            configuration for the graph
     * @param customServiceFactory
     *            factory that should be used regardless of the factory that would otherwise be chosen by
     *            examining the configuration object.
     */
    public Neo4jGraph(Configuration configuration, GraphDatabaseServiceFactory customServiceFactory) {
        initialize(configuration, customServiceFactory);
    }

    /**
     * Initialize the graph using the given configuration settings. The customServiceFactory will be utilized when specified.
     * @param customServiceFactory Custom service factory
     * @param conf neo4j module configuration
     */
    private void initialize(Configuration conf, GraphDatabaseServiceFactory customServiceFactory) {
        final String mode = conf.getMode();

        GraphDatabaseServiceFactory neo4jServiceFactory = customServiceFactory;
        // Check for the HA mode. In case of HA we need to utilize a different
        // service factory. Don't overwrite the customServiceFactory when it has been set
        if (neo4jServiceFactory == null && EMBEDDED_HA_MODE.equalsIgnoreCase(mode)) {
            neo4jServiceFactory = new Neo4jGraphDatabaseHAServiceFactory();
        } else if (neo4jServiceFactory == null) {
            neo4jServiceFactory = new Neo4jGraphDatabaseServiceFactory();
        }

        graphDatabaseService = neo4jServiceFactory.create(conf);

        switch (mode) {
        case EMBEDDED_HA_MODE:
            break;
        case EMBEDDED_MODE:
            break;
        case EMBEDDED_GUI_MODE:
            bootStrapper = new WrappingNeoServerBootstrapper((GraphDatabaseAPI)graphDatabaseService);
            bootStrapper.start();
            break;
        case REMOTE_MODE:
            break;
        default:
            break;
        }

        Finder finder = new Finder(graphDatabaseService, conf.getAlternateNodeIdField(), conf.getAlternateRelationshipIdField());
        nodes = new Neo4jNodes(graphDatabaseService, finder);
        relationships = new Neo4jRelationships(graphDatabaseService, finder);
        complex = new Neo4jComplex(graphDatabaseService, finder);
        executionEngine = new ExecutionEngine(graphDatabaseService);
    }

    @Override
    public Nodes nodes() {
        return nodes;
    }

    @Override
    public Relationships relationships() {
        return relationships;
    }

    @Override
    public Complex complex() {
        return complex;
    }

    @Override
    public void query(JsonObject request, Handler<String> handler) throws Exception {

        String url = "http://localhost:7474/db/data/cypher";

        URL obj = new URL(url);
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

        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + request.toString());
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ( (inputLine = in.readLine()) != null ) {
            response.append(inputLine);
        }
        in.close();

       handler.handle(response.toString());
    }

    @Override
    public void clear(final Handler<Boolean> handler) throws Exception {
        GlobalGraphOperations globalGraphOperations = GlobalGraphOperations.at(graphDatabaseService);
        Transaction transaction = graphDatabaseService.beginTx();
        try {
            for (Relationship relationship : globalGraphOperations.getAllRelationships()) {
                relationship.delete();
            }

            for (Node node : globalGraphOperations.getAllNodes()) {
                node.delete();
            }

            transaction.success();

            handler.handle(true);
        } catch (Exception exception) {
            transaction.failure();
            throw exception;
        } finally {
            transaction.close();
        }
    }

    @Override
    public void shutdown() {
        if (bootStrapper != null ) {
            bootStrapper.stop();
        }

        graphDatabaseService.shutdown();
    }

}
