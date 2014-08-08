package org.vertx.java.busmods.graph.neo4j;

import org.vertx.java.testframework.TestBase;

/**
 * The Neo4jGraphModuleTest object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/fraik[Freek Alleman]
 */
public class Neo4jGraphModuleTest extends TestBase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        startApp(Neo4jGraphTestClient.class.getName());
    }

    public void testCreateEntity() { startTest(getMethodName()); }
    public void testReadEntity() { startTest(getMethodName()); }
    public void testUpdateEntity() { startTest(getMethodName()); }
    public void testDeleteEntity() { startTest(getMethodName()); }
}
