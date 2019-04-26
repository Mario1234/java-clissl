package comun;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.security.Security;

import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.DefaultTlsClient;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsClientProtocol;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ClienteBouncyCastle {
	private static String pass = "";
    // Reference: http://boredwookie.net/index.php/blog/how-to-use-bouncy-castle-lightweight-api-s-tlsclient/
    //            bcprov-jdk15on-153.tar\src\org\bouncycastle\crypto\tls\test\TlsClientTest.java
    public static void main(String[] args) throws Exception {
//    	System.setProperty("javax.net.ssl.trustStore", "C:\\JDev.11.116\\jdk160_24\\jre\\lib\\security\\cacerts");
//    	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    	Security.addProvider(new BouncyCastleProvider());
    	//System.setProperty("https.protocols", "TLSv1");
        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        String host = "www.google.es";//"tokendes.central.sepg.minhac.age"
        String service = "/tokenser/tokenser.asmx";
        InetAddress inetAdd = InetAddress.getByName(host);//"https://"+
        Socket socket = new Socket(inetAdd,80);//new Socket("10.8.64.30", 443);
        TlsClientProtocol protocol = new TlsClientProtocol(socket.getInputStream(), socket.getOutputStream(),secureRandom);
        DefaultTlsClient client = new DefaultTlsClient() {
            public TlsAuthentication getAuthentication() throws IOException {
                TlsAuthentication auth = new TlsAuthentication() {
                    // Capture the server certificate information!
                    public void notifyServerCertificate(org.bouncycastle.crypto.tls.Certificate serverCertificate) throws IOException {
                    }

                    public TlsCredentials getClientCredentials(CertificateRequest certificateRequest) throws IOException {
                        return null;
                    }
                };
                return auth;
            }
        };
        protocol.connect(client);

        java.io.OutputStream output = protocol.getOutputStream();
        output.write("GET / HTTP/1.1\r\n".getBytes("UTF-8"));
        output.write(("Host: "+host+"\r\n").getBytes("UTF-8"));
        output.write("Connection: close\r\n".getBytes("UTF-8")); // So the server will close socket immediately.
        output.write("\r\n".getBytes("UTF-8")); // HTTP1.1 requirement: last line must be empty line.
        output.flush();

        java.io.InputStream input = protocol.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null)
        {
            System.out.println(line);
        }
        
        input.close();
        reader.close();
        
        output.close();
        
        //protocol.closeInput();        
        protocol.close();
        socket.close();
    }
}