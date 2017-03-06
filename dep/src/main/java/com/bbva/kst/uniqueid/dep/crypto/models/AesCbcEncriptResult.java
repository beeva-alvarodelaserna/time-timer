package com.bbva.kst.uniqueid.dep.crypto.models;

public class AesCbcEncriptResult {

	public final byte[] IV;
	public final byte[] encodeResult;

	public AesCbcEncriptResult(byte[] IV, byte[] encodeResult) {
		this.IV = IV;
		this.encodeResult = encodeResult;
	}

}
