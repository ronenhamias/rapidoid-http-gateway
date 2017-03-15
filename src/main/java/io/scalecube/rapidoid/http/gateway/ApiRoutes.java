package io.scalecube.rapidoid.http.gateway;

import io.scalecube.serialization.JsonMessageSerialization;
import io.scalecube.serialization.MessageSerialization;
import io.scalecube.services.ServiceDefinition;
import io.scalecube.services.annotations.AnnotationServiceProcessor;

import java.util.ArrayList;
import java.util.List;

public class ApiRoutes {

  private static MessageSerialization serialization = new JsonMessageSerialization();
  private static byte[] GET_BODY_DEFAULT_PAYLOAD = new byte[]{}; 
  private final List<Route> routes = new ArrayList<>();;
  
  static final class Route {

    private String verb = "POST";
    private String httpRoute = "/";
    private String toServiceName;
    private String toMethodName;
    private Class<?> requestType;



    public Route(String verb, String httpRoute, String toServiceName, String toMethodName, Class<?> requestType) {
      this.verb = verb;
      this.httpRoute = httpRoute;
      this.toServiceName = toServiceName;
      this.toMethodName = toMethodName;
      this.requestType = requestType;
    }



    public String verb() {
      return this.verb;
    }

    public String httpRoute() {
      return this.httpRoute;
    }

    public String toServiceName() {
      return this.toServiceName;
    }

    public String toMethodName() {
      return this.toMethodName;
    }

    public Class<?> requestType() {
      return this.requestType;
    }

    public Object parse(byte[] data) {
      try {
        return serialization.deserialize(data, requestType());
      } catch (Exception e) {
      }
      return GET_BODY_DEFAULT_PAYLOAD;
    }
  }

  public ApiRoutes post(String httpRoute, String toServiceName, String toMethodName, Class<?> requestType) {
    return this.addRoute("POST", httpRoute, toServiceName, toMethodName, requestType);
  }
  
  public ApiRoutes get(String httpRoute, String toServiceName, String toMethodName, Class<?> requestType) {
    return this.addRoute("GET", httpRoute, toServiceName, toMethodName, requestType);
  }
  
  public ApiRoutes addRoute(String verb, String httpRoute, String toServiceName, String toMethodName,
      Class<?> requestType) {
    this.routes.add(new Route(verb, httpRoute, toServiceName, toMethodName, requestType));
    return this;
  }

  private ApiRoutes addRoutes(Class<?> service) {
    AnnotationServiceProcessor proccessor = new AnnotationServiceProcessor();
    ServiceDefinition serviceInfo = proccessor.introspectServiceInterface(service);
    String serviceName = serviceInfo.serviceName();

    serviceInfo.methods().forEach((methodName, method) -> {
      Class<?>[] parameterTypes = method.getParameterTypes();
      String verb = "POST";
      if (parameterTypes.length ==0 ){
        verb = "GET";
      }
      Class<?> paramClass = method.getParameterTypes()[0];
      this.addRoute(verb, "/" + serviceName + "/" + methodName, serviceName, methodName, paramClass);
    });
    return this;
  }

  public List<Route> all() {
    return routes;
  }
}
