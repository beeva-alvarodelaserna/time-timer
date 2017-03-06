package com.bbva.kst.uniqueid.core.auth;

public class AuthException extends Exception {

	public static final int SERVER_ERROR = 0;
	public static final int USER_NOT_FOUND_CODE = 1;
	public static final int USER_NOT_AUTHENTICATED_CODE = 2;
	public static final int DUPLICATED_DEVICE_CODE = 3;
	public static final int INCORRECT_CREDENTIALS_CODE = 4;
	public static final int TICKET_TIMED_OUT_CODE = 5;
	public static final int BAD_CONNECTION_CODE = 6;
	public static final int EMAIL_NOT_VALIDATED = 7;
	public static final int NOT_USER_LINKED_TO_DEVICE = 8;

	public int errCode = -1;

	public AuthException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	public AuthException(Throwable cause, String message) {
		super(message, cause);
	}

	public AuthException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	public AuthException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
