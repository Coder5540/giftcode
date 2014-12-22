package com.bgate.giftcode.android;


import java.util.ArrayList;

import utils.factory.Factory;
import utils.factory.PlatformResolver.OnResultListener;
import utils.screen.GameCore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.coder5560.game.enums.NetworkState;
import com.coder5560.game.enums.PlatformType;
import com.coder5560.game.listener.NetworkManager;
import com.coder5560.game.screens.FlashScreen;

@SuppressLint("NewApi")
public class AndroidLauncher extends AndroidApplication implements NetworkManager{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		GameCore game = new GameCore() {
			@Override
			public void create() {
				super.create();
				setScreen(new FlashScreen(this));
			}
		};
		
		game.setPlatformResolver(new AndroidResolver(AndroidLauncher.this));
		game.getPlatformResolver().setPlatform(PlatformType.ANDROID);
		game.setNetworkManager(AndroidLauncher.this);
		initialize(game, config);
//		AndroidTextInputHelper textInputHelper = new AndroidTextInputHelper(
//				this);
//		Factory.helper = textInputHelper;
//
//		View inputView = textInputHelper.getView();
//		View gameView = initializeForView(game, config);
//
//		FrameLayout layout = new FrameLayout(this);
//		layout.addView(inputView);
//		layout.addView(gameView);
//		setContentView(layout);

//		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//				.permitAll().build();
//		StrictMode.setThreadPolicy(policy);
	}

	public void sendSMS(String phoneNumber, String message,
			final OnResultListener listener) {
		SmsManager smsManager = SmsManager.getDefault();

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> parts = sms.divideMessage(message);
		int messageCount = parts.size();

		Log.i("Message Count", "Message Count: " + messageCount);

		ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();
		ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		for (int j = 0; j < messageCount; j++) {
			sentIntents.add(sentPI);
			deliveryIntents.add(deliveredPI);
		}

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					listener.onComplete();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					listener.onError();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					listener.onError();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					listener.onError();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					listener.onError();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {

				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));
		smsManager.sendTextMessage(phoneNumber, null, message, sentPI,
				deliveredPI);
		/*
		 * sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents,
		 * deliveryIntents);
		 */
	}

	@Override
	public boolean isNetworkEnable() {
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conMan.getActiveNetworkInfo();
		if (info == null)
			return false;
		if (info.isConnectedOrConnecting())
			return true;
		return false;
	}
	
}
