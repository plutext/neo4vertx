package org.vertx.java.busmods.graph.neo4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.openpcf.neo4vertx.ComplexResetNodeRelationshipsResult;
import org.openpcf.neo4vertx.Graph;
import org.openpcf.neo4vertx.neo4j.Neo4jGraph;
import org.vertx.java.busmods.graph.neo4j.json.JsonConfiguration;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * The Neo4jGraphModule object.
 *
 * @author mailto:b.phifty@gmail.com[Philipp Brüll]
 * @author mailto:rubin.simons@raaftech.com[Rubin Simons]
 */
public class Neo4jGraphModule extends Verticle {

    private static final Logger logger = Logger.getLogger(Neo4jGraphModule.class);
    private Configuration configuration;
    private Graph database;

    @Override
    public void start() {
        try {
            initializeConfiguration();
            initializeDatabase();
            registerQueryHandler();
            registerStoreNodeHandler();
            registerFetchNodeHandler();
            registerRemoveNodeHandler();
            registerStoreRelationshipHandler();
            registerFetchRelationshipHandler();
            registerFetchAllRelationshipsOfNodeHandler();
            registerRemoveRelationshipHandler();
            registerComplexFetchAllRelatedNodes();
            registerComplexResettingOfNodeRelationships();
            registerClearHandler();
        } catch (ConfigurationException e) {
            logger.error("Error while starting the Neo4j graph module.",e);
        }
    }

    @Override
    public void stop() {
        if (database != null) {
            database.shutdown();
        }
        super.stop();
    }

    private void initializeConfiguration() {

        if (container.config().size() == 0) {

            InputStream inputStream = Neo4jGraphModule.class.getResourceAsStream("/neo4vertx.json");

            try {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();

                String inputString;
                while ((inputString = streamReader.readLine()) != null)
                    stringBuilder.append(inputString);

                JsonObject jsonObject = new JsonObject(stringBuilder.toString());
                configuration = new JsonConfiguration(jsonObject);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            configuration = new JsonConfiguration(container.config());
        }
    }

    private void initializeDatabase() throws ConfigurationException {
        database = new Neo4jGraph(configuration);
    }

    private void registerQueryHandler() {
        final Graph database = this.database;
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".cypher.query", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                try {
                    database.query(message.body(), new org.openpcf.neo4vertx.Handler<String>() {
                        @Override
                        public void handle(String value) {
                            message.reply(value);
                        }
                    });
                } catch (Exception exception) {
                    getContainer().logger().error("error while executing query", exception);
                }
            }
        });
    }

    private void registerStoreNodeHandler() {
        final Graph database = this.database;
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".nodes.store", new Handler<Message<JsonObject>>() {
            @Override
            @SuppressWarnings("unchecked")
            public void handle(final Message<JsonObject> message) {
                Map<String, Object> body = message.body().toMap();
                Map<String, Object> properties = body.containsKey("properties") ? (Map<String, Object>) body.get("properties") : null;

                if (body.containsKey("id")) {
                    Object id = getIdIn(message.body());

                    try {
                        database.nodes().update(id, properties, new org.openpcf.neo4vertx.Handler<Boolean>() {
                            @Override
                            public void handle(Boolean value) {
                                message.reply(Messages.done(true));
                            }
                        });
                    } catch (Exception exception) {
                        getContainer().logger().error("error while updating node", exception);
                    }
                } else {
                    try {
                        database.nodes().create(properties, new org.openpcf.neo4vertx.Handler<Object>() {
                            @Override
                            public void handle(Object id) {
                                message.reply(Messages.id(id));
                            }
                        });
                    } catch (Exception exception) {
                        getContainer().logger().error("error while creating node", exception);
                    }
                }
            }
        });
    }

    private void registerFetchNodeHandler() {
        final Graph database = this.database;
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".nodes.fetch", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                Object id = getIdIn(message.body());

                database.nodes().fetch(id, new org.openpcf.neo4vertx.Handler<Map<String, Object>>() {
                    @Override
                    public void handle(Map<String, Object> node) {
                        message.reply(node == null ? null : Messages.properties(node));
                    }
                });
            }
        });
    }

    private void registerRemoveNodeHandler() {
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".nodes.remove", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                Object id = getIdIn(message.body());

                try {
                    database.nodes().remove(id, new org.openpcf.neo4vertx.Handler<Boolean>() {
                        @Override
                        public void handle(Boolean value) {
                            message.reply(Messages.done(true));
                        }
                    });
                } catch (Exception exception) {
                    getContainer().logger().error("error while removing node", exception);
                }
            }
        });
    }

    private void registerStoreRelationshipHandler() {
        final Graph database = this.database;
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".relationships.store", new Handler<Message<JsonObject>>() {
            @Override
            @SuppressWarnings("unchecked")
            public void handle(final Message<JsonObject> message) {
                Map<String, Object> body = message.body().toMap();
                Map<String, Object> properties = body.containsKey("properties") ? (Map<String, Object>) body.get("properties") : null;

                if (body.containsKey("id")) {
                    Object id = getIdIn(message.body());

                    try {
                        database.relationships().update(id, properties, new org.openpcf.neo4vertx.Handler<Boolean>() {
                            @Override
                            public void handle(Boolean value) {
                                message.reply(Messages.done(true));
                            }
                        });
                    } catch (Exception exception) {
                        getContainer().logger().error("error while updating relationship", exception);
                    }
                } else {
                    Object fromId = getIdIn(message.body(), "from");
                    Object toId = getIdIn(message.body(), "to");
                    String name = message.body().getString("name");
                    try {
                        database.relationships().create(fromId, toId, name, properties, new org.openpcf.neo4vertx.Handler<Object>() {
                            @Override
                            public void handle(Object id) {
                                message.reply(Messages.id(id));
                            }
                        });
                    } catch (Exception exception) {
                        getContainer().logger().error("error while creating relationship", exception);
                    }
                }
            }
        });
    }

    private void registerFetchRelationshipHandler() {
        final Graph database = this.database;
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".relationships.fetch", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                Object id = getIdIn(message.body());

                database.relationships().fetch(id, new org.openpcf.neo4vertx.Handler<Map<String, Object>>() {
                    @Override
                    public void handle(Map<String, Object> node) {
                        message.reply(node == null ? null : Messages.properties(node));
                    }
                });
            }
        });
    }

    private void registerFetchAllRelationshipsOfNodeHandler() {
        final Graph database = this.database;
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".relationships.fetch-all-of-node", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                Object nodeId = getIdIn(message.body(), "node_id");
                String name = message.body().getString("name");
                String direction = message.body().getString("direction");

                database.relationships().fetchAllOfNode(nodeId, new org.openpcf.neo4vertx.Handler<Iterable<Map<String, Object>>>() {
                    @Override
                    public void handle(Iterable<Map<String, Object>> relationships) {
                        message.reply(Messages.relationships(relationships));
                    }
                });
            }
        });
    }

    private void registerRemoveRelationshipHandler() {
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".relationships.remove", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                Object id = getIdIn(message.body());

                try {
                    database.relationships().remove(id, new org.openpcf.neo4vertx.Handler<Boolean>() {
                        @Override
                        public void handle(Boolean value) {
                            message.reply(Messages.done(true));
                        }
                    });
                } catch (Exception exception) {
                    getContainer().logger().error("error while removing relationship", exception);
                }
            }
        });
    }

    private void registerComplexFetchAllRelatedNodes() {
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".complex.fetch-all-related-nodes", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                Object nodeId = getIdIn(message.body(), "node_id");
                String name = message.body().getString("name");
                String direction = message.body().getString("direction");

                database.complex().fetchAllRelatedNodes(nodeId, name, direction, new org.openpcf.neo4vertx.Handler<Iterable<Map<String, Object>>>() {
                    @Override
                    public void handle(Iterable<Map<String, Object>> nodes) {
                        message.reply(Messages.nodes(nodes));
                    }
                });
            }
        });

    }

    private void registerComplexResettingOfNodeRelationships() {
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".complex.reset-node-relationships", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                Object nodeId = getIdIn(message.body(), "node_id");
                String name = message.body().getString("name");
                final Set<Object> targetIds = getIdsIn(message.body(), "target_ids");

                try {
                    database.complex().resetNodeRelationships(
                            nodeId,
                            name,
                            targetIds,
                            new org.openpcf.neo4vertx.Handler<ComplexResetNodeRelationshipsResult>() {

                                @Override
                                public void handle(ComplexResetNodeRelationshipsResult value) {
                                    message.reply(Messages.resetNodeRelationships(value));
                                }
                            });
                } catch (Exception exception) {
                    getContainer().logger().error("error while resetting node relationships", exception);
                }
            }
        });
    }

    private void registerClearHandler() {
        vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".clear", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                try {
                    database.clear(new org.openpcf.neo4vertx.Handler<Boolean>() {
                        @Override
                        public void handle(Boolean value) {
                            message.reply(Messages.done(true));
                        }
                    });
                } catch (Exception exception) {
                    getContainer().logger().error("error while clearing graph", exception);
                }
            }
        });
    }

    private Set<Object> getIdsIn(JsonObject body, String field) {
        Set<Object> result = new HashSet<>();
        JsonArray ids = body.getArray(field);

        for (Object id : ids) {
            result.add(id instanceof String ? id : Long.parseLong(id.toString()));
        }

        return result;
    }

    private Object getIdIn(JsonObject body) {
        return getIdIn(body, "id");
    }

    private Object getIdIn(JsonObject body, String field) {
        return body.getField(field) instanceof String ? body.getString(field) : body.getLong(field);
    }

}
