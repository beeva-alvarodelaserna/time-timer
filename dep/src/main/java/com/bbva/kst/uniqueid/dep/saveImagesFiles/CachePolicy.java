package com.bbva.kst.uniqueid.dep.saveImagesFiles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

interface CachePolicy {

	boolean write(File outputFile, InputStream value) throws IOException;

	File read(File inputFile) throws IOException;

	long size(InputStream value);

}