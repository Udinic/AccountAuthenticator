package com.udinic.accounts_authenticator_example.authentication;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Udini
 * Date: 21/03/13
 * Time: 15:10
 */
public class UdinicServerAuthenticate implements ServerAuthenticate {

    private final static String BASE_URL = "http://sm-dev.any.do";
    private final static String TAG = UdinicServerAuthenticate.class.getSimpleName();

    public String userSignUp(final String name, final String email, final String pass, String authType) throws Exception {

        Log.d("udini", TAG + " > userSignUp");

        String user = "{\"phoneNumbers\":[],\"email\":\"" + email + "\",\"password\":\"" + pass + "\",\"name\":\"" + name + "\",\"fake\":false,\"anonymous\":false}";

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String authtoken = null;

        String url = BASE_URL + "/user";

        HttpPost httpPost = new HttpPost(url);

        HttpEntity entity = new StringEntity(user);
        httpPost.setEntity(entity);
        httpPost.addHeader("Content-Type", "application/json");

        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_CREATED)
                return userSignIn(email, pass, authType);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Connect to the server with the provided credentials and return the auth token
     * @param user
     * @param pass
     * @param authType
     * @return
     */
    public String userSignIn(final String user, final String pass, String authType) {

        Log.d("udini", "UdinicServerAuthenticate > userSignIn");

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String authtoken = null;

        BasicCookieStore cm = new BasicCookieStore();
        httpClient.setCookieStore(cm);

        String url = BASE_URL + "/j_spring_security_check";

        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("j_username", user));
        nameValuePairs.add(new BasicNameValuePair("j_password", pass));
        nameValuePairs.add(new BasicNameValuePair("_spring_security_remember_me", "on"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());

            List<Cookie> cookies = cm.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SPRING_SECURITY_REMEMBER_ME_COOKIE")) {
                    authtoken = cookie.getValue();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return authtoken;
    }

}
