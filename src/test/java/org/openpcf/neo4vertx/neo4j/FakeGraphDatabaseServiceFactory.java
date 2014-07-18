package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * The FakeGraphDatabaseServiceFactory object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 */
public class FakeGraphDatabaseServiceFactory implements GraphDatabaseServiceFactory {

    @Override
    public GraphDatabaseService create(String path) {
        return new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase();
    }

}
