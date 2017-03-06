package com.bbva.kst.uniqueid.dep.saveImagesFiles;

class SaveFilesExceptionFactory {

	static SaveFilesException SaveFilesException() {
		return new SaveFilesException(null);
	}

	static SaveFilesException SaveFilesException(Throwable e) {
		return new SaveFilesException(e);
	}

	static SaveFilesException SaveFilesException(Throwable e, String message) {
		return new SaveFilesException(e, message);
	}

	static SaveFilesException fileDoesNotExist(Throwable e, String message) {
		return new SaveFilesException(e, SaveFilesException.FILE_DOES_NOT_EXIST, "File not exist right now: " + message);
	}

	static SaveFilesException cannotLoadWithGivenId(Throwable e, String message) {
		return new SaveFilesException(e, SaveFilesException.BAD_ID_FILE, message);
	}
}
