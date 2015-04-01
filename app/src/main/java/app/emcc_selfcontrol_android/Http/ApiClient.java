package app.emcc_selfcontrol_android.Http;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import org.json.JSONException;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;

import app.emcc_selfcontrol_android.Application.Messages;
import app.emcc_selfcontrol_android.Application.MyAPP;
import app.emcc_selfcontrol_android.Application.User;


public class ApiClient {
    public static final String UTF_8 = "UTF-8";
    private static final String DOMAIN_NAME = "http://wx.zaixianchuangxin.com:88/zkl";
    public static final String USERPROTACAL = "";

    /**
     * HTTP协议属性配置
     *
     * @return
     */
    private static HttpClient getHttpClient() {

        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        // 设置 连接超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
        // 设置 读数据超时时
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(10000);
        // 设置 字符
        httpClient.getParams().setContentCharset(UTF_8);
        return httpClient;

    }

    private static GetMethod getHttpGet(String url) {
        GetMethod httpGet = new GetMethod(url);
        httpGet.getParams().setSoTimeout(10000);
        httpGet.setRequestHeader("Host", url);
        httpGet.setRequestHeader("Connection", "Keep-Alive");

        return httpGet;
    }

    private static PostMethod getHttpPost(String url) {
        PostMethod httpPost = new PostMethod(url);
        // 设置 请求超时时间
        httpPost.getParams().setSoTimeout(10000);
        httpPost.setRequestHeader("Host", url);
        httpPost.setRequestHeader("Connection", "Keep-Alive");
        return httpPost;
    }

    /**
     * 拼接请求URL
     *
     * @param data
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    private static String makePostData2(Map<String, String> data) throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        for (String name : data.keySet()) {
            postData.append(name);
            postData.append('=');
            postData.append(URLEncoder.encode((String) data.get(name), "utf-8"));
            postData.append('&');
        }
        return postData.deleteCharAt(postData.lastIndexOf("&")).toString();
    }



    /**
     * 拼接URL数据
     *
     * @param p_url
     * @param params
     *            待拼接的数据
     * @return
     */
    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?") < 0)
            url.append('?');
        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
        }

        return url.toString().replace("?&", "?");
    }

    /**
     * get请求
     *
     * @param url
     * @throws
     */
    private static String http_get( String url) {
        HttpClient httpClient = null;
        GetMethod httpGet = null;
        String responseBody = "";
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpGet(url);
                int statusCode = httpClient.executeMethod(httpGet);
                responseBody = httpGet.getResponseBodyAsString();
                break;
            } catch (HttpException e) {
                time++;
                if (time < 3) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问�?
                e.printStackTrace();
            } catch (IOException e) {
                time++;
                if (time < 3) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                    }
                    continue;
                }
                // 发生网络异常
                e.printStackTrace();
            } finally {
                // 释放连接
                httpGet.releaseConnection();
                httpClient = null;
            }
        } while (time < 3);
        return responseBody;
    }

    /**
     * 发送post 请求
     *
     * @param params
     * @return
     * @throws java.io.IOException
     * @throws org.apache.http.client.ClientProtocolException
     */
    public static String http_post(Map<String, Object> params, String url) throws ClientProtocolException, IOException {
        String ret = null;
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (Entry<String, Object> map : params.entrySet()) {
            list.add(new BasicNameValuePair(map.getKey(), (String) map.getValue()));
        }
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            System.out.println("code：200");
            InputStream in = httpResponse.getEntity().getContent(); // 取得字节输入流
            ret = Stream2String(in);
        }
        System.out.println("网络返回信息：" + ret);
        return ret;
    }

    private static String Stream2String(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 16 * 1024); // 强制缓存大小为16KB，一般Java类默认为8KB
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) { // 处理换行符
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 获取网络图片
     *
     * @param url
     * @return
     */
    public static Bitmap getNetBitmap(String url) {
        HttpClient httpClient = null;
        GetMethod httpGet = null;
        Bitmap bitmap = null;
        int time = 0;
        do {
            try {
                httpClient = getHttpClient();
                httpGet = getHttpGet(url);
                int statusCode = httpClient.executeMethod(httpGet);
                InputStream inStream = httpGet.getResponseBodyAsStream();
                bitmap = BitmapFactory.decodeStream(inStream);
                inStream.close();
                break;
            } catch (Exception e) {
                time++;
                if (time < 3) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        return bitmap;
                    }
                    continue;
                }
                // 发生致命的异常，可能是协议不对或者返回的内容有问�?
                e.printStackTrace();
            } finally {
                // 释放连接
                httpGet.releaseConnection();
                httpClient = null;
            }
        } while (time < 3);
        return bitmap;
    }

    public static User getUser(MyAPP appContext, String userName, String passWord) throws IOException, JSONException, NoSuchAlgorithmException, Exception {
        String url = DOMAIN_NAME + "/mobile/loginaction!userLogin.action";
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("userName", userName);
        params.put("password", passWord);
        String str = http_post(params, url);
        Log.e("str==",str);
        return User.parse(str);
    }

    public static Messages register(MyAPP appContext, String uname, String upass,String email) throws Exception {
        String url = DOMAIN_NAME + "/mobile/loginaction!userRegist.action";
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("userName", uname);
        params.put("password", upass);
        params.put("email", email);

        String str = http_post(params, url);
        Log.e("str==",str);
        return Messages.parse(str);
    }


}
