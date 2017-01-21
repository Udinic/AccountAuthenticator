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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.udinic.accounts_authenticator_example.R;

import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;

/**
 * In charge of the Sign up process. Since it's not an AuthenticatorActivity subclass,
 * it returns the result back to the calling activity, which is an AuthenticatorActivity,
 * and it return the result back to the Authenticator
 */
public class SignUpActivity extends Activity implements OnClickListener, SignUpContract.View {

    public static final String TAG = SignUpActivity.class.getSimpleName();

    private TextView mAccountNameTextView;
    private TextView mAlreadyMemberTextView;
    private TextView mNameTextView;
    private TextView mPasswordTextView;
    private Button mSignUpButton;

    private SignUpContract.Presenter mPresenter;

    private ProgressDialog mProgress;

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAccountNameTextView = (TextView) findViewById(R.id.account_name);
        mNameTextView = (TextView) findViewById(R.id.name);
        mPasswordTextView = (TextView) findViewById(R.id.account_password);

        mSignUpButton = (Button) findViewById(R.id.sign_up);
        mAlreadyMemberTextView = (TextView) findViewById(R.id.sign_in);

        mSignUpButton.setOnClickListener(this);
        mAlreadyMemberTextView.setOnClickListener(this);

        setPresenter(new SignUpPresenter(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
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

    @Override
    public void cancel() {
        setResult(RESULT_CANCELED);
        super.finish();
    }

    @Override
    public void finish(Bundle data) {
        Intent intent = null;
        if (null != data) {
            intent = new Intent();
            intent.putExtras(data);
        }
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void showDialog() {
        if (null == mProgress) {
            final String title = getString(R.string.login_label);
            mProgress = ProgressDialog.show(this, title, "Sign in account...", true);
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
    public void showError(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onClickSignIn() {
        cancel();
    }

    @Override
    public void onClickSignUp() {
        mPresenter.signUp();
    }

    @Override
    public String getAccountName() {
        return mAccountNameTextView.getText().toString().trim();
    }

    @Override
    public String getAccountType() {
        return getIntent().getStringExtra(KEY_ACCOUNT_TYPE);
    }

    @Override
    public String getName() {
        return mNameTextView.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mPasswordTextView.getText().toString().trim();
    }

}
