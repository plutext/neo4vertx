# org.openpcf.neo4vertx 1.1.x

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

    {
      "mode": "embedded",      # embedded, embedded-with-gui
      "path": "/var/graph",    # the path where to store the database
      "baseAddress": "graph"   # the vertx base address of the module
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
