package com.bbva.kst.uniqueid.dep.saveImagesFiles;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface SaveFiles {

	File getFromDisk(String id) throws SaveFilesException;

	String saveOnDisk(String id, InputStream inputStream, boolean toCache) throws SaveFilesException;

	String saveOnDisk(String id, String Base64String, boolean toCache);

	String saveOnDisk(String id, File inputFile, boolean toCache) throws SaveFilesException;

	String saveOnDisk(String id, OutputStream out, boolean cached) throws SaveFilesException;

	void deleteOnDisk(String id) throws SaveFilesException;

	void clearCache();
}
