package com.bbva.kst.uniqueid.domains.users;

import com.bbva.kst.uniqueid.domains.crypto.AesGcmEncryptedData;

public class UserEncrypted {

	public final String email;
	public final String phoneNumber;
	public final String uuid;
	public final String avatar;
	public final boolean validated_au10tix;
	public final boolean validated_directid;
	private final AesGcmEncryptedData directIdEncripted;
	private final String directIdEncriptedKey;
	public final String au10tixEncriptedKey;
	public final AesGcmEncryptedData au10tixEncripted;
	public final String au10tixError;

	private UserEncrypted(Builder builder) {
		this.phoneNumber = builder.phoneNumber;
		this.email = builder.email;
		this.uuid = builder.uuid;
		this.avatar = builder.avatar;
		this.directIdEncripted = builder.directIdEncripted;
		this.directIdEncriptedKey = builder.directIdEncriptedKey;
		this.au10tixEncriptedKey = builder.au10tixEncriptedKey;
		this.au10tixEncripted = builder.au10tixEncripted;
		this.au10tixError = builder.au10tixError;
		this.validated_au10tix = builder.validated_au10tix;
		this.validated_directid = builder.validated_directid;
	}

	public static class Builder {

		private String email;
		private String phoneNumber;
		private String uuid;
		private String avatar;
		private AesGcmEncryptedData directIdEncripted;
		private String directIdEncriptedKey;
		private String au10tixEncriptedKey;
		private AesGcmEncryptedData au10tixEncripted;
		private String au10tixError;
		private boolean validated_au10tix;
		private boolean validated_directid;

		public UserEncrypted build() {
			return new UserEncrypted(this);
		}

		public Builder toBuild(UserEncrypted userEncrypted) {
			this.email = userEncrypted.email;
			this.phoneNumber = userEncrypted.phoneNumber;
			this.uuid = userEncrypted.uuid;
			this.avatar = userEncrypted.avatar;
			this.directIdEncripted = userEncrypted.directIdEncripted;
			this.directIdEncriptedKey = userEncrypted.directIdEncriptedKey;
			this.au10tixEncriptedKey = userEncrypted.au10tixEncriptedKey;
			this.au10tixEncripted = userEncrypted.au10tixEncripted;
			this.au10tixError = userEncrypted.au10tixError;
			this.validated_au10tix = userEncrypted.validated_au10tix;
			this.validated_directid = userEncrypted.validated_directid;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public Builder uuid(String uuid) {
			this.uuid = uuid;
			return this;
		}

		public Builder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public Builder directIdEncripted(AesGcmEncryptedData directIdEncripted) {
			this.directIdEncripted = directIdEncripted;
			return this;
		}

		public Builder directIdEncriptedKey(String directIdEncriptedKey) {
			this.directIdEncriptedKey = directIdEncriptedKey;
			return this;
		}

		public Builder au10tixEncriptedKey(String au10tixEncriptedKey) {
			this.au10tixEncriptedKey = au10tixEncriptedKey;
			return this;
		}

		public Builder au10tixEncripted(AesGcmEncryptedData au10tixEncripted) {
			this.au10tixEncripted = au10tixEncripted;
			return this;
		}

		public Builder au10tixError(String au10tixError) {
			this.au10tixError = au10tixError;
			return this;
		}

		public Builder validatedAu10tix(boolean validated_au10tix) {
			this.validated_au10tix = validated_au10tix;
			return this;
		}

		public Builder validatedDirectId(boolean validated_directid) {
			this.validated_directid = validated_directid;
			return this;
		}
	}
}
