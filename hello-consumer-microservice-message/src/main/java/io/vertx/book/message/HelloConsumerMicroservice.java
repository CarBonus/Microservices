import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.ext.web.*;
import io.vertx.rxjava.ext.web.client.*;
import io.vertx.rxjava.ext.web.codec.BodyCodec;
import rx.Single;
public class HelloMicroservice extends AbstractVerticle {
@Override
public void start() {
 vertx.createHttpServer()
 .requestHandler(
 req -> {
 EventBus bus = vertx.eventBus();
 Single<JsonObject> obs1 = bus
 .<JsonObject>rxSend("hello", "Luke")
 .map(Message::body);
 Single<JsonObject> obs2 = bus
 .<JsonObject>rxSend("hello", "Leia")
 .map(Message::body);
 Single
 .zip(obs1, obs2, (luke, leia) ->
 new JsonObject()
 .put("Luke", luke.getString("message")
 + " from "
 + luke.getString("served-by"))
 .put("Leia", leia.getString("message")
 + " from "
 + leia.getString("served-by"))
 )
 .subscribe(
 x -> req.response().end(x.encodePrettily()),
 t -> {
 t.printStackTrace();
 req.response().setStatusCode(500)
.end(t.getMessage());
 }
 );
 })
 .listen(8082);
}
 public MyRestAPIClient(ServiceDiscovery discovery,
                       Handler<AsyncResult<Void>> completionHandler) {
  HttpEndpoint.getClient(discovery,
         new JsonObject().put("name", "my-rest-api"),
          ar -> {
            if (ar.failed()) {
             // No service
            completionHandler.handle(Future.failedFuture(
              "No matching services"));
           } else {
              client = ar.result();
              completionHandler.handle(Future.succeededFuture());
           }
  });
}
}
