package org.vertx.java.busmods.graph.neo4j.json;

import java.io.File;

import org.neo4j.cluster.ClusterSettings;
import org.neo4j.kernel.ha.HaSettings;
import org.openpcf.neo4vertx.neo4j.Neo4jGraph;
import org.vertx.java.busmods.graph.neo4j.Configuration;
import org.vertx.java.core.json.JsonObject;

/**
 * The JsonConfiguration object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public class JsonConfiguration implements Configuration {

    private final JsonObject object;

    private final static String DEFAULT_PATH = System.getProperty("user.dir") + File.separator + "db";
    private final static String DEFAULT_REST_URL = "http://localhost:7474/db/data/cypher";
    private final static String DEFAULT_BASE_ADDRESS = "neo4j-graph";


    public JsonConfiguration(JsonObject object) {
        this.object = object;
    }

    @Override
    public String getMode() {
        return object.getString("mode", Neo4jGraph.DEFAULT_MODE);
    }

    @Override
    public String getPath() {
        return object.getString("path", DEFAULT_PATH);
    }

    @Override
    public String getRestUrl() {
        return object.getString("restUrl", DEFAULT_REST_URL);
    }

    @Override
    public String getBaseAddress() {
        return object.getString("baseAddress", DEFAULT_BASE_ADDRESS);
    }

    @Override
    public String getHAInitialHosts() {
        return object.getString(ClusterSettings.initial_hosts.name(), null);
    }

    @Override
    public String getHAServerID() {
        return object.getString(ClusterSettings.server_id.name(), null);
    }

    @Override
    public boolean getHASlaveOnly() {
        return Boolean.valueOf(object.getString(HaSettings.slave_only.name(),
                HaSettings.slave_only.getDefaultValue()));
    }

    @Override
    public String getHAServer() {
        return object.getString(HaSettings.ha_server.name(),
                HaSettings.ha_server.getDefaultValue());
    }

    @Override
    public String getHAClusterServer() {
        return object.getString(ClusterSettings.cluster_server.name(),
                ClusterSettings.cluster_server.getDefaultValue());
    }

}
