package org.openpcf.neo4vertx.neo4j;

import org.vertx.java.busmods.graph.neo4j.Configuration;

/**
 * Stub for the test configuration.
 *
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public class TestConfiguration implements Configuration {

    @Override
    public String getMode() {
        return Fixtures.getConfig().getMode();
    }

    @Override
    public String getPath() {
        return Fixtures.getConfig().getPath();
    }

    @Override
    public String getRestUrl() {
        return Fixtures.getConfig().getRestUrl();
    }

    @Override
    public String getBaseAddress() {
        return null;
    }

    @Override
    public String getAlternateNodeIdField() {
        return Fixtures.NODE_ID_FIELD;
    }

    @Override
    public String getAlternateRelationshipIdField() {
        return Fixtures.RELATIONSHIP_ID_FIELD;
    }

    @Override
    public String getHAInitialHosts() {
        return null;
    }

    @Override
    public String getHAServerID() {
        return null;
    }

    @Override
    public boolean getHASlaveOnly() {
        return false;
    }

    @Override
    public String getHAServer() {
        return null;
    }

    @Override
    public String getHAClusterServer() {
        return null;
    }

}
