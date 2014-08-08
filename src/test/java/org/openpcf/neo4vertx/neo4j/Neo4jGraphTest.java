package org.openpcf.neo4vertx.neo4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openpcf.neo4vertx.FakeHandler;
import org.openpcf.neo4vertx.Graph;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * The Neo4jGraphTest object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/fraik[Freek Alleman]
 */
public class Neo4jGraphTest {

    protected final FakeHandler<JsonObject> resultHandler = new FakeHandler<>();
    protected Graph graph;

    @Before
    public void setUp() throws Exception {
        graph = new Neo4jGraph(new TestConfiguration());
        deleteTestEntity();
    }

    @After
    public void tearDown() throws Exception {
        deleteTestEntity();
        graph.shutdown();
    }

    @Test
    public void testCrud() throws Exception {
        String createResult = createTestEntity();
        assertEquals(Fixtures.NEO4VERTX_TEST_ENTITY_UUID, createResult);

        String readResult = readTestEntity();
        assertEquals(Fixtures.NEO4VERTX_TEST_ENTITY_UUID, readResult);

        String updateTestResult = updateTestEntity();
        assertEquals(Fixtures.NEO4VERTX_TEST_ENTITY_UPDATED_VALUE, updateTestResult);

        Boolean deleteTestResult = deleteTestEntity();
        assertTrue(deleteTestResult);
    }

    protected JsonObject executeQuery(String queryString) throws Exception {
        JsonObject queryJson = new JsonObject("{ \"query\":\"" + queryString + "\", \"params\":{} }");
        graph.query(queryJson, resultHandler);
        return resultHandler.getValue();
    }

    protected String createTestEntity() throws Exception {
        JsonObject queryResult = executeQuery(Fixtures.CREATE_TEST_ENTITY_QUERY);
        JsonArray jsonArray = queryResult.getArray("data").get(0);
        return jsonArray.get(0);
    }

    protected String readTestEntity() throws Exception {
        JsonObject queryResult = executeQuery(Fixtures.READ_TEST_ENTITY_QUERY);
        JsonArray jsonArray = queryResult.getArray("data");
        if (jsonArray.size()==0){
            return "";
        } else {
            JsonArray embeddedJsonArray = jsonArray.get(0);
            return embeddedJsonArray.get(0);
        }
    }

    protected String updateTestEntity() throws Exception {
        JsonObject queryResult = executeQuery(Fixtures.UPDATE_TEST_ENTITY_QUERY);
        JsonArray jsonArray = queryResult.getArray("data").get(0);
        return jsonArray.get(2);
    }

    protected Boolean deleteTestEntity() throws Exception {
        executeQuery(Fixtures.DELETE_TEST_ENTITY_QUERY);
        return (!Fixtures.NEO4VERTX_TEST_ENTITY_UUID.equals(readTestEntity()));
    }
}
