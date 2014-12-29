package com.bgate.engine.list;

import imp.view.ViewUserManager.ItemUserState;
import utils.elements.Img;
import utils.factory.FontFactory.FontType;
import utils.factory.Style;
import utils.listener.OnClickListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class SubItem extends Table {
	private int				id;
	private Img				icon, bg;
	private Label			lbText, lbTextNumber;
	private OnClickListener	onClickListener;
	Group			parent;
	ItemUserState	itemUserState	= ItemUserState.COLLAPSE;
	Array<SubItem>	listSubItem;

	public SubItem(Group parent, int id, float width, float height,
			TextureRegion reg_icon, String text, String textnumber,
			final OnClickListener onClickListener) {
		super();
		setClip(true);
		this.onClickListener = onClickListener;
		this.setTouchable(Touchable.enabled);
		this.setTransform(true);
		this.parent = parent;
		this.setSize(width, height);
		setOrigin(Align.center);
		this.id = id;
		this.icon = new Img(reg_icon);
		this.bg = new Img(Style.ins.np1);
		bg.setColor(Color.BLACK);
		this.lbText = new Label(text, Style.ins.getLabelStyle(22,
				FontType.Bold, Color.WHITE));
		this.lbTextNumber = new Label(textnumber, Style.ins.getLabelStyle(20,
				FontType.Regular, Color.WHITE));

		addActor(bg);
		addActor(icon);
		addActor(lbText);
		addActor(lbTextNumber);
		setUp();
	}

	public void setUp() {
		bg.setSize(getWidth(), getHeight());
		bg.setPosition(0, 0);
		icon.setSize(4 * getHeight() / 5, 4 * getHeight() / 5);
		icon.setPosition(getHeight() / 10, getHeight() / 10);
		lbText.setPosition(icon.getX(Align.right) + 20, icon.getY(Align.center)
				- lbText.getHeight() / 2);
		lbTextNumber.setPosition(lbText.getX(Align.right) + 20,
				icon.getY(Align.center) - lbText.getHeight() / 2);

		icon.setTouchable(Touchable.disabled);
		lbText.setTouchable(Touchable.disabled);
		lbTextNumber.setTouchable(Touchable.disabled);
		this.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				SubItem.this.clearActions();
				SubItem.this.addAction(Actions.sequence(
						Actions.scaleTo(1.1f, 1.1f, .05f),
						Actions.scaleTo(1f, 1f, .1f, Interpolation.swingOut),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								if (onClickListener != null) {
									onClickListener.onClick(id);
								}
							}
						})));
			}
		});
	}

}