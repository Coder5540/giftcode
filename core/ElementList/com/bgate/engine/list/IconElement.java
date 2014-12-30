package com.bgate.engine.list;

import utils.elements.Img;
import utils.factory.FontFactory.FontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.coder5560.game.assets.Assets;

public class IconElement extends Group {
	public Img		icon;
	public Label	text;
	String			_text;

	public IconElement(float width, float height, TextureRegion regIcon, String _text,
			Color textColor) {
		super();
		setSize(width, height);
		icon = new Img(regIcon);
		this._text = _text;
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Assets.instance.fontFactory.getFont(20,
				FontType.Light);
		labelStyle.fontColor = new Color(0, 191 / 255f, 1, 1);
		text = new Label(_text, labelStyle);
		text.setWrap(true);
		addActor(icon);
		addActor(text);
		validElements();
	}

	public IconElement(float width, float height, TextureRegion regIcon, String _text) {
		super();
		setSize(width, height);
		icon = new Img(regIcon);
		this._text = _text;
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Assets.instance.fontFactory.getFont(20,
				FontType.Light);
		labelStyle.fontColor = new Color(0, 191 / 255f, 1, 1);
		text = new Label(_text, labelStyle);
		text.setWrap(true);
		addActor(icon);
		addActor(text);
		validElements();
	}

	public void validElements() {
		icon.setSize(getHeight() - 10, getHeight() - 10);
		icon.setPosition(20, 5);
		text.setWidth(getWidth() - icon.getX() + icon.getWidth() - 10);
		text.setPosition(icon.getX() + icon.getWidth() + 10,
				icon.getY(Align.center) - text.getHeight() / 2);

	}

}