package io.scalecube;

import io.scalecube.rapidoid.http.gateway.ApiRoutes;
import io.scalecube.rapidoid.http.gateway.RapidoidHttpGateway;
import io.scalecube.services.Microservices;
import io.scalecube.services.example.GreetingServiceImpl;
import io.scalecube.services.example.api.GreetingRequest;

import org.rapidoid.setup.On;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Main {

  
  public static void main(String[] args) {
  
    // run this main() as java application and in the browser call http://localhost:8080/
    internalWebServer();
    
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
            .post("/hello-world-service/sayHello", "hello-world-service", "sayHello", GreetingRequest.class)
            .post("/hello-world-service/sayHello-v1", "hello-world-service", "sayHello", GreetingRequest.class))
        .build();

  }

  private static void internalWebServer() {
    String content =
          new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/index.html"))).lines()
              .collect(Collectors.joining("\n"));
      On.port(8080).get("/").html(content);
  }

}
