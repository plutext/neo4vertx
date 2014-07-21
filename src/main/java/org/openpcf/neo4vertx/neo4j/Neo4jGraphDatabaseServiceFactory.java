package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


/**
 * The Neo4jGraphDatabaseServiceFactory object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
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
