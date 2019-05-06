package serviSSL;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.codec.binary.Base64;

public class AtiendeGet implements HttpHandler {
	
	private String construyeRespuesta(String mensaje) {
		return "<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<body>\r\n" + 
				"	<p>" +
				mensaje+
				"</p>	\r\n" + 
				"</body>\r\n" + 
				"</html>";
	}

        @Override
        public void handle(HttpExchange he) throws IOException {
        	String destinoSSL = "https://www.google.es";//"https://notificaciones.060.es:8090/APE-SNE-WS?wsdl";
        	String cacerts = null;
        	String contras = null;//"changeit";
        	String respuestaSSL = null;
                // parse request
                Map<String, Object> parameters = new HashMap<String, Object>();
                URI uriPedida = he.getRequestURI();
                String query = uriPedida.getRawQuery();
                parseQuery(query, parameters);

                // send response
                String response = "";
                for (String key : parameters.keySet()){
                	response += key + " = " + parameters.get(key) + "\n";
                	if(key.equalsIgnoreCase("destinossl")) {
                		destinoSSL = (String)parameters.get(key);                		
                	}
                	if(key.equalsIgnoreCase("rutacacerts")) {
                		cacerts = (String)parameters.get(key); 
                	}
                	if(key.equalsIgnoreCase("contracacerts")) {
                		contras = (String)parameters.get(key); 
                	}
                }
                respuestaSSL = ejecutaPeticionSSL(destinoSSL,cacerts,contras);
                byte[] ba_b64 = Base64.encodeBase64(respuestaSSL.getBytes());
                String s_b64 = new String(ba_b64);
                //String s_b64 = Arrays.toString(ba_b64);
                response="{ \"toma\": \""+s_b64+"\"}";
                //response=respuestaSSL;
                he.getResponseHeaders().set("Content-Type", "appication/json");
                he.sendResponseHeaders(200, response.length());
                OutputStream os = he.getResponseBody();
                os.write(response.getBytes());
                os.close();
         }
        public static void parseQuery(String query, Map<String, 
            Object> parameters) throws UnsupportedEncodingException {

            if (query != null) {
                 String pairs[] = query.split("[&]");
                 for (String pair : pairs) {
                        String param[] = pair.split("[=]");
                        String key = null;
                        String value = null;
                        if (param.length > 0) {
                        key = URLDecoder.decode(param[0], 
                              System.getProperty("file.encoding"));
                        }

                        if (param.length > 1) {
                                 value = URLDecoder.decode(param[1], 
                                 System.getProperty("file.encoding"));
                                 System.out.println("nombre: "+key+" valor: "+value);
                        }

                        if (parameters.containsKey(key)) {
                                 Object obj = parameters.get(key);
                                 if (obj instanceof List<?>) {
                                          List<String> values = (List<String>) obj;
                                          values.add(value);

                                 } else if (obj instanceof String) {
                                          List<String> values = new ArrayList<String>();
                                          values.add((String) obj);
                                          values.add(value);
                                          parameters.put(key, values);
                                 }
                        } else {
                                 parameters.put(key, value);
                        }
                 }
            }
        }
        
        private String ejecutaPeticionSSL(String destinoSSL, String s_rutaCacerts, String s_contrasenia) {
        	if(s_rutaCacerts!=null && s_contrasenia!=null) {
        		System.setProperty("javax.net.ssl.trustStore", s_rutaCacerts);
        		System.setProperty("javax.net.ssl.trustStorePassword", s_contrasenia);
        	}        	
        	String accum = "";
        	try{
    			System.out.println(System.getProperty("java.runtime.version"));
    			System.setProperty("javax.net.ssl.debug","true");
    			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    			System.out.print("0");
    			URL url = new URL(destinoSSL);
    	    	HttpsURLConnection  httpsConnection = (HttpsURLConnection) url.openConnection();
    	    	httpsConnection.setSSLSocketFactory(sslsocketfactory);    
    	    	httpsConnection.connect();
    			System.out.print("1");
    	    	BufferedReader br = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
    	    		String input;    				
    				while ((input = br.readLine()) != null){
    				   accum+=input;//+"\n";
    				  //System.out.println(input);
    				}
    			System.out.print("2");
    	    	br.close();
    	    	httpsConnection.disconnect();
    		}
    		catch(Exception e){
    			System.out.println(e.getMessage());
    			e.printStackTrace();
    			return e.getMessage();
    		}
        	return accum;//"todo ok: "+accum;
        }
}
