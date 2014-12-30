package com.bgate.engine.list;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public interface IItem {

	public void addItem(IItem item);

	public IItem getItem();

	public void removeItem(IItem item);

	public void removeItem(int itemID);

	public Array<IItem> getAllItems();

	public void clearAll();

	public void layout();

	public void validate();

	public Table getRoot();

	public ScrollPane getScroll();

}
