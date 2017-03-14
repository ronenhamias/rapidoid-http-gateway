package io.scalecube.rapidoid.http.gateway;

import io.scalecube.serialization.JsonMessageSerialization;
import io.scalecube.serialization.MessageSerialization;
import io.scalecube.services.ServiceCall;
import io.scalecube.services.ServiceHeaders;
import io.scalecube.transport.Message;

import org.rapidoid.io.IO;
import org.rapidoid.setup.On;

/**
 * 
 * 
 *
 */
public class RapidoidHttpGateway {

  public static final class Builder {

    private static MessageSerialization serialization = new JsonMessageSerialization();
    
    private int port = 8080;

    private ApiRoutes routes;

    private ServiceCall proxy;
    public RapidoidHttpGateway build() {
      

      routes.all().forEach(route -> {
        On.port(port).route(route.verb(), route.httpRoute()).plain(req -> {
          req.async();

          if(req.verb()== "GET")
          proxy.invoke(Message.builder()
              
              .header(ServiceHeaders.SERVICE_REQUEST, route.toServiceName())
              .header(ServiceHeaders.METHOD, route.toMethodName())
              .data(route.parse(req.body()))
              .build()).whenComplete((success, error) -> {
                if(success!=null) {
                  try {
                    byte[] responseData = serialization.serialize(success.data(), success.data().getClass());
                    IO.write(req.response().out(), responseData);
                    req.done();
                  } catch (Exception e) {
                   // handle serialization error
                    req.done();
                  }
                } else {
                  // handle service call error
                  req.done();
                }
              });

          return req;
        });
      });

      return new RapidoidHttpGateway();
    }

    public Builder port(int port) {
      this.port = port;
      return this;
    }

    public Builder routes(ApiRoutes routes) {
      this.routes = routes;
      return this;
    }

    public Builder proxy(ServiceCall proxy) {
      this.proxy = proxy;
      return this;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

}
