package org.openpcf.neo4vertx;

import java.util.Map;

/**
 * The Nodes interface.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @since 2012-12-13
 * @version 1.0.0
 */
public interface Nodes {

    public void create(Map<String, Object> properties, Handler<Object> handler) throws Exception;

    public void update(Object id, Map<String, Object> properties, Handler<Boolean> handler) throws Exception;

    public void fetch(Object id, Handler<Map<String, Object>> handler);

    public void remove(Object id, Handler<Boolean> handler) throws Exception;

}