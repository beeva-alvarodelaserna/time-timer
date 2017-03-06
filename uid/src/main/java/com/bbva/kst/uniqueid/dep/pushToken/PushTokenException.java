package com.bbva.kst.uniqueid.dep.pushToken;

public class PushTokenException extends Exception {

	private int errCode = -1;

	public PushTokenException(Throwable cause) {
		super(cause);
	}

	public PushTokenException(Throwable cause, int errCode) {
		super(cause);
		this.errCode = errCode;
	}

	public PushTokenException(Throwable cause, String message) {
		super(message, cause);
	}

	public PushTokenException(Throwable cause, int errCode, String message) {
		super(message, cause);
		this.errCode = errCode;
	}
}
