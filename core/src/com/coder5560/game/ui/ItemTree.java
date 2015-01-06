package com.coder5560.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coder5560.game.assets.Assets;

public class ItemTree extends Group {
	Group item;
	Image bg;
	Image bgFocus;
	Image btnExpand;
	public Actor content, header;
	boolean isValidate;
	boolean isExpand;
	float maxheight;
	float minheight;
	float currentheight;
	Vector2 touchPoint;

	public int getChirldenSize() {
		return 100;
	}

	public ItemTree(Actor header, Actor content) {
		this.header = header;
		this.content = content;
		addActor(header);
		addActor(content);
		header.setY(content.getHeight());
		touchPoint = new Vector2();
		item = new Group();
		bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch, 1, 1, 1,
				1));
		bg.setColor(new Color(250 / 255f, 250 / 255f, 250 / 255f, 1f));
		bg.setSize(item.getWidth(), item.getHeight());

		bgFocus = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
		bgFocus.setColor(new Color(220 / 255f, 220 / 255f, 220 / 255f, 0));
		bgFocus.setSize(item.getWidth(), item.getHeight());

		btnExpand = new Image(Assets.instance.getRegion("down"));
		btnExpand.setSize(15, 10);
		btnExpand.setColor(new Color(130 / 255f, 130 / 255f, 130 / 255f, 1));
		btnExpand
				.setOrigin(btnExpand.getWidth() / 2, btnExpand.getHeight() / 2);

		btnExpand.setPosition(
				item.getX() + item.getWidth() - btnExpand.getWidth() - 10,
				bg.getY() + bg.getHeight() / 2 - btnExpand.getHeight() / 2);
		item.addActor(bg);
		item.addActor(bgFocus);
		item.addActor(btnExpand);

		item.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				touchPoint.set(x, y);
				bgFocus.getColor().a = 1;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				bgFocus.getColor().a = 0;
				if (touchPoint.epsilonEquals(x, y, 20)) {
					if (!isExpand) {
						setHeight(maxheight);
						btnExpand.addAction(Actions.rotateTo(180, 0.2f,
								Interpolation.exp10));
						isExpand = true;
					} else {
						setHeight(minheight);
						isExpand = false;
					}
				}
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				if (!touchPoint.epsilonEquals(x, y, 40)) {
					bgFocus.getColor().a = 0f;
					touchPoint.set(0, 0);
				}
				super.touchDragged(event, x, y, pointer);
			}
		});

	}

	@Override
	public float getHeight() {
		if (isExpand)
			return content.getHeight() + header.getHeight();
		else
			return header.getHeight();
	}
}