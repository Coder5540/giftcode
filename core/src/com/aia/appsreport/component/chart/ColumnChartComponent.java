package com.aia.appsreport.component.chart;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.coder5560.game.assets.Assets;

public class ColumnChartComponent extends Group {
	LabelStyle style;
	ArrayList<ColumnComponent> component;
	private Image bg;
	int index;
	public float maxValue;

	public ColumnChartComponent() {
		component = new ArrayList<ColumnComponent>();
		bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
				Color.GREEN));
		addActor(bg);
	}

	public void addColumnComponent(ColumnComponent component) {
		this.component.add(component);
		this.component.get(this.component.size() - 1).index = this.component
				.size() - 1;
		maxValue += component.value;
		addActor(component);
	}

	public void removeComponent(int index) {
		if (index >= component.size())
			return;
		maxValue -= component.get(index).value;
		removeActor(component.get(index));
		component.remove(index);
	}

	public void removeComponent(Actor component) {
		if (((ColumnComponent) component).index >= this.component.size()) {
			return;
		}
		maxValue -= ((ColumnComponent) component).value;
		removeActor(component);
		this.component.remove(((ColumnComponent) component).index);
	}

	@Override
	public void setSize(float width, float height) {
		// TODO Auto-generated method stub
		bg.setSize(width, height);
		super.setSize(width, height);
	}

	@Override
	public void setPosition(float x, float y) {
		// TODO Auto-generated method stub
		super.setPosition(x, y);
	}

	public void validateComponent() {
		if (component.size() == 0) {
			return;
		}
		float offset = getHeight() / maxValue;
		float x = 0;
		float y = 0;
		for (int i = 0; i < component.size(); i++) {
			component.get(i).setSize(getWidth(),
					offset * component.get(i).value);
			component.get(i).setPosition(x, y);
			y += component.get(i).getHeight();
		}
	}
}
