package org.openpcf.neo4vertx.examples;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.ext.graph.neo4j.Neo4jGraphVerticle;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;

/**
 * Abstract verticle for all java demos
 * 
 * @author https://github.com/Jotschi[Johannes Schüth]
 */
abstract class AbstractDemoVerticle extends AbstractVerticle {

    protected static Logger logger = LoggerFactory.getLogger(AbstractDemoVerticle.class);

    public static final String DEFAULT_ADDRESS = "graph.cypher.query";
    public static final int DEFAULT_HTTP_PORT = 8080;

    /**
     * Deploy verticle from local classpath
     *
     * @param clazz
     * @throws MalformedURLException
     */
    public static void deployVerticle(String verticleName, JsonObject config) throws MalformedURLException {
        Vertx.vertx().deployVerticle(verticleName, new DeploymentOptions().setConfig(config), handler -> {
            logger.info("Verticle deployed");
        });
    }

    public static void keepProcessAlive() throws IOException {
        System.in.read();
        System.exit(0);
    }

    public void undeployVerticle(String deploymentId) {
        vertx.undeployVerticle(deploymentId);
    }

    @Override
    public void stop() {
        logger.info("Stopped " + getClass().getName());
    }

    /**
     * Start the main neo4vertx verticle and use the configuration from the given file.
     * 
     * @param configFileName
     * @throws IOException
     */
    public void startNeo4Vertx(String configFileName) throws IOException {
        InputStream is = AbstractDemoVerticle.class.getResourceAsStream(configFileName);
        String jsonTxt = IOUtils.toString(is);
        JsonObject config = new JsonObject(jsonTxt);
        vertx.deployVerticle(Neo4jGraphVerticle.class.getCanonicalName(), new DeploymentOptions().setConfig(config));
    }



    /**
     * @param storeHandler
     * @param countHandler
     */
    protected void startHttpServer(Handler<AsyncResult<Message<JsonObject>>> storeHandler, Handler<AsyncResult<Message<JsonObject>>> countHandler) {
        startHttpServer(DEFAULT_HTTP_PORT, storeHandler, countHandler, null);
    }
    
    /**
     * @param port
     * @param storeHandler
     * @param countHandler
     */
    protected void startHttpServer(int port, Handler<AsyncResult<Message<JsonObject>>> storeHandler, Handler<AsyncResult<Message<JsonObject>>> countHandler) {
        startHttpServer(port, storeHandler, countHandler, null);
    }

    /**
     * Start a simple http server that will store a node into the graph and count the nodes in the graph. Handlers can be used to handle the replies.
     * 
     * @param port
     *            Http port of the http server
     * @param storeHandler
     * @param countHandler
     * @param requestHandler
     */
    // START SNIPPET: startHttpServer
    protected void startHttpServer(int port, 
            Handler<AsyncResult<Message<JsonObject>>> storeHandler,
            Handler<AsyncResult<Message<JsonObject>>> countHandler, 
            Runnable requestHandler) {
        HttpServerOptions options = new HttpServerOptions();
        options.setPort(port);
        vertx.createHttpServer(options).requestHandler(request -> {
            if (storeHandler != null) {
                storeNode(storeHandler);
            }
            if (countHandler != null) {
                countData(countHandler);
            }
            if (requestHandler != null) {
                new Thread(requestHandler).start();
            }
            request.response().headers().set("Content-Type", "text/plain");
            request.response().end("Request handled");
        }).listen();
        logger.info("Started " + getClass().getName());

    }
    // END SNIPPET: startHttpServer

    /**
     * Send event to store a node
     */
    // START SNIPPET: storeNode
    protected void storeNode(
            Handler<AsyncResult<Message<JsonObject>>> replyHandler) {
        JsonObject obj = new JsonObject();
        String query = "CREATE (testentry:ingredient" 
                + System.currentTimeMillis() 
                + " {time : '" + System.currentTimeMillis() + "'})";
        obj.put("query", query);
        logger.info("Sending query: " + query);
        vertx.eventBus().send(DEFAULT_ADDRESS, obj, replyHandler);
    }
    // END SNIPPET: storeNode


    /**
     * Send an event to the event bus which contains an query which counts the nodes in the graph.
     * 
     * @param countHandler
     */
    // START SNIPPET: countData
    protected void countData(
            Handler<AsyncResult<Message<JsonObject>>> countHandler) {

        JsonObject obj = new JsonObject();
        String query = "MATCH (n) RETURN count(*);";
        obj.put("query", query);
        logger.info("Sending query: " + query);
        EventBus eb = vertx.eventBus();

        eb.send(DEFAULT_ADDRESS, obj, countHandler);
    }
    // END SNIPPET: countData


}
