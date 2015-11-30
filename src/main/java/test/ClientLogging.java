package test;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.func.Pair;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.handling.UserId;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ratpack.jackson.Jackson.jsonNode;

public class ClientLogging implements Handler {
  private static final Logger TRACKING_LOG = LoggerFactory.getLogger("Tracker");

  @Override
  public void handle(Context context) throws Exception {
    TRACKING_LOG.info("POW!  Here it comes!");
    context.parse(jsonNode())                                   // extract the node
      .map(this::extractBody)                                   // munge the body
      .blockingOp(body -> log(context, body))                   // Log it in a blocking op
      .then(ignored -> context.render("{\"status\":\"ok\"}"));  // then render ok
  }

  // Where the magic happens
  private void log(Context ctx, String body) {
    String user = ctx.getRequest().maybeGet(UserId.class).map(UserId::toString).orElse("");
    String message = String.format("%s %s", user, body);
    TRACKING_LOG.info(message);
  }

  // God I miss Groovy
  private String extractBody(JsonNode json) {
    Iterable<String> iterable = json::fieldNames;
    return StreamSupport.stream(iterable.spliterator(), false)
      .map(name -> String.format("%s=%s", name, json.get(name)))
      .collect(Collectors.joining(", "));
  }
}
