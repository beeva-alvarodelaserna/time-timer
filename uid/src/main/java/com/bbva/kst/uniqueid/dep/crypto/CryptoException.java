package com.bbva.kst.uniqueid.dep.crypto;

public class CryptoException extends Exception {

	public static final int NO_KEY_FOUND_CODE = 1;
	public static final int USER_NOT_AUTHENTICATED_CODE = 2;
	public static final int NO_CIPHER_RETRIEVED_CODE = 3;
	public static final int NO_ENCODING_RESULT_CODE = 4;

	public int errCode = -1;

	public CryptoException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	public CryptoException(Throwable cause, String message) {
		super(message, cause);
	}

	public CryptoException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	public CryptoException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
