package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.RelationshipType;

import java.util.HashMap;
import java.util.Map;

/**
 * The DynamicRelationshipType object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @since 2012-12-13
 * @version 1.0.0
 */
public class DynamicRelationshipType implements RelationshipType {

    private String name;

    DynamicRelationshipType(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    private static Map<String, RelationshipType> relationshipTypeCache = new HashMap<>();

    public static RelationshipType forName(String name) {
        if (relationshipTypeCache.containsKey(name)) {
            return relationshipTypeCache.get(name);
        } else {
            RelationshipType relationshipType = new DynamicRelationshipType(name);
            relationshipTypeCache.put(name, relationshipType);
            return relationshipType;
        }
    }

    public static void clearCache() {
        relationshipTypeCache.clear();
    }

}
