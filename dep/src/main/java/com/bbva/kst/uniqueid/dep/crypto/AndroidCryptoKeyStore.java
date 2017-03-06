package com.bbva.kst.uniqueid.dep.crypto;

import com.bbva.kst.uniqueid.dep.crypto.models.AesGcmEncData;
import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import com.bbva.kst.uniqueid.instruments.utils.StringUtils;
import com.intelygenz.android.KeyValueKeeper;

public class AndroidCryptoKeyStore implements Crypto {

	private final KeyValueKeeper keyValueKeeper;

	public AndroidCryptoKeyStore(KeyValueKeeper keyValueKeeper) {
		this.keyValueKeeper = keyValueKeeper;
	}

	@Override
	public boolean checkKeysCreated() throws CryptoException {
		return CryptoKeys.thereIsMasterAppKey() && CryptoKeys.thereIsMasterKey(keyValueKeeper) && CryptoKeys.thereIsDeviceKey(keyValueKeeper);
	}

	@Override
	public boolean thereIsPublicDeviceKey() throws CryptoException {
		return CryptoKeys.thereIsDeviceKey(keyValueKeeper);
	}

	@Override
	public void deleteDeviceKey() throws CryptoException {
		CryptoKeys.clearDeviceKeys(keyValueKeeper);
	}

	@Override
	public String getDevicePublicKeyInBase64() throws CryptoException {
		return CipherManager.getPemBase64FromPublicKey(CryptoKeys.getDevicePublicKey(keyValueKeeper));
	}

	@Override
	public String getDevicePublicKey() throws CryptoException {
		return CryptoUtils.decodeFromBase64String(getDevicePublicKeyInBase64());
	}

	@Override
	public AesGcmEncryptedData getWebPrivateKeyEncWithKPass(String password) throws CryptoException {
		String privateKey = CipherManager.getPemBase64FromPrivateKey(CryptoKeys.getWebPrivateKey(keyValueKeeper));
		String decodeBase64priv = CryptoUtils.decodeFromBase64String(privateKey);
		AesGcmEncData aesGcmEncData = CipherManager.encodeAesGcmByKpass(keyValueKeeper, password, decodeBase64priv.getBytes());
		return CryptoUtils.aesGcmByteArrayDataToAesGcmStringData(aesGcmEncData);
	}

	@Override
	public String getWebPublicKey() throws CryptoException {
		return CipherManager.getPemBase64FromPublicKey(CryptoKeys.getWebPublicKey(keyValueKeeper));
	}

	@Override
	public String getWebPublicKeyClear() throws CryptoException {
		return CipherManager.getPemFromPublicKey(CryptoKeys.getWebPublicKey(keyValueKeeper));
	}

	@Override
	public String getWebPrivateKey() throws CryptoException {
		return CipherManager.getPemBase64FromPrivateKey(CryptoKeys.getWebPrivateKey(keyValueKeeper));
	}

	@Override
	public String getWebPrivateKeyClear() throws CryptoException {
		return CipherManager.getPemFromPrivateKey(CryptoKeys.getWebPrivateKey(keyValueKeeper));
	}

	@Override
	public String getSalt() {
		return CryptoUtils.getHexSalt(keyValueKeeper);
	}

	@Override
	public String getMasterKey() throws CryptoException {
		return StringUtils.bytesToHexString(CryptoKeys.getMasterKey(keyValueKeeper));
	}

	@Override
	public void setMasterKey(String key) throws CryptoException {
		CryptoKeys.storeMasterKey(keyValueKeeper, StringUtils.hexStringToByteArray(key));
	}

	@Override
	public AesGcmEncryptedData getMasterKeyEncodedByKpass(String password) throws CryptoException {
		byte[] masterKey = CryptoKeys.getOrCreateAndGetMasterKey(keyValueKeeper);
		return CryptoUtils.aesGcmByteArrayDataToAesGcmStringData(CipherManager.encodeAesGcmByKpass(keyValueKeeper, password,
																								   StringUtils.bytesToHexString(masterKey)
																									   .getBytes()));
	}

	@Override
	public String getMasterKeyEncodedByPublicKey() throws CryptoException {
		byte[] masterKey = CryptoKeys.getOrCreateAndGetMasterKey(keyValueKeeper);
		return CryptoUtils.encodeByteArrayToBase64(CipherManager.encryptByDeviceKey(StringUtils.bytesToHexString(masterKey)
																						.getBytes(), keyValueKeeper));
	}

	@Override
	public String getZKPass(String password) throws CryptoException {
		String phrase = password + getSalt();
		return StringUtils.bytesToHexString(CryptoUtils.getHashSha256(CryptoUtils.getHashSha256(phrase.getBytes())));
	}

	@Override
	public String getZKPassToken(String password, String salt, String challenge) throws CryptoException {
		return CipherManager.zkPassTokenHex(keyValueKeeper, password, salt, challenge);
	}

	@Override
	public String decodeAesGcmDeviceEncriptedKey(
		AesGcmEncryptedData aesGcmEncryptedData, String key) throws CryptoException {
		//return CryptoUtils.bidirectionalByteArrayToString(
		//	CipherManager.decodeAesGcmDeviceEncriptedKey(
		//		CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(aesGcmEncryptedData), key, keyValueKeeper));
		//Prueba Aes Envelope
		//CipherManager.envelopeSeal("ffjfjfjfjfjfjf".getBytes(), keyValueKeeper);
		AesGcmEncData aesGcmEncData = CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(aesGcmEncryptedData);
		return new String(CipherManager.envelopeOpen(CryptoUtils.decodeBase64ToByteArray(key), StringUtils.hexStringToByteArray(aesGcmEncData.iv),
													 aesGcmEncData.aad.getBytes(), aesGcmEncData.tag, aesGcmEncData.data, keyValueKeeper));

	}

	@Override
	public String decodeWithMasterKey(AesGcmEncryptedData aesGcmEncryptedData) throws CryptoException {
		//return CryptoUtils.encodeByteArrayToBase64(
		//	CipherManager.decodeAesGcmWithMasterKey(CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(aesGcmEncryptedData), keyValueKeeper));
		return new String(
			CipherManager.decodeAesGcmWithMasterKey(CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(aesGcmEncryptedData), keyValueKeeper));
	}

	@Override
	public String getSha1Hash(String body) throws CryptoException {
		return StringUtils.bytesToHexString(CryptoUtils.getHashSha1(body.getBytes()));
	}

	@Override
	public String signWithDeviceKey(String body) throws CryptoException {
		return CryptoUtils.encodeByteArrayToBase64(CryptoUtils.getRsaPssSign(body, keyValueKeeper));
	}

	@Override
	public byte[] generateAes128Key() throws CryptoException {
		return CryptoKeys.generateAes128();
	}

	@Override
	public byte[] encodeWithDeviceKey(String input) throws CryptoException {
		return CipherManager.encryptByDeviceKey(input.getBytes(), keyValueKeeper);
	}

	@Override
	public AesGcmEncryptedData encodeAesGcmWithBase64Key(byte[] keyBytes, String input) throws CryptoException {
		return encodeAesGcmWithBase64Key(keyBytes, input.getBytes());
	}

	@Override
	public AesGcmEncryptedData encodeAesGcmWithNonBase64Key(String keyBytes, String input) throws CryptoException {
		return encodeAesGcmWithBase64Key(StringUtils.hexStringToByteArray(new String(CryptoUtils.decodeBase64ToByteArray(keyBytes))),
										 input.getBytes());
	}

	@Override
	public AesGcmEncryptedData encodeAesGcmWithBase64Key(byte[] keyBytes, byte[] input) throws CryptoException {
		return CryptoUtils.aesGcmByteArrayDataToAesGcmStringData(CipherManager.encodeAesGcm(keyBytes, input));
	}

	@Override
	public AesGcmEncryptedData encodeAesGcmWithBase64KeyWebLogin(byte[] keyBytes, String input) throws CryptoException {
		return CipherManager.encodeAesGcmEnvelope(keyBytes, input.getBytes());
	}

	@Override
	public AesGcmEncryptedData encodeWithMasterKey(byte[] input) throws CryptoException {
		return CryptoUtils.aesGcmByteArrayDataToAesGcmStringData(CipherManager.encodeAesGcmByMasterKey(keyValueKeeper, input));
	}

	@Override
	public AesGcmEncryptedData encodeKeyWithMasterKey(String input) throws CryptoException {
		return CryptoUtils.aesGcmByteArrayDataToAesGcmStringData(
			//TODO ijfijfijfijfijf
			//CipherManager.encodeAesGcmByMasterKey(keyValueKeeper, CryptoUtils.encodeToBase64String(input)));
			CipherManager.encodeAesGcmByMasterKey(keyValueKeeper, input.getBytes()));
	}

	public byte[] decodeWithDeviceKey(String input) throws CryptoException {
		return CipherManager.decryptByDeviceKey(CryptoUtils.decodeBase64ToByteArray(input), keyValueKeeper);
	}

	@Override
	public String decodeMasterKeyByKpass(String password, AesGcmEncryptedData aesGcmEncryptedData) throws CryptoException {
		byte[] bytes = CipherManager.decodeAesGcmWithKPass(keyValueKeeper, password,
														   CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(aesGcmEncryptedData));
		return new String(bytes);
	}

	@Override
	public boolean compareWithMasterKey(String key) throws CryptoException {
		return CipherManager.compareWithMasterKey(key, keyValueKeeper);
	}

	@Override
	public String decodeAesGcmWithKey(AesGcmEncryptedData itemJson, String key) throws CryptoException {
		byte[] something = CipherManager.decodeAesGcmWithKey(StringUtils.hexStringToByteArray(key),
															 CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(itemJson));
		return CryptoUtils.decodeFromBase64String(CryptoUtils.encodeByteArrayToBase64(something));
	}

	@Override
	public String reencodeItemKeyWithCustomPublicKey(
		AesGcmEncryptedData input, String publicKeyBase64Pem) throws CryptoException {
		//TODO
		byte[] kItem = CipherManager.decodeAesGcmWithMasterKey(CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(input), keyValueKeeper);
		//String jey = CryptoUtils.encodeByteArrayToBase64(
		//	CipherManager.decodeAesGcmWithMasterKey(CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(input), keyValueKeeper));
		String jey = new String(CipherManager.decodeAesGcmWithMasterKey(CryptoUtils.aesGcmStringDataToAesGcmByteArrayData(input), keyValueKeeper));
		String pubKey = CryptoUtils.decodeFromBase64String(publicKeyBase64Pem)
			.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)" + "", "");
		//TODO revisar bien cuando se utiliza esta funcion
		return CryptoUtils.encodeByteArrayToBase64(CipherManager.encryptByPublicKeyOAEP(jey.getBytes(), CryptoUtils.decodeBase64ToByteArray(pubKey)));
	}

	@Override
	public String encodeWithCustomPublicKey(String input, String publicKeyBase64Pem) throws CryptoException {
		String pubKey = CryptoUtils.decodeFromBase64String(publicKeyBase64Pem)
			.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)" + "", "");
		return CryptoUtils.encodeByteArrayToBase64(
			CipherManager.encryptByPublicKeyPKCS(input.getBytes(), CryptoUtils.decodeBase64ToByteArray(pubKey)));
	}

	@Override
	public String encodeWithCustomPublicKey(byte[] input, String publicKeyBase64Pem) throws CryptoException {
		String pubKey = CryptoUtils.decodeFromBase64String(publicKeyBase64Pem)
			.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)" + "", "");
		return CryptoUtils.encodeByteArrayToBase64(CipherManager.encryptByPublicKeyPKCS(input, CryptoUtils.decodeBase64ToByteArray(pubKey)));
	}

	@Override
	public String decodeWithCustomPublicKey(String input, String privateKeyBase64Pem) throws CryptoException {
		String pubKey = CryptoUtils.decodeFromBase64String(privateKeyBase64Pem)
			.replaceAll("(-+BEGIN PRIVATE KEY-+\\r?\\n|-+END PRIVATE KEY-+\\r?\\n?)" + "", "")
			.replace("\r\n", "");
		return new String(
			CipherManager.decryptByPublicKeyPKCS(CryptoUtils.decodeBase64ToByteArray(input), CryptoUtils.decodeBase64ToByteArray(pubKey)));
	}

	@Override
	public void clearUserKeys() {
		CryptoKeys.clearUserKeys(keyValueKeeper);
	}

	@Override
	public void clearDeviceKeys() {
		CryptoKeys.clearDeviceKeys(keyValueKeeper);
	}

	@Override
	public void test() {
	}
}
