package com.bbva.kst.uniqueid.dep.saveImagesFiles;

public class SaveFilesException extends Exception {

	public static final int FILE_DOES_NOT_EXIST = 0;
	public static final int BAD_ID_FILE = 1;

	public int errCode = -1;

	public SaveFilesException(Throwable cause) {
		super(cause == null ? null : cause.getMessage(), cause);
	}

	public SaveFilesException(Throwable cause, String message) {
		super(message, cause);
	}

	public SaveFilesException(Throwable cause, int errCode, String message) {
		super(message, cause);
		this.errCode = errCode;
	}
}
