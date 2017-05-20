/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udinic.accounts_authenticator_example.authentication;

import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static android.accounts.AccountManager.ERROR_CODE_CANCELED;
import static android.accounts.AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE;

/**
 * Base class for implementing an Activity that is used to help implement an
 * AbstractAccountAuthenticator. If the AbstractAccountAuthenticator needs to use an activity
 * to handle the request then it can have the activity extend AccountAuthenticatorActivity.
 * The AbstractAccountAuthenticator passes in the response to the intent using the following:
 * <pre>
 *      intent.putExtra({@link AccountManager#KEY_ACCOUNT_AUTHENTICATOR_RESPONSE}, response);
 * </pre>
 * The activity then sets the result that is to be handed to the response via
 * {@link #setAccountAuthenticatorResult(android.os.Bundle)}.
 * This result will be sent as the result of the request when the activity finishes. If this
 * is never set or if it is set to null then error {@link AccountManager#ERROR_CODE_CANCELED}
 * will be called on the response.
 */
public abstract class AccountAuthenticatorActivity extends AppCompatActivity {

    private static final String ERROR_MESSAGE = "canceled";

    private AccountAuthenticatorResponse mCallback = null;
    private Bundle mAuthData = null;

    /**
     * Set the result that is to be sent as the result of the request that caused this
     * Activity to be launched. If result is null or this method is never called then
     * the request will be canceled.
     * @param result this is returned as the result of the AbstractAccountAuthenticator request
     */
    public final void setAccountAuthenticatorResult(Bundle result) {
        mAuthData = result;
    }

    /**
     * Retrieves the AccountAuthenticatorResponse from either the intent of the savedInstanceState,
     * if the savedInstanceState is non-zero.
     * @param savedInstanceState the save instance data of this Activity, may be null
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallback = getIntent().getParcelableExtra(KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (null != mCallback) {
            mCallback.onRequestContinued();
        }
    }

    /**
     * Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present.
     */
    public void finish() {
        if (null == mCallback) {
            super.finish();
            return;
        }

        // Send the result bundle back if set, otherwise send an error.
        if (null == mAuthData) {
            mCallback.onError(ERROR_CODE_CANCELED, ERROR_MESSAGE);
        } else {
            mCallback.onResult(mAuthData);
        }

        mCallback = null;
        super.finish();
    }

}

