package com.udinic.accounts_authenticator_example.authentication;

public interface ServerAuthenticate {

    String userSignUp(final String name, final String email, final String password) throws Exception;

    String userSignIn(final String user, final String pass) throws Exception;

}
