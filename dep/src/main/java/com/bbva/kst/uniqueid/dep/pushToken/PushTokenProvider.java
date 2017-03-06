package com.bbva.kst.uniqueid.dep.pushToken;

import android.content.Context;
import com.bbva.kst.uniqueid.AppConstants;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;

import static com.google.android.gms.gcm.GoogleCloudMessaging.INSTANCE_ID_SCOPE;

public class PushTokenProvider implements PushToken {

	private final Context context;

	public PushTokenProvider(Context context) {
		this.context = context;
	}

	@Override
	public String getPushToken() throws PushTokenException {
		try {
			InstanceID instanceID = InstanceID.getInstance(context);
			return instanceID.getToken(AppConstants.GOOGLE_GCM_SENDER_ID, INSTANCE_ID_SCOPE, null);
		} catch (IOException e) {
			throw PushTokenExceptionFactory.PushTokenExceptionFactory(e);
		}
	}

}
