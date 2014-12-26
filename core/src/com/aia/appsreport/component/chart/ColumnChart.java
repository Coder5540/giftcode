package com.aia.appsreport.component.chart;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class ColumnChart extends Group {
	private boolean isValidate;
	public float offsetX = 20;
	private float padBottom = 0;
	private float padLeft = 0;
	private float disperValueHeight;
	private float widthColumn;
	private float maxValue;

	public ArrayList<ColumnChartGroup> column;

	public ColumnChart() {
		column = new ArrayList<ColumnChartGroup>();
	}

	public void resetData() {
		clear();
		maxValue = 0;
		widthColumn = 0;
		disperValueHeight = 0;
	}

	public void addColumnChartComponent(ColumnChartGroup column) {
		this.column.add(column);
		this.column.get(this.column.size() - 1).index = this.column.size() - 1;
		addActor(column);
		if (maxValue <= column.maxValue) {
			maxValue = column.maxValue;
		}
		isValidate = false;
	}

	public void removeColumnChartComponent(int index) {
		if (index >= column.size()) {
			return;
		}
		removeActor(column.get(index));
		column.remove(index);
		isValidate = false;
	}

	public void removeColumnChartComponent(Actor column) {
		if (((ColumnChartComponent) column).index >= this.column.size()) {
			return;
		}
		removeActor(column);
		this.column.remove(((ColumnChartComponent) column).index);
		isValidate = false;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setWidthColumn(float widthColumn) {
		this.widthColumn = widthColumn;
	}

	public void setDisPerValueHeight(float disperValueHeight) {
		this.disperValueHeight = disperValueHeight;
	}

	@Override
	public void act(float delta) {
		// TODO Auto-generated method stub
		if (!isValidate) {
			validateComponent();
			isValidate = true;
		}
		super.act(delta);
	}

	public void validateComponent() {
		if (column.size() == 0) {
			return;
		}
		float x = padLeft + offsetX / 2;
		float y = padBottom;
		for (int i = 0; i < column.size(); i++) {
			column.get(i).setSize(widthColumn - offsetX,
					this.disperValueHeight * column.get(i).maxValue);
			column.get(i).disperValueHeight = disperValueHeight;
			column.get(i).setPosition(x, y);
			x += column.get(i).getWidth() + offsetX;
			column.get(i).validateComponent();
		}
	}

}
