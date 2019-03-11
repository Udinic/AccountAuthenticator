package com.udinic.accounts_authenticator_example.authentication;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles the comminication with Parse.com
 * <p>
 * User: udinic
 * Date: 3/27/13
 * Time: 3:30 AM
 */
public class ParseComServerAuthenticate implements ServerAuthenticate {
    @Override
    public String userSignUp(String name, String email, String pass, String authType) throws Exception {

        //https://api.parse.com/1/users
        URL url=new URL("https://applicationauthenticator.herokuapp.com/parse/users");
        HttpURLConnection httpClient = (HttpURLConnection) url.openConnection();

        httpClient.addRequestProperty("X-Parse-Application-Id", "myAppId");
        httpClient.addRequestProperty("X-Parse-REST-API-Key", "myMasterKey");
        httpClient.addRequestProperty("Content-Type", "application/json");
        httpClient.setRequestMethod("POST");
        JSONObject params=new JSONObject();
        params.put("username",email);
        params.put("password",pass);
        params.put("phone","999-999-9999");
        OutputStreamWriter wr = new OutputStreamWriter(httpClient.getOutputStream());
        wr.write(params.toString());
        wr.flush();
        String authtoken = null;
        try {
            String responseString = httpClient.getResponseMessage();

            if (httpClient.getResponseCode() != 201) {
                ParseComError error = new Gson().fromJson(responseString, ParseComError.class);
                throw new Exception(responseString);
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            User createdUser = new Gson().fromJson(response.toString(), User.class);

            authtoken = createdUser.sessionToken;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return authtoken;
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {

        Log.d("udini", "userSignIn");

        //https://api.parse.com/1/
        String tUrl="https://applicationauthenticator.herokuapp.com/parse/login";
        String query = null;
        try {
            query = String.format("%s=%s&%s=%s", "username", URLEncoder.encode(user, "UTF-8"), "password", pass);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tUrl += "?" + query;

        URL url = new URL(tUrl);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");

        httpURLConnection.setRequestProperty("X-Parse-Application-Id", "myAppId");
        httpURLConnection.setRequestProperty("X-Parse-REST-API-Key", "myMasterKey");


//        Map<String,Object> params = new LinkedHashMap<>();
//        params.put("username", user);
//        params.put("password", pass);
//        StringBuilder postData = new StringBuilder();
//        for (Map.Entry<String,Object> param : params.entrySet()) {
//            if (postData.length() != 0) postData.append('&');
//            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
//            postData.append('=');
//            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
//        }
//        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
//        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
//        httpURLConnection.getOutputStream().write(postDataBytes);
//        httpGet.getParams().setParameter("username", user).setParameter("password", pass);

        String authtoken = null;
        try {

            String responseString = httpURLConnection.getResponseMessage();
            if (httpURLConnection.getResponseCode() != 200) {
                //ParseComError error = new Gson().fromJson(httpURLConnection.getResponseMessage(), ParseComError.class);
                throw new Exception(responseString);
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            User loggedUser = new Gson().fromJson(response.toString(), User.class);
            authtoken = loggedUser.sessionToken;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return authtoken;
    }


    private class ParseComError implements Serializable {
        int code;
        String error;
    }

    private class User implements Serializable {

        private String firstName;
        private String lastName;
        private String username;
        private String phone;
        private String objectId;
        public String sessionToken;
        private String gravatarId;
        private String avatarUrl;


        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public void setSessionToken(String sessionToken) {
            this.sessionToken = sessionToken;
        }

        public String getGravatarId() {
            return gravatarId;
        }

        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
