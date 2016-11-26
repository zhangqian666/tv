package com.iptv.common;

import android.os.Bundle;
import android.text.TextUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.iptv.common.utils.LogUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class HttpUtils
{
	private static final String TAG = "HttpUtils";
	
    // public static final String HEADER_GZIP_KEY = "Accept-Encoding";

    public static final String HEADER_GZIP_VALUE = "gzip";

    public static final int DEFAULT_TIMEOUT = 30000;

    /** https */
    public static HttpClient getNewHttpsClient()
    {
        try
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);
        }
        catch (Exception e)
        {
            LogUtils.e(TAG, "e.getMessage:" + e.getMessage());
        }
        return new DefaultHttpClient();
    }

    /*
     * param has been urlencoderd
     */
    // public static String sendRequest(String url, String urlParam) throws
    // MalformedURLException, IOException
    // {
    //
    // String loginUrl = url + "?" + urlParam;
    //
    // LogUtils.debug(loginUrl);
    //
    // HttpURLConnection http = (HttpURLConnection) new
    // URL(loginUrl).openConnection();
    // http.setRequestMethod("GET");
    // http.setDoInput(true);
    // InputStream in = http.getInputStream();
    // int byteLen = 256;
    // byte[] buffer = new byte[byteLen];
    // int readCount = 0;
    // String response = new String();
    // while ((readCount = in.read(buffer)) > 0)
    // {
    // response += new String(buffer, 0, readCount);
    // }
    // LogUtils.debug(response);
    // return response;
    // }

    // public String postRequest(String url, String urlParam) throws
    // MalformedURLException, IOException
    // {
    //
    // HttpURLConnection http = (HttpURLConnection) new
    // URL(url).openConnection();
    // http.setRequestMethod("POST");
    // http.setDoInput(true);
    // http.setDoOutput(true);
    // http.connect();
    //
    // OutputStream out = http.getOutputStream();
    // out.write(urlParam.getBytes());
    // out.flush();
    // out.close();
    //
    // InputStream in = http.getInputStream();
    // int byteLen = 256;
    // byte[] buffer = new byte[byteLen];
    // int readCount = 0;
    // String response = new String();
    // while ((readCount = in.read(buffer)) > 0)
    // {
    // response += new String(buffer, 0, readCount);
    // }
    // return response;
    // }

    public static String httpGet(String url, String queryString, int timeout)
    {
    	LogUtils.info(url);
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        String responseData = null;

        if (!TextUtils.isEmpty(queryString))
        {
            url += "?" + queryString;
        }

        LogUtils.d(TAG, url + "");

        HttpClient httpClient;// = new DefaultHttpClient();
        if (url.startsWith("https://"))
        {
            httpClient = getNewHttpsClient();
        }
        else
        {
            httpClient = new DefaultHttpClient();
        }

        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        try
        {

            HttpGet httpGet = new HttpGet(url);

            // zip
            // httpGet.addHeader(HttpUtils.HEADER_GZIP_KEY,
            // HttpUtils.HEADER_GZIP_VALUE);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            // 200~300
            if (statusCode < HttpStatus.SC_OK || statusCode > HttpStatus.SC_MULTIPLE_CHOICES)
            {
                LogUtils.e(TAG, url + " failed: " + httpResponse.getStatusLine());
                return null;
            }

            HttpEntity entity = httpResponse.getEntity();

            // if (entity.getContentEncoding() != null &&
            // entity.getContentEncoding().getValue() != null
            // &&
            // entity.getContentEncoding().getValue().toLowerCase().contains(HttpUtils.HEADER_GZIP_VALUE))
            // {
            // LogUtils.info(entity.getContentEncoding().getValue() + ":" +
            // url);
            // }

            // Read the response body.
            responseData = EntityUtils.toString(entity, HTTP.UTF_8);

            LogUtils.d(TAG, "response:" + responseData);
        }
        catch (Exception e)
        {
            LogUtils.e(TAG, url + "");
        }
        finally
        {
            httpClient = null;
        }

        return responseData;
    }

    /**
     * Using GET method.
     * 
     * @param url The remote URL.
     * @param queryString The query string containing parameters
     * @return Response string.
     * @throws java.io.IOException
     */
    public static String httpGet(String url, String queryString)
    {
        return httpGet(url, queryString, DEFAULT_TIMEOUT);
    }

    /**
     * Using GET method.
     *
     * @param url The remote URL.
     * @param queryString The query string containing parameters
     * @return Response string.
     * @throws java.io.IOException
     */
    public static String httpGets(String url, Bundle param)
    {
        return httpGet(url, generateQuery(param));
    }

    public static String httpPost(String url, Bundle param)
    {
        return httpPost(url, param, DEFAULT_TIMEOUT);
    }

    public static String httpPost(String url, Bundle param, int timeout)
    {
        return httpPost(url, param, timeout, null);
    }

    public static String httpPost(String url, Bundle param, int timeout, String cookie)
    {
        if (TextUtils.isEmpty(url))
        {
            return null;
        }

        LogUtils.d(TAG, url + " " + param);

        // HttpClient httpClient = new DefaultHttpClient();
        HttpClient httpClient;// = new DefaultHttpClient();
        if (url.startsWith("https://"))
        {
            httpClient = getNewHttpsClient();
        }
        else
        {
            httpClient = new DefaultHttpClient();
        }

        HttpParams params = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);

        String responseData = null;
        try
        {
            HttpPost httpPost = new HttpPost(url);

            if (param != null && !param.isEmpty())
            {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                for (String key : param.keySet())
                {
                    nameValuePairs.add(new BasicNameValuePair(key, param.getString(key)));
                }

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            }
            
            if(!TextUtils.isEmpty(cookie))
            {
                httpPost.setHeader("Cookie", cookie);
            }

            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            {
                LogUtils.e(TAG, "HttpPost Method failed: " + httpResponse.getStatusLine());
            }
            responseData = EntityUtils.toString(httpResponse.getEntity());

            LogUtils.d(TAG, "responseData = " + responseData);
        }
        catch (Exception e)
        {
            LogUtils.e(TAG, url);
        }
        finally
        {
            httpClient = null;
        }

        return responseData;
    }

    /** http query */
    public static String generateQuery(Bundle param)
    {
        if (param == null || param.isEmpty())
        {
            return "";
        }

        StringBuffer sbBuffer = new StringBuffer();
        if (param != null)
        {
            int i = 0;
            for (String key : param.keySet())
            {
                if (i == 0)
                {
                    sbBuffer.append("");
                }
                else
                {
                    sbBuffer.append("&");
                }

                try
                {
                    sbBuffer.append(URLEncoder.encode(key, HTTP.UTF_8) + "="
                            + URLEncoder.encode(param.getString(key), HTTP.UTF_8));
                }
                catch (UnsupportedEncodingException e)
                {
                    LogUtils.e(TAG, e.toString());
                }

                i++;
            }
        }
        LogUtils.d(TAG, sbBuffer.toString());
        return sbBuffer.toString();
    }

    static class MySSLSocketFactory extends SSLSocketFactory
    {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException
        {
            super(truststore);

            TrustManager tm = new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] {tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                UnknownHostException
        {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException
        {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
