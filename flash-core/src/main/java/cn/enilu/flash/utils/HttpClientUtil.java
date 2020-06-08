package cn.enilu.flash.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpClientUtil {

    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";


    private static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

    private static final CloseableHttpClient httpClient = HttpClients
            .custom()
            .setConnectionManager(connManager)
            .setConnectionManagerShared(true)
            .build();

    private static final RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(2000)
            .setSocketTimeout(10000).build();

    /**
     * 发送get请求；带请求头和请求参数
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     * @throws Exception
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        String result = null;
        // 创建httpClient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = createSSLClientDefault();

        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setConfig(requestConfig);
        // 设置请求头
        packageHeader(headers, httpGet);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求并获得响应结果
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return result;
    }

    /**
     * POST请求 带参数
     *
     * @param url        请求地址
     * @param headers    请求头参数
     * @param parameters 参数
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, String> parameters) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        if (null != parameters) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity entity = null;
        String result = null;
        // 设置请求头
        packageHeader(headers, httpPost);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            entity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return result;
    }

    /**
     * POST请求 带参数
     *
     * @param url        请求地址
     * @param headers    请求头参数
     * @param parameters 参数
     * @return
     */
    public static String doPost(String url, Map<String, String> headers, String parameters) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        String result = null;
        // 设置请求头
        packageHeader(headers, httpPost);
        // 创建httpResponse对象
        HttpResponse httpResponse = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(60000)
                    .setSocketTimeout(60000).build();
            httpPost.setConfig(requestConfig);
            StringEntity postingString = new StringEntity(parameters, "UTF-8");
            httpPost.setEntity(postingString);
            httpPost.setHeader("Content-type", "application/json");

            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            // 释放资源
//            release(httpResponse, httpClient);
        }
        return result;
    }

    /**
     * POST请求   携带Json格式的参数
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static String doPostJson(String url, Object param) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        httpPost.setConfig(requestConfig);
        String parameter = JSON.toJSONString(param);
        StringEntity se = null;
        try {
            System.out.println(parameter);
            se = new StringEntity(parameter);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        se.setContentType(CONTENT_TYPE_TEXT_JSON);
        httpPost.setEntity(se);

        CloseableHttpResponse response = null;
        String result = null;
        try {
            response = httpClient.execute(httpPost);

            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 封装请求头
     *
     * @param params
     * @param httpMethod
     */
    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

    /**
     * 发送get请求
     *
     * @param url     请求地址
     * @param headers 请求头集合
     * @param params  请求参数集合
     * @return
     * @throws Exception
     */
    public static HttpResponse doGetPing(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        String result = null;
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建访问的地址
        URIBuilder uriBuilder = new URIBuilder(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
        }
        // 创建http对象
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setConfig(requestConfig);
        // 设置请求头
        packageHeader(headers, httpGet);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求并获得响应结果
            httpResponse = httpClient.execute(httpGet);

            System.out.println(EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return httpResponse;
    }

    /**
     * POST请求
     *
     * @param url        请求地址
     * @param headers    请求头参数
     * @param parameters 参数
     * @return
     */
    public static HttpResponse doPostPing(String url, Map<String, String> headers, Map<String, String> parameters) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        if (null != parameters) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity entity = null;
        String result = null;
        // 设置请求头
        packageHeader(headers, httpPost);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            entity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return httpResponse;
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    null, new TrustStrategy() {
                        //信任所有
                        public boolean isTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                            return true;
                        }
                    }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
}
