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

public class SignInTask implements Continuation<Bundle, Task<Bundle>> {

    private Bundle mAuthData;

    @Override
    public Task<Bundle> then(@NonNull Task<Bundle> task) throws Exception {
        mAuthData = checkNotNull(task.getResult());
        return signUp().continueWithTask(getAuthToken()).continueWith(getResult());
    }

    private Task<AuthResult> signUp() {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(
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
                mAuthData.putString(KEY_AUTHTOKEN, task.getResult().getToken());
                return mAuthData;
            }
        };
    }

}