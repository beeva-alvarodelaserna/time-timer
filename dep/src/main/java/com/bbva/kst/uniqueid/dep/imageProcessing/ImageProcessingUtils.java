package com.bbva.kst.uniqueid.dep.imageProcessing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.bbva.kst.uniqueid.instruments.utils.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageProcessingUtils {

	private static final Bitmap.CompressFormat mFormatJPEG = Bitmap.CompressFormat.JPEG;
	private static final Bitmap.CompressFormat mFormatPNG = Bitmap.CompressFormat.PNG;
	private static final int mQuality = 100;

	private static byte[] decodeBase64(String input) {
		if (!StringUtils.isNullOrEmpty(input)) {
			String aux = "data:image/jpg;base64,";
			byte[] decodedByte;
			if (input.contains(aux)) {
				decodedByte = Base64.decode(input.substring(aux.length()), Base64.NO_WRAP);
			} else {
				decodedByte = Base64.decode(input, Base64.NO_WRAP);
			}
			return decodedByte;
		} else {
			return null;
		}
	}

	private static ByteArrayOutputStream getBitmapJPEGByteArrayOutputStream(Bitmap sourceImage) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		sourceImage.compress(mFormatJPEG, mQuality, out);
		return out;
	}

	private static ByteArrayOutputStream getBitmapPNGByteArrayOutputStream(Bitmap sourceImage) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		sourceImage.compress(mFormatPNG, mQuality, out);
		return out;
	}

	private static byte[] byteArrayImageCompressedToByteArray(byte[] bytes, int width, boolean squared) {
		Bitmap bmp = imageByteArrayToBitmap(bytes, width, squared);
		ByteArrayOutputStream baos = getBitmapJPEGByteArrayOutputStream(bmp);
		bmp.recycle();
		return baos.toByteArray();
	}

	private static Bitmap imageByteArrayToBitmap(byte[] bytes, int width, boolean squared) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		options.inSampleSize = calculateInSampleSize(options.outWidth, width);
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		if (squared) {
			Bitmap bitmap1 = obtainSquareBitmap(bmp);
			if (bmp != bitmap1) {
				bmp.recycle();
			}
			bmp = bitmap1;
		}
		return bmp;
	}

	private static String encodeByteArrayToBase64WithoutDataType(byte[] bytes) {
		if (bytes != null) {
			String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
			return base64.replace("data:image/jpeg;base64,", "");
		} else {
			return "";
		}
	}

	private static Bitmap obtainSquareBitmap(Bitmap bitmap) {
		Bitmap imageRes;
		if (bitmap.getWidth() >= bitmap.getHeight()) {
			imageRes = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - bitmap.getHeight() / 2, 0, bitmap.getHeight(), bitmap.getHeight());

		} else {
			imageRes = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - bitmap.getWidth() / 2, bitmap.getWidth(), bitmap.getWidth());
		}
		return imageRes;
	}

	private static int calculateInSampleSize(
		int imgWidth, int reqWidth) {
		int inSampleSize = 1;
		if (imgWidth > reqWidth) {
			final int halfWidth = imgWidth / 2;
			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	private static Bitmap imageUriToBitmap(String sourcePath, ImageProcessPrefs prefs) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(sourcePath, options);
		options.inSampleSize = calculateInSampleSize(options.outWidth, prefs.width);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(sourcePath, options);
		if (prefs.squared) {
			Bitmap bitmap1 = obtainSquareBitmap(bitmap);
			if (bitmap != bitmap1) {
				bitmap.recycle();
			}
			bitmap = bitmap1;
		}
		return bitmap;
	}
	//private static int calculateInSampleSize(
	//	int imgWidth, int imgHeight, int reqWidth, int reqHeight) {
	//	int inSampleSize = 1;
	//	if (imgHeight > reqHeight || imgWidth > reqWidth) {
	//		final int halfHeight = imgHeight / 2;
	//		final int halfWidth = imgWidth / 2;
	//		// Calculate the largest inSampleSize value that is a power of 2 and keeps both
	//		// height and width larger than the requested height and width.
	//		while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
	//			inSampleSize *= 2;
	//		}
	//	}
	//	return inSampleSize;
	//}

	static String obtainBase64ThumbnailFromBase64String(String imageNotThumb, int width, boolean squared) {
		byte[] decodedByte = decodeBase64(imageNotThumb);
		byte[] thumbnailByte = byteArrayImageCompressedToByteArray(decodedByte, width, squared);
		return encodeByteArrayToBase64WithoutDataType(thumbnailByte);
	}

	static String obtainBase64FromFile(File path) {
		return encodeByteArrayToBase64WithoutDataType(fileToByteArray(path));
	}

	static byte[] fileToByteArray(File file) {
		try {
			byte[] bytesArray = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(bytesArray);//read file into bytes[]
			fis.close();
			return bytesArray;
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	public static OutputStream getByteArrayFromUriJPEGImageCompressed(String sourcePath, ImageProcessPrefs prefs) throws ImageProcessingException {
		Bitmap bitmap = imageUriToBitmap(sourcePath, prefs);
		OutputStream fileSaved = getBitmapJPEGByteArray(bitmap);
		bitmap.recycle();
		return fileSaved;
	}

	public static OutputStream getByteArrayFromUriPNGImageCompressed(String sourcePath, ImageProcessPrefs prefs) throws ImageProcessingException {
		Bitmap bitmap = imageUriToBitmap(sourcePath, prefs);
		OutputStream fileSaved = getBitmapPNGByteArray(bitmap);
		bitmap.recycle();
		return fileSaved;
	}

	public static OutputStream getByteArrayFromByteArrayImageCompressed(byte[] sourcePath, ImageProcessPrefs prefs) throws ImageProcessingException {
		Bitmap bitmap = imageByteArrayToBitmap(sourcePath, prefs.width, prefs.squared);
		OutputStream fileSaved = getBitmapJPEGByteArray(bitmap);
		bitmap.recycle();
		return fileSaved;
	}

	public static OutputStream getBitmapPNGByteArray(Bitmap sourceImage) {
		return getBitmapPNGByteArrayOutputStream(sourceImage);
	}

	private static OutputStream getBitmapJPEGByteArray(Bitmap sourceImage) {
		return getBitmapJPEGByteArrayOutputStream(sourceImage);
	}
	
}
