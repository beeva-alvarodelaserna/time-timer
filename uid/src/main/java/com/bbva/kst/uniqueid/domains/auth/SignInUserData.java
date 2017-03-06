package com.bbva.kst.uniqueid.domains.auth;

import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;

public class SignInUserData {

	public final String email;
	public final String uuid_user;
	private final String nsuuid;
	public final AesGcmEncryptedData master_enc_key;//kMaster encriptada con la clave derivada de la password
	public final String access_token;

	public SignInUserData(String email, String uuid_user, String nsuuid, AesGcmEncryptedData master_enc_key, String access_token) {
		this.email = email;
		this.uuid_user = uuid_user;
		this.nsuuid = nsuuid;
		this.master_enc_key = master_enc_key;
		this.access_token = access_token;
	}
}
