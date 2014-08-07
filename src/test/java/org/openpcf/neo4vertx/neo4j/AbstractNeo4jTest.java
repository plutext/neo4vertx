package org.openpcf.neo4vertx.neo4j;

import org.junit.After;
import org.junit.Before;
import org.openpcf.neo4vertx.Graph;

/**
 * Abstract class for various tests.
 *
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
public class AbstractNeo4jTest {

    protected Graph graph;

    @Before
    public void setUp() throws Exception {
        graph = new Neo4jGraph(new TestConfiguration());
    }

    @After
    public void tearDown() {
        graph.shutdown();
    }
}
