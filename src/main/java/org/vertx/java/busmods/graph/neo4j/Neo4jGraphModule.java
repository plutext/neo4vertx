package org.vertx.java.busmods.graph.neo4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.openpcf.neo4vertx.Graph;
import org.openpcf.neo4vertx.neo4j.Neo4jGraph;
import org.vertx.java.busmods.graph.neo4j.json.JsonConfiguration;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/**
 * The Neo4jGraphModule object.
 *
 * @author https://github.com/phifty[Philipp Brüll]
 * @author https://github.com/rubin55[Rubin Simons]
 * @author https://github.com/fraik[Freek Alleman]
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
                    database.query(message.body(), new org.openpcf.neo4vertx.Handler<JsonObject>() {
                        @Override
                        public void handle(JsonObject value) {
                            message.reply(value);
                        }
                    });
                } catch (Exception exception) {
                    getContainer().logger().error("error while executing query", exception);
                }
            }
        });
    }
}
