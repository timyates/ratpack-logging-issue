package test;

import ratpack.guice.Guice;
import ratpack.handling.Chain;
import ratpack.handling.RequestLogger;
import ratpack.server.RatpackServer;

public class Server {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec ->
      serverSpec.registry(Guice.registry(bindingSpec -> bindingSpec.bind(ClientLogging.class)))
                .handlers(chain -> chain.post("log", ClientLogging.class))
    );
  }
}
