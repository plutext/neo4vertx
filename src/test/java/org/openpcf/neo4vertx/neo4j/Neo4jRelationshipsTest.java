package org.openpcf.neo4vertx.neo4j;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The Neo4jRelationshipsTest object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class Neo4jRelationshipsTest extends AbstractNeo4jTest {

    @Test
    public void testCreateRelationship() throws Exception {
        Object fromId = addTestNode();
        Object toId = addTestNode();
        properties = Fixtures.testRelationship();

        graph.relationships().create(fromId, toId, "connected", properties, idHandler);
        assertNotNull(idHandler.getValue());

        graph.relationships().fetch(currentRelationshipId(), relationshipHandler);
        assertTestRelationship(relationshipHandler.getValue());
    }

    @Test
    public void testUpdateRelationship() throws Exception {
        Object id = addTestRelationship();

        properties = Fixtures.updatedTestRelationship();
        graph.relationships().update(id, properties, doneHandler);
        assertTrue(doneHandler.getValue());

        graph.relationships().fetch(currentRelationshipId(), relationshipHandler);
        assertUpdatedTestRelationship(relationshipHandler.getValue());
    }

    @Test
    public void testFetchRelationship() throws Exception {
        Object id = addTestRelationship();

        graph.relationships().fetch(id, relationshipHandler);
        assertTestRelationship(relationshipHandler.getValue());
    }

    @Test
    public void testFetchAllRelationshipsOfNode() throws Exception {
        addTestRelationship();

        graph.relationships().fetchAllOfNode(fromNodeId, relationshipsHandler);
        assertTestRelationship(relationshipsHandler.getValue().iterator().next());
    }

    @Test
    public void testRemoveRelationship() throws Exception {
        Object id = addTestRelationship();

        graph.relationships().remove(id, doneHandler);
        assertTrue(doneHandler.getValue());

        graph.relationships().fetch(id, relationshipHandler);
        assertNull(relationshipHandler.getValue());
    }

    private void assertTestRelationship(Map<String, Object> properties) {
        assertEquals("test relationship", properties.get("content"));
    }

    private void assertUpdatedTestRelationship(Map<String, Object> properties) {
        assertEquals("updated test relationship", properties.get("content"));
    }

}
