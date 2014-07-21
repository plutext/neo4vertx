package org.vertx.java.busmods.graph.neo4j;

import java.io.File;

/**
 * The DefaultConfiguration object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class DefaultConfiguration implements Configuration {

    @Override
    public String getMode() {
        return "embedded";
    }

    @Override
    public String getPath() {
        return System.getProperty("user.dir") + File.separator + "db";
    }

    @Override
    public String getBaseAddress() {
        return "neo4j-graph";
    }

    @Override
    public String getAlternateNodeIdField() {
        return null;
    }

    @Override
    public String getAlternateRelationshipIdField() {
        return null;
    }

}
