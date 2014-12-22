package com.bgate.giftcode.desktop;


import java.io.InputStream;

import com.badlogic.gdx.graphics.Pixmap;
import com.coder5560.game.enums.PlatformType;

import utils.factory.PlatformResolver;

public class DesktopResolver implements PlatformResolver {

	@Override
	public Pixmap formatBitmap(InputStream in) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeviceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDeviceID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPlatform(PlatformType flatformType) {
		// TODO Auto-generated method stub

	}

	@Override
	public PlatformType getPlatform() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendSMS(String number, String message, OnResultListener listener) {
		listener.onError();
	}

	@Override
	public String getCountryCode() {
		return "+84";
	}

}
