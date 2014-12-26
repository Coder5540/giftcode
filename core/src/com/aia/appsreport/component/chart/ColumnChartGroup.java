package com.aia.appsreport.component.chart;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Group;

public class ColumnChartGroup extends Group {
	public ArrayList<ColumnChartComponent> subcolumn;
	public float padLeft;
	public float offset;
	public int index = -1;
	public float maxValue;
	public float disperValueHeight;

	public ColumnChartGroup() {
		subcolumn = new ArrayList<ColumnChartComponent>();
	}

	public void addComponent(ColumnChartComponent column) {
		subcolumn.add(column);
		addActor(column);
		if (column.maxValue >= maxValue) {
			maxValue = column.maxValue;
		}
	}

	public void validateComponent() {
		for (int i = 0; i < subcolumn.size(); i++) {
			subcolumn.get(i).setSize(
					(getWidth() - i * subcolumn.size() - 2 * padLeft)
							/ subcolumn.size(),
					getHeight() * subcolumn.get(i).maxValue / maxValue);
			subcolumn.get(i).setPosition(
					padLeft + (subcolumn.get(i).getWidth() + offset) * i, 0);
			subcolumn.get(i).validateComponent();
		}
	}
}
