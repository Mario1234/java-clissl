
package serviSSL;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServiCutre {
    private static int port = 9000;
    
    public static void main(String[] args) throws IOException {   
    	String puerto = System.getProperty("puertito");
    	int i_puerto = Integer.parseInt(puerto);
        HttpServer server = HttpServer.create(new InetSocketAddress(i_puerto), 0);
        System.out.println("server started at " + i_puerto);
        server.createContext("/", new AtiendeRaiz(i_puerto));
        //server.createContext("/echoHeader", new AtiendeCabeceras());
        //server.createContext("/echoGet", new EchoGetHandler());
        server.createContext("/nuevaOrden", new AtiendeGet());
        server.setExecutor(null);
        server.start();
    }     
}
