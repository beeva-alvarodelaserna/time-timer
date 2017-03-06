package com.bbva.kst.uniqueid.dep.crypto;

import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;

public interface Crypto {

	boolean checkKeysCreated() throws CryptoException;

	boolean thereIsPublicDeviceKey() throws CryptoException;

	void deleteDeviceKey() throws CryptoException;

	String getDevicePublicKeyInBase64() throws CryptoException;

	String getDevicePublicKey() throws CryptoException;

	AesGcmEncryptedData getWebPrivateKeyEncWithKPass(String password) throws CryptoException;

	String getWebPublicKey() throws CryptoException;

	String getWebPublicKeyClear() throws CryptoException;

	String getWebPrivateKey() throws CryptoException;

	String getWebPrivateKeyClear() throws CryptoException;

	String getSalt() throws CryptoException;

	String getMasterKey() throws CryptoException;

	AesGcmEncryptedData getMasterKeyEncodedByKpass(String password) throws CryptoException;

	String getMasterKeyEncodedByPublicKey() throws CryptoException;

	String getZKPass(String pass) throws CryptoException;

	String getZKPassToken(String password, String salt, String challenge) throws CryptoException;

	String decodeAesGcmDeviceEncriptedKey(AesGcmEncryptedData aesGcmEncryptedData, String key) throws CryptoException;

	String decodeWithMasterKey(AesGcmEncryptedData aesGcmEncryptedData) throws CryptoException;

	String getSha1Hash(String body) throws CryptoException;

	String signWithDeviceKey(String body) throws CryptoException;

	byte[] generateAes128Key() throws CryptoException;

	byte[] encodeWithDeviceKey(String input) throws CryptoException;

	AesGcmEncryptedData encodeAesGcmWithBase64Key(byte[] keyBytes, String input) throws CryptoException;

	AesGcmEncryptedData encodeAesGcmWithNonBase64Key(String key, String input) throws CryptoException;

	AesGcmEncryptedData encodeAesGcmWithBase64Key(byte[] keyBytes, byte[] input) throws CryptoException;

	AesGcmEncryptedData encodeAesGcmWithBase64KeyWebLogin(byte[] keyBytes, String input) throws CryptoException;

	AesGcmEncryptedData encodeWithMasterKey(byte[] input) throws CryptoException;

	AesGcmEncryptedData encodeKeyWithMasterKey(String input) throws CryptoException;

	void setMasterKey(String key) throws CryptoException;

	byte[] decodeWithDeviceKey(String input) throws CryptoException;

	String decodeMasterKeyByKpass(String password, AesGcmEncryptedData aesGcmEncryptedData) throws CryptoException;

	boolean compareWithMasterKey(String key) throws CryptoException;

	String decodeAesGcmWithKey(AesGcmEncryptedData itemJson, String key) throws CryptoException;

	String reencodeItemKeyWithCustomPublicKey(AesGcmEncryptedData input, String publicKeyBase64Pem) throws CryptoException;

	String encodeWithCustomPublicKey(String input, String publicKeyBase64Pem) throws CryptoException;

	String encodeWithCustomPublicKey(byte[] input, String publicKeyBase64Pem) throws CryptoException;

	String decodeWithCustomPublicKey(String input, String privateKeyBase64Pem) throws CryptoException;

	void clearUserKeys();

	void clearDeviceKeys();

	void test();
}
