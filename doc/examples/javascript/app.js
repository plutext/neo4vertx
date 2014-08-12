var container = require("vertx/container"),
    console = require("vertx/console");

// webserver configuration
var webServerConfig = {
    port: 8080,
    host: "0.0.0.0",
    bridge: true,
    inbound_permitted: [
        { address_re: 'neo4j-graph.*' }
    ]
};

// deploy mod-web-server
container.deployModule("io.vertx~mod-web-server~2.0.0-final", webServerConfig, 1, deploymentCompleteHandler);

// neo4vertx configuration
var neo4jConfig = {
    mode: "default",
    path: "/tmp/banaan",
    baseAddress: "neo4j-graph"
};

// deploy neo4vertx
container.deployModule("org.openpcf~neo4vertx~1.3.1", neo4jConfig, 1, deploymentCompleteHandler);

// display deployment status
function deploymentCompleteHandler(error, deploymentID){
    if (error) {
        console.log("Something went wrong with deployment: ");
        console.log(error)
    } else {
        console.log("Deployment Completed succesfully, deployment ID: " + deploymentID);
    }
}
