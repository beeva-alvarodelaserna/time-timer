package com.bbva.kst.uniqueid.core.register;

public interface Register {

	String createUser(String password, String email, String phoneNumber) throws RegisterException;
}
