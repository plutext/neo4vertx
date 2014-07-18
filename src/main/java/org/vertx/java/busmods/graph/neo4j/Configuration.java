package org.vertx.java.busmods.graph.neo4j;

/**
 * The Configuration interface.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 */
public interface Configuration {

    public String getMode();

    public String getPath();

    public String getBaseAddress();

    public String getAlternateNodeIdField();

    public String getAlternateRelationshipIdField();

}
