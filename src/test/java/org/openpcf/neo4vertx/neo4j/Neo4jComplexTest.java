package org.openpcf.neo4vertx.neo4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openpcf.neo4vertx.ComplexResetNodeRelationshipsResult;
import org.openpcf.neo4vertx.FakeHandler;

/**
 * The Neo4jComplexTest object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class Neo4jComplexTest extends AbstractNeo4jTest {

    private FakeHandler<Iterable<Map<String, Object>>> nodeHandler = new FakeHandler<>();
    private FakeHandler<ComplexResetNodeRelationshipsResult> resetNodeRelationshipsHandler = new FakeHandler<>();

    @Test
    public void testFetchAllRelatedNodes() throws Exception {
        addTestRelationship();

        graph.complex().fetchAllRelatedNodes(fromNodeId, "connected", "outgoing", nodeHandler);
        assertTestNode(nodeHandler.getValue().iterator().next());

        DynamicRelationshipType.clearCache();

        graph.complex().fetchAllRelatedNodes(fromNodeId, "connected", "outgoing", nodeHandler);
        assertTestNode(nodeHandler.getValue().iterator().next());
    }

    @Test
    public void testResetNodeRelationships() throws Exception {
        Object id = addTestNode();
        Object targetIdOne = addTestNode();
        Object targetIdTwo = addTestNode();

        Set<Object> targetIds = new HashSet<>();
        targetIds.add(targetIdOne);
        targetIds.add(targetIdTwo);

        graph.complex().resetNodeRelationships(id, "connected", targetIds, resetNodeRelationshipsHandler);

        Set<Object> addedNodeIds = resetNodeRelationshipsHandler.getValue().addedNodeIds;
        assertEquals(2, addedNodeIds.size());
        assertTrue(addedNodeIds.contains(targetIdOne));
        assertTrue(addedNodeIds.contains(targetIdTwo));

        Set<Object> removedNodeIds = resetNodeRelationshipsHandler.getValue().removedNodeIds;
        assertEquals(0, removedNodeIds.size());

        targetIds.remove(targetIdTwo);
        graph.complex().resetNodeRelationships(id, "connected", targetIds, resetNodeRelationshipsHandler);

        addedNodeIds = resetNodeRelationshipsHandler.getValue().addedNodeIds;
        assertEquals(0, addedNodeIds.size());

        removedNodeIds = resetNodeRelationshipsHandler.getValue().removedNodeIds;
        assertEquals(1, removedNodeIds.size());
        assertTrue(removedNodeIds.contains(targetIdTwo));

        graph.relationships().fetchAllOfNode(id, relationshipsHandler);
        assertTrue(relationshipsHandler.getValue().iterator().hasNext());
    }

    private void assertTestNode(Map<String, Object> properties) {
        assertEquals("test node", properties.get("content"));
    }

}
