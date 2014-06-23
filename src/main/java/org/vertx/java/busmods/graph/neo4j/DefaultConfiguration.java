package org.vertx.java.busmods.graph.neo4j;

import java.io.File;

/**
 * The DefaultConfiguration object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @author Rubin Simons <rubin.simons@raaftech.com>
 * @since 2012-12-13
 * @version 1.1.1
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
