package com.bbva.kst.uniqueid.domains.users;

import com.bbva.kst.uniqueid.domains.items.Document;
import java.util.List;

public class OcrData {

	public String error;
	public List<OcrDataOcrItem> ocr;
	public int item_type;
	public Document item_json;
	public List<String> metadata;
}
