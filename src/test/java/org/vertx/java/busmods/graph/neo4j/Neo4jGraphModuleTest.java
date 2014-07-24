package org.vertx.java.busmods.graph.neo4j;

import org.vertx.java.testframework.TestBase;

/**
 * The Neo4jGraphModuleTest object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public class Neo4jGraphModuleTest extends TestBase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startApp(Neo4jGraphTestClient.class.getName());
    }

    public void testCreateNode() {
        startTest(getMethodName());
    }

    public void testUpdateNode() {
        startTest(getMethodName());
    }

    public void testFetchNode() {
        startTest(getMethodName());
    }

    public void testRemoveNode() {
        startTest(getMethodName());
    }

    public void testCreateRelationship() {
        startTest(getMethodName());
    }

    public void testUpdateRelationship() {
        startTest(getMethodName());
    }

    public void testFetchRelationship() {
        startTest(getMethodName());
    }

    public void testFetchAllRelationshipsOfNode() {
        startTest(getMethodName());
    }

    public void testRemoveRelationship() {
        startTest(getMethodName());
    }

    public void testComplexFetchAllRelatedNodes() {
        startTest(getMethodName());
    }

    public void testComplexResettingOfNodeRelationships() {
        startTest(getMethodName());
    }

    public void testClear() {
        startTest(getMethodName());
    }
}
