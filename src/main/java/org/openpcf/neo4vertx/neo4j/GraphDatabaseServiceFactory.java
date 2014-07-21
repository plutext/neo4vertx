package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * The GraphDatabaseServiceFactory interface.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 */
public interface GraphDatabaseServiceFactory {

    public GraphDatabaseService create(String path);

}
