package com.bbva.kst.uniqueid.dep.saveImagesFiles;

import android.content.Context;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SaveImage implements SaveFiles {

	private static final String DISK_CACHE_PREFFIX = "UniqueId_DISK_CACHE_";
	private static final String DISK_FILES_PREFFIX = "UniqueId_DISK_FILES_";
	private static final long MAX_CACHE_SIZE = 10 * 1024 * 1024;
	private File storageDir;
	private DiskCache cache;

	public SaveImage(Context context, String FolderName) {
		storageDir = context.getFilesDir();
		File outputDir = new File(context.getCacheDir(), FolderName); // context being the Activity pointer
		cache = DiskCache.newInstanceNoTimeLimit(MAX_CACHE_SIZE, outputDir);
	}

	@Override
	public String saveOnDisk(String id, InputStream image, boolean toCache) {
		String idOut;
		if (toCache) {
			idOut = DISK_CACHE_PREFFIX + checkId(id);
			cache.put(idOut, image);
		} else {
			idOut = DISK_FILES_PREFFIX + checkId(id);
		}
		return idOut;
	}

	@Override
	public String saveOnDisk(String id, OutputStream inputFile, boolean toCache) throws SaveFilesException {
		String idOut;
		if (toCache) {
			idOut = DISK_CACHE_PREFFIX + checkId(id);
			cache.put(idOut, new ByteArrayInputStream(((ByteArrayOutputStream) inputFile).toByteArray()));
		} else {
			idOut = DISK_FILES_PREFFIX + checkId(id);
			saveOnFilesDisk(idOut, inputFile);
		}
		return idOut;
	}

	private File saveOnFilesDisk(String id, OutputStream inputFile) throws SaveFilesException {
		try {
			File outFile = new File(storageDir, id);
			FileOutputStream outStream = new FileOutputStream(outFile);
			((ByteArrayOutputStream) inputFile).writeTo(outStream);
			return outFile;
		} catch (IOException e) {
			throw SaveFilesExceptionFactory.SaveFilesException(e);
		}
	}

	@Override
	public String saveOnDisk(String id, String Base64String, boolean toCache) {
		ByteArrayInputStream inputFile = new ByteArrayInputStream(Base64.decode(Base64String, Base64.NO_WRAP));
		return saveOnDisk(id, inputFile, toCache);
	}

	@Override
	public String saveOnDisk(String id, File inputFile, boolean toCache) throws SaveFilesException {
		String idOut;
		if (toCache) {
			idOut = DISK_CACHE_PREFFIX + checkId(id);
			cache.put(idOut, inputFile);
		} else {
			idOut = DISK_FILES_PREFFIX + checkId(id);
			saveOnFilesDisk(idOut, inputFile);
		}
		return idOut;
	}

	private String checkId(String id) {
		if (id != null && id.startsWith(DISK_CACHE_PREFFIX)) {
			id = id.replace(DISK_CACHE_PREFFIX, "");
		} else if (id != null && id.startsWith(DISK_FILES_PREFFIX)) {
			id = id.replace(DISK_FILES_PREFFIX, "");
		}
		return id;
	}

	private File saveOnFilesDisk(String id, File inputFile) throws SaveFilesException {
		InputStream in = null;
		OutputStream out = null;
		File outFile = new File(storageDir, id);
		try {
			in = new FileInputStream(inputFile);
			out = new FileOutputStream(outFile);
			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException ex) {
			throw SaveFilesExceptionFactory.SaveFilesException(ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				throw SaveFilesExceptionFactory.SaveFilesException(ex);
			}
		}
		return outFile;
	}

	@Override
	public File getFromDisk(String id) throws SaveFilesException {
		if (id != null && id.startsWith(DISK_CACHE_PREFFIX)) {
			File outFile = cache.get(id);
			if (outFile != null) {
				return outFile;
			} else {
				throw SaveFilesExceptionFactory.fileDoesNotExist(null, "id_file = " + id);
			}
		} else if (id != null && id.startsWith(DISK_FILES_PREFFIX)) {
			return getFromFilesDisk(id);
		}
		throw SaveFilesExceptionFactory.cannotLoadWithGivenId(null, "id_file = " + id);
	}

	private File getFromFilesDisk(String id) throws SaveFilesException {
		File outFile = new File(storageDir, id);
		if (outFile.exists()) {
			return outFile;
		}
		throw SaveFilesExceptionFactory.fileDoesNotExist(null, "id_file = " + id);
	}

	@Override
	public void deleteOnDisk(String id) {
		if (id != null && id.startsWith(DISK_CACHE_PREFFIX)) {
			cache.remove(id);
		} else if (id != null && id.startsWith(DISK_FILES_PREFFIX)) {
			deleteFromFilesDisk(id);
		}
	}

	private void deleteFromFilesDisk(String id) {
		File outFile = new File(storageDir, id);
		if (outFile.exists()) {
			outFile.delete();
		}
	}

	@Override
	public void clearCache() {
		cache.clear();
	}
}
