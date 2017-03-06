package com.bbva.kst.uniqueid.core.token;

public class TokenException extends Exception {

	public static final int SERVER_ERROR = 0;
	public static final int NO_TOKEN_IS_STORED_ERROR = 1;

	private int errCode = -1;

	public TokenException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	public TokenException(Throwable cause, String message) {
		super(message, cause);
	}

	public TokenException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	public TokenException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
