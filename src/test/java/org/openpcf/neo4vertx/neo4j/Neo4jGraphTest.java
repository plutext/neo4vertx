package org.openpcf.neo4vertx.neo4j;

import org.openpcf.neo4vertx.Graph;
import org.openpcf.neo4vertx.FakeHandler;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

/**
 * The Neo4jGraphTest object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @author Rubin Simons <rubin.simons@raaftech.com>
 * @since 2012-12-13
 * @version 1.1.1
 */
public class Neo4jGraphTest {

    private FakeHandler<Object> idHandler = new FakeHandler<>();
    private FakeHandler<Boolean> doneHandler = new FakeHandler<>();
    private FakeHandler<Map<String, Object>> nodeHandler = new FakeHandler<>();
    private FakeHandler<Map<String, Object>> relationshipHandler = new FakeHandler<>();
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
        relationshipHandler.reset();
        graph.shutdown();
    }

    @Test
    public void testClear() throws Exception {
        Object nodeId = addTestNode();
        Object relationshipId = addTestRelationship();

        graph.clear(doneHandler);
        Assert.assertTrue(doneHandler.getValue());

        graph.nodes().fetch(nodeId, nodeHandler);
        Assert.assertNull(nodeHandler.getValue());

        graph.relationships().fetch(relationshipId, relationshipHandler);
        Assert.assertNull(relationshipHandler.getValue());
    }

    private Object addTestNode() throws Exception {
        properties = Fixtures.testNode();
        graph.nodes().create(properties, idHandler);
        return currentNodeId();
    }

    private Object addTestRelationship() throws Exception {
        Object fromId = addTestNode();
        Object toId = addTestNode();
        properties = Fixtures.testRelationship();
        graph.relationships().create(fromId, toId, "connected", properties, idHandler);
        return currentRelationshipId();
    }

    private Object currentNodeId() {
        return Fixtures.NODE_ID_FIELD == null ? idHandler.getValue() : properties.get(Fixtures.NODE_ID_FIELD);
    }

    private Object currentRelationshipId() {
        return Fixtures.RELATIONSHIP_ID_FIELD == null ? idHandler.getValue() : properties.get(Fixtures.RELATIONSHIP_ID_FIELD);
    }

}
