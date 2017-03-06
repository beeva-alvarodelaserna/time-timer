package com.bbva.kst.uniqueid.core.items;

public class ItemsException extends Exception {

	public static final int SERVER_ERROR = 0;
	public static final int CANT_FIND_ITEM_TYPE = 1;
	public static final int CRYPTO_USER_NOT_AUTHENTICATED_CODE = 2;
	public static final int PAGE_LIMIT_REACHED_CODE = 3;
	public static final int BAD_CONNECTION_CODE = 4;
	public static final int MAXIMUM_STACK_CODE = 5;
	public static final int TICKET_TIMED_OUT_CODE = 6;

	public int errCode = -1;

	public ItemsException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	public ItemsException(Throwable cause, String message) {
		super(message, cause);
	}

	public ItemsException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	public ItemsException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
