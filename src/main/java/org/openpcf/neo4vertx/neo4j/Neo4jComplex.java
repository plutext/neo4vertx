package org.openpcf.neo4vertx.neo4j;

import org.openpcf.neo4vertx.Complex;
import org.openpcf.neo4vertx.ComplexResetNodeRelationshipsResult;
import org.openpcf.neo4vertx.Handler;
import org.neo4j.graphdb.*;

import java.util.*;

/**
 * The Neo4jComplex object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @author Rubin Simons <rubin.simons@raaftech.com>
 * @since 2012-12-13
 * @version 1.1.1
 */
public class Neo4jComplex implements Complex {

    private final GraphDatabaseService graphDatabaseService;
    private final Finder finder;

    public Neo4jComplex(GraphDatabaseService graphDatabaseService, Finder finder) {
        this.graphDatabaseService = graphDatabaseService;
        this.finder = finder;
    }

    @Override
    public void fetchAllRelatedNodes(Object nodeId, String relationshipName, String directionName, final Handler<Iterable<Map<String, Object>>> handler) {

        Transaction transaction = graphDatabaseService.beginTx();
        final Node node;
        try {
            node = finder.getNode(nodeId);
            RelationshipType relationshipType = DynamicRelationshipType.forName(relationshipName);
            Direction direction = Direction.valueOf(directionName.toUpperCase());

            final Iterable<Relationship> relationships = node.getRelationships(relationshipType, direction);
            handler.handle(new Iterable<Map<String, Object>>() {

                @Override
                public Iterator<Map<String, Object>> iterator() {
                    final Iterator<Relationship> iterator = relationships.iterator();
                    return new Iterator<Map<String, Object>>() {
                        @Override
                        public boolean hasNext() {
                            return iterator.hasNext();
                        }

                        @Override
                        public Map<String, Object> next() {
                            Relationship relationship = iterator.next();
                            Transaction transaction = graphDatabaseService.beginTx();
                            try {
                                Node targetNode = relationship.getOtherNode(node);
                                transaction.success();
                                return PropertyHandler.getProperties(targetNode);
                            } catch (Exception exception) {
                                transaction.failure();
                                throw exception;
                            } finally {
                                transaction.close();
                            }


                        }

                        @Override
                        public void remove() {
                            iterator.remove();
                        }
                    };
                }
            });

            transaction.success();

        } catch (Exception exception) {
            transaction.failure();
            throw exception;
        } finally {
            transaction.close();
        }
    }

    @Override
    public void resetNodeRelationships(
        Object nodeId,
        String name,
        Set<Object> targetIds,
        Handler<ComplexResetNodeRelationshipsResult> handler) throws Exception {

        Transaction transaction = graphDatabaseService.beginTx();
        try {
            Node node = finder.getNode(nodeId);
            RelationshipType relationshipType = DynamicRelationshipType.forName(name);

            Set<Object> addedNodeIds = new HashSet<>();
            Set<Object> removedNodeIds = new HashSet<>();
            Set<Object> notFoundNodeIds = new HashSet<>();

            Iterable<Relationship> relationships = node.getRelationships(relationshipType, Direction.OUTGOING);

            // remove obsolete nodes
            Set<Object> currentlyRelatedNodeIds = new HashSet<>();
            for (Relationship relationship : relationships) {
                Node targetNode = relationship.getOtherNode(node);
                Object targetNodeId = finder.getNodeId(targetNode);
                currentlyRelatedNodeIds.add(targetNodeId);

                if (!targetIds.contains(targetNodeId)) {
                    relationship.delete();
                    removedNodeIds.add(targetNodeId);
                }
            }

            // add missing nodes
            for (Object targetId : targetIds) {
                if (!currentlyRelatedNodeIds.contains(targetId)) {
                    Node targetNode = finder.getNode(targetId);
                    if (targetNode == null) {
                        notFoundNodeIds.add(targetId);
                    } else {
                        node.createRelationshipTo(targetNode, relationshipType);
                        addedNodeIds.add(targetId);
                    }
                }
            }

            transaction.success();

            handler.handle(new ComplexResetNodeRelationshipsResult(addedNodeIds, removedNodeIds, notFoundNodeIds));
        } catch (Exception exception) {
            transaction.failure();
            throw exception;
        } finally {
            transaction.close();
        }
    }

}
