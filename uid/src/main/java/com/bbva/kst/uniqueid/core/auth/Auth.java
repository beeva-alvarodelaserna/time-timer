package com.bbva.kst.uniqueid.core.auth;

public interface Auth {

	void authorizeDevice() throws AuthException;

	boolean userSignIn(String userEmail, String password) throws AuthException;

	void authorizeFingerprintWeb(String ticket, String temporal_public_key) throws AuthException;
}
