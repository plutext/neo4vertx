package org.openpcf.neo4vertx.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.vertx.java.busmods.graph.neo4j.Configuration;

/**
 * The FakeGraphDatabaseServiceFactory object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public class FakeGraphDatabaseServiceFactory implements GraphDatabaseServiceFactory {

    @Override
    public GraphDatabaseService create(String path) {
        return new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().newGraphDatabase();
    }

    @Override
    public GraphDatabaseService create(Configuration configuration) {
        return create("");
    }

}
