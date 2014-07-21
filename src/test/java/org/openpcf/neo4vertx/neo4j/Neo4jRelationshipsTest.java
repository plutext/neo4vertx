package org.openpcf.neo4vertx.neo4j;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openpcf.neo4vertx.FakeHandler;
import org.openpcf.neo4vertx.Graph;

/**
 * The Neo4jRelationshipsTest object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class Neo4jRelationshipsTest {

    private final FakeHandler<Object> idHandler = new FakeHandler<>();
    private final FakeHandler<Boolean> doneHandler = new FakeHandler<>();
    private final FakeHandler<Map<String, Object>> nodeHandler = new FakeHandler<>();
    private final FakeHandler<Map<String, Object>> relationshipHandler = new FakeHandler<>();
    private final FakeHandler<Iterable<Map<String, Object>>> relationshipsHandler = new FakeHandler<>();
    private Map<String, Object> properties;
    private Object fromNodeId;
    private Object toNodeId;
    private Graph graph;

    @Before
    public void setUp() throws Exception {
        graph = new Neo4jGraph(
            Fixtures.getConfig().getPath(),
            Fixtures.NODE_ID_FIELD,
            Fixtures.RELATIONSHIP_ID_FIELD,
            new FakeGraphDatabaseServiceFactory());
    }

    @After
    public void tearDown() {
        idHandler.reset();
        doneHandler.reset();
        nodeHandler.reset();
        relationshipHandler.reset();
        relationshipsHandler.reset();
        graph.shutdown();
    }

    @Test
    public void testCreateRelationship() throws Exception {
        Object fromId = addTestNode();
        Object toId = addTestNode();
        properties = Fixtures.testRelationship();

        graph.relationships().create(fromId, toId, "connected", properties, idHandler);
        Assert.assertNotNull(idHandler.getValue());

        graph.relationships().fetch(currentRelationshipId(), relationshipHandler);
        assertTestRelationship(relationshipHandler.getValue());
    }

    @Test
    public void testUpdateRelationship() throws Exception {
        Object id = addTestRelationship();

        properties = Fixtures.updatedTestRelationship();
        graph.relationships().update(id, properties, doneHandler);
        Assert.assertTrue(doneHandler.getValue());

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
        Assert.assertTrue(doneHandler.getValue());

        graph.relationships().fetch(id, relationshipHandler);
        Assert.assertNull(relationshipHandler.getValue());
    }

    private Object addTestNode() throws Exception {
        properties = Fixtures.testNode();
        graph.nodes().create(properties, idHandler);
        return currentNodeId();
    }

    private Object addTestRelationship() throws Exception {
        fromNodeId = addTestNode();
        toNodeId = addTestNode();
        properties = Fixtures.testRelationship();
        graph.relationships().create(fromNodeId, toNodeId, "connected", properties, idHandler);
        return currentRelationshipId();
    }

    private Object currentNodeId() {
        return Fixtures.NODE_ID_FIELD == null ? idHandler.getValue() : properties.get(Fixtures.NODE_ID_FIELD);
    }

    private Object currentRelationshipId() {
        return Fixtures.RELATIONSHIP_ID_FIELD == null ? idHandler.getValue() : properties.get(Fixtures.RELATIONSHIP_ID_FIELD);
    }

    private void assertTestRelationship(Map<String, Object> properties) {
        Assert.assertEquals("test relationship", properties.get("content"));
    }

    private void assertUpdatedTestRelationship(Map<String, Object> properties) {
        Assert.assertEquals("updated test relationship", properties.get("content"));
    }

}
