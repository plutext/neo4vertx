package org.openpcf.neo4vertx.neo4j;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

}
