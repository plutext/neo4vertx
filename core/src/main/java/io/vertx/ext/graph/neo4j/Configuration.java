package io.vertx.ext.graph.neo4j;

/**
 * The Configuration interface.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public interface Configuration {

    public String getMode();

    /**
     * Return the storage path for the neo4j database files.
     *
     * @return storage path
     */
    public String getPath();

    /**
     * Return the url for the cypher REST service.
     *
     * @return the cypher rest url.
     */
    public String getRestUrl();

    /**
     * Return the prefix for the vertex event bus address.
     *
     * @return base address prefix
     */
    public String getBaseAddress();

    /**
     * Return the ha.initial_hosts setting.
     *
     * @return ha.initial_hosts property value
     */
    public String getHAInitialHosts();

    /**
     * Return the ha.server_id setting.
     *
     * @return ha.server_id property value
     */
    public String getHAServerID();

    /**
     * Return the ha.slave_only setting.
     *
     * @return ha.slave_only flag
     */
    public boolean getHASlaveOnly();

    /**
     * Return the ha.server setting.
     *
     * @return ha.server property value
     */
    public String getHAServer();

    /**
     * Return the ha.server_cluster setting.
     *
     * @return ha.server_cluster property value
     */
    public String getHAClusterServer();
}
