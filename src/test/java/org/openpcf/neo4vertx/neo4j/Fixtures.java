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
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 */
public class Fixtures {

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
