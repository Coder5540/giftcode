package com.bgate.engine.list;

import imp.view.ViewUserManager.ItemUserState;
import utils.elements.Img;
import utils.factory.FontFactory.FontType;
import utils.factory.Log;
import utils.factory.Style;
import utils.listener.OnClickListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;

public class Item extends Group {
	private Table			data;
	private int				id;
	private Img				icon, bg;
	private Label			lbText, lbTextNumber;
	private OnClickListener	onClickListener;
	private ItemUserState	itemUserState	= ItemUserState.COLLAPSE;
	private Array<SubItem>	listSubItem;
	private ListEvent		event;

	public Item(ListEvent event, int id, float width, float height,
			TextureRegion reg_icon, String text, String textnumber,
			final OnClickListener onClickListener, Table data) {
		super();
		{
			this.event = event;
			this.onClickListener = onClickListener;
			this.setSize(width, height);
			this.setOrigin(Align.center);
			this.id = id;
		}
		this.icon = new Img(reg_icon);
		{
			this.bg = new Img(Style.ins.np1);
			bg.setColor(Color.BLACK);
		}
		{
			this.lbText = new Label(text, Style.ins.getLabelStyle(22,
					FontType.Bold, Color.WHITE));
			this.lbTextNumber = new Label(textnumber, Style.ins.getLabelStyle(
					20, FontType.Regular, Color.WHITE));
		}
		{
			NinePatch np = Style.ins.np4;
			np.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
			this.data = data;
			data.setTouchable(Touchable.childrenOnly);
			this.data.setBackground(new NinePatchDrawable(np));
			this.data.right().top();
		}
		{
			addActor(bg);
			addActor(icon);
			addActor(lbText);
			addActor(lbTextNumber);
		}
		setUp();
		data.setVisible(false);
	}

	public int getId() {
		return id;
	}

	public Table getData() {
		return data;
	}

	public void addSubItem(SubItem subItemUser) {
		subItemUser.toFront();
		data.add(subItemUser).width(subItemUser.getWidth())
				.height(subItemUser.getHeight()).padTop(5).row();
		if (listSubItem == null)
			listSubItem = new Array<SubItem>();
		listSubItem.add(subItemUser);

		itemUserState = ItemUserState.COLLAPSE;
		data.setVisible(false);
		event.notifyEvent(1);
		// root.getCell(data).height(0);
		// root.invalidateHierarchy();
		// scroll.invalidate();
		// ViewUserManager.this.layout();

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
		bg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clearActions();
				addAction(Actions.sequence(Actions.scaleTo(1.1f, 1.1f, .05f),
						Actions.scaleTo(1f, 1f, .1f, Interpolation.swingOut),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								Log.d("Click to : " + id);
								if (onClickListener != null) {
									onClickListener.onClick(id);
								}
								if (itemUserState == ItemUserState.EXPAND) {
									itemUserState = ItemUserState.COLLAPSE;
									data.setVisible(false);
									// root.getCell(data).height(0);
									// root.invalidateHierarchy();
									// scroll.invalidate();
									// ViewUserManager.this.layout();
								} else {
									itemUserState = ItemUserState.EXPAND;
									if (listSubItem != null) {
										data.setVisible(true);
										data.setHeight(70 * (listSubItem.size + 1) - 5);
										Item.this.event.notifyEvent(1);
										// root.getCell(data)
										// .height(70 * (listSubItem.size + 1) -
										// 5);
										// root.invalidateHierarchy();
										// scroll.invalidate();
										// ViewUserManager.this.layout();
									}
								}
							}
						})));
			}
		});
	}
}
