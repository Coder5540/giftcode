package utils.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.coder5560.game.enums.Constants;

public class AppPreference {
	public static final String TAG = AppPreference.class.getName();
	public static AppPreference instance = new AppPreference();

	private Preferences preferences;
	private String name, pass;
	public int type;
	public boolean isWaitActiveCode = false;

	private AppPreference() {
		preferences = Gdx.app.getPreferences(Constants.APP_NAME);
		load();
	}

	public void load() {
		name = preferences.getString("name");
		type = preferences.getInteger("type");
		isWaitActiveCode = preferences.getBoolean("waitactivecode");

	}

	public void save() {
		preferences.putString("name", name);
		preferences.putInteger("type", type);
		preferences.putBoolean("waitactivecode", isWaitActiveCode);
		preferences.flush();
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name, boolean flush) {
		this.name = name;
		if(flush) flush();
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass, boolean flush) {
		this.pass = pass;
		if(flush) flush();
	}
	public void flush(){
		preferences.flush();
	}
	public Preferences getPreferences() {
		return preferences;
	}
}
