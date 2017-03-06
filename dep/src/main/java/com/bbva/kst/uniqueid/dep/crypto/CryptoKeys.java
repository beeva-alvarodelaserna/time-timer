package com.bbva.kst.uniqueid.dep.crypto;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import com.bbva.kst.uniqueid.instruments.utils.StringUtils;
import com.intelygenz.android.KeyValueKeeper;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

class CryptoKeys {
	
	//KeyStoreAlias
	private static final String MASTER_APP_KEY_NAME = "master_app_key";
	//KVK keys
	private static final String MASTER_KEY_KEY = "master_key";
	private static final String MASTER_KEY_IV_KEY = "master_key_IV";
	private static final String WEB_PUB_KEY_KEY = "web_public_key";
	private static final String WEB_PUB_KEY_IV_KEY = "web_public_key_IV";
	private static final String WEB_PRI_KEY_KEY = "web_private_key";
	private static final String WEB_PRI_KEY_IV_KEY = "web_private_key_IV";
	private static final String DEV_PUB_KEY_KEY = "device_public_key";
	private static final String DEV_PUB_KEY_IV_KEY = "device_public_key_IV";
	private static final String DEV_PRI_KEY_KEY = "device_private_key";
	private static final String DEV_PRI_KEY_IV_KEY = "device_private_key_IV";
	private static final int SECONDS_TO_UN_AUTHENTICATE = 600;
	private static final boolean invalidatedByNewBiometricEnrollment = false;

	static boolean thereIsMasterAppKey() throws CryptoException {
		try {
			KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
			mKeyStore.load(null);
			//check masterAppKey Exist
			SecretKey key = (SecretKey) mKeyStore.getKey(MASTER_APP_KEY_NAME, null);
			if (key == null) {
				return false;
			}
		} catch (IOException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
		return true;
	}

	static boolean thereIsDeviceKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		//check deviceKey Exist
		String privateKey = keyValueKeeper.load(DEV_PRI_KEY_KEY, String.class);
		String privateKeyIV = keyValueKeeper.load(DEV_PRI_KEY_IV_KEY, String.class);
		return !(privateKey == null || privateKeyIV == null);
	}

	static boolean thereIsMasterKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		//check masterKey Exist
		String MasterKeyEnc = keyValueKeeper.load(MASTER_KEY_KEY, String.class);
		String MasterKeyIV = keyValueKeeper.load(MASTER_KEY_IV_KEY, String.class);
		return !(MasterKeyEnc == null || MasterKeyIV == null);
	}

	static void clearUserKeys(KeyValueKeeper keyValueKeeper) {
		keyValueKeeper.remove(MASTER_KEY_KEY);
		keyValueKeeper.remove(MASTER_KEY_IV_KEY);
		keyValueKeeper.remove(WEB_PUB_KEY_KEY);
		keyValueKeeper.remove(WEB_PUB_KEY_IV_KEY);
		keyValueKeeper.remove(WEB_PRI_KEY_KEY);
		keyValueKeeper.remove(WEB_PRI_KEY_IV_KEY);
	}

	static void clearDeviceKeys(KeyValueKeeper keyValueKeeper) {
		keyValueKeeper.remove(DEV_PRI_KEY_KEY);
		keyValueKeeper.remove(DEV_PUB_KEY_KEY);
		keyValueKeeper.remove(DEV_PRI_KEY_IV_KEY);
		keyValueKeeper.remove(DEV_PUB_KEY_IV_KEY);
	}

	static void createAndStoreMasterAppKey() throws CryptoException {
		// The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
		// for your flow. Use of keys is necessary if you need to know if the set of
		// enrolled fingerprints has changed.
		try {
			KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
			mKeyStore.load(null);
			// Set the alias of the entry in Android KeyStore where the key will appear
			// and the constrains (purposes) in the constructor of the Builder
			KeyGenerator mKeyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
			KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(MASTER_APP_KEY_NAME,
																				  KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(
				KeyProperties.BLOCK_MODE_CBC)
				// Require the user to authenticate with a fingerprint to authorize every use
				// of the key
				.setUserAuthenticationRequired(true)
				.setUserAuthenticationValidityDurationSeconds(SECONDS_TO_UN_AUTHENTICATE)
				.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
			// This is a workaround to avoid crashes on devices whose API level is < 24
			// because KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment is only
			// visible on API level +24.
			// Ideally there should be a compat library for KeyGenParameterSpec.Builder but
			// which isn't available yet.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				builder.setInvalidatedByBiometricEnrollment(invalidatedByNewBiometricEnrollment);
			}
			mKeyGenerator.init(builder.build());
			mKeyGenerator.generateKey();

		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
			| CertificateException | IOException | KeyStoreException | NoSuchProviderException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	private static void createAndStoreMasterKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		storeMasterKey(keyValueKeeper, generateAes128());
	}

	static void storeMasterKey(KeyValueKeeper keyValueKeeper, byte[] key) throws CryptoException {
		CryptoKeysStorage.storeKvkEncripted(keyValueKeeper, MASTER_KEY_KEY, MASTER_KEY_IV_KEY, key);
	}

	static byte[] generateAes128() throws CryptoException {
		try {
			final int outputKeyLength = 128;
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(outputKeyLength);
			SecretKey masterKey = keyGen.generateKey();
			return masterKey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static byte[] createKpassKey(KeyValueKeeper keyValueKeeper, String password) throws CryptoException {
		// Number of PBKDF2 hardening rounds to use. Larger values increase // computation time. You should select a value that causes computation // to take >100ms.
		// Generate a 128-bit key
		try {
			final int iterations = 1500;
			final int outputKeyLength = 128;
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec keySpec = new PBEKeySpec(password.toCharArray(), StringUtils.hexStringToByteArray(CryptoUtils.getHexSalt(keyValueKeeper)),
											 iterations, outputKeyLength);
			return secretKeyFactory.generateSecret(keySpec)
				.getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static void createAndStoreDeviceKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		KeyPair keyPair = createRsaKey();
		byte[] pri = keyPair.getPrivate()
			.getEncoded();
		byte[] pub = keyPair.getPublic()
			.getEncoded();
		CryptoKeysStorage.storeKvkEncripted(keyValueKeeper, DEV_PRI_KEY_KEY, DEV_PRI_KEY_IV_KEY, pri);
		CryptoKeysStorage.storeKvkEncripted(keyValueKeeper, DEV_PUB_KEY_KEY, DEV_PUB_KEY_IV_KEY, pub);
	}

	private static void createAndStoreWebKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		KeyPair keyPair = createRsaKey();
		byte[] pri = keyPair.getPrivate()
			.getEncoded();
		byte[] pub = keyPair.getPublic()
			.getEncoded();
		CryptoKeysStorage.storeKvkEncripted(keyValueKeeper, WEB_PRI_KEY_KEY, WEB_PRI_KEY_IV_KEY, pri);
		CryptoKeysStorage.storeKvkEncripted(keyValueKeeper, WEB_PUB_KEY_KEY, WEB_PUB_KEY_IV_KEY, pub);
	}

	private static KeyPair createRsaKey() throws CryptoException {
		try {
			final int outputKeyLength = 2048;
			SecureRandom random = new SecureRandom();
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA);
			keyPairGenerator.initialize(outputKeyLength, random);
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static SecretKey getCipherMasterAppKey() throws CryptoException {
		try {
			KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
			mKeyStore.load(null);
			return (SecretKey) mKeyStore.getKey(CryptoKeys.MASTER_APP_KEY_NAME, null);
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static byte[] getOrCreateAndGetMasterKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		byte[] masterKey;
		try {
			masterKey = getMasterKey(keyValueKeeper);
		} catch (CryptoException e) {
			if (e.errCode == CryptoException.NO_KEY_FOUND_CODE) {
				createAndStoreMasterKey(keyValueKeeper);
				masterKey = getMasterKey(keyValueKeeper);
			} else {
				throw e;
			}
		}
		return masterKey;

	}

	static byte[] getMasterKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		return CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, MASTER_KEY_KEY, MASTER_KEY_IV_KEY);
	}

	static byte[] getWebPrivateKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		byte[] priKey;
		try {
			priKey = CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, WEB_PRI_KEY_KEY, WEB_PRI_KEY_IV_KEY);
		} catch (CryptoException e) {
			if (e.errCode == CryptoException.NO_KEY_FOUND_CODE) {
				createAndStoreWebKey(keyValueKeeper);
				priKey = CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, WEB_PRI_KEY_KEY, WEB_PRI_KEY_IV_KEY);
			} else {
				throw e;
			}
		}
		return priKey;
	}

	static byte[] getWebPublicKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		byte[] pubKey;
		try {
			pubKey = CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, WEB_PUB_KEY_KEY, WEB_PUB_KEY_IV_KEY);
		} catch (CryptoException e) {
			if (e.errCode == CryptoException.NO_KEY_FOUND_CODE) {
				createAndStoreWebKey(keyValueKeeper);
				pubKey = CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, WEB_PUB_KEY_KEY, WEB_PUB_KEY_IV_KEY);
			} else {
				throw e;
			}
		}
		return pubKey;
	}

	static byte[] getDevicePrivateKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		byte[] priKey;
		try {
			priKey = CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, DEV_PRI_KEY_KEY, DEV_PRI_KEY_IV_KEY);
		} catch (CryptoException e) {
			if (e.errCode == CryptoException.NO_KEY_FOUND_CODE) {
				createAndStoreDeviceKey(keyValueKeeper);
				priKey = CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, DEV_PRI_KEY_KEY, DEV_PRI_KEY_IV_KEY);
			} else {
				throw e;
			}
		}
		return priKey;
	}

	static byte[] getDevicePublicKey(KeyValueKeeper keyValueKeeper) throws CryptoException {
		byte[] pubKey;
		try {
			pubKey = CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, DEV_PUB_KEY_KEY, DEV_PUB_KEY_IV_KEY);
		} catch (CryptoException e) {
			if (e.errCode == CryptoException.NO_KEY_FOUND_CODE) {
				createAndStoreDeviceKey(keyValueKeeper);
				pubKey = CryptoKeysStorage.loadKvkEncripted(keyValueKeeper, DEV_PUB_KEY_KEY, DEV_PUB_KEY_IV_KEY);
			} else {
				throw e;
			}
		}
		return pubKey;
	}
}
