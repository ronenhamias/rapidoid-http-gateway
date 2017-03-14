package io.scalecube.services.example.api;

import io.scalecube.services.annotations.Service;
import io.scalecube.services.annotations.ServiceMethod;

import java.util.concurrent.CompletableFuture;

@Service("hello-world-service")
public interface HelloWorldService {

  @ServiceMethod("sayHello")
  CompletableFuture<GreetingResponse> sayHello(GreetingRequest request);

}
