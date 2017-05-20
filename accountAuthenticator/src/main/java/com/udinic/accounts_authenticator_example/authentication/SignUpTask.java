/*
 * Copyright (C) 2017 Udi Cohen, Joao Paulo Fernandes Ventura
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udinic.accounts_authenticator_example.authentication;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_PASSWORD;
import static com.google.common.base.Preconditions.checkNotNull;

class SignUpTask implements Continuation<Bundle, Task<Bundle>> {

    private Bundle mAuthData;

    @Override
    public Task<Bundle> then(@NonNull Task<Bundle> task) throws Exception {
        mAuthData = checkNotNull(task.getResult());
        return signUp().continueWithTask(getAuthToken()).continueWith(getResult());
    }

    private Task<AuthResult> signUp() {
        return FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                checkNotNull(mAuthData.getString(KEY_ACCOUNT_NAME)),
                checkNotNull(mAuthData.getString(KEY_PASSWORD))
        );
    }

    private Continuation<AuthResult, Task<GetTokenResult>> getAuthToken() {
        return new Continuation<AuthResult, Task<GetTokenResult>>() {
            @Override
            public Task<GetTokenResult> then(@NonNull Task<AuthResult> task) throws Exception {
                return task.getResult().getUser().getToken(true);
            }
        };
    }

    private Continuation<GetTokenResult, Bundle> getResult() {
        return new Continuation<GetTokenResult, Bundle> () {
            @Override
            public Bundle then(@NonNull Task<GetTokenResult> task) throws Exception {
                mAuthData.putString(KEY_AUTHTOKEN, checkNotNull(task.getResult().getToken()));
                return mAuthData;
            }
        };
    }

}
