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

import android.os.AsyncTask;
import android.os.Bundle;

class AuthenticatorPresenter implements AuthenticatorContract.Presenter {

    private AuthenticatorContract.View mView;

    AuthenticatorPresenter(AuthenticatorContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
    }

    @Override
    public void signIn() {
        final AsyncTask<String, Void, Bundle> signInTask = new SignInTask(mView);
        signInTask.execute();
    }

}
