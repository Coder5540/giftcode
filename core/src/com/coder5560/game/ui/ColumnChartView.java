package com.coder5560.game.ui;

import java.util.ArrayList;

import utils.factory.FontFactory.FontType;

import com.aia.appsreport.component.chart.ChartBack;
import com.aia.appsreport.component.chart.ColumnChart;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;

public class ColumnChartView extends Group {
	public ChartBack	chartback;
	public ColumnChart	columnChart;
	public int			numbertype	= 0;

	public ColumnChartView(float height) {
		columnChart = new ColumnChart();
		chartback = new ChartBack();
		chartback.setStyleCol(new LabelStyle(Assets.instance.fontFactory
				.getFont(13, FontType.Light), Color.BLACK));
		chartback.setStyleRow(new LabelStyle(Assets.instance.fontFactory
				.getFont(15, FontType.Regular), Color.GRAY));
		addActor(chartback);
		addActor(columnChart);
		setHeight(height);
	}

	public void validateComponent(float colWidth, ArrayList<String> titledown,
			ArrayList<String> dir, ArrayList<Color> color) {
		int size = columnChart.column.size();
		float widthChartBack = colWidth * size + 100;
		if (widthChartBack < Constants.WIDTH_SCREEN) {
			widthChartBack = Constants.WIDTH_SCREEN;
		}
		chartback.setdata((long) columnChart.getMaxValue(),
				(long) columnChart.getMaxValue() / 5, size, numbertype,
				widthChartBack, getHeight(), 80);
		chartback.colWidth = colWidth;
		chartback.refresh();
		columnChart.setDisPerValueHeight(chartback.disPerValue);
		columnChart.setWidthColumn(colWidth);
		columnChart.setPosition(ChartBack.START_CHART_X,
				ChartBack.START_CHART_Y);
		columnChart.setSize(chartback.getWidth(), chartback.getHeight()
				- ChartBack.START_CHART_Y);
		chartback.addLabel(titledown, chartback.colWidth - 10);
		chartback.addDirection(dir, color, 20, numbertype);
		setWidth(chartback.getWidth());
	}
}
