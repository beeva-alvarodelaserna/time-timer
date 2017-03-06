package com.beeva.labs.timetimer.support.inject.modules.app;

import android.content.Context;
import com.android.intelygenz.kvksp.SpKeeper;
import com.bbva.kst.uniqueid.dep.base64.Base64Engine;
import com.bbva.kst.uniqueid.dep.base64.Base64Tools;
import com.bbva.kst.uniqueid.dep.crypto.AndroidCryptoKeyStore;
import com.bbva.kst.uniqueid.dep.crypto.Crypto;
import com.bbva.kst.uniqueid.dep.deviceId.DeviceIdProvider;
import com.bbva.kst.uniqueid.dep.deviceid.DeviceId;
import com.bbva.kst.uniqueid.dep.imageProcessing.ImageProcessing;
import com.bbva.kst.uniqueid.dep.imageProcessing.ImageProcessingNative;
import com.bbva.kst.uniqueid.dep.pushToken.PushToken;
import com.bbva.kst.uniqueid.dep.pushToken.PushTokenProvider;
import com.bbva.kst.uniqueid.dep.saveImagesFiles.SaveFiles;
import com.bbva.kst.uniqueid.dep.saveImagesFiles.SaveImage;
import com.intelygenz.android.KeyValueKeeper;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module
public class DepModule {

	@Provides(singleton = true)
	KeyValueKeeper provideKeyValueKeeper(Context context) {
		return new SpKeeper(context);
	}

	@Provides(singleton = true)
	Crypto provideCrypto(KeyValueKeeper keyValueKeeper) {
		return new AndroidCryptoKeyStore(keyValueKeeper);
	}

	@Provides(singleton = true)
	DeviceId provideDeviceId(Context context, KeyValueKeeper keyValueKeeper) {
		return new DeviceIdProvider(context, keyValueKeeper);
	}

	@Provides(singleton = true)
	PushToken providePushToken(Context context) {
		return new PushTokenProvider(context);
	}

	@Provides(singleton = true)
	SaveFiles provideSaveFiles(Context context) {
		return new SaveImage(context, "UniqueIdTemp");
	}

	@Provides(singleton = true)
	ImageProcessing provideImageProcessing(SaveFiles saveFiles) {
		return new ImageProcessingNative(saveFiles);
	}

	@Provides(singleton = true)
	Base64Tools provideBase64Tools() {
		return new Base64Engine();
	}

}
