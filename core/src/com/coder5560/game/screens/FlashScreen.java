package com.coder5560.game.screens;

import utils.keyboard.VirtualKeyboard;
import utils.screen.AbstractGameScreen;
import utils.screen.GameCore;
import utils.screen.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.GameState;
import com.coder5560.game.enums.PlatformType;

public class FlashScreen extends AbstractGameScreen {
	Image		imgFlash;

	boolean		loaded			= false;
	boolean		checkedNetwork	= false;
	GameScreen	gameScreen;

	public FlashScreen(GameCore game) {
		super(game);
	}

	@Override
	public void show() {
		gameState = GameState.INITIAL;
		camera = new OrthographicCamera(Constants.WIDTH_SCREEN,
				Constants.HEIGHT_SCREEN);
		viewport = new StretchViewport(Constants.WIDTH_SCREEN,
				Constants.HEIGHT_SCREEN, camera);
		batch = new SpriteBatch();

		imgFlash = new Image(new Texture(Gdx.files.internal("Img/splash.png")));
		imgFlash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		imgFlash.setOrigin(Align.center);
		imgFlash.setPosition(Constants.WIDTH_SCREEN / 2,
				Constants.HEIGHT_SCREEN / 2, Align.center);
		viewport.update(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN, true);
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
			gameScreen = new GameScreen(parent);
			parent.setScreen(gameScreen);
			switchScreen = true;
		}
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(viewport.getCamera().combined);
		update(delta);
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
