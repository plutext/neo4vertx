# org.openpcf.neo4vertx 1.3.x

Vert.x module to read and write a Neo4J graph database. Updated to support
the latest version of Vert.x and Neo4J (both 2.1 at the time of writing).

This module is based originally on the work done by Philip Brüll to make
Vert.x 1.3 talk to Neo4j 1.9M1 (https://github.com/phifty/vertx-neo4j-graph).


## Features

 * Maven build to automatically create a module with dependencies
 * A Vert.x 2.1 module to talk to an embedded Neo4J 2.1 database
 * Enables Neo4j web interface on embedded instances

## Usage

Deploy neo4vertx within your vert.x application and send a Cypher JSON query request.
Please refer to the [doc/examples/javascript](https://github.com/raaftech/neo4vertx/tree/master/examples/javascript-example) directory for a complete working example. To see how to format your Cypher queries, check the [Cypher REST API](http://docs.neo4j.org/chunked/stable/rest-api-cypher.html).


### Example "basic usage" of neo4vertx:

```
function doQuery(someCallback) {

    // A valid Cypher query string wrapped in JSON.
    var queryJsonObject = { "query": "MATCH (n) RETURN n" };

    // Send eventbus message, have someCallback handle it.
    eventBus.send(
        'neo4j-graph.cypher.query',
        queryJsonObject,
        someCallback
    );
}
```


## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this software except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
