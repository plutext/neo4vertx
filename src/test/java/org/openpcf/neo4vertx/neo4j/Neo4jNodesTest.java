package org.openpcf.neo4vertx.neo4j;

import org.openpcf.neo4vertx.Graph;
import org.openpcf.neo4vertx.FakeHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * The Neo4jNodesTest object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @author Rubin Simons <rubin.simons@raaftech.com>
 * @since 2012-12-13
 * @version 1.1.1
 */
public class Neo4jNodesTest {

    private FakeHandler<Object> idHandler = new FakeHandler<>();
    private FakeHandler<Boolean> doneHandler = new FakeHandler<>();
    private FakeHandler<Map<String, Object>> nodeHandler = new FakeHandler<>();
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
