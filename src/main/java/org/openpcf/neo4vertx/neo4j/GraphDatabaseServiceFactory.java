package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * The GraphDatabaseServiceFactory interface.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 */
public interface GraphDatabaseServiceFactory {

    public GraphDatabaseService create(String path);

}
