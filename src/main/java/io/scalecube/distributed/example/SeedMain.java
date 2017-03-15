package io.scalecube.distributed.example;

import io.scalecube.services.Microservices;

public class SeedMain {

  public static void main(String[] args) {
    // seed node to the cluster on known port 8000.
    Microservices seed = Microservices.builder().port(8000).build();

    System.out.println( seed.cluster().address());
  }

}
