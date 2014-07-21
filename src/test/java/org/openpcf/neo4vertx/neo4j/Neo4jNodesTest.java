package org.openpcf.neo4vertx.neo4j;

import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openpcf.neo4vertx.FakeHandler;
import org.openpcf.neo4vertx.Graph;

/**
 * The Neo4jNodesTest object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class Neo4jNodesTest {

    private final FakeHandler<Object> idHandler = new FakeHandler<>();
    private final FakeHandler<Boolean> doneHandler = new FakeHandler<>();
    private final FakeHandler<Map<String, Object>> nodeHandler = new FakeHandler<>();
    private Map<String, Object> properties;
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
        graph.shutdown();
    }

    @Test
    public void testCreateNode() throws Exception {
        properties = Fixtures.testNode();

        graph.nodes().create(properties, idHandler);
        Assert.assertNotNull(idHandler.getValue());

        graph.nodes().fetch(currentNodeId(), nodeHandler);
        assertTestNode(nodeHandler.getValue());
    }

    @Test
    public void testUpdateNode() throws Exception {
        Object id = addTestNode();

        properties = Fixtures.updatedTestNode();
        graph.nodes().update(id, properties, doneHandler);
        Assert.assertTrue(doneHandler.getValue());

        graph.nodes().fetch(currentNodeId(), nodeHandler);
        assertUpdatedTestNode(nodeHandler.getValue());
    }

    @Test
    public void testFetchNode() throws Exception {
        Object id = addTestNode();

        graph.nodes().fetch(id, nodeHandler);
        assertTestNode(nodeHandler.getValue());
    }

    @Test
    public void testRemoveNode() throws Exception {
        Object id = addTestNode();

        graph.nodes().remove(id, doneHandler);
        Assert.assertTrue(doneHandler.getValue());

        graph.nodes().fetch(id, nodeHandler);
        Assert.assertNull(nodeHandler.getValue());
    }

    private Object addTestNode() throws Exception {
        properties = Fixtures.testNode();
        graph.nodes().create(properties, idHandler);
        return currentNodeId();
    }

    private Object currentNodeId() {
        return Fixtures.NODE_ID_FIELD == null ? idHandler.getValue() : properties.get(Fixtures.NODE_ID_FIELD);
    }

    private void assertTestNode(Map<String, Object> properties) {
        Assert.assertEquals("test node", properties.get("content"));
    }

    private void assertUpdatedTestNode(Map<String, Object> properties) {
        Assert.assertEquals("updated test node", properties.get("content"));
    }

}
