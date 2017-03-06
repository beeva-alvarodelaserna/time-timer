package com.bbva.kst.uniqueid.domains.crypto;

public class AesGcmEncryptedData {

	public final String data;
	public final String iv;
	public final String aad;
	public final String tag;

	public AesGcmEncryptedData(String data, String iv, String aad, String tag) {
		this.data = data;
		this.iv = iv;
		this.aad = aad;
		this.tag = tag;
	}

}
