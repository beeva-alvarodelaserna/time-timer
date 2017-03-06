package com.bbva.kst.uniqueid.core.register;

public class RegisterException extends Exception {

	public static final int SERVER_ERROR = 0;
	public static final int NOT_FOUND_DEVICE_CODE = 1;
	public static final int DUPLICATED_USER_CODE = 2;
	public static final int CRYPTO_USER_NOT_AUTHENTICATED_CODE = 7;
	public static final int PHONE_TOO_LONG_CODE = 8;
	public static final int DEVICE_ASSIGNED_USER_CODE = 9;
	public static final int BAD_CONNECTION_CODE = 11;
	public static final int TICKET_TIMED_OUT_CODE = 12;
	public static final int PHONE_TOO_SHORT_CODE = 13;
	public static final int WRONG_PHONE_FORMAT_CODE = 14;

	public int errCode = -1;

	public RegisterException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	public RegisterException(Throwable cause, String message) {
		super(message, cause);
	}

	public RegisterException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	public RegisterException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
