
package serviSSL;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServiCutre {
    private static int port = 9000;
    
    public static void main(String[] args) throws IOException {   
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("server started at " + port);
        server.createContext("/", new AtiendeRaiz(port));
        //server.createContext("/echoHeader", new AtiendeCabeceras());
        //server.createContext("/echoGet", new EchoGetHandler());
        server.createContext("/nuevaOrden", new AtiendeGet());
        server.setExecutor(null);
        server.start();
    }     
}
