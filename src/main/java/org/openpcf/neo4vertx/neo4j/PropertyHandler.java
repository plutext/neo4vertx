package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.PropertyContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * The Neo4jGraphDatabaseServiceFactory object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @since 2012-12-13
 * @version 1.0.0
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
