package com.bbva.kst.uniqueid.domains.users;

public class OcrDataOcrItem {

	public final String label;
	public final Integer item_type;
	public final String value;

	public OcrDataOcrItem(String label, String value, Integer item_type) {
		this.label = label;
		this.value = value;
		this.item_type = item_type;
	}
}
