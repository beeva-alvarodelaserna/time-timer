package com.bbva.kst.uniqueid.core.users;

import com.bbva.kst.uniqueid.domains.activity.ActivityItemModel;
import com.bbva.kst.uniqueid.domains.items.Validation;
import com.bbva.kst.uniqueid.domains.users.AuthorizeSPItemsToShare;
import com.bbva.kst.uniqueid.domains.users.NotaryCamItem;
import com.bbva.kst.uniqueid.domains.users.OcrDataOcrItem;
import com.bbva.kst.uniqueid.domains.users.User;
import java.util.List;

public interface Users {

	String getAvatar();

	String getUserId() throws UsersException;

	void storeUserId(String userID);

	String getEmail();

	void storeEmail(String email);

	User getUserData() throws UsersException;

	List<User> getUsers(String emailOrPhone) throws UsersException;

	void uploadDocument(String imagePath) throws UsersException;

	boolean isUploadDocument();

	void deleteUser() throws UsersException;

	void confirmOcrItems(List<OcrDataOcrItem> items, Validation validation) throws UsersException;

	String updateAndGetUserAvatar(String avatar) throws UsersException;

	List<ActivityItemModel> getUserActivity(int page) throws UsersException;

	boolean isItTheFirstTime();

	void setFirstTime(boolean firstTime);

	String getPhone();

	void storePhone(String phone);

	String getOcrPicture();

	String authorizeShare(String ticket, String item_enc_key) throws UsersException;

	AuthorizeSPItemsToShare getAuthorizeSPItems(String ticket) throws UsersException;

	void authorizeSP(String ticket, AuthorizeSPItemsToShare items) throws UsersException;

	void storeAutoLock(long expireMillis);

	long getAutoLock();

	void setItIsTheFirstItemEverAsFalse();

	boolean itIsTheFirstItemEver();

	NotaryCamItem getVerifiedItem(String ticket, int id_sp) throws UsersException;

	String updateUserPhone(String phoneNumber) throws UsersException;

	void validatePhone(String ticket, String phoneNumber, String code) throws UsersException;

	void deleteOCRUploadingImage();
}
