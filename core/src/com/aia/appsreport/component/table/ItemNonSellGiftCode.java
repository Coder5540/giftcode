package com.aia.appsreport.component.table;

import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.coder5560.game.assets.Assets;

public class ItemNonSellGiftCode extends ItemTable {

	public TextButton btReturn;

	public ItemNonSellGiftCode(AbstractTable table, String[] content) {
		super(table);
		LabelStyle lbStyle = new LabelStyle(
				Assets.instance.fontFactory.getFont(15), Color.GRAY);
		Label[] lb = new Label[content.length];
		for (int i = 0; i < lb.length; i++) {
			lb[i] = new Label(content[i], lbStyle);
			lb[i].setWrap(true);
			lb[i].setAlignment(Align.center);
			lb[i].setWidth(table.widthCol[i]);
			lb[i].setHeight(lb[i].getTextBounds().height);
		}

		btReturn = new TextButton("Trả lại", Style.ins.textButtonStyle);
		btReturn.setSize(100, 40);
		Actor[] actor = new Actor[lb.length + 1];
		for (int i = 0; i < lb.length; i++) {
			actor[i] = lb[i];
		}
		actor[actor.length - 1] = btReturn;
		setComponentItem(actor);
		init();
	}

}
