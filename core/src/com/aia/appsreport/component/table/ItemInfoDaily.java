package com.aia.appsreport.component.table;

import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.coder5560.game.assets.Assets;

public class ItemInfoDaily extends ItemTable {

	public TextButton btSee;

	public ItemInfoDaily(AbstractTable table, String[] content) {
		super(table);
		LabelStyle lbStyle = new LabelStyle(
				Assets.instance.fontFactory.getFont(15), Color.GRAY);
		Label[] lb = new Label[content.length + 1];
		lb[0] = new Label("" + (table.getSize() + 1), lbStyle);
		lb[0].setWrap(true);
		lb[0].setAlignment(Align.center);
		lb[0].setWidth(table.widthCol[0]);
		lb[0].setHeight(lb[0].getTextBounds().height);

		for (int i = 1; i < lb.length; i++) {
			lb[i] = new Label(content[i - 1], lbStyle);
			lb[i].setWrap(true);
			lb[i].setAlignment(Align.center);
			lb[i].setWidth(table.widthCol[i]);
			lb[i].setHeight(lb[i].getTextBounds().height);
		}

		btSee = new TextButton("Xem", Style.ins.textButtonStyle);
		btSee.setSize(80, 40);
		Actor[] actor = new Actor[lb.length + 1];
		for (int i = 0; i < lb.length; i++) {
			actor[i] = lb[i];
		}
		actor[actor.length - 1] = btSee;
		setComponentItem(actor);
		init();
	}
}
