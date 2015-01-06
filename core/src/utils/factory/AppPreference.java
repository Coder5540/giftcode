package utils.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.coder5560.game.enums.Constants;

public class AppPreference {
	public static final String	TAG					= AppPreference.class
															.getName();
	public static AppPreference	instance			= new AppPreference();

	private Preferences			preferences;
	public String				name, pass;
	public int					type;
	public boolean				isWaitActiveCode	= false;
	public boolean				isLogin				= false;

	private AppPreference() {
		preferences = Gdx.app.getPreferences(Constants.APP_NAME);
		load();
	}

	public void load() {
		name = preferences.getString("name");
		type = preferences.getInteger("type");
		isWaitActiveCode = preferences.getBoolean("waitactivecode");
		isLogin = preferences.getBoolean("islogin", false);
	}

	public void save() {
		preferences.putString("name", name);
		preferences.putInteger("type", type);
		preferences.putBoolean("waitactivecode", isWaitActiveCode);
		preferences.putBoolean("islogin", isLogin);
		preferences.flush();
	}

	public String getName() {
		return name;
	}

	public void setName(String name, boolean flush) {
		this.name = name;
		preferences.putString("name", name);
		if (flush)
			flush();
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass, boolean flush) {
		this.pass = pass;
		preferences.putString("pass", pass);
		if (flush)
			flush();
	}

	public void setLogin(boolean isLogin, boolean flush) {
		this.isLogin = isLogin;
		preferences.putBoolean("islogin", isLogin);
		if (flush)
			flush();
	}

	// public String getDeviceId() {
	// return deviceId;
	// }

	// public void setDeviceId(String deviceId, boolean flush) {
	// this.deviceId = deviceId;
	// preferences.putString("deviceID", deviceId);
	// if (flush)
	// flush();
	// }
	//
	// public String getDeviceName() {
	// return deviceName;
	// }
	//
	// public void setDeviceName(String deviceName, boolean flush) {
	// this.deviceName = deviceName;
	// preferences.putString("deviceName", deviceName);
	// if (flush)
	// flush();
	// }
	//
	public void flush() {
		preferences.flush();
	}

	public Preferences getPreferences() {
		return preferences;
	}
}
