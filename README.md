# org.openpcf.neo4vertx 1.3.x

Vert.x module to read and write a Neo4J graph database. Updated to support
the latest version of Vert.x and Neo4J (both 2.1 at the time of writing).

This module is based originally on the work done by Philip Brüll to make
Vert.x 1.3 talk to Neo4j 1.9M1 (https://github.com/phifty/vertx-neo4j-graph).


## Features

 * Maven build to automatically create a module with dependencies
 * A Vert.x 2.1 module to talk to an embedded Neo4J 2.1 database
 * Enables Neo4j web interface on embedded instances


## Configuration

The configuration is stored in a JSON structure. You can pass this configuration
using the standard Vert.x parameter '-conf' when loading the module or you can
put it in 'neo4vertx.json' in the Java resources directory.


### The "mode" setting

There are three modes that neo4vertx can run in: default, cluster and remote
mode. In default mode, neo4vertx starts an embedded Neo4j graph and enables
the REST and web interface on it. In cluster mode, neo4vertx starts a HA
cluster enabled embedded Neo4j instance, handy for sharing graph data with
multiple Vert.x instances. In remote mode, no embedded Neo4j instance is 
started; neo4vertx simply uses the restUrl to talk to a remote Neo4j 
instance.


### The "path" setting

The path setting controls where Neo4j stores its data files when running in
default or cluster mode. When running in remote mode, this setting does not apply.
If for some reason the setting is ommitted, it will default to:

    System.getProperty("user.dir") + File.separator + "db"


### The "restUrl" setting

The restUrl setting is used by the query message functionality to decide what 
to talk to. The default (when unset) is "http://localhost:7474/db/data/cypher". 
In remote mode, this is the only setting that makes sense. When you use a 
separate, stand-alone neo4j instance on your local machine that listens on
the default localhost:7474, you might want to set remote mode (see previous).


### The baseAddress setting

This setting controls the baseAddress of the module for Vert.x'es event bus
purposes. It controls what name you use to talk to neo4vertx.


### Example "neo4vertx.json" in default mode

    {
      "mode": "default",
      "path": "/var/graph",
      "restUrl": "http://localhost:7474/db/data/cypher",
      "baseAddress": "graph"
    }


### Example "neo4vertx.json" in HA cluster mode

It is also possible to switch to neo4j HA cluster mode. You can do so by setting
the mode to 'cluster'. This is useful if you want to share neo4j graph data
in between multiple vert.x instances.

    {
        "mode": "cluster",
        "path": "/tmp/graph-master",
        "restUrl": "http://localhost:7474/db/data/cypher",
        "baseAddress": "graph",
        "ha.initial_hosts": "localhost:5001,localhost:5002",
        "ha.server_id": "1",
        "ha.server": "localhost:6001",
        "ha.slave_only": "false",
        "ha.cluster_server": "localhost:5001"
    }

Please refer to the [neo4j HA documentation](http://docs.neo4j.org/chunked/stable/ha-setup-tutorial.html)
for more information about the various neo4j ha settings.


### Example "neo4vertx.json" in remote mode

    {
      "mode": "remote",
      "restUrl": "http://localhost:7474/db/data/cypher",
      "baseAddress": "graph"
    }


## Usage

Deploy neo4vertx within your vert.x application and send a Cypher query request.
Please refer to the [doc/examples/javascript](/tree/master/doc/examples/javascript) directory for a complete working example.


### Example "basic usage" of neo4vertx:

    function doQuery(querystring, callback) {
        var queryJsonObject = { "query": querystring };

        eventBus.send(
            'neo4j-graph.cypher.query',
            queryJsonObject,
            callback
        );
    }


## License

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this software except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
