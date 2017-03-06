package com.bbva.kst.uniqueid.core.token;

public interface Token {

	String getAnonymousToken() throws TokenException;

	String loadUserToken() throws TokenException;

	void storeUserToken(String token);

	void refreshAnonymousToken(String authHeader) throws TokenException;
}
