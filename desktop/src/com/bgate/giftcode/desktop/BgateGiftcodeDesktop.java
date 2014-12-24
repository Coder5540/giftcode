package com.bgate.giftcode.desktop;

import utils.screen.GameCore;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.screens.FlashScreen;

public class BgateGiftcodeDesktop {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		GameCore game = new GameCore() {
			@Override
			public void create() {
				super.create();
				setScreen(new FlashScreen(this));
			}
		};
		config.width = (int) (Constants.WIDTH_SCREEN);
		config.height = (int) (Constants.HEIGHT_SCREEN);

		game.setPlatformResolver(new DesktopResolver());
		new LwjglApplication(game, config);
	}
}
