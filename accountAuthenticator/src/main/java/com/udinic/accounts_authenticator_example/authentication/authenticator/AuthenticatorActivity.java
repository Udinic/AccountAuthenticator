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

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.udinic.accounts_authenticator_example.R;
import com.udinic.accounts_authenticator_example.authentication.signup.SignUpActivity;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_PASSWORD;
import static com.udinic.accounts_authenticator_example.authentication.backend.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

/**
 * The Authenticator activity.
 *
 * Called by the Authenticator and in charge of identifing the user.
 *
 * It sends back to the Authenticator the result.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity implements
        AuthenticatorContract.View, OnClickListener {

    // FIXME: Find a OS key constant
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";

    private final int REQ_SIGNUP = 1;

    private final String TAG = this.getClass().getSimpleName();

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    private EditText mAccountName;
    private EditText mPassword;

    private AuthenticatorContract.Presenter mPresenter;

    private ProgressDialog mProgress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(KEY_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);

        mAccountName = (EditText) findViewById(R.id.account_name);
        mPassword = (EditText) findViewById(R.id.account_password);

        if (mAuthTokenType == null)
            mAuthTokenType = AUTHTOKEN_TYPE_FULL_ACCESS;

        if (accountName != null) {
            setAccountName(accountName);
        }

        findViewById(R.id.sign_in).setOnClickListener(this);
        findViewById(R.id.sign_up).setOnClickListener(this);

        setPresenter(new AuthenticatorPresenter(this));
    }

    public void cancel() {
    }

    @Override
    public void finish(Bundle authData) {
        final Intent intent = new Intent();
        intent.putExtras(authData);

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(KEY_PASSWORD);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (isAddAccount(account)) {
            Log.d(TAG, "> finishLogin > addAccountExplicitly");
            String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authTokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authTokenType, authToken);
        } else {
            Log.d(TAG, "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void showDialog() {
        if (null == mProgress) {
            final String title = getString(R.string.signup_title);
            mProgress = ProgressDialog.show(this, title, "Creating account...", true);
        }

        if (!mProgress.isShowing()) {
            mProgress.show();
        }
    }

    @Override
    public void dismissDialog() {
        if ((null != mProgress) && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public String getAccountName() {
        return mAccountName.getText().toString().trim();
    }

    @Override
    public String getAccountType() {
        return getIntent().getStringExtra(KEY_ACCOUNT_TYPE);
    }

    @Override
    public void setAccountName(String accountName) {
        mAccountName.setText(accountName);
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString().trim();
    }

    @Override
    public void onClickSignIn() {
        mPresenter.signIn();
    }

    @Override
    public void onClickSignUp() {
        // Since there can only be one AuthenticatorActivity, we call
        // the sign up activity, get his results, and return them in
        // setAccountAuthenticatorResult(). See finishLogin().
        Intent signUp = new Intent(getBaseContext(), SignUpActivity.class);
        signUp.putExtras(getIntent().getExtras());
        startActivityForResult(signUp, REQ_SIGNUP);
    }

    @Override
    public void setPresenter(AuthenticatorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in) {
            onClickSignIn();
            return;
        }

        if (view.getId() == R.id.sign_up) {
            onClickSignUp();
            return;
        }
    }

    private void finishLogin(Intent intent) {
        Log.d(TAG, "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(KEY_PASSWORD);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (isAddAccount(account)) {
            Log.d(TAG, "> finishLogin > addAccountExplicitly");
            String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authTokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authTokenType, authToken);
        } else {
            Log.d(TAG, "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isAddAccount(Account account) {
        final Account[] accounts = AccountManager.get(this).getAccountsByType(account.type);

        for (int i = 0; i < accounts.length; i++) {
            if (accounts[i].name.equals(account.name)) {
                return false;
            }
        }

        return true;
    }

}
