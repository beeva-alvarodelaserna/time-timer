package com.bbva.kst.uniqueid.domains.items;

import java.util.HashMap;
import java.util.List;

public class NewItemFormResultElement {

	public final String key;
	public String value;
	public String errorMessage;
	public String pattern;
	public HashMap<Integer, List<LinkedValue>> linkedReferences;

	public NewItemFormResultElement(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
