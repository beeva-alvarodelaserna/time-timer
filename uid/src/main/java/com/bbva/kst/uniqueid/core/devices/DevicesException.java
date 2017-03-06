package com.bbva.kst.uniqueid.core.devices;

public class DevicesException extends Exception {

	public static final int SERVER_ERROR = 0;
	public static final int DUPLICATED_DEVICE_CODE = 1;
	public static final int DEVICE_USER_NOT_AUTHENTICATED_CODE = 2;
	public static final int USER_HAS_ANOTHER_DEVICE_CODE = 3;
	public static final int BAD_CONNECTION_ERROR = 4;

	public int errCode = -1;

	public DevicesException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	public DevicesException(Throwable cause, String message) {
		super(message, cause);
	}

	public DevicesException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	public DevicesException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
