package com.bbva.kst.uniqueid.core.items;

import com.bbva.kst.uniqueid.domains.SharedItemList;
import com.bbva.kst.uniqueid.domains.activity.ActivityItemModel;
import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import com.bbva.kst.uniqueid.domains.items.ItemDetail;
import com.bbva.kst.uniqueid.domains.items.ItemSubtypeModel;
import com.bbva.kst.uniqueid.domains.items.ItemTypePresentationData;
import com.bbva.kst.uniqueid.domains.items.ItemsModel;
import com.bbva.kst.uniqueid.domains.items.NewItemFormResult;
import com.bbva.kst.uniqueid.domains.items.Validation;
import com.bbva.kst.uniqueid.domains.items.VaultItem;
import com.bbva.kst.uniqueid.domains.users.NotaryCamItem;
import com.bbva.kst.uniqueid.domains.users.User;
import java.util.List;

public interface Items {

	List<VaultItem> updateVaultItemTypesMetadata() throws ItemsException;

	VaultItem getVaultItemTypeMetadata(int typeId) throws ItemsException;

	List<ItemSubtypeModel> getItemSubtypesByType(int type) throws ItemsException;

	ItemTypePresentationData getItemSubtypeByTypeAndId(int rootTypeId, int subTypeId) throws ItemsException;

	List<ItemsModel> getItemsByType(int type, int page) throws ItemsException;

	void createItem(NewItemFormResult formData, int subtypeType) throws ItemsException;

	void createItemNotaryCam(String ticket, NotaryCamItem item) throws ItemsException;

	void createSignedItem(NewItemFormResult formData, int subtypeType, Validation validation) throws ItemsException;

	ItemTypePresentationData getItemDetailBusta(int itemId) throws ItemsException;

	void deleteItem(int itemId) throws ItemsException;

	void editItem(
		NewItemFormResult formData, int subtypeType, int itemId, AesGcmEncryptedData itemEncKey) throws ItemsException;

	List<User> shareItem(
		final ItemTypePresentationData itemDetail, final List<User> selectedUsers, final int expirationTimeValue) throws ItemsException;

	SharedItemList getItemsSharedByMe() throws ItemsException;

	SharedItemList getItemsSharedWithMe() throws ItemsException;
	
	List<ActivityItemModel> getItemActivity(int itemId, int page) throws ItemsException;

	List<ItemsModel> getItemsByName(String itemName) throws ItemsException;

	ItemDetail getItemDetail(int itemId) throws ItemsException;

	ItemTypePresentationData getEditableItemDetail(int itemId) throws ItemsException;
}
