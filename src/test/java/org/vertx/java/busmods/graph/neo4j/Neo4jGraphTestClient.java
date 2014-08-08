package org.vertx.java.busmods.graph.neo4j;

import org.openpcf.neo4vertx.neo4j.Fixtures;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.testframework.TestClientBase;

/**
 * The Neo4jGraphTestClient object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/fraik[Freek Alleman]
 */
public class Neo4jGraphTestClient extends TestClientBase {

    @Override
    public void start() {
        super.start();

        container.deployVerticle(Neo4jGraphModule.class.getName(), null, 1, new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult deploymentId) {
                tu.appReady();
                vertx.eventBus().send(
                    "test.neo4j-graph.cypher.query",
                    Fixtures.asJsonCypherQuery(Fixtures.DELETE_TEST_ENTITY_QUERY),
                    new Handler<Message<JsonObject>>() {
                        @Override
                        public void handle(Message<JsonObject> message) { /* do nothing */ }
                    });
            }
        });
    }

    public void testCreateEntity() {
        vertx.eventBus().send(
            "test.neo4j-graph.cypher.query",
            Fixtures.asJsonCypherQuery(Fixtures.CREATE_TEST_ENTITY_QUERY),
            new Handler<Message<JsonObject>>() {
                @Override
                public void handle(Message<JsonObject> message) {
                    JsonArray jsonArray = message.body().getArray("data").get(0);
                    tu.azzert(Fixtures.NEO4VERTX_TEST_ENTITY_UUID.equals(jsonArray.get(0)));
                    tu.testComplete();
                }
            }
        );
    }

    public void testReadEntity(){
        vertx.eventBus().send(
            "test.neo4j-graph.cypher.query",
            Fixtures.asJsonCypherQuery(Fixtures.CREATE_TEST_ENTITY_QUERY),
            new Handler<Message<JsonObject>>() {
                @Override
                public void handle(Message<JsonObject> message) {
                    vertx.eventBus().send(
                        "test.neo4j-graph.cypher.query",
                        Fixtures.asJsonCypherQuery(Fixtures.READ_TEST_ENTITY_QUERY),
                        new Handler<Message<JsonObject>>() {
                            @Override
                            public void handle(Message<JsonObject> message) {
                                JsonArray jsonArray = message.body().getArray("data").get(0);
                                tu.azzert(Fixtures.NEO4VERTX_TEST_ENTITY_UUID.equals(jsonArray.get(0)));
                                tu.testComplete();
                            }
                        }
                    );
                }
            }
        );
    }

    public void testUpdateEntity(){
        vertx.eventBus().send(
            "test.neo4j-graph.cypher.query",
            Fixtures.asJsonCypherQuery(Fixtures.CREATE_TEST_ENTITY_QUERY),
            new Handler<Message<JsonObject>>() {
                @Override
                public void handle(Message<JsonObject> message) {
                    vertx.eventBus().send(
                        "test.neo4j-graph.cypher.query",
                        Fixtures.asJsonCypherQuery(Fixtures.UPDATE_TEST_ENTITY_QUERY),
                        new Handler<Message<JsonObject>>() {
                            @Override
                            public void handle(Message<JsonObject> message) {
                                JsonArray jsonArray = message.body().getArray("data").get(0);
                                tu.azzert(Fixtures.NEO4VERTX_TEST_ENTITY_UPDATED_VALUE.equals(jsonArray.get(2)));
                                tu.testComplete();
                            }
                        }
                    );
                }
            }
        );
    }

    public void testDeleteEntity() {
        vertx.eventBus().send(
            "test.neo4j-graph.cypher.query",
            Fixtures.asJsonCypherQuery(Fixtures.CREATE_TEST_ENTITY_QUERY),
            new Handler<Message<JsonObject>>() {
                @Override
                public void handle(Message<JsonObject> message) {
                    vertx.eventBus().send(
                        "test.neo4j-graph.cypher.query",
                        Fixtures.asJsonCypherQuery(Fixtures.DELETE_TEST_ENTITY_QUERY),
                        new Handler<Message<JsonObject>>() {
                            @Override
                            public void handle(Message<JsonObject> message) {
                                vertx.eventBus().send(
                                    "test.neo4j-graph.cypher.query",
                                    Fixtures.asJsonCypherQuery(Fixtures.READ_TEST_ENTITY_QUERY),
                                    new Handler<Message<JsonObject>>() {
                                        @Override
                                        public void handle(Message<JsonObject> message) {
                                            JsonArray jsonArray = message.body().getArray("data");
                                            tu.azzert(0==jsonArray.size());
                                            tu.testComplete();
                                        }
                                    }
                                );
                            }
                        }
                    );
                }
            }
        );
    }
}
