package io.scalecube.distributed.example;

import io.scalecube.services.Microservices;
import io.scalecube.services.example.GreetingServiceImpl;
import io.scalecube.transport.Address;

public class ServiceMain {

  public static void main(String[] args) {
    
    if(args.length > 1){
      // some node that provision the GreetingServiceImpl in the cluster.
      Microservices serviceNode = Microservices.builder()
            .seeds(Address.create(args[0] ,Integer.parseInt(args[1]))) 
            .services(new GreetingServiceImpl()) 
            .build();
      
      System.out.println( serviceNode.cluster().members());
    } else {
      System.out.println( "please specify the seed ip as arg[0] and seed-ip arg[1] param, ** NOTE: ** if you havent run a seed node yet please run seed node first!.");
      System.out.println( "example: java -cp target/rapidoid-http-gateway-0.9.1-SNAPSHOT.jar io.scalecube.distributed.example.ServiceMain 10.150.4.47 8000");
    }
    

  }

}
