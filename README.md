# org.openpcf.neo4vertx 1.1.1

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

neo4vertx is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

neo4vertx is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with neo4vertx. If not, see <http://www.gnu.org/licenses/>.
