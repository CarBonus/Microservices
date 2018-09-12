package io.vertx.book.http;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.core.json.JsonObject;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
public class HelloMicroservice extends AbstractVerticle {

@Override
public void start() {
 // Receive message from the address 'hello'
 vertx.eventBus().<String>consumer("hello", message -> {
 JsonObject json = new JsonObject()
 .put("served-by", this.toString());
 // Check whether we have received a payload in the
 // incoming message
 if (message.body().isEmpty()) {
 message.reply(json.put("message", "hello"));
 } else {
 message.reply(json.put("message",
 "hello " + message.body()));
 }
 });
}
 ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
discovery.publish(HttpEndpoint.createRecord(
    "my-rest-api",
    "localhost", 8080,
    "/names"),
    ar -> {
      if (ar.succeeded()) {
        System.out.println("REST API published");
      } else {
        System.out.println("Unable to publish the REST API: " +
             ar.cause().getMessage());
      }
    });
}
