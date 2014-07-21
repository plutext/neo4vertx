package org.vertx.java.busmods.graph.neo4j.json;

import org.vertx.java.busmods.graph.neo4j.Configuration;
import org.vertx.java.busmods.graph.neo4j.DefaultConfiguration;
import org.vertx.java.core.json.JsonObject;

/**
 * The JsonConfiguration object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public class JsonConfiguration implements Configuration {

    private final JsonObject object;
    private final Configuration defaultConfiguration;

    public JsonConfiguration(JsonObject object) {
        this.object = object;
        this.defaultConfiguration = new DefaultConfiguration();
    }

    @Override
    public String getMode() {
        return object.getString("mode", defaultConfiguration.getMode());
    }

    @Override
    public String getPath() {
        return object.getString("path", defaultConfiguration.getPath());
    }

    @Override
    public String getBaseAddress() {
        return object.getString("baseAddress", defaultConfiguration.getBaseAddress());
    }

    @Override
    public String getAlternateNodeIdField() {
        return object.getString("alternateNodeIdField", defaultConfiguration.getAlternateNodeIdField());
    }

    @Override
    public String getAlternateRelationshipIdField() {
        return object.getString("alternateRelationshipIdField", defaultConfiguration.getAlternateRelationshipIdField());
    }

}
