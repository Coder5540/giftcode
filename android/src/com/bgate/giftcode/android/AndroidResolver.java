package com.bgate.giftcode.android;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import utils.factory.PlatformResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.badlogic.gdx.graphics.Pixmap;
import com.coder5560.game.enums.PlatformType;

public class AndroidResolver implements PlatformResolver {
	AndroidLauncher	main;
	MediaPlayer		mediaPlayer;
	PlatformType	type;

	public AndroidResolver(AndroidLauncher main) {
		this.main = main;
		mediaPlayer = new MediaPlayer();
	}

	@Override
	public Pixmap formatBitmap(InputStream in) {
		Pixmap pixmap = null;

		try {
			Bitmap myBitmap = BitmapFactory.decodeStream(in);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			myBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			pixmap = new Pixmap(outStream.toByteArray(), 0,
					outStream.toByteArray().length);
		} catch (Exception e) {
		}
		return pixmap;
	}

	@Override
	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	@Override
	public String getDeviceID() {
		TelephonyManager mngr = (TelephonyManager) main
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mngr.getDeviceId();
		return imei;
	}

	@Override
	public void setPlatform(PlatformType platformType) {
		this.type = platformType;
	}

	@Override
	public PlatformType getPlatform() {
		return type;
	}

	@Override
	public void sendSMS(String number, String message, OnResultListener listener) {
		main.sendSMS(number, message, listener);
	}

	@Override
	public String getCountryCode() {

		String CountryID = "";
		String CountryZipCode = "";

		TelephonyManager manager = (TelephonyManager) main
				.getSystemService(main.TELEPHONY_SERVICE);
		// getNetworkCountryIso
		CountryID = manager.getSimCountryIso().toUpperCase();
		String[] rl = main.getResources().getStringArray(R.array.CountryCodes);
		for (int i = 0; i < rl.length; i++) {
			String[] g = rl[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				CountryZipCode = g[0];
				break;
			}
		}
		return CountryZipCode;
	}
}