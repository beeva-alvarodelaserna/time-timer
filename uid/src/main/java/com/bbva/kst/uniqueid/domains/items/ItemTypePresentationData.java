package com.bbva.kst.uniqueid.domains.items;

import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import com.bbva.kst.uniqueid.domains.users.UserShare;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//This model is used to join the returned value for detail, edit and new item
public class ItemTypePresentationData {

	public int id;
	public int itemType;
	public int root_type;
	public String type_name;
	public String type_alias;
	public String url_image;
	public List<String> metadata = new ArrayList<>();
	public List<FormItemPresentationData> formItems = new ArrayList<>();
	public List<NewItemPictureModel> images = new ArrayList<>();
	public List<String> files = new ArrayList<>();
	public ItemVerifiedIssuerModel verified;
	public List<UserShare> usersShareItem = new ArrayList<>();
	public boolean shared;
	public int editable;
	public AesGcmEncryptedData itemEncKey;
	public int take_pictures;
	public Map<String, Object> itemJson;
}
