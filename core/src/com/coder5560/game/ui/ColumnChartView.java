package com.coder5560.game.ui;

import utils.factory.FontFactory.fontType;

import com.aia.appsreport.component.chart.ChartBack;
import com.aia.appsreport.component.chart.ColumnChart;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;

public class ColumnChartView extends Group {
	public ChartBack chartback;
	public ColumnChart columnChart;
	public int numbertype = 0;

	public ColumnChartView(float height) {
		columnChart = new ColumnChart();
		chartback = new ChartBack();
		chartback.setStyleCol(new LabelStyle(Assets.instance.fontFactory
				.getFont(13, fontType.Light), Color.BLACK));
		chartback.setStyleRow(new LabelStyle(Assets.instance.fontFactory
				.getFont(15, fontType.Regular), Color.GRAY));
		addActor(chartback);
		addActor(columnChart);
		setHeight(height);
	}

	public void validateComponent(float colWidth, String[] titledown,
			String[] dir, Color[] color) {
		int size = columnChart.column.size();
		float widthChartBack = colWidth * size + 100;
		if (widthChartBack < Constants.WIDTH_SCREEN) {
			widthChartBack = Constants.WIDTH_SCREEN;
		}
		chartback.setdata(columnChart.getMaxValue(),
				columnChart.getMaxValue() / 5, size, 2, widthChartBack,
				getHeight(), 80);
		chartback.colWidth = 150;
		chartback.refresh();
		columnChart.setDisPerValueHeight(chartback.disPerValue);
		columnChart.setWidthColumn(150);
		columnChart.setPosition(ChartBack.START_CHART_X,
				ChartBack.START_CHART_Y);
		columnChart.setSize(chartback.getWidth(), chartback.getHeight()
				- ChartBack.START_CHART_Y);
		chartback.addLabel(titledown, chartback.colWidth - 10);
		chartback.addDirection(dir, color, 20, numbertype);
		setWidth(chartback.getWidth());
	}
}
