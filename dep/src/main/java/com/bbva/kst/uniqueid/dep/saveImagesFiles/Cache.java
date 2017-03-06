package com.bbva.kst.uniqueid.dep.saveImagesFiles;

interface Cache<KeyType, InputValueType, OutputValueType> {

	OutputValueType get(KeyType key);

	boolean put(KeyType key, InputValueType value);

	void remove(KeyType key);

	void clear();
}
