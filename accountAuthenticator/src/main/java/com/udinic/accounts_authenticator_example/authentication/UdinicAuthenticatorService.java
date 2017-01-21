package com.udinic.accounts_authenticator_example.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UdinicAuthenticatorService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        UdinicAuthenticator authenticator = new UdinicAuthenticator(this);
        return authenticator.getIBinder();
    }

}
