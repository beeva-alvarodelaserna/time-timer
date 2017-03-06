package com.bbva.kst.uniqueid.core.health;

public class HealthException extends Exception {

	public static final int BAD_CONNECTION_CODE = 1;
	public int errCode = -1;

	HealthException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	HealthException(Throwable cause, String message) {
		super(message, cause);
	}

	HealthException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	HealthException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
