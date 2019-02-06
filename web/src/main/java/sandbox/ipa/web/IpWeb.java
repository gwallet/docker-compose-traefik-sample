package sandbox.ipa.web;

import net.codestory.http.WebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.Optional;

public class IpWeb {
  public static void main(String[] args) {
    String API_HOSTNAME = Optional.ofNullable(System.getenv("API_HOSTNAME"))
      .orElse("api");
    int API_LISTEN_PORT = Optional.ofNullable(System.getenv("API_LISTEN_PORT"))
      .map(Integer::parseInt)
      .orElse(8080);
    int port = Optional.ofNullable(System.getenv("LISTEN_PORT"))
      .map(Integer::parseInt)
      .orElse(8080);
    new WebServer()
      .configure(routes -> routes
        .get("/ping", "pong")
        .get("/ipa", ctx -> ipa(api(API_HOSTNAME, API_LISTEN_PORT)))
      )
      .start(port);
  }

  private static String api(String host, int port) {
    return httpGet("http://"+host+":"+port+"/ipa");
  }

  private static String httpGet(String url) {
    String response;
    try {
      HttpURLConnection connection = doGet(url);
      if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
          StringBuilder buffer = new StringBuilder();
          String line;
          while ((line = reader.readLine()) != null) {
            buffer.append(line);
          }
          response = buffer.toString();
        }
      }
      else {
        response = "{\""+connection.getResponseMessage()+"\":"+connection.getResponseCode()+"}";
      }
    }
    catch (IOException cause) {
      cause.printStackTrace(System.err);
      return "{\""+cause.getClass().getName()+"\":\""+cause.getMessage()+"\"}";
    }
    return response;
  }

  private static HttpURLConnection doGet(String url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    connection.setRequestMethod("GET");
    connection.setConnectTimeout(1000);
    connection.setReadTimeout(1000);
    connection.connect();
    return connection;
  }

  private static String ipa(String api) {
    return "{\"web\":"+ ipa()+",\"api\":"+api+"}";
  }

  private static String ipa() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      String ipa = "{\"" + InetAddress.getLocalHost().getHostName() + "\":{";
      while (interfaces.hasMoreElements()) {
        NetworkInterface networkInterface = interfaces.nextElement();
        ipa += "\"" + networkInterface.getDisplayName() + "\":[";
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
          ipa += "\"" + addresses.nextElement().toString() + "\"";
          ipa += addresses.hasMoreElements()
            ? ","
            : "";
        }
        ipa += "]";
        ipa += interfaces.hasMoreElements()
          ? ","
          : "";
      }
      ipa += "}}";
      return ipa;
    } catch (SocketException | UnknownHostException e) {
      throw new RuntimeException(e);
    }
  }
}
