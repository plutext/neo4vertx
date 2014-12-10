package org.openpcf.neo4vertx;

import io.vertx.core.json.JsonObject;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * The Graph interface.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 */
public interface Graph {
    
    public GraphDatabaseService getGraphDatabaseService();

    public JsonObject query(JsonObject request) throws Exception;

    public void shutdown();
}
