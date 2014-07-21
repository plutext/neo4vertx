package org.openpcf.neo4vertx.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.RelationshipType;

/**
 * The DynamicRelationshipType object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public class DynamicRelationshipType implements RelationshipType {

    private final String name;

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
