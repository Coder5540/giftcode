package com.coder5560.game.ui;

import java.util.ArrayList;

import utils.factory.FontFactory.FontType;
import utils.networks.UserInfo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.ui.MoneyPicker.MyPartner;

public class MoneyPicker extends SelectBox<MyPartner> {

	ArrayList<MyPartner> list;

	public static int ALL = -1;
	protected LabelStyle style = new LabelStyle(
			Assets.instance.fontFactory.getFont(20, FontType.Regular),
			Color.BLACK);

	public MoneyPicker(SelectBoxStyle style) {
		super(style);
		init();
	}

	public MoneyPicker(Skin skin) {
		super(skin);
		init();
	}

	public void init() {
		list = new ArrayList<MyPartner>();
	}

	public void addPartner(MyPartner partner) {
		list.add(partner);
		setItems(getListData());
	}

	public void addPartner(int code) {
		addPartner(new MyPartner(code));
	}

	public Array<MyPartner> getListData() {
		Array<MyPartner> data = new Array<MyPartner>();
		for (MyPartner part : list) {
			data.add(part);
		}
		return data;
	}

	public int getMoney() {
		return getSelected().value;
	}

	public class MyPartner {
		public int value;

		public MyPartner(int code) {
			this.value = code;
		}

		@Override
		public String toString() {
			return value + " " + UserInfo.currency;
		}
	}

	public int getSize() {
		return list.size();
	}
}
