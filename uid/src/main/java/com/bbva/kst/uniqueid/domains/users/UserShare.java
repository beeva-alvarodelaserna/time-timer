package com.bbva.kst.uniqueid.domains.users;

public class UserShare {

	public final String email;
	public final String phoneNumber;
	public String avatar;
	public final String expires;

	private UserShare(Builder builder) {
		this.phoneNumber = builder.phoneNumber;
		this.email = builder.email;
		this.avatar = builder.avatar;
		this.expires = builder.expires;
	}

	public static class Builder {

		private String email;
		private String phoneNumber;
		private String avatar;
		private String expires;

		public UserShare build() {
			return new UserShare(this);
		}

		public UserShare.Builder toBuild(UserShare userEncrypted) {
			this.email = userEncrypted.email;
			this.phoneNumber = userEncrypted.phoneNumber;
			this.avatar = userEncrypted.avatar;
			this.expires = userEncrypted.expires;
			return this;
		}

		public UserShare.Builder email(String email) {
			this.email = email;
			return this;
		}

		public UserShare.Builder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public UserShare.Builder avatar(String avatar) {
			this.avatar = avatar;
			return this;
		}

		public UserShare.Builder expiration(String expiration) {
			this.expires = expiration;
			return this;
		}

	}
}
