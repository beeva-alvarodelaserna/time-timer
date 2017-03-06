package com.bbva.kst.uniqueid.domains.users;

import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import com.bbva.kst.uniqueid.domains.items.ItemsLink;
import java.util.List;

public class AuthorizeSPItem {

	public boolean accepted;
	public String item_name;
	public AesGcmEncryptedData item_enc_key;
	public int id;
	public int id_item_type;
	public List<ItemsLink> links;
}
