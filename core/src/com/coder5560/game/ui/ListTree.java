package com.coder5560.game.ui;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

public class ListTree extends Group {

	Group group;
	ScrollPane panel;
	ArrayList<ItemTree> listItem = new ArrayList<ItemTree>();

	public ListTree(Rectangle bound) {
		setSize(bound.width, bound.height);
		group = new Group();
		panel = new ScrollPane(group);
		panel.setBounds(bound.x, bound.y, bound.width, bound.height);
		addActor(panel);
	}

	public void addItem(ItemTree item) {
		listItem.add(item);
		group.addActor(item);
		resetPosition();
	}

	public void removeItem(ItemTree item) {
		listItem.remove(item);
		group.removeActor(item);
		resetPosition();
	}

	public void resetPosition() {
		float currentY = 0;
		for (int i = listItem.size() - 1; i >= 0; i--) {
			ItemTree item = listItem.get(i);
			item.setY(currentY);
			currentY += item.getHeight();
		}
		group.setSize(group.getWidth(), Math.abs(currentY));
	}
}
