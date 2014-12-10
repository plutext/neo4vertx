package io.vertx.ext.graph.neo4j;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.ext.graph.neo4j.json.JsonConfiguration;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
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
    private Configuration configuration;
    private static Graph database;

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
    }

    public static GraphDatabaseService getDatabase() throws Exception {
        if (database == null) {
            throw new Exception("Database not yet initalized.");
        }
        return database.getGraphDatabaseService();
    }

    private void initializeConfiguration() {

        if (config().size() == 0) {

            InputStream inputStream = Neo4jGraphVerticle.class.getResourceAsStream("/neo4vertx.json");

            try {
                String jsonText = IOUtils.toString(inputStream);
                JsonObject jsonObject = new JsonObject(jsonText);
                configuration = new JsonConfiguration(jsonObject);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            configuration = new JsonConfiguration(config());
        }
    }

    private void initializeDatabase() throws Exception {
        database = new Neo4jGraph(configuration);
    }

    private void registerQueryHandler() {
        EventBus eb = vertx.eventBus();

        JsonObject reply = new JsonObject();
        reply.put("type", "reply message");

        eb.<JsonObject> consumer(configuration.getBaseAddress() + ".cypher.query").handler(msg -> {
            if (msg.replyAddress() != null) {
                try {
                    msg.reply(database.query(msg.body()));
                } catch (Exception e) {
                    logger.error("Error druing query handling:" + msg.body() , e);
                    e.printStackTrace();
                    msg.fail(1, "Query failed: " + e.getMessage());
            }
        }
    }   );
    }
}
