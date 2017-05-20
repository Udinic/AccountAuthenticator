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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_PASSWORD;
import static com.udinic.accounts_authenticator_example.authentication.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

/**
 * The Authenticator activity.
 * <p>
 * Called by the Authenticator and in charge of identifying the user.
 * <p>
 * It sends back to the Authenticator the result.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity implements
        OnClickListener, OnFailureListener, OnSuccessListener<Bundle> {

    private final String LOG_TAG = this.getClass().getSimpleName();


    public final static String KEY_AUTH_TOKEN_TYPE = "AUTH_TYPE";

    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    private final int REQ_SIGNUP = 1;

    private EditText mEmail;
    private EditText mPassword;

    private AccountManager mAccountManager;
    private String mAccountType;
    private String mAuthTokenType;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit) {
            submit();
            return;
        }

        if (view.getId() == R.id.sign_up) {
            // Since there can only be one AuthenticatorActivity, we call the sign up activity, get his results,
            // and return them in setAccountAuthenticatorResult(). See finishLogin().
            Intent signup = new Intent(getBaseContext(), SignUpActivity.class);
            signup.putExtras(getIntent().getExtras());
            startActivityForResult(signup, REQ_SIGNUP);
            return;
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAccountManager = AccountManager.get(getBaseContext());

        mEmail = (EditText) findViewById(R.id.account_name);
        mPassword = (EditText) findViewById(R.id.account_password);

        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.sign_up).setOnClickListener(this);

        final Bundle authData = getIntent().getExtras();
        mAccountType = authData.getString(KEY_ACCOUNT_TYPE);
        mAuthTokenType = authData.getString(KEY_AUTH_TOKEN_TYPE, AUTHTOKEN_TYPE_FULL_ACCESS);

        setAccountName(authData.getString(KEY_ACCOUNT_NAME, null));
    }

    @Override
    public void onFailure(@NonNull Exception error) {
        Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        error.printStackTrace();
    }

    @Override
    public void onSuccess(Bundle authData) {
        final Intent intent = new Intent();
        intent.putExtras(authData);
        finishLogin(intent);
    }

    public void submit() {
        final Bundle authData = new Bundle();
        authData.putString(KEY_ACCOUNT_NAME, getAccountName());
        authData.putString(KEY_ACCOUNT_TYPE, mAccountType);
        authData.putString(KEY_AUTH_TOKEN_TYPE, mAuthTokenType);
        authData.putString(KEY_PASSWORD, getPassword());

        Tasks.forResult(authData)
                .continueWithTask(new SignInTask())
                .addOnFailureListener(this)
                .addOnSuccessListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void finishLogin(Intent intent) {
        Log.d(LOG_TAG, "> finishLogin");

        String accountName = intent.getStringExtra(KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(KEY_PASSWORD);
        final Account account = new Account(accountName, intent.getStringExtra(KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d(LOG_TAG, "> finishLogin > addAccountExplicitly");
            String authToken = intent.getStringExtra(KEY_AUTHTOKEN);

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, mAuthTokenType, authToken);
        } else {
            Log.d(LOG_TAG, "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    private String getAccountName() {
        return mEmail.getText().toString().trim();
    }

    private void setAccountName(String name) {
        mEmail.setText(name);
    }

    private String getPassword() {
        return mPassword.getText().toString().trim();
    }

}
