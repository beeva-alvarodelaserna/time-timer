package com.bbva.kst.uniqueid.dep.imageProcessing;

import com.bbva.kst.uniqueid.dep.saveImagesFiles.SaveFiles;
import com.bbva.kst.uniqueid.dep.saveImagesFiles.SaveFilesException;
import java.io.File;
import java.io.OutputStream;

public class ImageProcessingNative implements ImageProcessing {

	private SaveFiles saveImage;

	public ImageProcessingNative(SaveFiles saveImage) {
		this.saveImage = saveImage;
	}
	
	@Override
	public String obtainBase64ThumbnailFromBase64String(String imageNotThumb, int width, boolean squared) {
		return ImageProcessingUtils.obtainBase64ThumbnailFromBase64String(imageNotThumb, width, squared);
	}

	@Override
	public String obtainBase64FromFile(File file) {
		return ImageProcessingUtils.obtainBase64FromFile(file);
	}

	@Override
	public byte[] fileToByteArray(File file) {
		return ImageProcessingUtils.fileToByteArray(file);
	}

	@Override
	public String saveUriImageJPEGCompressed(String sourcePath, ImageProcessPrefs prefs, boolean cached) throws ImageProcessingException {
		try {
			OutputStream bitmapByteArray = ImageProcessingUtils.getByteArrayFromUriJPEGImageCompressed(sourcePath, prefs);
			return saveImage.saveOnDisk(prefs.id, bitmapByteArray, false);
		} catch (SaveFilesException e) {
			throw ImageProcessingExceptionFactory.imageProcessingException(e);
		}
	}

	@Override
	public String saveUriImagePNGCompressed(String sourcePath, ImageProcessPrefs prefs, boolean cached) throws ImageProcessingException {
		try {
			OutputStream bitmapByteArray = ImageProcessingUtils.getByteArrayFromUriPNGImageCompressed(sourcePath, prefs);
			return saveImage.saveOnDisk(prefs.id, bitmapByteArray, false);
		} catch (SaveFilesException e) {
			throw ImageProcessingExceptionFactory.imageProcessingException(e);
		}
	}

	@Override
	public String saveBytearrayImageCompressed(byte[] sourceByteArray, ImageProcessPrefs prefs, boolean cached) throws ImageProcessingException {
		try {
			OutputStream bitmapByteArray = ImageProcessingUtils.getByteArrayFromByteArrayImageCompressed(sourceByteArray, prefs);
			return saveImage.saveOnDisk(prefs.id, bitmapByteArray, false);
		} catch (SaveFilesException e) {
			throw ImageProcessingExceptionFactory.imageProcessingException(e);
		}
	}
}
