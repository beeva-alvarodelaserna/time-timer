package com.bbva.kst.uniqueid.domains.device;

public class Device {

	public final String platform = "android";
	private final String name;
	private final String nsuuid;
	private final String public_key;

	public Device(String name, String nsuuid, String public_key) {
		this.name = name;
		this.nsuuid = nsuuid;
		this.public_key = public_key;
	}
}
