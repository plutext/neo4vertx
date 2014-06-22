package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


/**
 * The Neo4jGraphDatabaseServiceFactory object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @author Rubin Simons <rubin.simons@raaftech.com>
 * @since 2012-12-13
 * @version 1.1.1
 */
public class Neo4jGraphDatabaseServiceFactory implements GraphDatabaseServiceFactory {

    @Override
    public GraphDatabaseService create(String path) {
        GraphDatabaseService graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(path).newGraphDatabase();
        registerShutdownHook(graphDatabaseService);

        return graphDatabaseService;
    }

    private void registerShutdownHook(final GraphDatabaseService graphDatabaseService) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDatabaseService.shutdown();
            }
        });
    }

}
