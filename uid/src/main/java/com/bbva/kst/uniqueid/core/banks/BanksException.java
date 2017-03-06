package com.bbva.kst.uniqueid.core.banks;

public class BanksException extends Exception {

	private int errCode = -1;

	public BanksException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	private BanksException(Throwable cause, String message) {
		super(message, cause);
	}

	public BanksException(Throwable cause, int errCode) {
		this(cause);
		this.errCode = errCode;
	}

	public BanksException(Throwable cause, int errCode, String message) {
		this(cause, message);
		this.errCode = errCode;
	}
}
