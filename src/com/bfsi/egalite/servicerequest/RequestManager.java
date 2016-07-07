
package com.bfsi.egalite.servicerequest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.util.CommonContexts;
@SuppressWarnings("unused")
public class RequestManager {
   
	private static final String CONNECTION_POST = "POST";
    private static final String CONNECTION_GET = "GET";
    private static HttpURLConnection urlconn = null;

    /**
     * Initiate the server request
     * 
     * @param url
     * @param payload
     * @return jsonString
     * @throws ServiceException
     */
    public static String initiateRequest(String url, String requestType,
            String payload) throws ServiceException {
        String response = null;
        try {
            URL urls = new URL(url);
            CookieHandler.setDefault(new CookieManager(null,CookiePolicy.ACCEPT_ALL));
            // urlconn = (HttpURLConnection) urls.openConnection();

            HttpsURLConnection sslUrlconn = (HttpsURLConnection) urls.openConnection();
            sslUrlconn.setSSLSocketFactory(getSSLContext().getSocketFactory());
            urlconn = sslUrlconn;
            urlconn.setDoInput(true);
            urlconn.setDoOutput(true);
            urlconn.setRequestMethod(requestType);
            urlconn.setRequestProperty("Content-Type","application/json");
            
            OutputStream os = urlconn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(payload);
            writer.close();
            os.close();
            response = readDataFromInputStream(urlconn.getInputStream());
            urlconn.disconnect();
        } catch (Exception e) {
        	
           throw new ServiceException(e.getMessage(),e);
        }
        return response;
    }
    
    public static String getSysRequest(String url, String requestType,String payload) throws ServiceException {
        String response = null;
        try {
        	 URL urls = new URL(url);
        	 CookieHandler.setDefault(new CookieManager(null,CookiePolicy.ACCEPT_ALL));
             URLConnection conn = urls.openConnection();
             urlconn = (HttpURLConnection) conn;
             urlconn.setAllowUserInteraction(false);
             urlconn.setInstanceFollowRedirects(true);
             urlconn.setRequestMethod(requestType);
             urlconn.setRequestProperty("Content-Type", "application/json");
//             urlconn.connect();
             OutputStream os = urlconn.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
             writer.write(payload);
             writer.close();
             os.close();
             response = readDataFromInputStream(urlconn.getInputStream());
             urlconn.disconnect();
        } catch (Exception e) {
        	
            throw new ServiceException(e.getMessage(), e);

        }
        return response;
    }

    public static SSLContext getSSLContext() throws CertificateException,
            IOException, KeyStoreException, NoSuchAlgorithmException,
            KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        String registeredUrl = CommonContexts.NONSERCURE_SERVERIP;
        URL url = new URL(CommonContexts.BASE_URL_TYPE_NON + registeredUrl  + CommonContexts.SSL_CERT_PATH);
        URLConnection conn = url.openConnection();
        urlconn = (HttpURLConnection) conn;
        urlconn.setAllowUserInteraction(false);
        urlconn.setInstanceFollowRedirects(true);
        urlconn.setRequestMethod("GET");
        urlconn.setRequestProperty("Content-Type", "application/json");
        urlconn.connect();

        InputStream caInput = urlconn.getInputStream();
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        urlconn.disconnect();

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new X509TrustManager[] {
            new NullX509TrustManager()
        }, null);

        return context;
    }

    /**
     * @param urls
     * @param requestType
     * @return
     * @throws ServiceException
     */
    public static String handleReadData(String urls, String requestType)
            throws ServiceException {
        String response = null;
        try {
            URL url = new URL(urls);
            // URLConnection conn = url.openConnection();
            HttpsURLConnection sslUrlconn = (HttpsURLConnection) url.openConnection();
            sslUrlconn.setSSLSocketFactory(getSSLContext().getSocketFactory());
            urlconn = sslUrlconn;
            // urlconn = (HttpURLConnection) conn;
            urlconn.setAllowUserInteraction(false);
            urlconn.setInstanceFollowRedirects(true);
            urlconn.setRequestMethod(requestType);
            urlconn.setRequestProperty("Content-Type", "application/json");
            urlconn.connect();
            response = readDataFromInputStream(urlconn.getInputStream());
            urlconn.disconnect();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);

        }
        return response;
    }

    /**
     * @param url
     * @param requestType
     * @param payload
     * @return
     * @throws ServiceException
     */
    public static String transactionRequest(String url, String requestType,
            String payload) throws ServiceException {
        String response = null;
        try {
            URL urls = new URL(url);
           urlconn = (HttpURLConnection) urls.openConnection();
            HttpsURLConnection sslUrlconn = (HttpsURLConnection) urls.openConnection();
            sslUrlconn.setSSLSocketFactory(getSSLContext().getSocketFactory());
            urlconn = sslUrlconn;
            urlconn.setDoInput(true);
            urlconn.setDoOutput(true);
            urlconn.setRequestMethod(requestType);
            urlconn.setRequestProperty("Content-Type","application/json");
            OutputStream os = urlconn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    os));
            writer.write(payload);
            writer.close();
            os.close();
            response = readDataFromInputStream(urlconn.getInputStream());
            urlconn.disconnect();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return response;
    }

    public static String enrolmentRequest(String url, String requestType,
            String payload) throws ServiceException {
        String response = null;
        try {
            URL urls = new URL(url);
            HttpsURLConnection sslUrlconn = (HttpsURLConnection) urls.openConnection();
            sslUrlconn.setSSLSocketFactory(getSSLContext().getSocketFactory());
            urlconn = sslUrlconn;
            urlconn.setDoInput(true);
            urlconn.setDoOutput(true);
            urlconn.setRequestMethod(requestType);
            urlconn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = urlconn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    os));
            writer.write(payload);
            writer.close();
            os.close();
            BufferedReader is=new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            response = readDataFromInputStream(is);
            urlconn.disconnect();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return response;
    }

    /**
     * Converts input stream to string.
     * 
     * @param is
     * @return
     */
    public static String readDataFromInputStream(BufferedReader is) {
        StringBuffer sb = new StringBuffer();
   
		int ch;
        try {
           /* sb = new StringBuffer();
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }*/
        	String line;
        	while((line=is.readLine())!=null){
        		sb.append(line);
        	}
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return sb.toString();
    }
    public static String readDataFromInputStream(InputStream is) {
        StringBuffer sb = null;
        int ch;
        try {
           sb = new StringBuffer();
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return sb.toString();
    }

    public static class NullHostNameVerifier implements HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            // Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            String registeredUrl = CommonContexts.NONSERCURE_SERVERIP;
            registeredUrl = registeredUrl.substring(0, registeredUrl.indexOf(":"));
//            return hostname.equals(registeredUrl);
            return true;
        }
    }

    public static class NullX509TrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] cert, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] cert, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
