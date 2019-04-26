package comun;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class ClienteSSL {
	private static String pass = "";
	public static void llamaSSLurl(String s_url) throws Exception{

		SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		//"https://notificaciones.060.es:8090/APE-SNE-WS?wsdl"
		//https://tokendes.central.sepg.minhac.age/tokenser/tokenser.asmx
		//https://www.google.es/
		URL url = new URL(s_url);
    	HttpsURLConnection  httpsConnection = (HttpsURLConnection) url.openConnection();
    	httpsConnection.setSSLSocketFactory(sslsocketfactory);    
    	httpsConnection.connect();
    	
    	BufferedReader br = 
    			new BufferedReader(
    				new InputStreamReader(httpsConnection.getInputStream()));
    					
    		   String input;
    		   String accum = "";
    		   while ((input = br.readLine()) != null){
    			   accum+=input+"\n";
    		      System.out.println(input);
    		   }
    	br.close();
    	
    	httpsConnection.disconnect();	    	
    	throw new Exception("Exito: "+accum);
	}
	
	/* copiar y pegar este codigo:
	 * 
                  try { 
             ClienteSSL.llamaSSLurl("https://notificaciones.060.es:8090/APE-SNE-WS?wsdl");
         }catch(Exception e){
             StringWriter errors = new StringWriter();
             e.printStackTrace(new PrintWriter(errors));
             return errors.toString();
         }
         
	 * copiarlo en cerezo, en cualquier atencion de peticion soap que devuelva String, por ejemplo consultaNivelRenta
	 * en PetWSCostesPersonal
	 */
	
	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.trustStore", "C:\\JDev.11.116\\jdk160_24\\jre\\lib\\security\\cacerts");
    	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    	System.setProperty("javax.net.ssl.debug","true");
		try {
			//https://notificaciones.060.es:8090/APE-SNE-WS?wsdl
			//https://tokendes.central.sepg.minhac.age/tokenser/tokenser.asmx
			//https://www.google.es/
			ClienteSSL.llamaSSLurl("https://notificaciones.060.es:8090/APE-SNE-WS?wsdl");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
    	
	}

}
