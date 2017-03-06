package com.bbva.kst.uniqueid.dep.crypto;

import android.util.Base64;
import com.bbva.kst.uniqueid.dep.crypto.models.AesGcmEncData;
import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;
import com.bbva.kst.uniqueid.instruments.utils.StringUtils;
import com.intelygenz.android.KeyValueKeeper;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.spongycastle.jce.provider.BouncyCastleProvider;

class CryptoUtils {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	static String getHexIV() {
		return generateRandomHexString(16);
	}

	static String getBase64IV() {
		return CryptoUtils.encodeByteArrayToBase64(generateRandomBytes(12));
	}

	static String getAAD() {
		return "masterEncKey";
	}

	private static String generateRandomHexString(int length) {
		SecureRandom sr = new SecureRandom();
		byte[] saltBytes = new byte[length];
		sr.nextBytes(saltBytes);
		return StringUtils.bytesToHexString(saltBytes);
	}

	private static byte[] generateRandomBytes(int length) {
		SecureRandom sr = new SecureRandom();
		byte[] saltBytes = new byte[length];
		sr.nextBytes(saltBytes);
		return saltBytes;
	}

	static byte[] getHashSha256(byte[] input) throws CryptoException {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			return digest.digest(input);
		} catch (NoSuchAlgorithmException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static byte[] getHashSha1(byte[] input) throws CryptoException {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			return digest.digest(input);
		} catch (NoSuchAlgorithmException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static String getHexSalt(KeyValueKeeper keyValueKeeper) {
		String salt = CryptoKeysStorage.storeSalt(keyValueKeeper);
		if (salt == null) {
			salt = generateRandomHexString(16);
			CryptoKeysStorage.storeSalt(keyValueKeeper, salt);
		}
		return salt;
	}

	static byte[] byteArrayXOR(byte[] arr1, byte[] arr2) throws CryptoException {
		if (arr1.length == arr2.length) {
			byte output[] = new byte[arr1.length];
			for (int i = 0; i < arr1.length; i++) {
				int xor = arr1[i] ^ arr2[i];
				output[i] = (byte) (0xff & xor);
			}
			return output;
		} else {
			throw CryptoExceptionFactory.cryptoException();
		}
	}

	static byte[] joinByteArray(byte[] arr1, byte[] arr2) {
		byte inputBytes[] = new byte[arr1.length + arr2.length];
		System.arraycopy(arr1, 0, inputBytes, 0, arr1.length);
		System.arraycopy(arr2, 0, inputBytes, arr1.length, arr2.length);
		return inputBytes;
	}

	static byte[] getRsaPssSign(String text, KeyValueKeeper keyValueKeeper) throws CryptoException {
		try {
			SecureRandom random = new SecureRandom();
			KeyFactory rsaFact = KeyFactory.getInstance("RSA", "SC");
			RSAPrivateKey key = (RSAPrivateKey) rsaFact.generatePrivate(new PKCS8EncodedKeySpec(CryptoKeys.getDevicePrivateKey(keyValueKeeper)));
			PSSParameterSpec pssParam = new PSSParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, 20, 1);
			Signature pss = Signature.getInstance("RSASSA-PSS", "SC");
			pss.initSign(key, random);
			pss.setParameter(pssParam);
			pss.update(text.getBytes());
			return pss.sign();
		} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | NoSuchProviderException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
			throw CryptoExceptionFactory.cryptoException(e);
		}
	}

	static RSAKeyParameters generatePrivateKeyParameter(
		java.security.interfaces.RSAPrivateKey key) {
		if (key instanceof RSAPrivateCrtKey) {
			RSAPrivateCrtKey k = (RSAPrivateCrtKey) key;
			return new RSAPrivateCrtKeyParameters(k.getModulus(), k.getPublicExponent(), k.getPrivateExponent(), k.getPrimeP(), k.getPrimeQ(),
												  k.getPrimeExponentP(), k.getPrimeExponentQ(), k.getCrtCoefficient());
		} else {
			java.security.interfaces.RSAPrivateKey k = key;
			return new RSAKeyParameters(true, k.getModulus(), k.getPrivateExponent());
		}
	}

	static AesGcmEncData aesGcmStringDataToAesGcmByteArrayData(AesGcmEncryptedData data) {
		return new AesGcmEncData(CryptoUtils.decodeBase64ToByteArray(data.data), data.iv, data.aad, StringUtils.hexStringToByteArray(data.tag));
	}

	static AesGcmEncryptedData aesGcmByteArrayDataToAesGcmStringData(AesGcmEncData data) {
		return new AesGcmEncryptedData(CryptoUtils.encodeByteArrayToBase64(data.data), data.iv, data.aad, StringUtils.bytesToHexString(data.tag));
	}

	static String encodeByteArrayToBase64(byte[] bytes) {
		return Base64.encodeToString(bytes, Base64.NO_WRAP);
	}

	static byte[] decodeBase64ToByteArray(String str) {
		return Base64.decode(str, Base64.NO_WRAP);
	}

	static String bidirectionalByteArrayToString(byte[] input) {
		return Base64.encodeToString(input, Base64.NO_WRAP);
	}

	static String decodeFromBase64String(String input) {
		byte[] bytes = Base64.decode(input, Base64.NO_WRAP);
		return new String(bytes);
	}

	static String encodeToBase64String(String input) {
		return new String(Base64.encode(input.getBytes(), Base64.NO_WRAP));
	}
}
