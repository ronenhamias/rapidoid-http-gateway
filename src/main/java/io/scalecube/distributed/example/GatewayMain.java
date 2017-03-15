package io.scalecube.distributed.example;

import io.scalecube.Main;
import io.scalecube.rapidoid.http.gateway.ApiRoutes;
import io.scalecube.rapidoid.http.gateway.RapidoidHttpGateway;
import io.scalecube.services.Microservices;
import io.scalecube.services.example.api.GreetingRequest;
import io.scalecube.transport.Address;

import org.rapidoid.setup.On;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class GatewayMain {

  public static void main(String[] args) {
    
    // run this main() as java application and in the browser call http://localhost:8080/
    internalWebServer();

    // seed node to the cluster on known port 8000.
    Microservices gateway = Microservices.builder()
         .seeds(Address.create(Configuration.seedIp,8000)) 
         .build();
    
    System.out.println( gateway.cluster().members());
    
    // create Rapidoid HTTP gateway and specify the routes to the service(s).
    RapidoidHttpGateway.builder().port(8080)
        .proxy(gateway.dispatcher().create())
        .routes(new ApiRoutes()
            .addRoute("POST", "/hello-world-service/sayHello",    "hello-world-service", "sayHello", GreetingRequest.class)
            .addRoute("POST", "/hello-world-service/sayHello-v1", "hello-world-service", "sayHello", GreetingRequest.class))
        .build();

   
    
  }

  private static void internalWebServer() {
    String content =
          new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/index.html"))).lines()
              .collect(Collectors.joining("\n"));
      On.port(8080).get("/").html(content);
  }
  
}
