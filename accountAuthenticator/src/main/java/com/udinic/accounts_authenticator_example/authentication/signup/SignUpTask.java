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


package com.udinic.accounts_authenticator_example.authentication.signup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;
import static android.accounts.AccountManager.KEY_PASSWORD;
import static android.accounts.AccountManager.KEY_USERDATA;
import static com.udinic.accounts_authenticator_example.authentication.backend.AccountGeneral.sServerAuthenticate;

class SignUpTask extends AsyncTask<Bundle, Void, Bundle> {

    private static final String TAG = SignUpTask.class.getSimpleName();

    private final SignUpContract.View mView;

    SignUpTask(SignUpContract.View view) {
        mView = view;
    }

    @Override
    protected void onPreExecute() {
        mView.showDialog();
    }

    @Override
    protected Bundle doInBackground(Bundle... params) {
        Log.d(TAG, "SignUpTask");
        final Bundle authData = params[0];
        final String accountName = authData.getString(KEY_ACCOUNT_NAME);
        final String name = authData.getString(KEY_USERDATA);
        final String password = authData.getString(KEY_PASSWORD);

        try {
            String authToken = sServerAuthenticate.userSignUp(name, accountName, password);
            authData.putString(KEY_AUTHTOKEN, authToken);
        } catch (Exception e) {
            authData.clear();
            authData.putString(KEY_ERROR_MESSAGE, e.getMessage());
        }

        return authData;
    }

    @Override
    protected void onPostExecute(Bundle data) {
        mView.dismissDialog();

        if (data.containsKey(KEY_ERROR_MESSAGE)) {
            mView.showError(data.getString(KEY_ERROR_MESSAGE));
        } else {
            mView.finish(data);
        }
    }

}
