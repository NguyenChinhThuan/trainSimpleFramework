import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

public class Main implements Container {

   public void handle(Request request, Response response) {
      try {
         PrintStream body = response.getPrintStream();
         long time = System.currentTimeMillis();
   
         response.setValue("Content-Type", "text/plain");
         response.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
         response.setDate("Date", time);
         response.setDate("Last-Modified", time);
   
         body.println("Hello World");
         body.close();
      } catch(Exception e) {
         e.printStackTrace();
      }
   } 

   public static void main(String[] list) throws Exception {
      Container container = new Main();
      Server server = new ContainerServer(container);
      Connection connection = new SocketConnection(server);
      SocketAddress address = new InetSocketAddress(8080);
       System.out.println("run server with port : 8080 => try with : http://localhost:8008 ");
      connection.connect(address);
   }
}