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
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_PASSWORD;
import static android.accounts.AccountManager.KEY_USERDATA;

public class SignUpPresenter implements SignUpContract.Presenter {

    private static final String TAG = SignUpPresenter.class.getSimpleName();

    private final SignUpContract.View mView;

    SignUpPresenter(SignUpContract.View view) {
        mView = view;
    }

    @Override
    public void signUp() {
        Log.i(TAG, "signUp");
        Bundle data = new Bundle();
        data.putString(KEY_ACCOUNT_NAME, mView.getAccountName());
        data.putString(KEY_ACCOUNT_TYPE, mView.getAccountType());
        data.putString(KEY_USERDATA, mView.getPassword());
        data.putString(KEY_PASSWORD, mView.getPassword());

        AsyncTask<Bundle, Void, Bundle> SignUpTask = new SignUpTask(mView);
        SignUpTask.execute(data);
    }

    @Override
    public void start() {
        Log.d(TAG, "start");
    }

}
