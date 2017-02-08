/*
 * Copyright (c) 2017 Udi Cohen, Joao Paulo Fernandes Ventura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udinic.accounts_authenticator_example.authentication.authenticator;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.udinic.accounts_authenticator_example.authentication.backend.IParseEndpoint;
import com.udinic.accounts_authenticator_example.authentication.backend.ParseEndpoint;

import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;
import static android.accounts.AccountManager.KEY_PASSWORD;

public class SignInTask extends AsyncTask<String, Void, Bundle> {

    private static final String TAG = SignInTask.class.getSimpleName();

    private static IParseEndpoint sEndpoint;

    private final String mAccountName;
    private final String mAccountType;
    private final String mPassword;

    private AuthenticatorContract.View mView;

    SignInTask(AuthenticatorContract.View view) {
        mView = view;
        mAccountName = mView.getAccountName();
        mAccountType = mView.getAccountType();
        mPassword = mView.getPassword();
        sEndpoint = new ParseEndpoint();
    }

    @Override
    protected void onPreExecute() {
        mView.showDialog();
    }

    @Override
    protected Bundle doInBackground(String... params) {
        Log.d(TAG, "> Started authenticating");
        Bundle authData = new Bundle();

        try {
            String authToken = sEndpoint.userSignIn(mAccountName, mPassword);

            authData.putString(AccountManager.KEY_ACCOUNT_NAME, mAccountName);
            authData.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
            authData.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            authData.putString(KEY_PASSWORD, mPassword);

        } catch (Exception e) {
            authData.putString(KEY_ERROR_MESSAGE, e.getMessage());
        }

        return authData;
    }

    @Override
    protected void onPostExecute(Bundle authData) {
        mView.dismissDialog();

        final Intent intent = new Intent();
        intent.putExtras(authData);

        if (authData.containsKey(KEY_ERROR_MESSAGE)) {
            mView.showError(authData.getString(KEY_ERROR_MESSAGE));
        } else {
            mView.finish(authData);
        }
    }

}
