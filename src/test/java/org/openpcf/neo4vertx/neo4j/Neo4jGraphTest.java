package org.openpcf.neo4vertx.neo4j;

import org.junit.Test;
import org.openpcf.neo4vertx.FakeHandler;
import org.vertx.java.core.json.JsonObject;

import java.util.Map;
import static org.junit.Assert.*;

/**
 * The Neo4jGraphTest object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 */
public class Neo4jGraphTest extends AbstractNeo4jTest {

    protected final FakeHandler<JsonObject> stringHandler = new FakeHandler<>();

    @Test
    public void testQuery() throws Exception {
        graph.query(Fixtures.testJsonCypherQuery(), stringHandler);
    }
}
