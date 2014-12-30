package com.coder5560.game.screens;

import utils.screen.AbstractGameScreen;
import utils.screen.GameCore;
import utils.screen.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.PlatformType;

public class FlashScreen extends AbstractGameScreen {
	Image		imgFlash;

	boolean		loaded			= false;
	boolean		checkedNetwork	= false;
	GameScreen	gameScreen;

	public FlashScreen(GameCore game) {
		super(game);
		imgFlash = new Image(new Texture(Gdx.files.internal("Img/splash.png")));
		imgFlash.setOrigin(Align.center);
		imgFlash.setPosition(Constants.WIDTH_SCREEN / 2,
				Constants.HEIGHT_SCREEN / 2, Align.center);
	}

	@Override
	public void show() {
		super.show();
		gameScreen = new GameScreen(parent);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	float	time	= 0;

	@Override
	public void update(float delta) {
		if (time <= 0.1f)
			time += delta;
		if (!loaded && time > 0.1f) {
			if (Assets.instance.assetManager.update()) {
				Assets.instance.init();
				if (parent.getPlatformResolver().getPlatform() == PlatformType.ANDROID)
					checkNetworkAndInitialConfig();
				else {
					// Request.getInstance().loadConfig();
					switchScreen();
				}
				checkedNetwork = false;
				loaded = true;
			}
		}
	}

	boolean	switchScreen	= false;

	void switchScreen() {
		// TestScreen testScreen = new TestScreen(parent);
		// if (!switchScreen) {
		// parent.setScreen(testScreen);
		// switchScreen = true;
		// }

		if (!switchScreen) {
			parent.setScreen(gameScreen);
			switchScreen = true;
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		imgFlash.act(delta);
		imgFlash.draw(batch, 1f);
		batch.end();
	}

	void checkNetworkAndInitialConfig() {
		if (parent.getNetworkManager().isNetworkEnable()) {
			if (!checkedNetwork) {
				imgFlash.addAction(Actions.sequence(Actions.run(new Runnable() {

					@Override
					public void run() {
						switchScreen();
						checkedNetwork = true;
					}
				})));
			}
		} else {
			imgFlash.addAction(Actions.forever(Actions.sequence(
					Actions.run(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(stage, "No network avaiable !",
									Toast.LENGTH_SHORT);
						}
					}), Actions.delay(3.5f), Actions.run(new Runnable() {

						@Override
						public void run() {
							if (parent.getNetworkManager().isNetworkEnable()) {
								if (!checkedNetwork) {
									// Request.getInstance().loadConfig();
									imgFlash.addAction(Actions.sequence(
											Actions.alpha(0f, .2f),
											Actions.run(new Runnable() {
												@Override
												public void run() {
													switchScreen();
													checkedNetwork = true;
												}
											})));
								}
							}
						}
					}))));
		}
	}
}
