package com.aia.appsreport.component.chart;

import java.util.ArrayList;

import utils.factory.Factory;
import utils.factory.FontFactory.FontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.coder5560.game.assets.Assets;

public class ChartBack extends Table {
	public static int NUM_ROW = 8, START_CHART_Y = 50, START_CHART_X = 50;
	long max, jump;
	protected int numCol;
	int numRow;
	int numType;
	public float disPerValue;// So pixel tren moi don vi do truyen vao
	Table direction;
	public ArrayList<Direction> listDirection = new ArrayList<Direction>();
	public float colWidth;

	LabelStyle styleCol, styleRow;
	float maxHeightLabel;

	public Array<Color> colors = new Array<Color>();

	public ChartBack(long max, long jump, int numCol, int numLine, float width,
			float height, int startChartY) {
		this.max = max;
		this.jump = jump;
		this.numCol = numCol;
		this.numType = numLine;
		caculJump(max);
		START_CHART_Y = startChartY;
		styleCol = style;
		styleRow = style;
		setSize(width, height);
		init();
	}

	private void caculJump(long max) {
		jump = 1;
		for (int i = 1; i < 15; i++) {
			if (max > Math.pow(10, i)) {
				jump = (long) Math.pow(10, i);
				if ((int) (max / jump) < 2)
					jump = jump / 5;
				if ((max / jump) >= 2 && (max / jump) <= 5)
					jump = jump / 2;
			} else {
				break;
			}
		}
		jump = jump * 2;
		if (max / jump < 4)
			jump = jump / 2;
	}

	public ChartBack() {
		styleCol = style;
		styleRow = style;
	}

	public void refresh() {
		clear();
		float dis = (getHeight() - START_CHART_Y) / numRow;
		float temp = 0;
		int index = 0;
		while (temp < max + jump) {
			// String tempS = Factory.getDotMoney((long) temp);
			// Label value = new Label(tempS, styleRow);
			// value.setY(START_CHART_Y + dis * index - value.getHeight() / 2);
			// value.setX(5);
			Image line = new Image(new NinePatch(
					Assets.instance.ui.reg_ninepatch, new Color(Color.GRAY)));
			line.setWidth(getWidth() - styleCol.font.getBounds("100000").width);
			line.setHeight(1);
			line.setPosition(START_CHART_X, START_CHART_Y + dis * index);
			addActor(line);
			// addActor(value);
			temp += jump;
			index++;
		}
		for (int i = 0; i < numCol + 1; i++) {
			Image line = new Image(new NinePatch(
					Assets.instance.ui.reg_ninepatch, new Color(Color.GRAY)));
			line.setSize(1, 5);
			line.setPosition(START_CHART_X + i * colWidth,
					START_CHART_Y - line.getHeight());
			addActor(line);
		}
	}

	public void setdata(long max, long jump, int numCol, int numType,
			float width, float height, int startChartY) {
		this.max = max;
		this.jump = jump;
		this.numCol = numCol;
		this.numType = numType;
		START_CHART_Y = startChartY;
		caculJump(max);
		clear();
		setSize(width, height);
		init();
	}

	public void resetData() {
		max = 0;
		jump = 0;
		numCol = 0;
		numType = 0;
		START_CHART_Y = 50;
		START_CHART_X = 50;
		clear();
	}

	public void setStyleCol(LabelStyle styleCol) {
		this.styleCol = styleCol;
	}

	public void setStyleRow(LabelStyle styleRow) {
		this.styleRow = styleRow;
	}

	protected LabelStyle style = new LabelStyle(
			Assets.instance.fontFactory.getFont(15, FontType.Regular),
			Color.BLACK);

	public void init() {
		numRow = (int) ((max) / jump) + 1;
		float dis = (getHeight() - START_CHART_Y) / numRow;
		disPerValue = dis / jump;
		float temp = 0;
		int index = 0;
		while (temp < max + jump) {
			String tempS = Factory.getDotMoney((long) temp);
			Label value = new Label(tempS, styleRow);
			value.setY(START_CHART_Y + dis * index - value.getHeight() / 2);
			value.setX(5);

			Image line = new Image(new NinePatch(
					Assets.instance.ui.reg_ninepatch, new Color(Color.GRAY)));
			line.setWidth(getWidth() - styleCol.font.getBounds("100000").width);
			line.setHeight(1);
			line.setPosition(START_CHART_X, START_CHART_Y + dis * index);
			addActor(line);

			addActor(value);
			temp += jump;
			index++;
		}
		dis = (getWidth() - START_CHART_X) / numCol;
		colWidth = dis;
		for (int i = 0; i < numCol + 1; i++) {
			Image line = new Image(new NinePatch(
					Assets.instance.ui.reg_ninepatch, new Color(Color.GRAY)));
			line.setSize(1, 5);
			line.setPosition(START_CHART_X + i * dis,
					START_CHART_Y - line.getHeight());
			addActor(line);
		}
	}

	Label[] listDirText;
	Image[] listDirImg;
	boolean[] isShowDir;

	public void addDirection(ArrayList<String> type, ArrayList<Color> color,
			float height, int numCol) {
		listDirImg = new Image[type.size()];
		listDirText = new Label[type.size()];
		isShowDir = new boolean[type.size()];

		direction = new Table();
		direction.setSize(getWidth(), START_CHART_Y - maxHeightLabel);
		for (int i = 0; i < numType; i++) {
			Label value = new Label(type.get(i), style) {

			};
			Image line = new Image(new NinePatch(
					Assets.instance.ui.reg_ninepatch, color.get(i))) {
				@Override
				public Actor hit(float x, float y, boolean touchable) {
					if (touchable && this.getTouchable() != Touchable.enabled)
						return null;
					return x >= 0 && x < this.getWidth() * 2
							&& y >= -this.getHeight()
							&& y < this.getHeight() * 2 ? this : null;
				}
			};
			colors.add(color.get(i));
			line.setHeight(height);

			direction.add(line).width(50).height(height).pad(10);
			direction.add(value);
			listDirImg[i] = line;
			listDirText[i] = value;
			isShowDir[i] = true;
			if (i % numCol == numCol - 1)
				direction.row();
		}
		addActor(direction);
	}

	public boolean isShowDirection(int i) {
		return isShowDir[i];
	}

	public void setShowHideDirection(boolean isShow, int index) {
		isShowDir[index] = isShow;
		if (isShow) {
			listDirImg[index].setColor(colors.get(index));
			listDirImg[index].getColor().a = 1f;
			listDirText[index].getColor().a = 1f;
		} else {
			listDirImg[index].setColor(Color.GRAY);
			listDirImg[index].getColor().a = 0.6f;
			listDirText[index].getColor().a = 0.6f;

		}
	}

	public void ShowHideDirection(int index) {
		if (!isShowDir[index]) {
			listDirImg[index].setColor(colors.get(index));
			listDirImg[index].getColor().a = 1f;
			listDirText[index].getColor().a = 1f;
		} else {
			listDirImg[index].setColor(Color.GRAY);
			listDirImg[index].getColor().a = 0.6f;
			listDirText[index].getColor().a = 0.6f;

		}
		isShowDir[index] = !isShowDir[index];
	}

	public void addListenerDir(ClickListener listener, int index) {
		clearListenerDir(index);
		listDirImg[index].addListener(listener);
		listDirText[index].addListener(listener);
	}

	public void clearListenerDir(int index) {
		listDirImg[index].clearListeners();
		listDirText[index].clearListeners();
	}

	public void addLabel(String[] listLabel) {
		for (int i = 0; i < numCol; i++) {
			Label text = new Label(listLabel[i], styleCol);
			text.setBounds(
					START_CHART_X + colWidth * i + colWidth / 2
							- text.getTextBounds().width / 2, START_CHART_Y
							- text.getTextBounds().height - 3,
					text.getTextBounds().width, text.getTextBounds().height);
			if (maxHeightLabel <= text.getTextBounds().height) {
				maxHeightLabel = text.getTextBounds().height;
			}
			addActor(text);
		}
	}

	public void addLabel(ArrayList<String> listLabel, float boundWidth) {
		for (int i = 0; i < numCol; i++) {
			Label text = new Label(listLabel.get(i), styleCol);
			text.setWrap(true);
			text.setWidth(boundWidth);
			text.setBounds(
					START_CHART_X + colWidth * i + colWidth / 2
							- text.getTextBounds().width / 2, START_CHART_Y
							- text.getTextBounds().height - 3, boundWidth,
					text.getTextBounds().height);
			if (maxHeightLabel <= text.getTextBounds().height) {
				maxHeightLabel = text.getTextBounds().height;
			}
			addActor(text);
		}
	}

	public class Direction {
		Color color;
		String type;

		public Direction(Color color, String type) {
			this.color = color;
			this.type = type;
		}
	}
}