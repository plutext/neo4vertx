package org.openpcf.neo4vertx.neo4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.vertx.java.busmods.graph.neo4j.Configuration;
import org.vertx.java.busmods.graph.neo4j.json.JsonConfiguration;
import org.vertx.java.core.json.JsonObject;

/**
 * The Fixtures object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class Fixtures {

    public static String NODE_ID_FIELD = "id";
    public static String RELATIONSHIP_ID_FIELD = "id";

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

    public static Map<String, Object> testNode() {
        Map<String, Object> result = new HashMap<>();
        if (NODE_ID_FIELD != null) {
            result.put(NODE_ID_FIELD, UUID.randomUUID().toString());
        }
        result.put("content", "test node");
        return result;
    }

    public static Map<String, Object> updatedTestNode() {
        Map<String, Object> result = new HashMap<>();
        if (NODE_ID_FIELD != null) {
            result.put(NODE_ID_FIELD, UUID.randomUUID().toString());
        }
        result.put("content", "updated test node");
        return result;
    }

    public static Map<String, Object> testRelationship() {
        Map<String, Object> result = new HashMap<>();
        if (RELATIONSHIP_ID_FIELD != null) {
            result.put(RELATIONSHIP_ID_FIELD, UUID.randomUUID().toString());
        }
        result.put("content", "test relationship");
        return result;
    }

    public static Map<String, Object> updatedTestRelationship() {
        Map<String, Object> result = new HashMap<>();
        if (RELATIONSHIP_ID_FIELD != null) {
            result.put(RELATIONSHIP_ID_FIELD, UUID.randomUUID().toString());
        }
        result.put("content", "updated test relationship");
        return result;
    }

}
