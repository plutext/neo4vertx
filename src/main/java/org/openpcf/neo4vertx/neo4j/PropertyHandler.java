package org.openpcf.neo4vertx.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.PropertyContainer;

/**
 * The Neo4jGraphDatabaseServiceFactory object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public class PropertyHandler {

    public static void setProperties(PropertyContainer propertyContainer, Map<String, Object> properties) {
        for (Map.Entry<String, Object> property : properties.entrySet()) {
            propertyContainer.setProperty(property.getKey(), property.getValue());
        }
    }

    public static Map<String, Object> getProperties(PropertyContainer propertyContainer) {
        Map<String, Object> result = new HashMap<>();
        for (String key : propertyContainer.getPropertyKeys()) {
            result.put(key, propertyContainer.getProperty(key));
        }
        return result;
    }

}
