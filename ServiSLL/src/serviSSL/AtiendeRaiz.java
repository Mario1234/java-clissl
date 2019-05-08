package serviSSL;

import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class AtiendeRaiz implements HttpHandler {
    
    private int port;
    
    public AtiendeRaiz(int puerto) {
        port=puerto;
    }
    
    public void handle(com.sun.net.httpserver.HttpExchange he) throws IOException {
            String response = 
            		"<!DOCTYPE html>\n" +
            		"<html>\n" +
            		"<body>\n" +
            		"	<form id='idFormulario1' action='/nuevaOrden' method='POST'>\n" +
            		"		<input type='text' id='idDestSSL' name='nameDestSSL' />\n" +   
            		"		<input type='text' id='idRutaCert' name='nameRutaCert' />\n" + 
            		"		<input type='button' id='idBoton' name='nameBoton' value='Boton' onclick='enviaOrden()'/>\n" +
            		"	</form>\n" +
            		"	<script>\n" + 
            		"		function mandarDatosServidor(j_parametros1){\n" + 
            		"			var s_urlActual = window.location.href;\n" + 
            		"			console.log(s_urlActual);\n" + 
            		"			var s_base = s_urlActual.substr(0, (s_urlActual).lastIndexOf('/'));\n" + 
            		"			console.log(s_base);\n" + 
            		"			var u_url1 = new URL('/nuevaOrden',s_base);\n" + 
            		"			u_url1.search = new URLSearchParams(j_parametros1);\n" + 
            		"			fetch(u_url1)\n" + 
            		"			.then(res => res.json())\n" + 
            		"			.then(json => {\n"+
            		"				var deco = decodeURIComponent(window.atob( json['toma'] ));\n" +
            		"				console.log(deco)})\n" +
            		"			.catch(function (error1) {\n" + 
            		"				console.log('Algo salio mal', error1);\n" + 
            		"			});\n" + 
            		"		}\n" + 
            		"		function enviaOrden(){\n" + 
            		"			var s_destSSL = document.getElementById('idDestSSL').value;\n" + 
            		"			var s_rutaCert = document.getElementById('idRutaCert').value;\n" + 
            		"			var j_parametros1 = {destinossl:s_destSSL, rutacert:s_rutaCert};\n" + 
            		"			mandarDatosServidor(j_parametros1);\n" + 
            		"		}\n" + 
            		"	</script>\n" + 
            		"\n" + 
            		"</body>\n" + 
            		"</html>";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.getBytes());
            os.close();
    }
}
//"			.then(function (respuesta) {\n" + 
//"				console.log(respuesta);\n" + 
//"			})\n" + 
