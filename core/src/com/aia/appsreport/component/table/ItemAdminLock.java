package com.aia.appsreport.component.table;

import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;

public class ItemAdminLock extends ItemTable {

	public TextButton btLock;

	public ItemAdminLock(AbstractTable table, String[] strings) {
		super(table);
		LabelStyle lbStyle = new LabelStyle(
				Assets.instance.fontFactory.getFont(15), Color.GRAY);

		Label[] lb = new Label[strings.length];
		for (int i = 0; i < lb.length; i++) {
			lb[i] = new Label("", lbStyle);
			lb[i].setWrap(true);
			lb[i].setAlignment(Align.center);
			lb[i].setWidth(table.widthCol[i]);
		}
		for (int i = 0; i < lb.length - 1; i++) {
			lb[i].setText(strings[i]);
		}
		lb[lb.length - 1].setText(Constants.stateAccount[Integer
				.parseInt(strings[lb.length - 1])]);
		for (int i = 0; i < lb.length; i++) {
			lb[i].setHeight(lb[i].getTextBounds().height);
		}
		Table action = new Table();
		action.setSize(200, 40);
		btLock = new TextButton("Mở khóa", Style.ins.textButtonStyle);
		action.add(btLock).width(80).height(35).padLeft(5);
		Actor[] componentItem = new Actor[lb.length + 1];
		for (int i = 0; i < lb.length; i++) {
			componentItem[i] = lb[i];
		}
		componentItem[lb.length] = action;
		setComponentItem(componentItem);
		init();
	}

}
