package com.bbva.kst.uniqueid.dep.deviceId;

import android.content.Context;
import com.bbva.kst.uniqueid.dep.deviceid.DeviceId;
import com.bbva.kst.uniqueid.instruments.utils.StringUtils;
import com.intelygenz.android.KeyValueKeeper;
import java.security.SecureRandom;

public class DeviceIdProvider implements DeviceId {

	private static final String DEVICE_ID_KEY = "DEVICE_ID";

	private final Context context;
	private final KeyValueKeeper keyValueKeeper;

	public DeviceIdProvider(Context context, KeyValueKeeper kvk) {
		this.context = context;
		this.keyValueKeeper = kvk;
	}

	@Override
	public String getDeviceUniqueId() {
		//UUID uuid = recoverUuid(context);
		//return toFormattedString(uuid);
		String deviceId = keyValueKeeper.load(DEVICE_ID_KEY, null, String.class);
		if (StringUtils.isNullOrEmpty(deviceId)) {
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[16];
			random.nextBytes(bytes);
			deviceId = StringUtils.bytesToHexString(bytes);
			keyValueKeeper.save(DEVICE_ID_KEY, deviceId);
		}
		return deviceId;
	}

	@Override
	public String getDeviceName() {
		return android.os.Build.MODEL;
	}

	//@SuppressLint("HardwareIds")
	//private static UUID recoverUuid(Context context) {
	//	TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	//	String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	//	String tmDevice = StringUtils.emptyIfNullString(telephonyManager.getDeviceId());
	//	String tmSerial = StringUtils.emptyIfNullString(telephonyManager.getSimSerialNumber());
	//	return new UUID(deviceId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
	//}
	//
	//private static String toFormattedString(UUID deviceUuid) {
	//	String result = deviceUuid.toString();
	//	if (result.length() > 32) {
	//		result = result.replaceAll("-", "");
	//	}
	//	return result;
	//}
}
