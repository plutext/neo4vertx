package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.vertx.java.busmods.graph.neo4j.Configuration;

/**
 * The GraphDatabaseServiceFactory interface.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public interface GraphDatabaseServiceFactory {

    /**
     * Create a new database service.
     * 
     * @param path
     *            basepath for the neo4j data directory
     * @return neo4j service
     */
    public GraphDatabaseService create(String path);

    /**
     * Create and configure the service.
     * 
     * @param configuration
     *            configuration that should be used for service creation
     * @return neo4j service
     */
    public GraphDatabaseService create(Configuration configuration);

}
