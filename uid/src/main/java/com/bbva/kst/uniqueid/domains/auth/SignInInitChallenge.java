package com.bbva.kst.uniqueid.domains.auth;

public class SignInInitChallenge {

	public final String salt;
	public final String challenge;
	public final String ticket;

	private SignInInitChallenge(Builder builder) {
		this.salt = builder.salt;
		this.challenge = builder.challenge;
		this.ticket = builder.ticket;
	}

	public static class Builder {

		private String salt;
		private String challenge;
		private String ticket;

		public Builder salt(String salt) {
			this.salt = salt;
			return this;
		}

		public Builder challenge(String challenge) {
			this.challenge = challenge;
			return this;
		}

		public Builder ticket(String ticket) {
			this.ticket = ticket;
			return this;
		}

		public SignInInitChallenge build() {
			return new SignInInitChallenge(this);
		}

	}

}
