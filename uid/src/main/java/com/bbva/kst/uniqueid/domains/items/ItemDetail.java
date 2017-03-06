package com.bbva.kst.uniqueid.domains.items;

import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import com.bbva.kst.uniqueid.domains.users.UserShare;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemDetail {

	public int id;
	public int itemType;
	public List<String> metadata = new ArrayList<>();
	public Map<String, Object> itemJson;
	public int editable;
	public int rootType;
	public List<ItemSubtypeForm> form = new ArrayList<>();
	public int takePictures;
	public String urlImage;
	public AesGcmEncryptedData itemEncKey;
	public List<UserShare> usersShareItem = new ArrayList<>();
	public boolean shared;
	public List<NewItemPictureModel> images = new ArrayList<>();
	public ItemVerifiedIssuerModel verified;
}
