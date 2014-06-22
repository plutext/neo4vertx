package org.vertx.java.busmods.graph.neo4j;

/**
 * The Configuration interface.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @since 2012-12-13
 * @version 1.0.0
 */
public interface Configuration {

    public String getBaseAddress();

    public String getPath();

    public String getAlternateNodeIdField();

    public String getAlternateRelationshipIdField();

}
