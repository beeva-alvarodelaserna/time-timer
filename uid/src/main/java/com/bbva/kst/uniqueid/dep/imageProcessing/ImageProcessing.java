package com.bbva.kst.uniqueid.dep.imageProcessing;

import java.io.File;

public interface ImageProcessing {

	int MAXIMUM_IMAGE_WIDTH = 600;
	int AVATAR_WIDTH = 300;
	int ITEM_THUMBNAIL_WIDTH = 150;

	String obtainBase64ThumbnailFromBase64String(String imageNotThumb, int width, boolean squared);

	String obtainBase64FromFile(File path);

	byte[] fileToByteArray(File file);

	String saveUriImageJPEGCompressed(String sourcePath, ImageProcessPrefs prefs, boolean cached) throws ImageProcessingException;

	String saveUriImagePNGCompressed(String sourcePath, ImageProcessPrefs prefs, boolean cached) throws ImageProcessingException;

	String saveBytearrayImageCompressed(byte[] sourcePath, ImageProcessPrefs prefs, boolean cached) throws ImageProcessingException;
}
