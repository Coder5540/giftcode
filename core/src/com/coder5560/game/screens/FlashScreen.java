package com.coder5560.game.screens;

import utils.factory.AppPreference;
import utils.factory.Log;
import utils.screen.AbstractGameScreen;
import utils.screen.GameCore;
import utils.screen.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.GameState;
import com.coder5560.game.enums.PlatformType;
import com.coder5560.game.ui.DialogCustom;

public class FlashScreen extends AbstractGameScreen {
	private Image imgFlash;

	private boolean loaded = false;

	private boolean checkedNetwork = false;

	private GameScreen gameScreen;

	private JsonValue responseGetVersion = null;
	
	private DialogCustom dia;

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
		stage = new Stage(viewport);
		parent.inputMultiplexer = new InputMultiplexer(stage);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	float time = 0;

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
					requestGetVersion();
				}
				checkedNetwork = false;
				loaded = true;
			}
		}
		updateVersion(delta);
		catchBackKey();
	}

	void catchBackKey() {
		if (Gdx.input.isKeyPressed(Keys.BACK)
				|| Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	void requestGetVersion() {
		getVersion(new HttpResponseListener() {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				responseGetVersion = new JsonReader().parse(httpResponse
						.getResultAsString());
				Log.d(responseGetVersion.toString());
			}

			@Override
			public void failed(Throwable t) {
				System.out.println("Response Fail");
			}

			@Override
			public void cancelled() {
				System.out.println("Response cancel");
			}
		});
	}

	void updateVersion(float delta) {
		if (responseGetVersion != null) {
			int currentVersion = responseGetVersion.getInt("current_version");
			if (currentVersion == AppPreference.instance.version) {
				switchScreen();
			} else {
				boolean mustUpdate = responseGetVersion
						.getBoolean("must_update");
				final String link = responseGetVersion
						.getString((parent.getPlatformResolver().getPlatform() == PlatformType.ANDROID) ? "link_android"
								: "link_ios");
				String message = responseGetVersion.getString("notify_content");
				dia = new DialogCustom("");
				dia.text(message);
				if (mustUpdate) {
					dia.button("Ok", new Runnable() {
						@Override
						public void run() {
							Gdx.net.openURI(link);
							Gdx.app.exit();
						}
					});
					dia.show(stage);
				} else {
					dia.button("Ok", new Runnable() {
						@Override
						public void run() {
							Gdx.net.openURI(link);
							Gdx.app.exit();
						}
					});
					dia.button("Cancel", new Runnable() {
						@Override
						public void run() {
							switchScreen();
						}
					});
					dia.show(stage);
				}
			}
			responseGetVersion = null;
		}
	}

	boolean switchScreen = false;
	boolean checkVersion = false;

	void switchScreen() {
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
		stage.act();
		stage.draw();
	}

	void checkNetworkAndInitialConfig() {
		if (parent.getNetworkManager().isNetworkEnable()) {
			if (!checkedNetwork) {
				imgFlash.addAction(Actions.sequence(Actions.run(new Runnable() {

					@Override
					public void run() {
						requestGetVersion();
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

	void getVersion(HttpResponseListener httpResponseListener) {
		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
		httpRequest.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		httpRequest.setUrl("http://bai8b8.com/config/gift_code.html");
		Gdx.net.sendHttpRequest(httpRequest, httpResponseListener);
	}

}
