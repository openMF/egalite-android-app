package com.bfsi.egalite.sync;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.bfsi.egalite.servicerequest.RequestManager;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;

public class FileUpload {

	public FileUpload() {
		
	}
	
	public static int uploadFile(String sourceFileUri, String serverUrl)
	{
		int serverResponseCode = 0;
      //  String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri); 
         
        if (!sourceFile.isFile()) {
             
             return 0;
        }
        else
        {
             try { 
                   // open a URL connection to the Servlet
                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
                 URL url = new URL(serverUrl);
//                 conn = (HttpURLConnection) url.openConnection(); 
                 // URLConnection conn = url.openConnection();
                 HttpsURLConnection sslUrlconn = (HttpsURLConnection) url.openConnection();
                 sslUrlconn.setSSLSocketFactory(RequestManager.getSSLContext().getSocketFactory());
                 conn = sslUrlconn;
                 
                 conn.setDoInput(true); 
                 conn.setDoOutput(true); 
                 conn.setUseCaches(false); 
                 conn.setRequestMethod(Constants.POST);
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("uploaded_file", CommonContexts.LOG_FILE_NAME); 
                  
                 dos = new DataOutputStream(conn.getOutputStream());
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                 dos.writeBytes("Content-Disposition: form-data;name=\"uploadedfile\";filename=\"" + CommonContexts.LOG_FILE_NAME +"\"" + lineEnd);
                 dos.writeBytes(lineEnd);
        
                 // create a buffer of  maximum size
                 bytesAvailable = fileInputStream.available(); 
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
        
                 // read file and write it into form...
                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                 while (bytesRead > 0) {
                      
                   dos.write(buffer, 0, bufferSize);
                   bytesAvailable = fileInputStream.available();
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                  }
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        
                 // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
                 //close the streams //
                 fileInputStream.close();
                 dos.flush();
                 dos.close();
                   
            }  catch (Exception e) {
                 
                e.printStackTrace();
            }
                   
            return serverResponseCode; 
             
         } // End else block 
       } 
	
	@SuppressWarnings("unused")
	public static void uploadImage(byte[] data,String url)
	{
		String response = null;
        try{
        	
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpPost postRequest = new HttpPost(url);
	        
	        ByteArrayBody bab = new ByteArrayBody(data, "Document");
	        MultipartEntity reqEntity = new MultipartEntity(
	                HttpMultipartMode.BROWSER_COMPATIBLE);
	        reqEntity.addPart("Photo", bab);
	        postRequest.setEntity(reqEntity);
	        
	        HttpResponse httpResponse = httpClient.execute(postRequest);
	        HttpEntity entity = httpResponse.getEntity();
	        
        }catch(Exception e)
        {
        	  e.printStackTrace();
        }
	}
}
