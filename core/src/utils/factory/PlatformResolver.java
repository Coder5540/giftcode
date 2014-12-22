package utils.factory;

import java.io.InputStream;

import com.badlogic.gdx.graphics.Pixmap;
import com.coder5560.game.enums.PlatformType;

public interface PlatformResolver {

	public Pixmap formatBitmap(InputStream in);

	public String getDeviceName();

	public String getDeviceID();

	public void setPlatform(PlatformType flatformType);

	public PlatformType getPlatform();

	public void sendSMS(String number, String message, OnResultListener listener);

	public interface OnResultListener {
		public void onComplete();

		public void onError();
	}

	public String getCountryCode();
}
