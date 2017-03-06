package com.bbva.kst.uniqueid.dep.saveImagesFiles;

import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileCachePolicy {

	private Bitmap.CompressFormat mFormat;
	private int mQuality;

	public FileCachePolicy() {
		mFormat = Bitmap.CompressFormat.PNG;
		mQuality = 100;
	}

	public boolean write(File outputFile, InputStream inputStream) throws IOException {
		boolean success = false;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outputFile);
			int read;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			success = true;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return success;
	}

	public boolean write(File outputFile, Bitmap bitmap) throws IOException {
		boolean success = false;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outputFile);
			bitmap.compress(mFormat, mQuality, out);
			success = true;
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return success;
	}

	public File read(File inputFile) throws IOException {
		return inputFile;
	}

	public long size(InputStream value) {
		try {
			return value.available();
		} catch (IOException e) {
			return 0;
		}
	}

	public long size(Bitmap value) {
		return value.getByteCount();
	}

}
