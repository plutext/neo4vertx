# org.openpcf.neo4vertx 1.2.x

Vert.x module to read and write a Neo4J graph database. Updated to support
the latest version of Vert.x and Neo4J (both 2.1 at the time of writing).

This module is based originally on the work done by Philip Brüll to make
Vert.x 1.3 talk to Neo4j 1.9M1 (https://github.com/phifty/vertx-neo4j-graph).


## Features

 * A Vert.x 2.1 module to talk to an embedded Neo4J 2.1 database
 * Option to specify enablement of Neo4J web interface
 * Maven build to automatically create a module with dependencies
 

## Configuration

The following JSON structure shows an example configuration. You can pass 
this configuration using the standard Vert.x parameter '-conf' when loading
the module or you can put it in 'neo4vertx.json' in the Java resources 
directory.

### Example for embedded neo4j

    {
      "mode": "embedded-with-gui",                      # embedded-with-gui, embedded-ha, embedded
      "path": "/var/graph",                             # the path where to store the database
      "restUrl": "http://localhost:7474/db/data/cypher" # url to the cypher rest service (not available with embedded mode)
      "baseAddress": "graph"                            # the vertx base address of the module
    }

### Example for embedded neo4j in HA cluster mode

It is also possible to switch to neo4j HA cluster mode. You can do so by setting
the mode to 'embedded-ha'. This is useful if you want to share neo4j graph data
in between multiple vert.x instances.

{
    "mode": "embedded-ha",
    "path": "/tmp/graph-master",
    "restUrl": "http://localhost:7474/db/data/cypher"
    "baseAddress": "graph",
    "ha.initial_hosts": "localhost:5001,localhost:5002",
    "ha.server_id": "1",
    "ha.server": "localhost:6001",
    "ha.slave_only": "false",
    "ha.cluster_server": "localhost:5001"
}

Please refer to the http://docs.neo4j.org/chunked/stable/ha-setup-tutorial.html[neo4j HA documentation]
for more information about the various neo4j ha settings.

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
