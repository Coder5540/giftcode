package com.aia.appsreport.component.table;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.coder5560.game.assets.Assets;

public class ItemTable extends Group {

	public AbstractTable table;
	private Actor[] componentItem;

	public ItemTable(AbstractTable table) {
		this.table = table;
	}

	public void setComponentItem(Actor[] componentItem) {
		this.componentItem = componentItem;
	}

	public void init() {
		// ////////////Calculate heightItem//////////////
		float maxHeight = 0;
		for (int i = 0; i < componentItem.length; i++) {
			this.addActor(componentItem[i]);
			if (maxHeight < componentItem[i].getHeight()) {
				maxHeight = componentItem[i].getHeight();
			}
		}
		maxHeight += 20;
		for (int i = 0; i < componentItem.length; i++) {
			if (componentItem[i] instanceof Label) {
				componentItem[i].setBounds(table.positionXCol[i], 0,
						table.widthCol[i], maxHeight);
			} else {
				componentItem[i].setPosition(table.positionXCol[i]
						+ table.widthCol[i] / 2 - componentItem[i].getWidth()
						/ 2, maxHeight / 2 - componentItem[i].getHeight() / 2);
			}
		}

		// //////////////////////////////////////////////
		this.setSize(table.getWidthTable(), maxHeight);

		Image borderBottom = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch, table.color));
		borderBottom.setSize(this.getWidth(), 2f);
		this.addActor(borderBottom);

		Image[] border = new Image[2];
		for (int i = 0; i < border.length; i++) {
			border[i] = new Image(new NinePatch(
					Assets.instance.ui.reg_ninepatch, table.color));
			border[i].setSize(2f, this.getHeight());
			this.addActor(border[i]);
		}
		border[1].setX(table.getWidthTable());

		// Image[] border = new Image[table.numberCol + 1];
		// for (int i = 0; i < border.length; i++) {
		// border[i] = new Image(new NinePatch(
		// Assets.instance.ui.reg_ninepatch, table.color));
		// border[i].setSize(2f, this.getHeight());
		// this.addActor(border[i]);
		// }
		// for (int i = 1; i <= table.numberCol; i++) {
		// border[i].setPosition(border[i - 1].getX() + table.widthCol[i - 1],
		// 0);
		// }
	}

}
