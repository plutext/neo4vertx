package org.openpcf.neo4vertx.neo4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.vertx.java.busmods.graph.neo4j.Configuration;
import org.vertx.java.busmods.graph.neo4j.json.JsonConfiguration;
import org.vertx.java.core.json.JsonObject;

/**
 * The Fixtures object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/fraik[Freek Alleman]
 */
public class Fixtures {

    public static final String NEO4VERTX_TEST_ENTITY_UUID = "3f2e3e50-1efa-11e4-8c21-0800200c9a66";
    public static final String NEO4VERTX_TEST_ENTITY_DESC = "Neo4vertx Test Entity. Safe to remove.";
    public static final String NEO4VERTX_TEST_ENTITY_INITIAL_VALUE = "Initial value";
    public static final String NEO4VERTX_TEST_ENTITY_UPDATED_VALUE = "Updated value";

    public static final String CREATE_TEST_ENTITY_QUERY = String.format("MERGE (n {uuid: '%s', desc:'%s', value:'%s'}) RETURN n.uuid, n.desc, n.value", NEO4VERTX_TEST_ENTITY_UUID, NEO4VERTX_TEST_ENTITY_DESC, NEO4VERTX_TEST_ENTITY_INITIAL_VALUE);
    public static final String READ_TEST_ENTITY_QUERY = String.format("MATCH (n {uuid: '%s'}) RETURN n.uuid, n.desc, n.value", NEO4VERTX_TEST_ENTITY_UUID);
    public static final String UPDATE_TEST_ENTITY_QUERY = String.format("MATCH (n {uuid: '%s'}) SET n.value = '%s' RETURN n.uuid, n.desc, n.value", NEO4VERTX_TEST_ENTITY_UUID, NEO4VERTX_TEST_ENTITY_UPDATED_VALUE);
    public static final String DELETE_TEST_ENTITY_QUERY = String.format("MATCH (n {uuid: '%s'}) DELETE n", NEO4VERTX_TEST_ENTITY_UUID);

    public static Configuration getConfig() {

        InputStream inputStream = Neo4jGraphTest.class.getResourceAsStream("/neo4vertx.json");

        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();

            String inputString;
            while ((inputString = streamReader.readLine()) != null)
                stringBuilder.append(inputString);

            JsonObject jsonObject = new JsonObject(stringBuilder.toString());
            return new JsonConfiguration(jsonObject);

        } catch (IOException e) {
            e.printStackTrace();
            return new JsonConfiguration(new JsonObject(""));
        }
    }

    public static JsonObject testJsonCypherQuery() {
        return new JsonObject( "{\n" +
         "  \"query\" : \"CREATE (n:Person { props } ) RETURN n\",\n" +
         "  \"params\" : {\n" +
         "    \"props\" : [ {\n" +
         "      \"name\" : \"Rubin\",\n" +
         "      \"position\" : \"Developer\"\n" +
         "    }, {\n" +
         "      \"name\" : \"Freek\",\n" +
         "      \"position\" : \"Developer\"\n" +
         "    } ]\n" +
         "  }\n" +
         "}");
    }

}
