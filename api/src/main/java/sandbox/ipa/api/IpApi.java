package sandbox.ipa.api;

import net.codestory.http.Context;
import net.codestory.http.WebServer;
import net.codestory.http.filters.PayloadSupplier;
import net.codestory.http.filters.log.LogRequestFilter;
import net.codestory.http.logs.Logs;
import net.codestory.http.payload.Payload;

import java.net.*;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Optional;

public class IpApi {
  public static void main(String[] args) {
    int port = Optional.ofNullable(System.getenv("LISTEN_PORT"))
      .map(Integer::parseInt)
      .orElse(8080);
    new WebServer()
      .configure(routes -> routes
        .filter(IpApi::log)
        .get("/ping", "pong")
        .get("/ipa", ctx -> ipa())
      )
      .start(port);
  }

  private static Payload log(String uri, Context context, PayloadSupplier supplier) throws Exception {
    InetSocketAddress clientAddress = context.request().clientAddress();
    clientAddress.getHostName();
    Logs.uri(String.format("%s: %s, %s", LocalDateTime.now().toString(), clientAddress.toString(), uri));
    return supplier.get();
  }

  private static String ipa() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      String ipa = "{\""+InetAddress.getLocalHost().getHostName()+"\":{";
      while (interfaces.hasMoreElements()) {
        NetworkInterface networkInterface = interfaces.nextElement();
        ipa+="\""+networkInterface.getDisplayName()+"\":[";
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
          ipa+="\""+addresses.nextElement().toString()+"\"";
          ipa+=addresses.hasMoreElements()
            ? ","
            : "";
        }
        ipa+="]";
        ipa+=interfaces.hasMoreElements()
          ? ","
          : "";
      }
      ipa+="}}";
      return ipa;
    } catch (SocketException | UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }
}
