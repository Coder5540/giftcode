package com.coder5560.game.ui;

import utils.factory.FontFactory.fontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.coder5560.game.assets.Assets;

public class TextfieldStatic extends Group {

	public Label lbContent;
	private Image bg;

	public TextfieldStatic(String content, Color color, float width) {
		setSize(width, 35);
		bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch1, 6, 6,
				6, 6));
		bg.setColor(new Color(245 / 255f, 245 / 255f, 245 / 255f, 1));

		lbContent = new Label(content, new LabelStyle(
				Assets.instance.fontFactory.getFont(20, fontType.Regular),
				color));
		lbContent.setWrap(true);
		lbContent.setWidth(width - 10);
		if (lbContent.getTextBounds().height > 35) {
			setHeight(lbContent.getTextBounds().height + 10);
		}
		bg.setSize(getWidth(), getHeight());
		lbContent.setPosition(5, getHeight() / 2 - lbContent.getHeight() / 2);
		this.addActor(bg);
		this.addActor(lbContent);
	}

	public TextfieldStatic(String content, LabelStyle style, Color color,
			float width) {
		setSize(width, 35);
		bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch1, 6, 6,
				6, 6));
		bg.setColor(new Color(245 / 255f, 245 / 255f, 245 / 255f, 1));

		lbContent = new Label(content, style);
		lbContent.setColor(color);
		lbContent.setWrap(true);
		lbContent.setWidth(width - 10);
		if (lbContent.getTextBounds().height > 35) {
			setHeight(lbContent.getTextBounds().height + 10);
		}
		bg.setSize(getWidth(), getHeight());
		lbContent.setPosition(5, getHeight() / 2 - lbContent.getHeight() / 2);
		this.addActor(bg);
		this.addActor(lbContent);
	}

	public void setContent(String content) {
		this.lbContent.setText(content);
		if (lbContent.getTextBounds().height > getHeight()) {
			setHeight(lbContent.getTextBounds().height + 10);
		}
		bg.setSize(getWidth(), getHeight());
	}

	public void setHeight(float height) {
		super.setHeight(height);
		bg.setHeight(height);
		lbContent.setY(height / 2 - lbContent.getHeight() / 2);
	}

}
