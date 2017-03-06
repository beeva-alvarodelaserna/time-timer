package com.bbva.kst.uniqueid.dep.imageProcessing;

class ImageProcessingExceptionFactory {

	static ImageProcessingException imageProcessingException(Throwable e) {
		return new ImageProcessingException(e);
	}
	
}
