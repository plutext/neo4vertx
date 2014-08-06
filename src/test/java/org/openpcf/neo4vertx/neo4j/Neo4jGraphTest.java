package org.openpcf.neo4vertx.neo4j;

import org.junit.Test;
import org.vertx.java.core.json.JsonObject;

import java.util.Map;
import static org.junit.Assert.*;

/**
 * The Neo4jGraphTest object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class Neo4jGraphTest extends AbstractNeo4jTest {

    @Test
    public void testClear() throws Exception {
        Object nodeId = addTestNode();
        Object relationshipId = addTestRelationship();

        graph.clear(doneHandler);
        assertTrue(doneHandler.getValue());

        graph.nodes().fetch(nodeId, nodeHandler);
        assertNull(nodeHandler.getValue());

        graph.relationships().fetch(relationshipId, relationshipHandler);
        assertNull(relationshipHandler.getValue());
    }


    private void assertTestNode(Map<String, Object> properties) {
        assertEquals("test node", properties.get("content"));
    }

    @Test
    public void testQuery() throws Exception {
        Object nodeId = addTestNode();

        graph.nodes().fetch(nodeId, nodeHandler);
        assertTestNode(nodeHandler.getValue());


        graph.query(Fixtures.testJsonCypherQuery(), stringHandler);
    }
}
