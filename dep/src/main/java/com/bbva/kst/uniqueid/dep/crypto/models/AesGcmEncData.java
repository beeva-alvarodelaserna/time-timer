package com.bbva.kst.uniqueid.dep.crypto.models;

public class AesGcmEncData {

	public final byte[] data;
	public final String iv;
	public final String aad;
	public final byte[] tag;

	public AesGcmEncData(byte[] data, String iv, String aad, byte[] tag) {
		this.data = data;
		this.iv = iv;
		this.aad = aad;
		this.tag = tag;
	}
}
