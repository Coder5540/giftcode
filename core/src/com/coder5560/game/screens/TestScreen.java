package com.coder5560.game.screens;

import utils.factory.StringSystem;
import utils.factory.UpdateHandlerList;
import utils.screen.AbstractGameScreen;
import utils.screen.GameCore;
import alphabethame.updatehandler.MemoryManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.GameEvent;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnSelectListener;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.ViewController;

public class TestScreen extends AbstractGameScreen {
	ViewController			controller;
	Image					flash;
	public GestureDetector	gestureDetector;
	UpdateHandlerList		updateHandlerList;

	public TestScreen(GameCore game) {
		super(game);
		updateHandlerList = new UpdateHandlerList();
		MemoryManager memoryManager = new MemoryManager();
		updateHandlerList.add(memoryManager);
	}

	@Override
	public void show() {
		super.show();
		controller = new ViewController(parent, this);
		controller.platformResolver = parent.getPlatformResolver();
		controller.buidTest(stage);
		try {
			Constants.DEVICE_ID = controller.platformResolver.getDeviceID();
			Constants.DEVICE_NAME = controller.platformResolver.getDeviceName();
		} catch (Exception e) {

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

	boolean	isExit		= false;
	float	timeExit	= 0;

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {

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

	OnSelectListener	selectListener	= new OnSelectListener() {

											@Override
											public void onSelect(int i) {
											}
										};

}
