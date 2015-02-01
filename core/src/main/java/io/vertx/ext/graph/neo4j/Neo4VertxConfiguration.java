package io.vertx.ext.graph.neo4j;

import io.vertx.core.json.JsonObject;

import java.io.File;

import org.neo4j.cluster.ClusterSettings;
import org.neo4j.kernel.ha.HaSettings;
import org.openpcf.neo4vertx.neo4j.Neo4jGraph;

/**
 * The Configuration interface.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public class Neo4VertxConfiguration {

    public static final String MODE_KEY = "mode";
    public static String PATH_KEY = "path";
    public static String REST_URL_KEY = "restUrl";
    public static String BASE_ADDR_KEY = "baseAddress";
    public static final String WEB_SERVER_BIND_ADDRESS_KEY = "webServerBindAddress";

    public final static String DEFAULT_WEBSERVER_BIND_ADDRESS = "localhost";
    public final static String DEFAULT_PATH = System.getProperty("user.dir") + File.separator + "db";
    public final static String DEFAULT_REST_URL = "http://localhost:7474/db/data/cypher";
    public final static String DEFAULT_BASE_ADDRESS = "neo4j-graph";

    private String webServerBindAddress;
    private String mode;
    private String path;
    private String restUrl;
    private String baseAddress;
    private String haInitialHosts;
    private String haClusterServer;
    private boolean haSlaveOnly;
    private String haServer;
    private String haServerId;

    public Neo4VertxConfiguration() {

    }

    public Neo4VertxConfiguration(JsonObject jsonConfig) {
        webServerBindAddress = jsonConfig.getString(WEB_SERVER_BIND_ADDRESS_KEY, DEFAULT_WEBSERVER_BIND_ADDRESS);
        mode = jsonConfig.getString(MODE_KEY, Neo4jGraph.DEFAULT_MODE);
        path = jsonConfig.getString(PATH_KEY, DEFAULT_PATH);
        restUrl = jsonConfig.getString(REST_URL_KEY, DEFAULT_REST_URL);
        baseAddress = jsonConfig.getString(BASE_ADDR_KEY, DEFAULT_BASE_ADDRESS);
        haInitialHosts = jsonConfig.getString(ClusterSettings.initial_hosts.name(), null);
        haClusterServer = jsonConfig.getString(ClusterSettings.cluster_server.name(), ClusterSettings.cluster_server.getDefaultValue());
        haSlaveOnly = Boolean.valueOf(jsonConfig.getString(HaSettings.slave_only.name(), HaSettings.slave_only.getDefaultValue()));
        haServer = jsonConfig.getString(HaSettings.ha_server.name(), HaSettings.ha_server.getDefaultValue());
        haServerId = jsonConfig.getString(ClusterSettings.server_id.name(), null);
    }

    public String getMode() {
        return mode;
    }

    /**
     * Return the storage path for the neo4j database files.
     *
     * @return storage path
     */
    public String getPath() {
        return path;
    }

    /**
     * Return the url for the cypher REST service.
     *
     * @return the cypher rest url.
     */

    public String getRestUrl() {
        return restUrl;
    }

    /**
     * Return the prefix for the vertex event bus address.
     *
     * @return base address prefix
     */
    public String getBaseAddress() {
        return baseAddress;

    }

    /**
     * Return the webserver address. The address is used to bind the rest server on an ip/interface. You may use 0.0.0.0 to bind on all network devices.
     * 
     * @return webserver address
     */
    public String getWebServerBindAddress() {
        return webServerBindAddress;
    }

    /**
     * Return the ha.initial_hosts setting.
     *
     * @return ha.initial_hosts property value
     */
    public String getHAInitialHosts() {
        return haInitialHosts;
    }

    /**
     * Return the ha.server_id setting.
     *
     * @return ha.server_id property value
     */
    public String getHAServerID() {
        return haServerId;
    }

    /**
     * Return the ha.slave_only setting.
     *
     * @return ha.slave_only flag
     */
    public boolean getHASlaveOnly() {
        return haSlaveOnly;
    }

    /**
     * Return the ha.server setting.
     *
     * @return ha.server property value
     */
    public String getHAServer() {
        return haServer;
    }

    /**
     * Return the ha.server_cluster setting.
     *
     * @return ha.server_cluster property value
     */
    public String getHAClusterServer() {
        return haClusterServer;
    }

    public void setWebServerBindAddress(String webServerBindAddress) {
        this.webServerBindAddress = webServerBindAddress;
    }

    public void setHaInitialHosts(String haInitialHosts) {
        this.haInitialHosts = haInitialHosts;
    }

    public void setHaClusterServer(String haClusterServer) {
        this.haClusterServer = haClusterServer;
    }

    public boolean isHaSlaveOnly() {
        return haSlaveOnly;
    }

    public void setHaSlaveOnly(boolean haSlaveOnly) {
        this.haSlaveOnly = haSlaveOnly;
    }

    public void setHaServer(String haServer) {
        this.haServer = haServer;
    }

    public void setHaServerId(String haServerId) {
        this.haServerId = haServerId;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

}
