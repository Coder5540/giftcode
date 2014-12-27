package com.coder5560.game.views;

import imp.view.TopBarView;
import utils.factory.StringSystem;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnCompleteListener;

/**
 * @author Administrator
 * 
 */
public class View extends Table implements IViews {
	public Stage			_stage;
	public IViewController	_viewController;
	public ViewState		state;
	public String			name;

	public View() {
		super();
		setUp();
	}

	public View(Skin skin) {
		super(skin);
		setUp();
	}

	private void setUp() {
		state = ViewState.INITIAL;
		name = "";
	}

	@Override
	public String getLabel() {
		return "Bài 69 Giftcode";
	}

	@Override
	public void build(Stage stage, IViewController viewController,
			String viewName, Rectangle bound) {
		this._stage = stage;
		setName(viewName);
		this.name = viewName;
		setBounds(bound.x, bound.y, bound.width, bound.height);
		setClip(true);
		setTouchable(Touchable.enabled);
		_viewController = viewController;
		stage.addActor(this);
		viewController.addView(this);
		if (getViewController().isContainView(StringSystem.VIEW_ACTION_BAR)) {
			((Actor) viewController.getView(StringSystem.VIEW_ACTION_BAR))
					.toFront();
		}
		if (getViewController().isContainView(StringSystem.VIEW_MAIN_MENU))
			((Actor) viewController.getView(StringSystem.VIEW_MAIN_MENU))
					.toFront();

	}

	@Override
	public void show(OnCompleteListener listener) {
		_viewController.setCurrentView(this);
		_viewController.toFront(name);
		setTouchable(Touchable.enabled);
		setViewState(ViewState.SHOW);
		if (listener != null)
			listener.done();
		if (getViewController().isContainView(StringSystem.VIEW_ACTION_BAR)) {
			if (getName() != StringSystem.VIEW_MAIN_MENU)
				((TopBarView) getViewController().getView(
						StringSystem.VIEW_ACTION_BAR)).setTopName(getLabel());
		}
	}

	@Override
	public void hide(OnCompleteListener listener) {
		setViewState(ViewState.HIDE);
		setTouchable(Touchable.disabled);
		TraceView.instance.removeView(this.getName());
		try{
		((TopBarView) getViewController().getView(StringSystem.VIEW_ACTION_BAR))
				.setTopName(getViewController().getView(
						TraceView.instance.getLastView()).getLabel());
		}catch(Exception e){
			e.printStackTrace();
		};
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void setViewState(ViewState state) {
		this.state = state;
	}

	@Override
	public ViewState getViewState() {
		return state;
	}

	@Override
	public Rectangle getBound() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void setName(String name) {
		super.setName(name);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void destroyComponent() {
		// =============== destroy all of your object here ==========
		this.clear();
		_stage.getActors().removeValue(this, false);
	}

	@Override
	public IViewController getViewController() {
		return _viewController;
	}

	@Override
	public boolean onLeftSide() {
		return false;
	}

	@Override
	public boolean onRightSide() {
		return false;
	}

	@Override
	public void back() {
		// TraceSystem.backView();
		hide(null);
	}
}
