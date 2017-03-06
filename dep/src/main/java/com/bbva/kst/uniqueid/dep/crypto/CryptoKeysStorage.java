package com.bbva.kst.uniqueid.dep.crypto;

import com.bbva.kst.uniqueid.dep.crypto.models.AesCbcEncriptResult;
import com.bbva.kst.uniqueid.instruments.utils.StringUtils;
import com.intelygenz.android.KeyValueKeeper;

class CryptoKeysStorage {

	private static final String SALT_KEY = "salt";

	static void storeKvkEncripted(KeyValueKeeper keyValueKeeper, String KeyKey, String IvKey, byte[] input) throws CryptoException {
		AesCbcEncriptResult KpassEncResult;
		try {
			KpassEncResult = CipherManager.tryEncryptByMasterAppKey(input);
		} catch (CryptoException e) {
			if (e.errCode == CryptoException.NO_KEY_FOUND_CODE) {
				CryptoKeys.createAndStoreMasterAppKey();
				KpassEncResult = CipherManager.tryEncryptByMasterAppKey(input);
			} else {
				throw e;
			}
		}
		if (KpassEncResult != null) {
			keyValueKeeper.save(KeyKey, CryptoUtils.encodeByteArrayToBase64(KpassEncResult.encodeResult));
			keyValueKeeper.save(IvKey, StringUtils.bytesToHexString(KpassEncResult.IV));
			return;
		}
		throw CryptoExceptionFactory.noEncodingResultCryptoException();
	}

	static byte[] loadKvkEncripted(KeyValueKeeper keyValueKeeper, String KeyKey, String IvKey) throws CryptoException {
		String MasterKeyEnc = keyValueKeeper.load(KeyKey, String.class);
		String MasterKeyIV = keyValueKeeper.load(IvKey, String.class);
		if (MasterKeyEnc == null || MasterKeyIV == null) {
			throw CryptoExceptionFactory.noKeyFoundCryptoException();
		}
		return CipherManager.tryDecryptByMasterAppKey(CryptoUtils.decodeBase64ToByteArray(MasterKeyEnc),
													  StringUtils.hexStringToByteArray(MasterKeyIV));
	}

	static void storeSalt(KeyValueKeeper keyValueKeeper, String salt) {
		keyValueKeeper.save(SALT_KEY, salt);
	}

	static String storeSalt(KeyValueKeeper keyValueKeeper) {
		return keyValueKeeper.load(SALT_KEY, String.class);
	}

}
