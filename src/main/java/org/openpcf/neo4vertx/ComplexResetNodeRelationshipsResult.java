package org.openpcf.neo4vertx;

import java.util.Set;

/**
 * The ComplexResetNodeRelationshipsResult object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public class ComplexResetNodeRelationshipsResult {

    public ComplexResetNodeRelationshipsResult(
        Set<Object> addedNodeIds,
        Set<Object> removedNodeIds,
        Set<Object> notFoundNodeIds) {

        this.addedNodeIds = addedNodeIds;
        this.removedNodeIds = removedNodeIds;
        this.notFoundNodeIds = notFoundNodeIds;
    }

    public Set<Object> addedNodeIds;

    public Set<Object> removedNodeIds;

    public Set<Object> notFoundNodeIds;

}
