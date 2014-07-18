package org.openpcf.neo4vertx.neo4j;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.openpcf.neo4vertx.Handler;
import org.openpcf.neo4vertx.Nodes;

/**
 * The Neo4jNodes object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @author Rubin Simons <rubin.simons@raaftech.com>
 */
public class Neo4jNodes implements Nodes {

    private final GraphDatabaseService graphDatabaseService;
    private final Finder finder;

    public Neo4jNodes(GraphDatabaseService graphDatabaseService, Finder finder) {
        this.graphDatabaseService = graphDatabaseService;
        this.finder = finder;
    }

    @Override
    public void create(Map<String, Object> properties, Handler<Object> handler) throws Exception {
        Transaction transaction = graphDatabaseService.beginTx();
        try {
            Node node = graphDatabaseService.createNode();
            PropertyHandler.setProperties(node, properties);
            transaction.success();
            handler.handle(node.getId());
        } catch (Exception exception) {
            transaction.failure();
            throw exception;
        } finally {
            transaction.close();
        }
    }

    @Override
    public void update(Object id, Map<String, Object> properties, Handler<Boolean> handler) throws Exception {
        Transaction transaction = graphDatabaseService.beginTx();
        try {
            Node node = finder.getNode(id);
            PropertyHandler.setProperties(node, properties);
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
    public void fetch(Object id, Handler<Map<String, Object>> handler) {
        Transaction transaction = graphDatabaseService.beginTx();
        try {
            Node node = finder.getNode(id);
            if (node == null) {
                handler.handle(null);
            } else {
                handler.handle(PropertyHandler.getProperties(node));
            }

            transaction.success();

        } catch (Exception exception) {
            transaction.failure();
            throw exception;
        } finally {
            transaction.close();
        }
    }

    @Override
    public void remove(Object id, Handler<Boolean> handler) throws Exception {
        Transaction transaction = graphDatabaseService.beginTx();
        try {
            Node node = finder.getNode(id);
            node.delete();
            transaction.success();
            handler.handle(true);
        } catch (Exception exception) {
            transaction.failure();
            throw exception;
        } finally {
            transaction.close();
        }
    }

}
