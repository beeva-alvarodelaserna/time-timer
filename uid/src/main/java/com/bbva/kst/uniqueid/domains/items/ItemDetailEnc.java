package com.bbva.kst.uniqueid.domains.items;

import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import com.bbva.kst.uniqueid.domains.users.UserShare;
import java.util.ArrayList;
import java.util.List;

public class ItemDetailEnc {

	public int id;
	public int itemType;
	public List<String> metadata = new ArrayList<>();
	public AesGcmEncryptedData itemJson;
	public int editable;
	public int rootType;
	public List<ItemSubtypeForm> form = new ArrayList<>();
	public int takePictures;
	public String urlImage;
	public AesGcmEncryptedData itemEncKey;
	public List<UserShare> usersShareItem = new ArrayList<>();
	public boolean shared;
}
