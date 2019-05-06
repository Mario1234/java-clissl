package serviSSL;

import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AtiendeCabeceras implements HttpHandler {

    public void handle(com.sun.net.httpserver.HttpExchange he) throws IOException {
            Map<String, List<String>> entries1 = he.getRequestHeaders();
            Set<Map.Entry<String, List<String>>> entries = entries1.entrySet();
            String response = "";
            for (Map.Entry<String, List<String>> entry : entries)
                     response += entry.toString() + "\n";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
    }
}
