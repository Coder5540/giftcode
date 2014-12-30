package com.aia.appsreport.component.list;

import java.util.ArrayList;

import utils.factory.FontFactory.FontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.coder5560.game.assets.Assets;

public class ListDetail extends ScrollPane {

	public Table table;
	ArrayList<ItemList> listItem = new ArrayList<ItemList>();
	LabelStyle lbStyle;

	public ListDetail(final Table table, Rectangle bound) {
		super(table);
		this.table = table;
		setBounds(bound.x, bound.y, bound.width, bound.height);
		table.top();
		lbStyle = new LabelStyle(Assets.instance.fontFactory.getFont(20,
				FontType.Bold), Color.BLACK);
	}

	void addLine(Table table, float height) {
		Image line = new Image(Assets.instance.ui.reg_ninepatch);
		line.setColor(new Color(0, 220 / 255f, 0, 1f));
		line.setHeight(height);
		line.setWidth(table.getWidth());
		table.add(line).expandX().fillX().height(height).padTop(10);
		table.row();
	}

	void setHeightForCell(Actor a, float height) {
		table.getCell(a).height(height);
		table.invalidate();
		layout();
	}

	public void addItemMenu(ItemList item) {
		table.add(item).row();
	}
}
