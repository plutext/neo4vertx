# Example neo4vertx usage
This example shows the basic usage of neo4vertx. Follow the steps below to run the example.


### Start the application
> vertx run app.js

If this is the first time you run the example, you'll see vertx downloading and deploying 2 modules (mod-web-server and ofcourse neo4vertx). So far so good, now let's run the example.

### Run the example
Open [http://localhost:8080/](http://localhost:8080/) in a webbrowser to run the example (you can see the output of client.js in the webconsole). It should look something like this:
```
"Eventbus connected"
"displayResult() received the following result: "
Object { columns: Array[1], data: Array[0] }
```

### Open the neo4j browser
Open [http://localhost:7474/](http://localhost:7474/) to see the neo4j browser.