/*
 * Copyright (c) 2017 Udi Cohen
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

package com.udinic.accounts_authenticator_example.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.udinic.accounts_authenticator_example.R;

import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_PASSWORD;
import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;

import static com.udinic.accounts_authenticator_example.authentication.AccountGeneral.sServerAuthenticate;

/**
 * In charge of the Sign up process. Since it's not an AuthenticatorActivity decendent,
 * it returns the result back to the calling activity, which is an AuthenticatorActivity,
 * and it return the result back to the Authenticator
 */
public class SignUpActivity extends Activity implements OnClickListener {

    public static final String TAG = SignUpActivity.class.getSimpleName();

    private TextView mAccountNameTextView;
    private TextView mAlreadyMemberTextView;
    private TextView mNameTextView;
    private TextView mPasswordTextView;
    private Button mSignUpButton;

    private String mAccountType;

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);

        mAlreadyMemberTextView = (TextView) findViewById(R.id.already_member);
        mAccountNameTextView = (TextView) findViewById(R.id.account_name);
        mNameTextView = (TextView) findViewById(R.id.name);
        mPasswordTextView = (TextView) findViewById(R.id.account_password);
        mSignUpButton = (Button) findViewById(R.id.sign_up);

        mAlreadyMemberTextView.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);

        mAccountType = getIntent().getStringExtra(KEY_ACCOUNT_TYPE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.already_member) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        if (view.getId() == R.id.sign_up) {
            createAccount();
            return;
        }
    }

    private void createAccount() {
        new AsyncTask<String, Void, Intent>() {

            String name = mNameTextView.getText().toString().trim();
            String accountName = mAccountNameTextView.getText().toString().trim();
            String accountPassword = mPasswordTextView.getText().toString().trim();

            @Override
            protected Intent doInBackground(String... params) {

                Log.d(TAG, "> Started authenticating");

                Bundle data = new Bundle();
                try {
                    String authToken = sServerAuthenticate.userSignUp(name, accountName, accountPassword);

                    data.putString(KEY_ACCOUNT_NAME, accountName);
                    data.putString(KEY_ACCOUNT_TYPE, mAccountType);
                    data.putString(KEY_AUTHTOKEN, authToken);
                    data.putString(KEY_PASSWORD, accountPassword);
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }.execute();
    }

}
