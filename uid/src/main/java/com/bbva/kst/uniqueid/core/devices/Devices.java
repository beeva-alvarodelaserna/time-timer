package com.bbva.kst.uniqueid.core.devices;

public interface Devices {

	void registerDevice() throws DevicesException;

	void putDevice(String userId) throws DevicesException;

}
