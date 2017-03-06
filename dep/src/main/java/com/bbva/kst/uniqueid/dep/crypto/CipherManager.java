package com.bbva.kst.uniqueid.dep.crypto;

import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import com.bbva.kst.uniqueid.dep.crypto.models.AesCbcEncriptResult;
import com.bbva.kst.uniqueid.dep.crypto.models.AesGcmEncData;
import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import com.bbva.kst.uniqueid.instruments.utils.StringUtils;
import com.intelygenz.android.KeyValueKeeper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemWriter;

class CipherManager {

	private static final String AesCbcAlgorithm = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7;
	private static final String AesGcmAlgorithm = "AES/GCM/NoPadding";
	private static final String RsaAlgorithm = KeyProperties.KEY_ALGORITHM_RSA + "/" + KeyProperties.DIGEST_NONE + "/" + "OAEPWithSHA-1AndMGF1Padding";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	//By MasterAppKey
	static AesCbcEncriptResult tryEncryptByMasterAppKey(byte[] input) throws CryptoException {
		try {
			Cipher cipher = getCipherMasterAppKey(Cipher.ENCRYPT_MODE, null);
			if (cipher != null) {
				byte[] encrypted = cipher.doFinal(input);
				return new AesCbcEncriptResult(cipher.getIV(), encrypted);
			}
			throw CryptoExceptionFactory.noCipherCryptoException();
		} catch (BadPaddingException | IllegalBlockSizeException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static byte[] tryDecryptByMasterAppKey(byte[] input, byte[] IV) throws CryptoException {
		try {
			Cipher cipher = getCipherMasterAppKey(Cipher.DECRYPT_MODE, IV);
			if (cipher != null) {
				CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(input), cipher);
				ArrayList<Byte> values = new ArrayList<>();
				int nextByte;
				while ((nextByte = cipherInputStream.read()) != -1) {
					values.add((byte) nextByte);
				}
				byte[] bytes = new byte[values.size()];
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = values.get(i);
				}
				if (bytes.length > 0) {
					return bytes;
				} else {
					return cipher.doFinal(input);
				}
			}
			throw CryptoExceptionFactory.noCipherCryptoException();
		} catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	//By DeviceKey
	static byte[] encryptByDeviceKey(byte[] input, KeyValueKeeper keyValueKeeper) throws CryptoException {
		try {
			Cipher cipher;
			try {
				cipher = getCipherDeviceKey(Cipher.ENCRYPT_MODE, keyValueKeeper);
			} catch (CryptoException e) {
				if (e.errCode == CryptoException.NO_KEY_FOUND_CODE) {
					CryptoKeys.createAndStoreDeviceKey(keyValueKeeper);
					cipher = getCipherDeviceKey(Cipher.ENCRYPT_MODE, keyValueKeeper);
				} else {
					throw e;
				}
			}
			if (cipher != null) {
				return cipher.doFinal(input);
			}
			throw CryptoExceptionFactory.noCipherCryptoException();
		} catch (BadPaddingException | IllegalBlockSizeException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}

	}

	static byte[] decryptByDeviceKey(byte[] input, KeyValueKeeper keyValueKeeper) throws CryptoException {
		try {
			Cipher cipher = getCipherDeviceKey(Cipher.DECRYPT_MODE, keyValueKeeper);
			if (cipher != null) {
				CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(input), cipher);
				ArrayList<Byte> values = new ArrayList<>();
				int nextByte;
				while ((nextByte = cipherInputStream.read()) != -1) {
					values.add((byte) nextByte);
				}
				byte[] bytes = new byte[values.size()];
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = values.get(i);
				}
				if (bytes.length > 0) {
					return bytes;
				} else {
					return cipher.doFinal(input);
				}
			}
			throw CryptoExceptionFactory.noCipherCryptoException();
		} catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}

	}

	//By Data RSA Key
	static byte[] encryptByPublicKeyOAEP(byte[] input, byte[] pemPublicKey) throws CryptoException {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(pemPublicKey));
			Cipher cipher = Cipher.getInstance(RsaAlgorithm);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(input);
		} catch (BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static byte[] decryptByPublicKeyOAEP(byte[] input, byte[] pemPrivateKey) throws CryptoException {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(pemPrivateKey));
			Cipher cipher = Cipher.getInstance(RsaAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(input);
		} catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException |
			InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static byte[] encryptByPublicKeyPKCS(byte[] input, byte[] pemPublicKey) throws CryptoException {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(pemPublicKey));
			//Cipher cipher = Cipher.getInstance(RsaAlgorithm);
			//Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA);
			Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(input);
		} catch (BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchProviderException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static byte[] decryptByPublicKeyPKCS(byte[] input, byte[] pemPrivateKey) throws CryptoException {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(pemPrivateKey));
			//Cipher cipher = Cipher.getInstance(RsaAlgorithm);
			Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			//Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(input);
		} catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException |
			InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	//By KPass
	static AesGcmEncData encodeAesGcmByKpass(KeyValueKeeper keyValueKeeper, String password, byte[] input) throws CryptoException {
		byte[] kpassBytes = CryptoKeys.createKpassKey(keyValueKeeper, password);
		return encodeAesGcm(kpassBytes, input);
	}

	static byte[] decodeAesGcmWithKPass(KeyValueKeeper keyValueKeeper, String password, AesGcmEncData aesGcmEncryptedData) throws CryptoException {
		byte[] kpassBytes = CryptoKeys.createKpassKey(keyValueKeeper, password);
		byte ivBytes[] = StringUtils.hexStringToByteArray(aesGcmEncryptedData.iv);
		byte inputBytes[] = CryptoUtils.joinByteArray(aesGcmEncryptedData.data, aesGcmEncryptedData.tag);
		byte aadBytes[] = aesGcmEncryptedData.aad.getBytes();
		return decodeAesGcm(kpassBytes, aadBytes, ivBytes, inputBytes);
	}

	//By MasterKey
	static AesGcmEncData encodeAesGcmByMasterKey(KeyValueKeeper keyValueKeeper, String input) throws CryptoException {
		byte[] masterKeyBytes = CryptoKeys.getOrCreateAndGetMasterKey(keyValueKeeper);
		//TODO
		return encodeAesGcm(masterKeyBytes, CryptoUtils.decodeBase64ToByteArray(input));
	}

	static AesGcmEncData encodeAesGcmByMasterKey(KeyValueKeeper keyValueKeeper, byte[] input) throws CryptoException {
		byte[] masterKeyBytes = CryptoKeys.getOrCreateAndGetMasterKey(keyValueKeeper);
		return encodeAesGcm(masterKeyBytes, input);
	}

	static byte[] decodeAesGcmWithMasterKey(AesGcmEncData aesGcmEncryptedData, KeyValueKeeper keyValueKeeper) throws CryptoException {
		byte[] keyBytes = CryptoKeys.getOrCreateAndGetMasterKey(keyValueKeeper);
		byte ivBytes[] = StringUtils.hexStringToByteArray(aesGcmEncryptedData.iv);
		byte inputBytes[] = CryptoUtils.joinByteArray(aesGcmEncryptedData.data, aesGcmEncryptedData.tag);
		byte aadBytes[] = aesGcmEncryptedData.aad.getBytes();
		return decodeAesGcm(keyBytes, aadBytes, ivBytes, inputBytes);
	}

	//By Device Encripted Key
	static byte[] decodeAesGcmDeviceEncriptedKey(AesGcmEncData aesGcmEncryptedData, String key, KeyValueKeeper keyValueKeeper)
		throws CryptoException {
		byte keyBytes[] = decryptByDeviceKey(CryptoUtils.decodeBase64ToByteArray(key), keyValueKeeper);
		byte ivBytes[] = StringUtils.hexStringToByteArray(aesGcmEncryptedData.iv);
		byte inputBytes[] = CryptoUtils.joinByteArray(aesGcmEncryptedData.data, aesGcmEncryptedData.tag);
		byte aadBytes[] = aesGcmEncryptedData.aad.getBytes();
		return decodeAesGcm(keyBytes, aadBytes, ivBytes, inputBytes);
	}

	//Main Operations
	static AesGcmEncData encodeAesGcm(byte[] keyBytes, byte[] inputBytes) throws CryptoException {
		try {
			String iv = CryptoUtils.getHexIV();
			String aad = CryptoUtils.getAAD();
			SecureRandom random = new SecureRandom();
			GCMParameterSpec gcmSpec = new GCMParameterSpec(keyBytes.length * 8, StringUtils.hexStringToByteArray(iv));
			Cipher cipher = Cipher.getInstance(AesGcmAlgorithm);
			SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, originalKey, gcmSpec, random);
			cipher.updateAAD(aad.getBytes());
			byte[] enc = cipher.doFinal(inputBytes);
			ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
			ByteArrayOutputStream tag = new ByteArrayOutputStream();
			int tlen = gcmSpec.getTLen() / 8;
			int clen = enc.length - tlen;
			encrypted.write(enc, 0, clen);
			tag.write(enc, clen, tlen);
			byte[] dataOut = encrypted.toByteArray();
			byte[] tagOut = tag.toByteArray();
			return new AesGcmEncData(dataOut, iv, aad, tagOut);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	private static byte[] decodeAesGcm(byte[] keyBytes, byte[] aadBytes, byte[] ivBytes, byte[] inputBytes) throws CryptoException {
		try {
			GCMParameterSpec gcmSpec = new GCMParameterSpec(keyBytes.length * 8, ivBytes);
			Cipher cipher = Cipher.getInstance(AesGcmAlgorithm);
			SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
			cipher.init(Cipher.DECRYPT_MODE, originalKey, gcmSpec);
			cipher.updateAAD(aadBytes);
			return cipher.doFinal(inputBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException |
			InvalidAlgorithmParameterException | InvalidKeyException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	//Main Operations
	static AesGcmEncryptedData encodeAesGcmEnvelope(byte[] keyBytes, byte[] inputBytes) throws CryptoException {
		try {
			String iv = CryptoUtils.getBase64IV();
			String aad = CryptoUtils.getAAD();
			SecureRandom random = new SecureRandom();
			GCMParameterSpec gcmSpec = new GCMParameterSpec(keyBytes.length * 8, CryptoUtils.decodeBase64ToByteArray(iv));
			Cipher cipher = Cipher.getInstance(AesGcmAlgorithm);
			SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, originalKey, gcmSpec, random);
			cipher.updateAAD(aad.getBytes());
			byte[] enc = cipher.doFinal(inputBytes);
			ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
			ByteArrayOutputStream tag = new ByteArrayOutputStream();
			int tlen = gcmSpec.getTLen() / 8;
			int clen = enc.length - tlen;
			encrypted.write(enc, 0, clen);
			tag.write(enc, clen, tlen);
			byte[] dataOut = encrypted.toByteArray();
			byte[] tagOut = tag.toByteArray();
			return new AesGcmEncryptedData(CryptoUtils.encodeByteArrayToBase64(dataOut), iv, aad, CryptoUtils.encodeByteArrayToBase64(tagOut));
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	private static byte[] decodeAesGcmEnvelope(byte[] keyBytes, byte[] aadBytes, byte[] ivBytes, byte[] inputBytes) throws CryptoException {
		try {
			GCMParameterSpec gcmSpec = new GCMParameterSpec(keyBytes.length * 8, ivBytes);
			Cipher cipher = Cipher.getInstance(AesGcmAlgorithm);
			SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
			cipher.init(Cipher.DECRYPT_MODE, originalKey, gcmSpec);
			cipher.updateAAD(aadBytes);
			return cipher.doFinal(inputBytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException |
			InvalidAlgorithmParameterException | InvalidKeyException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}
	//-------------------------------

	static String getPemBase64FromPublicKey(byte[] publicKey) throws CryptoException {
		try {
			StringWriter stringWriter = new StringWriter();
			PemObject pemObject = new PemObject("PUBLIC KEY", publicKey);
			PemWriter pemWriter = new PemWriter(stringWriter);
			pemWriter.writeObject(pemObject);
			pemWriter.flush();
			pemWriter.close();
			return CryptoUtils.encodeByteArrayToBase64(stringWriter.toString()
														   .getBytes());
		} catch (IOException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static String getPemFromPublicKey(byte[] publicKey) throws CryptoException {
		try {
			StringWriter stringWriter = new StringWriter();
			PemObject pemObject = new PemObject("PUBLIC KEY", publicKey);
			PemWriter pemWriter = new PemWriter(stringWriter);
			pemWriter.writeObject(pemObject);
			pemWriter.flush();
			pemWriter.close();
			return stringWriter.toString();
		} catch (IOException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static String getPemBase64FromPrivateKey(byte[] privateKey) throws CryptoException {
		try {
			StringWriter stringWriter = new StringWriter();
			PemObject pemObject = new PemObject("PRIVATE KEY", privateKey);
			PemWriter pemWriter = new PemWriter(stringWriter);
			pemWriter.writeObject(pemObject);
			pemWriter.flush();
			pemWriter.close();
			return CryptoUtils.encodeByteArrayToBase64(stringWriter.toString()
														   .getBytes());
		} catch (IOException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static String getPemFromPrivateKey(byte[] privateKey) throws CryptoException {
		try {
			StringWriter stringWriter = new StringWriter();
			PemObject pemObject = new PemObject("PRIVATE KEY", privateKey);
			PemWriter pemWriter = new PemWriter(stringWriter);
			pemWriter.writeObject(pemObject);
			pemWriter.flush();
			pemWriter.close();
			return stringWriter.toString();
		} catch (IOException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static String zkPassTokenHex(KeyValueKeeper keyValueKeeper, String pass, String salt, String challenge) throws CryptoException {
		try {
			CryptoKeysStorage.storeSalt(keyValueKeeper, salt);
			byte H1[] = CryptoUtils.getHashSha256(CryptoUtils.joinByteArray(pass.getBytes(), salt.getBytes()));
			byte tokenOp1[] = CryptoUtils.getHashSha256(CryptoUtils.joinByteArray(challenge.getBytes(), CryptoUtils.getHashSha256(H1)));
			return StringUtils.bytesToHexString(CryptoUtils.byteArrayXOR(tokenOp1, H1));
		} catch (CryptoException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static boolean compareWithMasterKey(String key, KeyValueKeeper keyValueKeeper) throws CryptoException {
		byte[] masterKey = CryptoKeys.getMasterKey(keyValueKeeper);
		String storedMasterKey = StringUtils.bytesToHexString(masterKey);
		return storedMasterKey.equals(key);
	}

	static byte[] decodeAesGcmWithKey(byte[] keyBytes, AesGcmEncData itemJson) throws CryptoException {
		byte ivBytes[] = StringUtils.hexStringToByteArray(itemJson.iv);
		byte inputBytes[] = CryptoUtils.joinByteArray(itemJson.data, itemJson.tag);
		byte aadBytes[] = itemJson.aad.getBytes();
		return decodeAesGcm(keyBytes, aadBytes, ivBytes, inputBytes);
	}

	private static Cipher getCipherMasterAppKey(int mode, byte[] IV) throws CryptoException {
		try {
			Cipher MasterAppCipher;
			SecretKey key = CryptoKeys.getCipherMasterAppKey();
			if (key != null) {
				MasterAppCipher = Cipher.getInstance(AesCbcAlgorithm);
			} else {
				throw CryptoExceptionFactory.noKeyFoundCryptoException();
			}
			if (mode == Cipher.DECRYPT_MODE) {
				MasterAppCipher.init(mode, key, new IvParameterSpec(IV));
			} else {
				MasterAppCipher.init(mode, key);
			}
			return MasterAppCipher;
		} catch (InvalidKeyException e) {
			throw CryptoExceptionFactory.userNotAuthenticatedCryptoException(e);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException |
			InvalidAlgorithmParameterException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	private static Cipher getCipherDeviceKey(int mode, KeyValueKeeper keyValueKeeper) throws CryptoException {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
			PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(CryptoKeys.getDevicePrivateKey(keyValueKeeper)));
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(CryptoKeys.getDevicePublicKey(keyValueKeeper)));
			Cipher DevicePrivateCipher = Cipher.getInstance(RsaAlgorithm);
			if (mode == Cipher.DECRYPT_MODE) {
				DevicePrivateCipher.init(mode, privateKey);
			} else {
				DevicePrivateCipher.init(mode, publicKey);
			}
			return DevicePrivateCipher;
		} catch (UserNotAuthenticatedException e) {
			throw CryptoExceptionFactory.userNotAuthenticatedCryptoException(e);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static void envelopeSeal(byte[] plaintext, KeyValueKeeper keyValueKeeper) throws CryptoException {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(CryptoKeys.getDevicePublicKey(keyValueKeeper)));

			/* Generate a random AES key and IV */
			byte[] key = CryptoKeys.generateAes128();

			/* Encrypt the plaintext with the AES key */
			AesGcmEncData aesGcmEncData = encodeAesGcm(key, plaintext);

			/* Encrypt the AES key with the RSA public key */
			Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] ekeyBytes = cipher.doFinal(key);
			String s = new String(
				CipherManager.envelopeOpen(ekeyBytes, StringUtils.hexStringToByteArray(aesGcmEncData.iv), aesGcmEncData.aad.getBytes(),
										   aesGcmEncData.tag, aesGcmEncData.data, keyValueKeeper));
			System.out.println(s);
		} catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException |
			NoSuchPaddingException | NoSuchProviderException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static byte[] envelopeOpen(
		byte[] ekey, byte[] iv, byte[] aad, byte[] tag, byte[] ciphertext, KeyValueKeeper keyValueKeeper) throws CryptoException {
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(CryptoKeys.getDevicePrivateKey(keyValueKeeper)));

		/* Decrypt the AES key with the RSA private key */
			//Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding","SC");
			Cipher cipher = Cipher.getInstance("RSA/None/" + KeyProperties.ENCRYPTION_PADDING_RSA_OAEP);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] keyBytes = cipher.doFinal(ekey);
			byte inputBytes[] = CryptoUtils.joinByteArray(ciphertext, tag);

		/* Decrypt AES encrypted data */
			byte[] data = decodeAesGcm(StringUtils.hexStringToByteArray(new String(keyBytes)), aad, iv, inputBytes);
			return data;
		} catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
			throw CryptoExceptionFactory.cryptoException(e);
			//} catch (NoSuchProviderException e) {
			//	throw CryptoExceptionFactory.cryptoException(e);
		}
	}
}
