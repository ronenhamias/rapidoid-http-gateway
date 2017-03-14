package io.scalecube.rapidoid.http.gateway;

import io.scalecube.rapidoid.http.gateway.ApiRoutes.Route;
import io.scalecube.serialization.JsonMessageSerialization;
import io.scalecube.serialization.MessageSerialization;
import io.scalecube.services.ServiceCall;
import io.scalecube.services.ServiceHeaders;
import io.scalecube.transport.Message;

import org.rapidoid.http.Req;
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

          proxy.invoke(toMessage(route, req)).whenComplete((success, error) -> {
            if (success != null) {
              try {
                byte[] responseData = serialization.serialize(success.data(), success.data().getClass());
                IO.write(req.response().out(), responseData);
                req.done();
              } catch (Exception e) {
                IO.write(req.response().out(), toErrorResponse(e));
                req.done();
              }
            } else {
              // handle service call error
              IO.write(req.response().out(), toErrorResponse(error));
              req.done();
            }
          });

          return req;
        });
      });

      return new RapidoidHttpGateway();
    }

    private byte[] toErrorResponse(Throwable ex) {
      try {
        if (ex != null) {
          return serialization.serialize(new ServiceRequestError(ex.getMessage()), ServiceRequestError.class);
        } else{
          return serialization.serialize(new ServiceRequestError("no reason specified"), ServiceRequestError.class);
        }
      } catch (Exception e1) {
        e1.printStackTrace();
        // log error.
      }
      return null;
    }

    private Message toMessage(Route route, Req req) {
      if (!req.data().isEmpty()) {
        return Message.builder()
            .header(ServiceHeaders.SERVICE_REQUEST, route.toServiceName())
            .header(ServiceHeaders.METHOD, route.toMethodName())
            .data(route.parse(req.body()))
            .build();
      } else {
        return Message.builder()
            .header(ServiceHeaders.SERVICE_REQUEST, route.toServiceName())
            .header(ServiceHeaders.METHOD, route.toMethodName())
            .build();
      }
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
