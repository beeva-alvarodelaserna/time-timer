package com.bbva.kst.uniqueid.dep.imageProcessing;

public class ImageProcessingException extends Exception {

	private int errCode = -1;

	public ImageProcessingException(Throwable cause) {
		super(cause);
	}

	public ImageProcessingException(Throwable cause, int errCode) {
		super(cause);
		this.errCode = errCode;
	}

	public ImageProcessingException(Throwable cause, String message) {
		super(message, cause);
	}

	public ImageProcessingException(Throwable cause, int errCode, String message) {
		super(message, cause);
		this.errCode = errCode;
	}
}
