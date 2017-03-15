# rapidoid-http-gateway
Example project - kick starter project implementing http gateway using rapidoid http project.


# How to use:

``` java

// seed node to the cluster.
Microservices seed = Microservices.builder().build();

// some node that provision the GreetingServiceImpl in the cluster.
Microservices.builder()
	.seeds(seed.cluster().address())
	.services(new GreetingServiceImpl())
	.build();

// create Rapidoid HTTP gateway and specify the routes to the service(s).
RapidoidHttpGateway.builder().port(8080)
	.proxy(seed.dispatcher().create())
	.routes(new ApiRoutes()
	    .addRoute("POST", "/hello-world-service/sayHello",    "hello-world-service", "sayHello", GreetingRequest.class)
	    .addRoute("POST", "/hello-world-service/sayHello-v1", "hello-world-service", "sayHello", GreetingRequest.class))
	.build();
        

```

# How to run the single jvm example project.

After cloning the project and adding it to your favorite IDE:

* run the Main as java application.
* open your browser and open the url http://localhost:8080 (this will open the index.html)

The src/main/resources/index.html makes 3 XHR(POST) requests to the hello world service via the defined HTTP routes.



# How to run the distributed example project.

After cloning the project and adding it to your favorite IDE:

* run the SeedMain as java application its ip will be printed in the console.
* configure the seed ip in the ServiceMain and in the GatewayMain as seed.
* run the ServiceMain as java application (you can run more then one instance).
* run the GatewayMain as java application.
* open your browser and open the url http://localhost:8080 (this will open the index.html)

The src/main/resources/index.html makes 3 XHR(POST) requests to the hello world service via the defined HTTP routes.

* NOTE: if running more than one ServiceMain instance you should see how service requests are distributed/balanced over the service cluster.

# run the distributed example from command line:

java -cp target/rapidoid-http-gateway-0.9.1-SNAPSHOT.jar io.scalecube.distributed.example.SeedMain 8000
expected result: seed will print out the seed ip.

java -cp target/rapidoid-http-gateway-0.9.1-SNAPSHOT.jar io.scalecube.distributed.example.SeedMain 8000



