package io.scalecube.services.example;

import io.scalecube.services.example.api.GreetingRequest;
import io.scalecube.services.example.api.GreetingResponse;
import io.scalecube.services.example.api.HelloWorldService;

import java.util.concurrent.CompletableFuture;

public class GreetingServiceImpl implements HelloWorldService {

  public CompletableFuture<GreetingResponse> sayHello(GreetingRequest request) {
    String responseMessage;
    if (request == null) {
      responseMessage = "greetings: unknown";
    } else {
      responseMessage = "greetings: " + request.name();
    }
    System.out.println(System.currentTimeMillis() +" - "+ responseMessage);
    return CompletableFuture.completedFuture(new GreetingResponse(responseMessage));
  }

}
