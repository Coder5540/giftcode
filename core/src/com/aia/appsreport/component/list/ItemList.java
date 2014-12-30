package com.aia.appsreport.component.list;

import java.util.ArrayList;

import utils.factory.FontFactory.FontType;
import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.ui.ItemListener;
import com.coder5560.game.ui.LabelButton;

public class ItemList extends Group {
	public Group			item;
	Image					bgItem;
	Image					bg;
	public Image			btnExpand;
	ArrayList<Actor>		subItem;
	ArrayList<LabelButton>	button;
	boolean					isValidate;
	boolean					isExpand;
	float					maxheight;
	float					minheight;
	float					currentheight;
	Vector2					touchPoint;

	private boolean			isHasChild	= true;
	private ItemListener	listener;

	ListDetail				parent;

	float					padLeft		= 40;

	// float padTop = 15;

	public ItemList(final ListDetail parent, float width, float height) {
		this.parent = parent;
		touchPoint = new Vector2();
		subItem = new ArrayList<Actor>();
		button = new ArrayList<LabelButton>();
		setSize(width, height);
		item = new Group();
		item.setSize(width, height);
		bgItem = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch5, 4,
				4, 4, 4));
		bgItem.setColor(Constants.COLOR_ACTIONBAR);
		bgItem.setSize(item.getWidth(), item.getHeight());

		bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch2, 4, 4,
				4, 4));
		bg.setColor(new Color(255 / 255f, 255 / 255f, 255 / 255f, 0));
		bg.setSize(getWidth(), getHeight());

		btnExpand = new Image(Assets.instance.getRegion("down"));
		btnExpand.setSize(25, 20);
		btnExpand.setColor(Color.WHITE);
		btnExpand
				.setOrigin(btnExpand.getWidth() / 2, btnExpand.getHeight() / 2);

		btnExpand.setPosition(
				item.getX() + item.getWidth() - btnExpand.getWidth() - 10,
				bgItem.getY() + bgItem.getHeight() / 2 - btnExpand.getHeight()
						/ 2);
		item.addActor(bgItem);
		item.addActor(btnExpand);

		maxheight = height;
		minheight = height;
		currentheight = height;

		item.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				touchPoint.set(x, y);
				bgItem.getColor().a = 0.5f;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				bgItem.setColor(Constants.COLOR_ACTIONBAR);
				if (isHasChild) {
					if (touchPoint.epsilonEquals(x, y, 20)) {
						if (!isExpand) {
							setHeight(maxheight);
							showSubButton();
							parent.setHeightForCell(ItemList.this, maxheight);
							bg.setHeight(maxheight);
							btnExpand.addAction(Actions.rotateTo(180, 0.2f,
									Interpolation.exp10));
							isExpand = true;
						} else {
							hideSubButton();
							isExpand = false;
						}
					}
				} else {
					if (listener != null) {
						listener.onItemClick();
					}
				}
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				if (!touchPoint.epsilonEquals(x, y, 40)) {
					touchPoint.set(0, 0);
				}
				super.touchDragged(event, x, y, pointer);
			}
		});
	}

	public void addComponent(Actor a, float x, float y) {
		a.setPosition(x, y);
		item.addActor(a);
	}

	public int getChirldenSize() {
		return subItem.size();
	}

	public void setListener(ItemListener listener) {
		this.listener = listener;
	}

	public void setNoChild() {
		this.isHasChild = false;
		this.btnExpand.setVisible(false);
	}

	public void addSubItem(Actor subItemList) {
		subItemList.setVisible(false);
		subItemList.setPosition(item.getX() + 10, item.getY());
		this.subItem.add(subItemList);
		isValidate = false;
		this.subItem.get(this.subItem.size() - 1).setTouchable(
				Touchable.disabled);
		maxheight += (subItemList.getHeight());
	}

	public void addSubItem(Actor subItemList, float width, float height) {
		subItemList.setSize(width, height);
		subItemList.setVisible(false);
		subItemList.setPosition(item.getX() + 10, item.getY());
		this.subItem.add(subItemList);
		isValidate = false;
		this.subItem.get(this.subItem.size() - 1).setTouchable(
				Touchable.disabled);
		maxheight += (subItemList.getHeight());
	}

	public void addLableButton(String... title) {
		int size = title.length;
		float width = 130;
		float height = 45;
		float offset = getWidth() - 130 / size;

		for (int i = 0; i < title.length; i++) {
			LabelButton btn = new LabelButton(title[i],
					Style.ins.getLabelStyle(17, FontType.Regular, Color.BLACK),
					width, height, LabelButton.CENTER);
			btn.bg.setColor(Constants.COLOR_ACTIONBAR);
			btn.setPosition(offset / 2 + (width + offset / 2) * i,
					item.getY() + 5);
			this.button.add(btn);
			btn.setVisible(false);
		}
		maxheight += (height);
		isValidate = false;
	}

	public void validate() {
		item.setPosition(getX(), getY());
		bg.setSize(item.getWidth(), item.getHeight());
		// addActor(bg);
		for (int i = button.size() - 1; i >= 0; i--) {
			addActor(button.get(i));
		}
		for (int i = subItem.size() - 1; i >= 0; i--) {
			addActor(subItem.get(i));
			subItem.get(i).setPosition(getX() + padLeft, getY());
		}
		addActor(item);
	}

	@Override
	public void act(float delta) {
		if (!isValidate) {
			clear();
			validate();
			isValidate = true;
		}
		super.act(delta);
	}

	public void showSubButton() {
		item.setY(getHeight() - item.getHeight());
		float x = item.getX() + padLeft;
		float y = item.getY();
		item.clearActions();
		for (int i = 0; i < subItem.size(); i++) {
			subItem.get(i).clearActions();
			subItem.get(i).setVisible(true);
			subItem.get(i).setPosition(x, y);
			y -= (subItem.get(i).getHeight());
			subItem.get(i).addAction(
					Actions.moveTo(x, y, 0.3f, Interpolation.exp5Out));
			subItem.get(i).setTouchable(Touchable.enabled);
		}
		y -= 10;
		for (int i = 0; i < button.size(); i++) {
			float offset = (getWidth() - button.get(i).getWidth()
					* button.size());
			button.get(i).clearActions();
			button.get(i).setVisible(true);
			button.get(i).setPosition(
					offset / 2 + (button.get(i).getWidth() + offset) * i,
					item.getY() + 5);
			button.get(i).addAction(
					Actions.moveTo(button.get(i).getX(), y
							- button.get(i).getHeight(), 0.3f,
							Interpolation.exp5Out));
			button.get(i).setTouchable(Touchable.enabled);
		}
	}

	public void hideSubButton() {
		for (int i = 0; i < subItem.size(); i++) {
			subItem.get(i).addAction(
					Actions.sequence(Actions.moveTo(item.getX() + padLeft,
							item.getY(), 0.25f, Interpolation.exp10In), Actions
							.visible(false)));
			subItem.get(i).setTouchable(Touchable.disabled);
		}

		for (int i = 0; i < button.size(); i++) {
			button.get(i).clearActions();
			button.get(i).setVisible(true);
			button.get(i).addAction(
					Actions.sequence(Actions.moveTo(button.get(i).getX(),
							item.getY(), 0.25f, Interpolation.exp10In), Actions
							.visible(false)));
			button.get(i).setTouchable(Touchable.disabled);
		}

		item.addAction(Actions.sequence(Actions.delay(0.25f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						setHeight(minheight);
						item.setY(getHeight() - item.getHeight());
						parent.setHeightForCell(ItemList.this, minheight);
						bg.setHeight(minheight);
						bg.setPosition(0, 0);
						for (int i = 0; i < subItem.size(); i++) {
							subItem.get(i).setPosition(item.getX() + padLeft,
									item.getY());
						}
						for (int i = 0; i < button.size(); i++) {
							button.get(i).setY(item.getY());
						}
						btnExpand.addAction(Actions.rotateTo(0, 0.2f,
								Interpolation.exp10));
					}
				})));
	}
}