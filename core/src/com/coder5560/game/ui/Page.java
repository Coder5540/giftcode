package com.coder5560.game.ui;

import java.util.ArrayList;

import utils.factory.StringUtil;
import utils.factory.FontFactory.fontType;

import com.aia.appsreport.component.table.ItemTable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.coder5560.game.assets.Assets;

public class Page extends Table {

	private ArrayList<ItemTable> data;
	private ArrayList<ItemTable> dataCurrentPage;
	private ArrayList<ItemPage> listItemPage;
	private Table tablePage;
	private ScrollPane scrollPage;
	private ItemPage currentPage;
	private int indexCurrentPage;
	private Button btBack;
	private Button btNext;
	private ItemListener listener;

	public Page(float width, float height) {
		this.setSize(width, height);
		tablePage = new Table();
		tablePage.setTransform(true);
		scrollPage = new ScrollPane(tablePage);
		scrollPage.setTransform(true);
		listItemPage = new ArrayList<Page.ItemPage>();
		data = new ArrayList<ItemTable>();

		ButtonStyle nextStyle = new ButtonStyle();
		// nextStyle.up = new TextureRegionDrawable(
		// Assets.instance.uiP.icon_next_up);
		nextStyle.up = new TextureRegionDrawable(Assets.instance.ui.reg_submenu);
		// nextStyle.down = new TextureRegionDrawable(
		// Assets.instance.uiP.icon_next_down);
		nextStyle.down = new TextureRegionDrawable(
				Assets.instance.ui.reg_submenu);
		btNext = new Button(nextStyle);
		btNext.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (indexCurrentPage != listItemPage.size()) {
					currentPage.focus(false);
					indexCurrentPage++;
					currentPage = listItemPage.get(indexCurrentPage - 1);
					currentPage.focus(true);
					scrollPage.scrollToCenter(currentPage.getX(), 0,
							scrollPage.getWidth() / 2, 50);
					dataCurrentPage = getDataFromPage(indexCurrentPage);
					listener.onItemClick();
				}
			}
		});

		ButtonStyle backStyle = new ButtonStyle();
		// backStyle.up = new TextureRegionDrawable(
		// Assets.instance.uiP.icon_next_up);
		backStyle.up = new TextureRegionDrawable(Assets.instance.ui.reg_submenu);
		// backStyle.down = new TextureRegionDrawable(
		// Assets.instance.uiP.icon_next_down);
		backStyle.down = new TextureRegionDrawable(
				Assets.instance.ui.reg_submenu);
		btBack = new Button(backStyle);
		btBack.setTransform(true);
		btBack.setOrigin(10, 15);
		btBack.setRotation(180);
		btBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (indexCurrentPage > 1) {
					currentPage.focus(false);
					indexCurrentPage--;
					currentPage = listItemPage.get(indexCurrentPage - 1);
					currentPage.focus(true);
					scrollPage.scrollToCenter(currentPage.getX(), 0,
							scrollPage.getWidth() / 2, 50);
					dataCurrentPage = getDataFromPage(indexCurrentPage);
					listener.onItemClick();
				}
			}
		});
	}

	public void init() {
		int pageNumber = StringUtil.round((float) (data.size() / 30f));
		for (int i = 0; i < pageNumber; i++) {
			addPage();
		}
		dataCurrentPage = getDataFromPage(1);
		if (listItemPage.size() > 0) {
			indexCurrentPage = 1;
			currentPage = listItemPage.get(0);
			currentPage.focus(true);
		}
		if (pageNumber > 1) {
			this.add(btBack).padLeft(10).width(20).height(30);
		}
		this.add(scrollPage);
		if (pageNumber > 1) {
			this.add(btNext).padRight(10).width(20).height(30);
		}
	}

	public void addPage() {
		final ItemPage newPage = new ItemPage(listItemPage.size() + 1);
		newPage.addListener(new InputListener() {
			Vector2 touchPoint = new Vector2();

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				touchPoint.set(x, y);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if (touchPoint.epsilonEquals(x, y, 20)) {
					if (newPage != currentPage) {
						currentPage.focus(false);
						currentPage = newPage;
						currentPage.focus(true);
						scrollPage.scrollToCenter(currentPage.getX(), 0,
								scrollPage.getWidth() / 2, 50);
						indexCurrentPage = newPage.index;
						dataCurrentPage = getDataFromPage(indexCurrentPage);
						listener.onItemClick();
					}
				}
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				super.touchDragged(event, x, y, pointer);
				if (!touchPoint.epsilonEquals(x, y, 40)) {
					touchPoint.set(0, 0);
				}
			}
		});
		listItemPage.add(newPage);
		tablePage.add(newPage).padLeft(15);
	}

	public void addData(ItemTable data) {
		this.data.add(data);
	}

	public void removeAllPage() {
		data.clear();
		listItemPage.clear();
		tablePage.reset();
		this.clear();
	}

	public ArrayList<ItemTable> getCurrentDataPage() {
		return dataCurrentPage;
	}

	public ArrayList<ItemTable> getDataFromPage(int i) {
		ArrayList<ItemTable> data = new ArrayList<ItemTable>();
		int begin = (i - 1) * 30;
		int end = 0;
		for (int a = 0; a < 30; a++) {
			if (this.data.size() > end + a) {
				end = begin + a;
			} else {
				break;
			}
		}
		if (end != 0) {
			for (int j = begin; j <= end; j++) {
				ItemTable item = this.data.get(j);
				data.add(item);
			}
		}
		return data;
	}

	public void setListener(ItemListener listener) {
		this.listener = listener;
	}

	class ItemPage extends Table {

		int index;
		Label page;

		public ItemPage(int index) {
			this.index = index;
			this.setTransform(true);
			this.page = new Label("" + index, new LabelStyle(
					Assets.instance.fontFactory.getFont(30, fontType.Regular),
					Color.BLACK));
			this.setOrigin(page.getWidth() / 2, page.getHeight() / 2);
			this.setScale(0.7f);
			this.add(this.page);
		}

		public void focus(boolean isFocus) {
			if (isFocus) {
				this.addAction(Actions.sequence(
						Actions.scaleTo(1.2f, 1.2f, 0.2f, Interpolation.linear),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								page.getStyle().fontColor = Color.RED;
							}
						})));
			} else {
				this.addAction(Actions.sequence(
						Actions.scaleTo(0.7f, 0.7f, 0.2f, Interpolation.linear),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								page.getStyle().fontColor = Color.BLACK;
							}
						})));

			}
		}
	}

}
