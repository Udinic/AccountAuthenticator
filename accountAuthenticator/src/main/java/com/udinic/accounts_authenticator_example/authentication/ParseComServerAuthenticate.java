package com.udinic.accounts_authenticator_example.authentication;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;


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
        URL url = new URL(Config.URL + "/parse/users");
        HttpURLConnection httpClient = (HttpURLConnection) url.openConnection();

        httpClient.addRequestProperty("X-Parse-Application-Id", Config.APP_ID);
        httpClient.addRequestProperty("X-Parse-REST-API-Key", Config.APP_KEY);
        httpClient.addRequestProperty("Content-Type", "application/json");
        httpClient.setRequestMethod("POST");
        JSONObject params = new JSONObject();
        params.put("username", email);
        params.put("password", pass);
        params.put("phone", "999-999-9999");
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
        String tUrl = Config.URL + "/parse/login";

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

        httpURLConnection.setRequestProperty("X-Parse-Application-Id", Config.APP_ID);
        httpURLConnection.setRequestProperty("X-Parse-REST-API-Key", Config.APP_KEY);


        String authtoken = null;
        try {

            String responseString = httpURLConnection.getResponseMessage();
            Log.d("here", responseString);
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
