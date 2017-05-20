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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_PASSWORD;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.udinic.accounts_authenticator_example.authentication.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
import static com.udinic.accounts_authenticator_example.authentication.AuthenticatorActivity.KEY_AUTH_TOKEN_TYPE;

/**
 * In charge of the Sign up process. Since it's not an AuthenticatorActivity descendant,
 * it returns the result back to the calling activity, which is an AuthenticatorActivity,
 * and it return the result back to the Authenticator
 */
public class SignUpActivity extends Activity implements OnClickListener, OnFailureListener,
        OnSuccessListener<Bundle> {

    private static final String LOG_TAG = SignUpActivity.class.getSimpleName();

    private EditText mEmail;
    private EditText mName;
    private EditText mPassword;

    private String mAccountType;
    private String mAuthTokenType;

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.already_member) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        if (view.getId() == R.id.submit) {
            createAccount();
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.account_name);
        mPassword = (EditText) findViewById(R.id.account_password);

        findViewById(R.id.already_member).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);

        Bundle authData = getIntent().getExtras();
        mAccountType = checkNotNull(authData.getString(KEY_ACCOUNT_TYPE));
        mAuthTokenType = authData.getString(KEY_AUTH_TOKEN_TYPE, AUTHTOKEN_TYPE_FULL_ACCESS);
    }

    @Override
    public void onFailure(@NonNull Exception error) {
        Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(@NonNull final Bundle authData) {
        final Intent intent = new Intent();
        intent.putExtras(checkNotNull(authData));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void createAccount() {
        Bundle authData = new Bundle();
        authData.putString(KEY_ACCOUNT_NAME, getAccountName());
        authData.putString(KEY_ACCOUNT_TYPE, mAccountType);
        authData.putString(KEY_PASSWORD, getPassword());
        authData.putString(KEY_AUTH_TOKEN_TYPE, mAuthTokenType);

        Tasks.forResult(authData).continueWithTask(new SignUpTask())
                .addOnFailureListener(this)
                .addOnSuccessListener(this);
    }

    private String getAccountName() {
        return mEmail.getText().toString().trim();
    }

    private String getPassword() {
        return mPassword.getText().toString().trim();
    }

}
