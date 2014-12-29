package com.aia.appsreport.component.table;

import utils.factory.FontFactory.FontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.coder5560.game.assets.Assets;

public class AbstractTable extends ScrollPane {

	public int numberCol;
	public float[] widthCol;
	public float[] positionXCol;
	public Group rowTitle;
	public float heightRowTitle;
	public Color color;
	private int size = 0;

	Image borderTop;
	Image borderBottom;
	Image bgRowTitle;
	Image[] borderTitle;

	public Table content;
	private Table tableScroll;

	public AbstractTable(Table table, float[] widthCol) {
		super(table);
		this.tableScroll = table;
		this.numberCol = widthCol.length;
		this.widthCol = widthCol;
		this.setOverscroll(false, false);
		positionXCol = new float[numberCol];
		color = new Color(211 / 255f, 211 / 255f, 211 / 255f, 1f);
		float width = 0;
		for (int i = 0; i < widthCol.length; i++) {
			width += widthCol[i];
			if (i > 0) {
				positionXCol[i] = positionXCol[i - 1] + widthCol[i - 1];
			}
		}
		this.setWidth(width + 2);
		tableScroll.top();
		rowTitle = new Group();

		borderTop = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
				color));
		borderBottom = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch, color));
		borderTitle = new Image[2];
		for (int i = 0; i < borderTitle.length; i++) {
			borderTitle[i] = new Image(new NinePatch(
					Assets.instance.ui.reg_ninepatch, color));
		}
		borderTitle[1].setX(width);
		bgRowTitle = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
				new Color(245 / 255f, 245 / 255f, 245 / 255f, 1f)));

		rowTitle.addActor(bgRowTitle);
		rowTitle.addActor(borderTitle[0]);
		rowTitle.addActor(borderTitle[1]);
		rowTitle.addActor(borderTop);
		rowTitle.addActor(borderBottom);
	}

	public void init() {
		rowTitle.setSize(getWidth() - 2, heightRowTitle);

		borderTop.setSize(getWidth(), 2);
		borderTop.setPosition(0, rowTitle.getHeight());
		borderBottom.setSize(getWidth(), 2);
		bgRowTitle.setSize(rowTitle.getWidth(), rowTitle.getHeight());
		for (int i = 0; i < borderTitle.length; i++) {
			borderTitle[i].setSize(2, rowTitle.getHeight());
		}
		// rowTitle.addActor(borderTop);
		// rowTitle.addActor(borderBottom);
		// Image[] border = new Image[numberCol + 1];
		// for (int i = 0; i < border.length; i++) {
		// border[i] = new Image(new NinePatch(
		// Assets.instance.ui.reg_ninepatch, color));
		// border[i].setSize(2f, rowTitle.getHeight());
		// rowTitle.addActor(border[i]);
		// }

		// for (int i = 1; i <= numberCol; i++) {
		// border[i].setPosition(border[i - 1].getX() + widthCol[i - 1], 0);
		// }

		content = new Table();
		tableScroll.add(rowTitle).padTop(5).row();
		tableScroll.add(content);
	}

	public void addItem(ItemTable itemTable) {
		content.add(itemTable).row();
		size++;
	}

	public void removeAll() {
		content.clear();
		size = 0;
	}

	public int getSize() {
		return size;
	}

	public void setTitle(String[] title) {
		float maxHeight = 0;
		Label[] lbTitle = new Label[title.length];
		for (int i = 0; i < title.length; i++) {
			lbTitle[i] = new Label(title[i], new LabelStyle(
					Assets.instance.fontFactory.getFont(20, FontType.Regular),
					Color.BLACK));
			lbTitle[i].setWrap(true);
			lbTitle[i].setAlignment(Align.center);
			lbTitle[i].setWidth(widthCol[i]);
			if (maxHeight < lbTitle[i].getTextBounds().height) {
				maxHeight = lbTitle[i].getTextBounds().height;
			}
		}
		for (int i = 0; i < title.length; i++) {
			heightRowTitle = maxHeight + 20;
			lbTitle[i].setBounds(positionXCol[i], 0, widthCol[i],
					heightRowTitle);
			rowTitle.addActor(lbTitle[i]);
		}
		init();
	}

	public float getWidthTable() {
		return rowTitle.getWidth();
	}

}
