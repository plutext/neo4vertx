package org.openpcf.neo4vertx;

import org.vertx.java.core.json.JsonObject;

/**
 * The Graph interface.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 */
public interface Graph {
    public void query(JsonObject request, Handler<JsonObject> handler) throws Exception;
    public void shutdown();
}
