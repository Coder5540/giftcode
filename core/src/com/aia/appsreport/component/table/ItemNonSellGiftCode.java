package com.aia.appsreport.component.table;

import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coder5560.game.assets.Assets;

public class ItemNonSellGiftCode extends ItemTable {

	public TextButton btReturn, btCopy;

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
			lb[i].addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					click();
				}
			});
		}

		Table bt = new Table();
		bt.setHeight(40);
		btReturn = new TextButton("Trả lại", Style.ins.textButtonStyle);
		btReturn.setSize(100, 40);
		btCopy = new TextButton("Copy-Bán", Style.ins.textButtonStyle);
		btCopy.setSize(120, 40);
		bt.add(btReturn).width(btReturn.getWidth())
				.height(btReturn.getHeight());
		bt.add(btCopy).width(btCopy.getWidth()).height(btCopy.getHeight())
				.padLeft(10);

		Actor[] actor = new Actor[lb.length + 1];
		for (int i = 0; i < lb.length; i++) {
			actor[i] = lb[i];
		}
		actor[actor.length - 1] = bt;
		setComponentItem(actor);
		init();
	}

	public void click() {

	}

}
