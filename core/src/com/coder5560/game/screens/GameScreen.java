package com.coder5560.game.screens;

import imp.view.MainMenuView;
import updatehandler.MemoryManager;
import utils.factory.AppPreference;
import utils.factory.IUpdateHandler;
import utils.factory.Log;
import utils.factory.StringSystem;
import utils.factory.UpdateHandlerList;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.GameCore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.GameEvent;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnSelectListener;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.ViewController;

public class GameScreen extends AbstractGameScreen {
	ViewController			controller;
	Image					flash;
	public GestureDetector	gestureDetector;
	UpdateHandlerList		updateHandlerList;

	public GameScreen(GameCore game) {
		super(game);
		updateHandlerList = new UpdateHandlerList();
		MemoryManager memoryManager = new MemoryManager();
		updateHandlerList.add(memoryManager);
		updateHandlerList.add(onPingHandler);
	}

	@Override
	public void show() {
		super.show();
		controller = new ViewController(parent, this);
		controller.setFacebookConnector(parent.facebookConnector);
		controller.platformResolver = parent.getPlatformResolver();
		controller.build(stage);
		try {
			Constants.DEVICE_ID = controller.platformResolver.getDeviceID();
			Constants.DEVICE_NAME = controller.platformResolver.getDeviceName();
			Log.d("Device_id=" + Constants.DEVICE_ID);
			Log.d("Device_name=" + Constants.DEVICE_NAME);
		} catch (Exception e) {
			Request.getInstance().killAllProcess();
			Request.getInstance().requestQuitApp(UserInfo.phone,
					new HttpResponseListener() {

						@Override
						public void handleHttpResponse(HttpResponse httpResponse) {
						}

						@Override
						public void failed(Throwable t) {
							Log.d("PING : " + "Fail ");
							((MainMenuView) controller
									.getView(StringSystem.VIEW_MAIN_MENU)).onLogoutListener
									.onClick(0, 0);
						}

						@Override
						public void cancelled() {
							Log.d("PING : " + "Cancel !");
						}
					});
		}
		Gdx.input.setCatchBackKey(true);
		System.gc();

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void update(float delta) {
		updateHandlerList.onUpdate(delta);
		controller.update(delta);
		if (isExit) {
			timeExit += delta;
			if (timeExit >= 2) {
				timeExit = 0;
				isExit = false;
			}
		}
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	boolean	isExit		= false;
	float	timeExit	= 0;

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			if (controller.getCurrentView().getName()
					.equalsIgnoreCase(StringSystem.VIEW_LOGIN)) {
				controller.getView(StringSystem.VIEW_LOGIN).back();
				return true;
			}

			if (controller.getView(StringSystem.VIEW_MAIN_MENU) != null
					&& controller.getView(StringSystem.VIEW_MAIN_MENU)
							.getViewState() == ViewState.SHOW) {
				controller.getView(StringSystem.VIEW_MAIN_MENU).hide(null);
				return true;
			}

			if (AbstractGameScreen.keyboard.isShowing()) {
				AbstractGameScreen.keyboard.hide();
				return true;
			}
			TraceView.instance.debug();
			if (controller.getView(TraceView.instance.getLastView()) != null) {
				controller.getView(TraceView.instance.getLastView()).back();
				controller.notifyEvent(GameEvent.ONBACK);
			}
		}
		return false;
	}

	public void setGestureDetector(GestureDetector detector) {
		this.gestureDetector = detector;
		parent.inputMultiplexer = new InputMultiplexer(keyboard, detector,
				this, stage);
		Gdx.input.setInputProcessor(parent.inputMultiplexer);
	}

	IUpdateHandler		onPingHandler	= new IUpdateHandler() {
											Actor	actor	= new Actor();
											boolean	create	= false;

											@Override
											public void reset() {

											}

											@Override
											public void onUpdate(float delta) {
												if (AppPreference.instance.isLogin) {
													if (!create) {
														actor.clearActions();
														actor.addAction(Actions
																.forever(Actions
																		.sequence(
																				Actions.delay(1f),
																				Actions.run(new Runnable() {

																					@Override
																					public void run() {
																						Request.getInstance()
																								.requestPing(
																										UserInfo.phone,
																										parent.getPlatformResolver()
																												.getDeviceName(),
																										new HttpResponseListener() {

																											@Override
																											public void handleHttpResponse(
																													HttpResponse httpResponse) {

																											}

																											@Override
																											public void failed(
																													Throwable t) {

																											}

																											@Override
																											public void cancelled() {

																											}
																										});
																					}
																				}))));
														create = true;
													}
													actor.act(delta);
												} else {
													if (create)
														create = false;
												}
											}
										};

	OnSelectListener	selectListener	= new OnSelectListener() {

											@Override
											public void onSelect(int i) {
											}
										};

}
