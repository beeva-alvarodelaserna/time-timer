package com.bbva.kst.uniqueid.instruments.logger;

public interface Logger {
	//String KEY_USERNAME = "USERNAME";
	//String KEY_USER_FULL_NAME = "USER_FULL_NAME";
	//String KEY_CONTRACT_ID = "CONTRACT_ID";
	//String KEY_CONTRACT_USER_FULL_NAME = "CONTRACT_USER_FULL_NAME";

	void debugMessage(String tag, String msg);

	void debugError(String tag, String msg, Throwable e);

	void nonFatalError(String tag, String msg, Throwable e);

	void setKey(String key, String value);

}
