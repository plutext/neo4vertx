package org.vertx.java.busmods.graph.neo4j;

import org.openpcf.neo4vertx.neo4j.Fixtures;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.testframework.TestClientBase;

/**
 * The Neo4jGraphTestClient object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 */
public class Neo4jGraphTestClient extends TestClientBase {

    @Override
    public void start() {
        super.start();

        container.deployVerticle(Neo4jGraphModule.class.getName(), null, 1, new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult deploymentId) {
                tu.appReady();
            }
        });
    }

    public void testRunQuery() {

        vertx.eventBus().send(
            "test.neo4j-graph.cypher.query",
            Fixtures.testJsonCypherQuery(),
            new Handler<Message<JsonObject>>() {

                @Override
                public void handle(Message<JsonObject> message) {

                    JsonObject testJsonCypherQueryResults = new JsonObject("{\"query\" : \"MATCH (n) RETURN n\"}");


                    vertx.eventBus().send(
                        "test.neo4j-graph.cypher.query",
                        testJsonCypherQueryResults,
                        new Handler<Message<JsonObject>>() {
                            @Override
                            public void handle(Message<JsonObject> message) {

                                System.out.println("DEBUG: (message.body()): " + message.body());
                                tu.testComplete();
                            }
                        }
                    );
                }
            }
        );
    }
}
