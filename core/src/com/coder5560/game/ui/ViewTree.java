package com.coder5560.game.ui;

import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.views.View;

public class ViewTree extends View {
	public void buildComponent() {
		ListTree listMenu = new ListTree(getBound());
		Table header = new Table();
		header.setBackground(new NinePatchDrawable(new NinePatch(new NinePatch(
				Style.ins.np2), Color.WHITE)));
		header.setSize(200, 80);
		header.add(new Image(Assets.instance.ui.getRegUsermanagement()))
				.width(200).height(80);
		Table content = new Table();
		content.setSize(200, 180);
		content.setBackground(new NinePatchDrawable(new NinePatch(
				new NinePatch(Style.ins.np2), Color.WHITE)));
		content.add(new Image(Assets.instance.ui.getAvatar())).width(200)
				.height(180);
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		listMenu.addItem(new ItemTree(
				new Image(Assets.instance.ui.getAvatar()), new Image(
						Assets.instance.ui.getAvatar())));
		add(listMenu);
	}
}
