package com.function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;


/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        String message = "Deleted.";

        try {

            //change the webapp name and path of file (relative to /home)
            URL url = new URL("https://<webappname>.scm.azurewebsites.net/api/vfs/testfile"); //testfile in present under /home
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("If-Match","*");

            String auth = "username" + ":" + "password"; //change it
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));

            String authHeaderValue = "Basic " + new String(encodedAuth);
            conn.setRequestProperty("Authorization", authHeaderValue);
        
            if (conn.getResponseCode() != 200) {

                if(conn.getResponseCode() == 404){
                    message = "File does not exist.";
                }else{
                    throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
                }
            }
    
            BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
    
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
    
            conn.disconnect();
    
          } catch (MalformedURLException e) {
    
            e.printStackTrace();
    
          } catch (IOException e) {
    
            e.printStackTrace();
    
          }
    
        
          return request.createResponseBuilder(HttpStatus.OK).body(message).build();


    }
}
