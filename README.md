# rapidoid-http-gateway
Example project - kick starter project implementing http gateway using rapidoid http project.


# how to use:

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
