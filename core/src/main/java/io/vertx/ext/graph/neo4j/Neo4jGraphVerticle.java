package io.vertx.ext.graph.neo4j;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;

import org.neo4j.graphdb.GraphDatabaseService;
import org.openpcf.neo4vertx.Graph;
import org.openpcf.neo4vertx.neo4j.Neo4jGraph;

/**
 * The Neo4jGraphModule object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/fraik[Freek Alleman]
 */
public class Neo4jGraphVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(Neo4jGraphVerticle.class);
    private Neo4VertxConfiguration configuration;
    private static Graph database;
    private static GraphDatabaseService service;
    private MessageConsumer<JsonObject> queryMessageConsumer;

    @Override
    public void start() throws Exception {
        if (database != null) {
            throw new Exception("Verticle has already been initiated. Only a single instance of this verticle is allowed.");
        }
        initializeConfiguration();
        initializeDatabase();
        registerQueryHandler();
    }

    @Override
    public void stop() throws Exception {
        if (database != null) {
            database.shutdown();
            database = null;
        }
        if (queryMessageConsumer != null) {
            queryMessageConsumer.unregister();
        }
    }

    public static GraphDatabaseService getDatabaseService() throws Exception {
        if (service != null) {
            return service;
        }
        if (database == null) {
            throw new Exception("Database not yet initalized.");
        }
        return database.getGraphDatabaseService();
    }

    /**
     * Set the database service. This is useful when you want to inject your 
     * own database service for testing purposes.
     * @param service
     */
    public static void setDatabaseService(GraphDatabaseService service) {
        Neo4jGraphVerticle.service = service;
    }

    private void initializeConfiguration() {

        // No configuration was set. Use default settings
        if (config().size() == 0) {
            config().put(Neo4VertxConfiguration.MODE_KEY, "default");
            config().put(Neo4VertxConfiguration.PATH_KEY, "path");
            config().put(Neo4VertxConfiguration.BASE_ADDR_KEY, "neo4j-graph");
        }
        configuration = new Neo4VertxConfiguration(config());
    }

    private void initializeDatabase() throws Exception {
        database = new Neo4jGraph(configuration);
    }

    private void registerQueryHandler() {
        EventBus eb = vertx.eventBus();

        JsonObject reply = new JsonObject();
        reply.put("type", "reply message");

        queryMessageConsumer = eb.<JsonObject> consumer(configuration.getBaseAddress() + ".cypher.query").handler(msg -> {
            if (msg.replyAddress() != null) {
                try {
                    msg.reply(database.query(msg.body()));
                } catch (Exception e) {
                    logger.error("Error druing query handling:" + msg.body(), e);
                    e.printStackTrace();
                    msg.fail(1, "Query failed: " + e.getMessage());
                }
            }
        });
        
    }
}
