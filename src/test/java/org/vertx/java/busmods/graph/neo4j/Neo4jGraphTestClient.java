package org.vertx.java.busmods.graph.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.testframework.TestClientBase;

/**
 * The Neo4jGraphTestClient object.
 *
 * @author Philipp Brüll <b.phifty@gmail.com>
 * @author Rubin Simons <rubin.simons@raaftech.com>
 */
public class Neo4jGraphTestClient extends TestClientBase {

    private long testNodeId;
    private long testRelationshipId;

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

    public void testCreateNode() {
        vertx.eventBus().send(
            "test.neo4j-graph.nodes.store",
            MessagesTest.createNode("test node"),
            new Handler<Message<JsonObject>>() {

                @Override
                public void handle(Message<JsonObject> message) {
                    testNodeId = message.body().getLong("id");
                    fetchTestNode(new Handler<String>() {
                        @Override
                        public void handle(String content) {
                            try {
                                tu.azzert("test node".equals(content), "should store the right node");
                            } finally {
                                clearAll(new Handler<Boolean>() {
                                    @Override
                                    public void handle(Boolean done) {
                                        tu.testComplete();
                                    }
                                });
                            }
                        }
                    });
                }
            });
    }

    public void testUpdateNode() {
        addTestNode("test node", new Handler<Long>() {
            @Override
            public void handle(Long id) {
                vertx.eventBus().send(
                    "test.neo4j-graph.nodes.store",
                    MessagesTest.updateNode(id, "updated test node"),
                    new Handler<Message<JsonObject>>() {

                        @Override
                        public void handle(Message<JsonObject> message) {
                            fetchTestNode(new Handler<String>() {
                                @Override
                                public void handle(String content) {
                                    try {
                                        tu.azzert("updated test node".equals(content), "should update the right node");
                                    } finally {
                                        clearAll(new Handler<Boolean>() {
                                            @Override
                                            public void handle(Boolean done) {
                                                tu.testComplete();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
            }
        });
    }

    public void testFetchNode() {
        addTestNode("test node", new Handler<Long>() {
            @Override
            public void handle(Long id) {
                vertx.eventBus().send("test.neo4j-graph.nodes.fetch", MessagesTest.id(id), new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        try {
                            tu.azzert("test node".equals(message.body().getString("content")), "should respond the right content");
                        } finally {
                            clearAll(new Handler<Boolean>() {
                                @Override
                                public void handle(Boolean done) {
                                    tu.testComplete();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void testRemoveNode() {
        addTestNode("test node", new Handler<Long>() {
            @Override
            public void handle(Long id) {
                vertx.eventBus().send("test.neo4j-graph.nodes.remove", MessagesTest.id(id), new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(Message<JsonObject> message) {
                        fetchTestNode(new Handler<String>() {
                            @Override
                            public void handle(String content) {
                                try {
                                    tu.azzert(content == null, "should remove the content");
                                } finally {
                                    tu.testComplete();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void testCreateRelationship() {
        addTestNode("test node one", new Handler<Long>() {
            @Override
            public void handle(final Long idOne) {
                addTestNode("test node two", new Handler<Long>() {
                    @Override
                    public void handle(final Long idTwo) {
                        vertx.eventBus().send(
                            "test.neo4j-graph.relationships.store",
                            MessagesTest.createRelationship(idOne, idTwo, "test relationship"),
                            new Handler<Message<JsonObject>>() {

                                @Override
                                public void handle(Message<JsonObject> message) {
                                    testRelationshipId = message.body().getLong("id");
                                    fetchTestRelationship(new Handler<String>() {
                                        @Override
                                        public void handle(String content) {
                                            try {
                                                tu.azzert("test relationship".equals(content), "should store the right relationship");
                                            } finally {
                                                clearAll(new Handler<Boolean>() {
                                                    @Override
                                                    public void handle(Boolean done) {
                                                        tu.testComplete();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                    }
                });
            }
        });
    }

    public void testUpdateRelationship() {
        addTestRelationship("test relationship", new Handler<Long>() {
            @Override
            public void handle(Long id) {
                vertx.eventBus().send(
                    "test.neo4j-graph.relationships.store",
                    MessagesTest.updateRelationship(id, "updated test relationship"),
                    new Handler<Message<JsonObject>>() {

                        @Override
                        public void handle(Message<JsonObject> message) {
                            fetchTestRelationship(new Handler<String>() {
                                @Override
                                public void handle(String content) {
                                    try {
                                        tu.azzert("updated test relationship".equals(content), "should store the right relationship");
                                    } finally {
                                        clearAll(new Handler<Boolean>() {
                                            @Override
                                            public void handle(Boolean done) {
                                                tu.testComplete();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });

            }
        });
    }

    public void testFetchRelationship() {
        addTestRelationship("test relationship", new Handler<Long>() {
            @Override
            public void handle(Long id) {
                vertx.eventBus().send(
                    "test.neo4j-graph.relationships.fetch",
                    MessagesTest.id(id),
                    new Handler<Message<JsonObject>>() {

                        @Override
                        public void handle(Message<JsonObject> message) {
                            try {
                                tu.azzert("test relationship".equals(message.body().getString("content")), "should respond the right relationship");
                            } finally {
                                clearAll(new Handler<Boolean>() {
                                    @Override
                                    public void handle(Boolean done) {
                                        tu.testComplete();
                                    }
                                });
                            }
                        }
                    });
            }
        });
    }

    public void testFetchAllRelationshipsOfNode() {
        addTestRelationship("test relationship", new Handler<Long>() {
            @Override
            public void handle(final Long id) {
                vertx.eventBus().send(
                    "test.neo4j-graph.relationships.fetch-all-of-node",
                    MessagesTest.fetchIncomingRelationshipsOfNode(testNodeId),
                    new Handler<Message<JsonObject>>() {

                        @Override
                        public void handle(Message<JsonObject> message) {
                            try {
                                tu.azzert("test relationship".equals(((JsonObject) message.body().getArray("relationships").get(0)).getString("content")),
                                    "should respond the right relationship");
                            } finally {
                                clearAll(new Handler<Boolean>() {
                                    @Override
                                    public void handle(Boolean done) {
                                        tu.testComplete();
                                    }
                                });
                            }
                        }
                    });
            }
        });
    }

    public void testRemoveRelationship() {
        addTestRelationship("test relationship", new Handler<Long>() {
            @Override
            public void handle(Long id) {
                vertx.eventBus().send(
                    "test.neo4j-graph.relationships.remove",
                    MessagesTest.id(id),
                    new Handler<Message<JsonObject>>() {

                        @Override
                        public void handle(Message<JsonObject> message) {
                            fetchTestRelationship(new Handler<String>() {
                                @Override
                                public void handle(String content) {
                                    try {
                                        tu.azzert(content == null, "should remove the right relationship");
                                    } finally {
                                        clearAll(new Handler<Boolean>() {
                                            @Override
                                            public void handle(Boolean done) {
                                                tu.testComplete();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
            }
        });
    }

    public void testComplexFetchAllRelatedNodes() {
        addTestRelationship("test relationship", new Handler<Long>() {
            @Override
            public void handle(Long id) {
                vertx.eventBus().send(
                    "test.neo4j-graph.complex.fetch-all-related-nodes",
                    MessagesTest.fetchRelatedNodes(testNodeId, "incoming"),
                    new Handler<Message<JsonObject>>() {

                        @Override
                        public void handle(Message<JsonObject> message) {
                            try {
                                JsonArray nodes = message.body().getArray("nodes");
                                tu.azzert(nodes != null, "should respond some nodes");
                                tu.azzert(nodes.size() > 0, "should respond at least one node");
                                tu.azzert("test node one".equals(((JsonObject) nodes.get(0)).getString("content")),
                                    "should respond all target node that are related to the given one");
                            } finally {
                                clearAll(new Handler<Boolean>() {
                                    @Override
                                    public void handle(Boolean done) {
                                        tu.testComplete();
                                    }
                                });
                            }
                        }
                    }
                );
            }
        });
    }

    public void testComplexResettingOfNodeRelationships() {
        addTestNode("test node one", new Handler<Long>() {
            @Override
            public void handle(final Long idOne) {
                addTestNode("test node two", new Handler<Long>() {
                    @Override
                    public void handle(final Long idTwo) {
                        vertx.eventBus().send(
                            "test.neo4j-graph.complex.reset-node-relationships",
                            MessagesTest.resettingNodeRelationships(idOne, new long[]{idTwo, 66666}),
                            new Handler<Message<JsonObject>>() {

                                @Override
                                public void handle(final Message<JsonObject> message) {
                                    fetchRelatedNodes(idOne, new Handler<Set<String>>() {
                                        @Override
                                        public void handle(Set<String> contents) {
                                            try {
                                                tu.azzert(message.body().getArray("not_found_node_ids").contains((long) 66666),
                                                    "should respond all target node ids that couldn't be found");

                                                tu.azzert(contents.size() == 1 && contents.contains("test node two"),
                                                    "should connect the given node with the given list of other nodes");
                                            } finally {
                                                clearAll(new Handler<Boolean>() {
                                                    @Override
                                                    public void handle(Boolean done) {
                                                        tu.testComplete();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            });
                    }
                });
            }
        });
    }

    public void testClear() {
        addTestNode("test node", new Handler<Long>() {
            @Override
            public void handle(final Long nodeId) {
                addTestRelationship("test relationship", new Handler<Long>() {
                    @Override
                    public void handle(final Long relationshipId) {

                        JsonObject someJson = new JsonObject();
                        someJson.putString("foo", "bar");

                        vertx.eventBus().send(
                            "test.neo4j-graph.clear",
                            someJson,
                            new Handler<Message<JsonObject>>() {

                                @Override
                                public void handle(Message<JsonObject> message) {
                                    fetchTestNode(new Handler<String>() {
                                        @Override
                                        public void handle(final String nodeContent) {
                                            fetchTestRelationship(new Handler<String>() {
                                                @Override
                                                public void handle(final String relationshipContent) {
                                                    try {
                                                        tu.azzert(nodeContent == null && relationshipContent == null, "should clear all nodes and relationships");
                                                    } finally {
                                                        tu.testComplete();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                    }
                });
            }
        });
    }

    private void addTestNode(String content, final Handler<Long> handler) {
        vertx.eventBus().send(
            "test.neo4j-graph.nodes.store",
            MessagesTest.createNode(content),
            new Handler<Message<JsonObject>>() {

                @Override
                public void handle(Message<JsonObject> message) {
                    testNodeId = message.body().getLong("id");
                    handler.handle(testNodeId);
                }
            });
    }

    private void fetchTestNode(final Handler<String> handler) {
        vertx.eventBus().send(
            "test.neo4j-graph.nodes.fetch",
            MessagesTest.id(testNodeId),
            new Handler<Message<JsonObject>>() {

                @Override
                public void handle(Message<JsonObject> message) {
                    handler.handle(message.body() == null ? null : message.body().getString("content"));
                }
            });
    }

    private void fetchRelatedNodes(long id, final Handler<Set<String>> handler) {
        vertx.eventBus().send(
            "test.neo4j-graph.complex.fetch-all-related-nodes",
            MessagesTest.fetchRelatedNodes(id),
            new Handler<Message<JsonObject>>() {

                @Override
                public void handle(Message<JsonObject> message) {
                    JsonArray nodes = message.body().getArray("nodes");
                    Set<String> contents = new HashSet<>();
                    for (Object node : nodes) {
                        contents.add(((JsonObject) node).getString("content"));
                    }
                    handler.handle(contents);
                }
            });
    }

    private void addTestRelationship(final String content, final Handler<Long> handler) {
        addTestNode("test node one", new Handler<Long>() {
            @Override
            public void handle(final Long idOne) {
                addTestNode("test node two", new Handler<Long>() {
                    @Override
                    public void handle(final Long idTwo) {
                        vertx.eventBus().send(
                            "test.neo4j-graph.relationships.store",
                            MessagesTest.createRelationship(idOne, idTwo, content),
                            new Handler<Message<JsonObject>>() {

                                @Override
                                public void handle(Message<JsonObject> message) {
                                    testRelationshipId = message.body().getLong("id");
                                    handler.handle(testRelationshipId);
                                }
                            });
                    }
                });
            }
        });
    }

    private void fetchTestRelationship(final Handler<String> handler) {
        vertx.eventBus().send(
            "test.neo4j-graph.relationships.fetch",
            MessagesTest.id(testRelationshipId),
            new Handler<Message<JsonObject>>() {

                @Override
                public void handle(Message<JsonObject> message) {
                    handler.handle(message.body() == null ? null : message.body().getString("content"));
                }
            });
    }

    private void clearAll(Handler<Boolean> handler) {
        sendAndReceiveDoneMessage("test.neo4j-graph.clear", null, handler);
    }

    private void sendAndReceiveDoneMessage(String address, JsonObject message, final Handler<Boolean> handler) {
        vertx.eventBus().send(address, message, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                handler.handle(message.body().getBoolean("done"));
            }
        });
    }

}
