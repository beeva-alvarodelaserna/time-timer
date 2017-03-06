package com.bbva.kst.uniqueid.instruments;

abstract class CodeException extends Exception {

	private final int errCode;
	private final String message;

	protected CodeException(Throwable e) {
		super(e);
		boolean isInstanceOf = e instanceof CodeException;
		errCode = isInstanceOf ? ((CodeException) e).errCode : -1;
		message = isInstanceOf ? ((CodeException) e).message : "";
	}

	protected CodeException(Throwable e, int errorCode) {
		super(e);
		this.errCode = errorCode;
		this.message = "";
	}

}
