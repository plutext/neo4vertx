package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;
import org.openpcf.neo4vertx.*;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.server.Bootstrapper;
import org.vertx.java.busmods.graph.neo4j.Configuration;

/**
 * The Neo4jGraph object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @author Rubin Simons <rubin.simons@raaftech.com>
 * @since 2012-12-13
 * @version 1.1.1
 */
public class Neo4jGraph implements Graph {

    private GraphDatabaseService graphDatabaseService;
    private Bootstrapper bootStrapper;
    private Nodes nodes;
    private Relationships relationships;
    private Complex complex;

    public Neo4jGraph(Configuration configuration) {
        this(configuration.getMode(), configuration.getPath(), configuration.getAlternateNodeIdField(), configuration.getAlternateRelationshipIdField(), new Neo4jGraphDatabaseServiceFactory());
    }

    public Neo4jGraph(String path) {
        this("embedded", path, null, null, new Neo4jGraphDatabaseServiceFactory());
    }

    public Neo4jGraph(String path, String alternateNodeIdField) {
        this("embedded", path, alternateNodeIdField, null, new Neo4jGraphDatabaseServiceFactory());
    }

    public Neo4jGraph(String path, String alternateNodeIdField, String alternateRelationshipIdField) {
        this("embedded", path, alternateNodeIdField, alternateRelationshipIdField, new Neo4jGraphDatabaseServiceFactory());
    }

    public Neo4jGraph(String path, String alternateNodeIdField, String alternateRelationshipIdField, GraphDatabaseServiceFactory graphDatabaseServiceFactory) {
        this("embedded", path, alternateNodeIdField, alternateRelationshipIdField, new Neo4jGraphDatabaseServiceFactory());
    }

    public Neo4jGraph(String mode, String path, String alternateNodeIdField, String alternateRelationshipIdField, GraphDatabaseServiceFactory graphDatabaseServiceFactory) {

        graphDatabaseService = graphDatabaseServiceFactory.create(path);

        switch (mode) {
            case "embedded":
                break;
            case "embedded-with-gui":
                bootStrapper = new WrappingNeoServerBootstrapper((GraphDatabaseAPI)graphDatabaseService);
                bootStrapper.start();
                break;
            case "remote":
                break;
            default:
                break;
        }

        Finder finder = new Finder(graphDatabaseService, alternateNodeIdField, alternateRelationshipIdField);
        nodes = new Neo4jNodes(graphDatabaseService, finder);
        relationships = new Neo4jRelationships(graphDatabaseService, finder);
        complex = new Neo4jComplex(graphDatabaseService, finder);
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
