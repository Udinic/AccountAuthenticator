package com.udinic.accounts_authenticator_example.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created with IntelliJ IDEA.
 * User: Udini
 * Date: 19/03/13
 * Time: 19:10
 */
public class UdinicAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        UdinicAuthenticator authenticator = new UdinicAuthenticator(this);
        return authenticator.getIBinder();
    }
}
