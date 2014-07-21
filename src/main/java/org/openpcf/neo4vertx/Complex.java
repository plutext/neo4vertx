package org.openpcf.neo4vertx;

import java.util.Map;
import java.util.Set;

/**
 * The Complex interface.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public interface Complex {

    public void fetchAllRelatedNodes(Object nodeId, String name, String direction, Handler<Iterable<Map<String, Object>>> handler);

    public void resetNodeRelationships(
        Object nodeId, String name, Set<Object> targetIds, Handler<ComplexResetNodeRelationshipsResult> handler) throws Exception;

}
