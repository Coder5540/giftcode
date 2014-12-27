package com.aia.appsreport.component.table;

import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.coder5560.game.assets.Assets;

public class ItemGiftCodeNormal extends ItemTable {

	private Table tbButton;
	public TextButton btReturn;
	public TextButton btCopy;
	public TextButton btSell;

	private int isSold;
	private Label lbStateSold;

	public ItemGiftCodeNormal(AbstractTable table, int isSold, String[] content) {
		super(table);
		this.isSold = isSold;
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

		lbStateSold = new Label("", lbStyle);
		lbStateSold.setWrap(true);
		lbStateSold.setAlignment(Align.center);
		lbStateSold.setWidth(table.widthCol[content.length]);

		tbButton = new Table();
		tbButton.setSize(250, 40);
		btReturn = new TextButton("Trả lại", Style.ins.textButtonStyle);
		btCopy = new TextButton("Copy", Style.ins.textButtonStyle);
		tbButton.add(btCopy).width(80).height(40);
		if (isSold == 0) {
			btSell = new TextButton("Bán", Style.ins.textButtonStyle);
			tbButton.add(btSell).padLeft(5).width(80).height(40);
			lbStateSold.setText("Chưa bán");
		} else {
			lbStateSold.setText("Đã bán");
		}
		lbStateSold.setHeight(lbStateSold.getTextBounds().height);

		tbButton.add(btReturn).padLeft(5).width(80).height(40);
		Actor[] actor = new Actor[lb.length + 2];
		for (int i = 0; i < lb.length; i++) {
			actor[i] = lb[i];
		}
		actor[actor.length - 2] = lbStateSold;
		actor[actor.length - 1] = tbButton;
		setComponentItem(actor);
		init();
	}

	public int getStateSold() {
		return isSold;
	}

	public void changeStateSold() {
		if (isSold == 0) {
			isSold = 1;
			lbStateSold.setText("Đã bán");
			tbButton.clear();
			tbButton.add(btCopy).width(80).height(40);
			tbButton.add(btReturn).padLeft(5).width(80).height(40);
		}
	}

}
