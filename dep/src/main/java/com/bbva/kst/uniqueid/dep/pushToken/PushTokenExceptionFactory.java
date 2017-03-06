package com.bbva.kst.uniqueid.dep.pushToken;

@SuppressWarnings("MethodNameSameAsClassName")
class PushTokenExceptionFactory {

	static PushTokenException PushTokenExceptionFactory() {
		return new PushTokenException(null);
	}

	static PushTokenException PushTokenExceptionFactory(Throwable e) {
		return new PushTokenException(e);
	}

	static PushTokenException PushTokenExceptionFactory(Throwable e, String message) {
		return new PushTokenException(e, message);
	}
	
}
