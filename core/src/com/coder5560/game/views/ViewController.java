package com.coder5560.game.views;

import imp.view.ViewLogin;
import utils.factory.AppPreference;
import utils.factory.PlatformResolver;
import utils.factory.StringSystem;
import utils.networks.FacebookConnector;
import utils.screen.GameCore;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.screens.GameScreen;

public class ViewController implements IViewController {
	public Stage				stage;
	public Array<IViews>		views;

	public IViews				currentView;
	public FacebookConnector	facebookConnector;
	private GameCore			_gameParent;
	private GameScreen			_gameScreen;
	public PlatformResolver		platformResolver;

	public ViewController(GameCore _gameParent, GameScreen gameScreen) {
		super();
		this._gameParent = _gameParent;
		this._gameScreen = gameScreen;
		AppPreference.instance.load();
	}

	public void build(Stage stage) {
		this.stage = stage;
		views = new Array<IViews>();
		ViewLogin viewLogin = new ViewLogin();
		viewLogin.build(stage, this, StringSystem.VIEW_LOGIN, new Rectangle(0,
				0, Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
		viewLogin.buildComponent();
		viewLogin.show(new OnCompleteListener() {

			@Override
			public void onError() {
			}

			@Override
			public void done() {
			}
		});
	}

	@Override
	public void update(float delta) {
		for (int i = 0; i < views.size; i++) {
			views.get(i).update(delta);
			if (views.get(i).getViewState() == ViewState.DISPOSE) {
				removeView(views.get(i).getName());
			}
		}
	}

	@Override
	public boolean isContainView(String name) {
		if (avaiable()) {
			for (int i = 0; i < views.size; i++) {
				if (views.get(i).getName().equalsIgnoreCase(name))
					return true;
			}
		}
		return false;
	}

	@Override
	public boolean addView(IViews view) {
		if (!avaiable())
			return false;
		if (isContainView(view.getName()))
			return false;
		views.add(view);
		return true;
	}

	@Override
	public void removeView(String name) {
		if (!avaiable())
			return;
		IViews view = getView(name);
		if (view == null)
			return;
		view.destroyComponent();
		views.removeValue(view, false);
		stage.getActors().removeValue((Actor) view, true);
	}

	@Override
	public void toFront(String name) {
		if (isContainView(name)) {
			((Actor) getView(name)).toFront();
			if (isContainView(StringSystem.VIEW_MAIN_MENU))
				((Actor) getView(StringSystem.VIEW_MAIN_MENU)).toFront();
			if (isContainView(StringSystem.VIEW_ACTION_BAR))
				((Actor) getView(StringSystem.VIEW_ACTION_BAR)).toFront();
		}
	}

	@Override
	public IViews getView(String name) {
		for (int i = 0; i < views.size; i++) {
			if (views.get(i).getName().equalsIgnoreCase(name)) {
				return views.get(i);
			}
		}
		return null;
	}

	@Override
	public Array<IViews> getViews() {
		if (avaiable())
			return views;
		return null;
	}

	public boolean avaiable() {
		return views != null && stage != null;
	}

	@Override
	public Stage getStage() {
		return stage;
	}

	public void setFacebookConnector(FacebookConnector facebookConnector) {
		this.facebookConnector = facebookConnector;
	}

	public FacebookConnector getFacebookConnector() {
		return facebookConnector;
	}

	@Override
	public void setGameParent(GameCore gameParent) {
		this._gameParent = gameParent;
	}

	@Override
	public GameCore getGameParent() {
		return _gameParent;
	}

	// this method will sort that all of our view from a container of view.
	@Override
	public void sortView() {
	}

	@Override
	public IViews getCurrentView() {
		return currentView;
	}

	@Override
	public void setCurrentView(IViews view) {
		this.currentView = view;
		TraceView.instance.addViewToTrace(view.getName());
	}

	@Override
	public GameScreen getGameScreen() {
		return _gameScreen;
	}

	public PlatformResolver getPlatformResolver() {
		return platformResolver;
	}

	public void setPlatformResolver(PlatformResolver platformResolver) {
		this.platformResolver = platformResolver;
	}

}
