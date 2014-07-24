package org.openpcf.neo4vertx.neo4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

/**
 * The Neo4jNodesTest object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class Neo4jNodesTest extends AbstractNeo4jTest {

    @Test
    public void testCreateNode() throws Exception {
        properties = Fixtures.testNode();

        graph.nodes().create(properties, idHandler);
        assertNotNull(idHandler.getValue());

        graph.nodes().fetch(currentNodeId(), nodeHandler);
        assertTestNode(nodeHandler.getValue());
    }

    @Test
    public void testUpdateNode() throws Exception {
        Object id = addTestNode();

        properties = Fixtures.updatedTestNode();
        graph.nodes().update(id, properties, doneHandler);
        assertTrue(doneHandler.getValue());

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
        assertTrue(doneHandler.getValue());

        graph.nodes().fetch(id, nodeHandler);
        assertNull(nodeHandler.getValue());
    }

    private void assertTestNode(Map<String, Object> properties) {
        assertEquals("test node", properties.get("content"));
    }

    private void assertUpdatedTestNode(Map<String, Object> properties) {
        assertEquals("updated test node", properties.get("content"));
    }

}
