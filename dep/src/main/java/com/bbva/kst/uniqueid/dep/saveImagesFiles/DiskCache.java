package com.bbva.kst.uniqueid.dep.saveImagesFiles;

import android.graphics.Bitmap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DiskCache implements Cache<String, InputStream, File> {

	private static final long FOREVER = -1;

	private long mMaxSize;
	private File mCacheDirectory;
	private FileCachePolicy mDiskCachePolicy;
	private long mCurrentSize;
	private LinkedHashMap<Integer, CacheEntry> mFilesInCache;
	private long mMaxCacheTime;

	public static DiskCache newInstance(long maxSizeBytes, long maxCacheTime, File cacheDir) {
		return new DiskCache(maxSizeBytes, maxCacheTime, cacheDir);
	}

	public static DiskCache newInstanceNoTimeLimit(long maxSizeBytes, File cacheDir) {
		return new DiskCache(maxSizeBytes, FOREVER, cacheDir);
	}

	private DiskCache(long maxSizeBytes, long maxCacheTime, File cacheDir) {
		mMaxSize = maxSizeBytes;
		mCacheDirectory = cacheDir;
		mDiskCachePolicy = new FileCachePolicy();
		mMaxCacheTime = maxCacheTime;
		initializeFromDisk();
	}

	private boolean isFileStillValid(long modTime) {
		return mMaxCacheTime == FOREVER || System.currentTimeMillis() - mMaxCacheTime < modTime;
	}

	private void initializeFromDisk() {
		mCacheDirectory.mkdirs();
		File files[] = mCacheDirectory.listFiles();
		mCurrentSize = 0;
		mFilesInCache = new LinkedHashMap<>(16, .75f, true); //Create the hash map in access order
		List<CacheEntry> allFiles = new ArrayList<>();
		//Store all the files in a list, then sort them on reverse modification time.  The idea is
		//that an older file still in the cache either is so tiny it doesn't matter or is used a lot
		for (File file : files) {
			if (!file.isDirectory() && isFileStillValid(file.lastModified())) {
				long length = file.length();
				int hashedValue = Integer.valueOf(file.getName());
				CacheEntry entry = new CacheEntry(file, length, hashedValue, file.lastModified());
				allFiles.add(entry);
			}
		}
		Collections.sort(allFiles, new Comparator<CacheEntry>() {
			@Override
			public int compare(CacheEntry lhs, CacheEntry rhs) {
				//We can't just return a cast of diff because of overflow.
				//We want to sort new to old, so that old files are added last to the hash and are
				//treated as MRU
				long diff = rhs.file.lastModified() - lhs.file.lastModified();
				if (diff > 0) {
					return 1;
				} else if (diff < 0) {
					return -1;
				}
				return 0;
			}
		});
		for (CacheEntry entry : allFiles) {
			addEntryToHash(entry);
		}
	}

	private void removeFromHash(CacheEntry entry) {
		entry.file.delete();
		mFilesInCache.remove(entry.hash);
		mCurrentSize -= entry.sizeBytes;
	}

	@Override
	public synchronized File get(String key) {
		int hash = key.hashCode();
		CacheEntry cachedData = mFilesInCache.get(hash);
		if (cachedData != null && cachedData.file != null && cachedData.file.exists()) {
			if (isFileStillValid(cachedData.modTime)) {
				try {
					return mDiskCachePolicy.read(cachedData.file);
				} catch (IOException ex) {
					//If we can't read the file, lets pretend it wasn't there
					return null;
				}
			} else {
				//Not valid kick it from cache
				removeFromHash(cachedData);
			}
		}
		return null;
	}

	@Override
	public synchronized boolean put(String key, InputStream value) {
		long size = mDiskCachePolicy.size(value);
		int hash = key.hashCode();
		//If the object is already cached, remove it
		CacheEntry cachedData = mFilesInCache.get(hash);
		if (cachedData != null) {
			removeFromHash(cachedData);
		}
		if (!ensureFileCanFit(size)) {
			return false;
		}
		//Perform the actual add
		File outputFile = new File(mCacheDirectory, Integer.toString(hash));
		try {
			//Write the file.  If we can't, tell them we couldn't cache it.
			mDiskCachePolicy.write(outputFile, value);
		} catch (IOException ex) {
			return false;
		}
		cachedData = new CacheEntry(outputFile, size, hash, System.currentTimeMillis());
		addEntryToHash(cachedData);
		return true;
	}

	public synchronized boolean put(String key, Bitmap value) {
		long size = mDiskCachePolicy.size(value);
		int hash = key.hashCode();
		//If the object is already cached, remove it
		CacheEntry cachedData = mFilesInCache.get(hash);
		if (cachedData != null) {
			removeFromHash(cachedData);
		}
		if (!ensureFileCanFit(size)) {
			return false;
		}
		//Perform the actual add
		File outputFile = new File(mCacheDirectory, Integer.toString(hash));
		try {
			//Write the file.  If we can't, tell them we couldn't cache it.
			mDiskCachePolicy.write(outputFile, value);
		} catch (IOException ex) {
			return false;
		}
		cachedData = new CacheEntry(outputFile, size, hash, System.currentTimeMillis());
		addEntryToHash(cachedData);
		return true;
	}

	private boolean ensureFileCanFit(long newItemSize) {
		//If it can't ever fit, short circuit
		if (newItemSize > mMaxSize) {
			return false;
		}
		//remove enough object so it will fit
		while (mCurrentSize + newItemSize > mMaxSize && mFilesInCache.size() > 0) {
			//Keep removing the head until we have room.
			Iterator it = mFilesInCache.values()
				.iterator();
			CacheEntry entry = (CacheEntry) it.next();
			removeFromHash(entry);
		}
		return true;
	}

	private void addEntryToHash(CacheEntry entry) {
		mFilesInCache.put(entry.hash, entry);
		mCurrentSize += entry.sizeBytes;
	}

	// Sometimes you download a file and want to add it to the cache, but not immediately use it
	// This function allows you to do so without round tripping through memory
	public synchronized boolean put(String key, File origFile) {
		long size = origFile.length();
		int hash = key.hashCode();
		//If the object is already cached, remove it
		CacheEntry cachedData = mFilesInCache.get(hash);
		if (cachedData != null) {
			removeFromHash(cachedData);
		}
		if (!ensureFileCanFit(size)) {
			return false;
		}
		File outputFile = new File(mCacheDirectory, Integer.toString(hash));
		if (!copy(origFile, outputFile)) {
			return false;
		}
		cachedData = new CacheEntry(outputFile, size, hash, System.currentTimeMillis());
		addEntryToHash(cachedData);
		return true;
	}

	private boolean copy(File src, File dst) {
		boolean success = false;
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dst);
			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			success = true;
		} catch (IOException ex) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
			}
		}
		return success;
	}

	@Override
	public synchronized void remove(String key) {
		int hash = key.hashCode();
		CacheEntry cachedData = mFilesInCache.get(hash);
		if (cachedData != null) {
			removeFromHash(cachedData);
		}
	}

	@Override
	public synchronized void clear() {
		Iterator it = mFilesInCache.entrySet()
			.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			CacheEntry cachedData = (CacheEntry) entry.getValue();
			cachedData.file.delete();
		}
		mFilesInCache.clear();
		mCurrentSize = 0;
	}

}