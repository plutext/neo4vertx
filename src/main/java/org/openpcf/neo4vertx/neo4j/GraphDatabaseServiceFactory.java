package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * The GraphDatabaseServiceFactory interface.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @since 2012-12-13
 * @version 1.0.0
 */
public interface GraphDatabaseServiceFactory {

    public GraphDatabaseService create(String path);

}
