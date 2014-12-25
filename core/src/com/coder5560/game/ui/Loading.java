package com.coder5560.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;

public class Loading {
	public Image			loading;
	public Image			bg;
	Table					table;
	public boolean			isLoading	= false;
	public static Loading	ins			= new Loading();
	
	
	
	private Loading() {
		super();
	}

	public void build(Stage stage) {
		init();
		bg.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN);
		loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);

		loading.setPosition(
				Constants.WIDTH_SCREEN / 2 - loading.getWidth() / 2,
				Constants.HEIGHT_SCREEN / 2 - loading.getHeight() / 2);
		stage.addActor(table);
	}

	public void build(Group parent) {
		init();
		bg.setSize(parent.getWidth(), parent.getHeight());
		loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);

		loading.setPosition(parent.getWidth() / 2 - loading.getWidth() / 2,
				parent.getHeight() / 2 - loading.getHeight() / 2);
		parent.addActor(table);
	}

	public void init() {
		bg = new Image(Assets.instance.ui.reg_ninepatch);
		bg.setColor(Color.BLACK);
		bg.addAction(Actions.alpha(.7f));
		bg.setTouchable(Touchable.enabled);

		loading = new Image(Assets.instance.getRegion("loading"));
		table = new Table() {
			@Override
			public void act(float delta) {
				update(delta);
				super.act(delta);
			}
		};
		table.addActor(bg);
		table.addActor(loading);
		table.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
			}

		});
		hide();
	}

	public void update(float delta) {
		if (table.isVisible()) {
			loading.rotateBy(Interpolation.swing.apply(-Gdx.graphics
					.getDeltaTime() * 50));
		}
	}

	public void show(Group parent) {
		if (table == null)
			init();
		bg.setSize(parent.getWidth(), parent.getHeight());
		loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);

		loading.setPosition(parent.getWidth() / 2 - loading.getWidth() / 2,
				parent.getHeight() / 2 - loading.getHeight() / 2 + 15);
		parent.addActor(table);

		table.setVisible(true);
		table.toFront();
	}

	public void show(Group parent, int upHeight) {
		if (table == null)
			init();
		bg.setSize(parent.getWidth(), parent.getHeight() + upHeight);
		loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);

		loading.setPosition(parent.getWidth() / 2 - loading.getWidth() / 2,
				parent.getHeight() / 2 - loading.getHeight() / 2 + 15);
		parent.addActor(table);

		table.setVisible(true);
		table.toFront();
	}

	public void show(Group parent, Rectangle bound) {
		if (table == null)
			init();
		bg.setSize(bound.width, bound.height);
		loading.setOrigin(loading.getWidth() / 2, loading.getHeight() / 2);

		loading.setPosition(bound.x + bound.width / 2 - loading.getWidth() / 2,
				bound.y + bound.height / 2 - loading.getHeight() / 2);
		parent.addActor(table);

		table.setVisible(true);
		table.toFront();
	}

	public void hide() {
		table.toBack();
		table.setVisible(false);
	}

}
