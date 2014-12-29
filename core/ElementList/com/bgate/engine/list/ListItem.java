package com.bgate.engine.list;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class ListItem extends Table {
	Table		root;
	ScrollPane	scroll;
	Array<Item>	listItem = new Array<Item>();

	public ListItem(Table root, ScrollPane scroll) {
		super();
		this.root = root;
		this.scroll = scroll;
		setUp();
	}

	private void setUp() {
		// root = new Table();
		// root.setSize(getWidth(), getHeight());
		// root.top();
		// root.defaults().expandX().fillX().height(60).align(Align.center);
		// scroll = new ScrollPane(root);
		// scroll.setScrollingDisabled(true, false);
		// add(scroll).expand().fill().top();
	}

	private void addItemUser(Table rootTable, Item itemUser) {
		rootTable.add(itemUser).pad(4).row();
		rootTable.add(itemUser.getData())
				.height(itemUser.getData().getHeight())
				.padLeft(rootTable.getWidth() / 2 - itemUser.getWidth() / 2)
				.padRight(rootTable.getWidth() / 2 - itemUser.getWidth() / 2)
				.row();
	}

	public void addItem(Item item) {
		addItemUser(root, item);
		listItem.add(item);
	}

	public Item getItem(int id) {
		for (int i = 0; i < listItem.size; i++) {
			if (listItem.get(i).getId() == id)
				return listItem.get(i);
		}
		return null;
	}
}
