package com.bbva.kst.uniqueid.domains.items;

import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import java.util.ArrayList;
import java.util.List;

public class ItemsModelEnc {

	public int id;
	public int verificationLevel;
	public int itemType;
	public int rootType;
	public int editable;
	public boolean shared;
	public int countShared;
	public List<String> metadata = new ArrayList<>();
	public AesGcmEncryptedData itemEncKey;
	public AesGcmEncryptedData thumbnail;
	public List<ItemsLink> links = new ArrayList<>();
	public String expires;
	public String dateUpdate;
}
