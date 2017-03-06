package com.bbva.kst.uniqueid.dep.saveImagesFiles;

import java.io.File;

/*
 * CacheEntry is meant to hold info about the cached data for fast lookup.
 */
class CacheEntry {

	public File file;
	public long sizeBytes;
	public int hash;
	public long modTime;

	public CacheEntry(File cacheFile, long bytes, int valueHash, long time) {
		file = cacheFile;
		sizeBytes = bytes;
		hash = valueHash;
		modTime = time;
	}
}
