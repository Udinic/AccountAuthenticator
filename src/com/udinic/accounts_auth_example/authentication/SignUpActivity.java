package com.udinic.accounts_auth_example.authentication;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.udinic.accounts_auth_example.R;

import static com.udinic.accounts_auth_example.authentication.AccountGeneral.sServerAuthenticate;
import static com.udinic.accounts_auth_example.authentication.AuthenticatorActivity.ARG_ACCOUNT_TYPE;
import static com.udinic.accounts_auth_example.authentication.AuthenticatorActivity.PARAM_USER_PASS;

/**
 * Created with IntelliJ IDEA.
 * User: udinic
 * Date: 3/25/13
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class SignUpActivity extends Activity {

    private String TAG = getClass().getSimpleName();
    private String mAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

        setContentView(R.layout.act_register);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {

        // Validation!

        new AsyncTask<String, Void, Intent>() {

            String name = ((TextView) findViewById(R.id.name)).getText().toString().trim();
            String accountName = ((TextView) findViewById(R.id.accountName)).getText().toString().trim();
            String accountPassword = ((TextView) findViewById(R.id.accountPassword)).getText().toString().trim();

            @Override
            protected Intent doInBackground(String... params) {

                Log.d("udini", TAG + "> Started authenticating");

                String authtoken = null;
                try {
                    authtoken = sServerAuthenticate.userSignUp(name, accountName, accountPassword, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final Intent res = new Intent();
                res.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
                res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
                res.putExtra(AccountManager.KEY_AUTHTOKEN, authtoken);
                res.putExtra(PARAM_USER_PASS, accountPassword);

                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                setResult(RESULT_OK, intent);
                finish();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
