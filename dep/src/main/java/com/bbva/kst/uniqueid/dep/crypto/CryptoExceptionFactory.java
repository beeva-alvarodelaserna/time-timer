package com.bbva.kst.uniqueid.dep.crypto;

class CryptoExceptionFactory {

	static CryptoException cryptoException(Throwable e) {
		return new CryptoException(e);
	}

	static CryptoException cryptoException() {
		return new CryptoException(null, "Two byte arrays have different length");
	}

	static CryptoException noCipherCryptoException() {
		return new CryptoException(null, CryptoException.NO_CIPHER_RETRIEVED_CODE);
	}

	static CryptoException noEncodingResultCryptoException() {
		return new CryptoException(null, CryptoException.NO_ENCODING_RESULT_CODE);
	}

	static CryptoException noKeyFoundCryptoException() {
		return new CryptoException(null, CryptoException.NO_KEY_FOUND_CODE);
	}

	static CryptoException userNotAuthenticatedCryptoException(Throwable e) {
		return new CryptoException(e, CryptoException.USER_NOT_AUTHENTICATED_CODE);
	}
	
}
