package com.coder5560.game.views;

import utils.factory.PlatformResolver;
import utils.networks.FacebookConnector;
import utils.screen.GameCore;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.coder5560.game.enums.GameEvent;
import com.coder5560.game.screens.GameScreen;

public interface IViewController {

	public void update(float delta);

	/* Check whether stage contain a view by name or not. */
	public boolean isContainView(String name);

	/* add a new view to this stage */
	public boolean addView(IViews view);

	/* remove a view from stage, it's not available anymore */
	public void removeView(String name);

	public void toFront(String name);

	public IViews getView(String name);

	/* return all the view in this stage */
	public Array<IViews> getViews();

	/* this method sorted view by their index to draw to stage */
	public void sortView();

	public IViews getCurrentView();

	public void setCurrentView(IViews view);

	public void setGameParent(GameCore gameParent);

	public FacebookConnector getFacebookConnector();

	public Stage getStage();

	public GameCore getGameParent();

	public GameScreen getGameScreen();

	public PlatformResolver getPlatformResolver();

	public void setPlatformResolver(PlatformResolver platformResolver);

	public void resetAll();

	public void notifyEvent(GameEvent gameEvent);

	public void resetHome();

}
