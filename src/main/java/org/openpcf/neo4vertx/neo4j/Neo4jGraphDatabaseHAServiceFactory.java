package org.openpcf.neo4vertx.neo4j;

import org.neo4j.cluster.ClusterSettings;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.HighlyAvailableGraphDatabaseFactory;
import org.neo4j.kernel.ha.HaSettings;
import org.vertx.java.busmods.graph.neo4j.Configuration;

/**
 * Service Factory that is able to create {@link GraphDatabaseService} using the
 * {@link HighlyAvailableGraphDatabaseFactory} and therefore add HA support to
 * the returned service.
 *
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public class Neo4jGraphDatabaseHAServiceFactory implements GraphDatabaseServiceFactory {

    @Override
    public GraphDatabaseService create(String path) {
        GraphDatabaseBuilder builder = new HighlyAvailableGraphDatabaseFactory().newHighlyAvailableDatabaseBuilder(path);
        GraphDatabaseService graphDatabaseService = builder.newGraphDatabase();
        registerShutdownHook(graphDatabaseService);
        return graphDatabaseService;
    }

    private void registerShutdownHook(
            final GraphDatabaseService graphDatabaseService) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDatabaseService.shutdown();
            }
        });
    }

    @Override
    public GraphDatabaseService create(Configuration configuration) {
        GraphDatabaseBuilder builder = new HighlyAvailableGraphDatabaseFactory().newHighlyAvailableDatabaseBuilder(configuration.getPath());

        // Set various HA settings we support
        builder.setConfig(ClusterSettings.server_id, configuration.getHAServerID());
        builder.setConfig(HaSettings.ha_server, configuration.getHAServer());
        builder.setConfig(HaSettings.slave_only, String.valueOf(configuration.getHASlaveOnly()));
        builder.setConfig(ClusterSettings.cluster_server, configuration.getHAClusterServer());
        builder.setConfig(ClusterSettings.initial_hosts, configuration.getHAInitialHosts());

        GraphDatabaseService graphDatabaseService = builder.newGraphDatabase();
        registerShutdownHook(graphDatabaseService);
        return graphDatabaseService;
    }
}
