package io.scalecube.distributed.example;

import io.scalecube.services.Microservices;
import io.scalecube.services.example.GreetingServiceImpl;
import io.scalecube.transport.Address;

public class ServiceMain {

  public static void main(String[] args) {
    // some node that provision the GreetingServiceImpl in the cluster.
    Microservices serviceNode = Microservices.builder()
          .seeds(Address.create("10.150.4.47",8000))
          .services(new GreetingServiceImpl())
          .build();
    
    System.out.println( serviceNode.cluster().members());

  }

}
