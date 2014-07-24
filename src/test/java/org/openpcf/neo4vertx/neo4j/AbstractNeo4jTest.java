package org.openpcf.neo4vertx.neo4j;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.openpcf.neo4vertx.FakeHandler;
import org.openpcf.neo4vertx.Graph;

/**
 * Abstract class for various tests.
 * 
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public class AbstractNeo4jTest {

    protected Graph graph;
    protected Map<String, Object> properties;
    protected final FakeHandler<Object> idHandler = new FakeHandler<>();
    protected final FakeHandler<Boolean> doneHandler = new FakeHandler<>();
    protected final FakeHandler<Map<String, Object>> nodeHandler = new FakeHandler<>();
    protected final FakeHandler<Map<String, Object>> relationshipHandler = new FakeHandler<>();
    protected final FakeHandler<Iterable<Map<String, Object>>> relationshipsHandler = new FakeHandler<>();
    protected Object fromNodeId;
    protected Object toNodeId;

    @Before
    public void setUp() throws Exception {
        graph = new Neo4jGraph(new TestConfiguration(),
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

    protected Object currentNodeId() {
        return Fixtures.NODE_ID_FIELD == null ? idHandler.getValue()
                : properties.get(Fixtures.NODE_ID_FIELD);
    }

    protected Object currentRelationshipId() {
        return Fixtures.RELATIONSHIP_ID_FIELD == null ? idHandler.getValue()
                : properties.get(Fixtures.RELATIONSHIP_ID_FIELD);
    }

    protected Object addTestNode() throws Exception {
        properties = Fixtures.testNode();
        graph.nodes().create(properties, idHandler);
        return currentNodeId();
    }

    protected Object addTestRelationship() throws Exception {
        fromNodeId = addTestNode();
        toNodeId = addTestNode();
        properties = Fixtures.testRelationship();
        graph.relationships().create(fromNodeId, toNodeId, "connected",
                properties, idHandler);
        return currentRelationshipId();
    }
}
