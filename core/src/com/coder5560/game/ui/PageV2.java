package com.coder5560.game.ui;

import java.util.ArrayList;

import utils.factory.FontFactory.FontType;
import utils.factory.StringUtil;

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
import com.coder5560.game.enums.Constants;

public class PageV2 extends Table {

	private ArrayList<ArrayList<String>> data;
	private ArrayList<ArrayList<String>> dataCurrentPage;
	private ArrayList<ItemPage> listItemPage;
	private Table tablePage;
	private ScrollPane scrollPage;
	private ItemPage currentPage;
	private int indexCurrentPage;
	private Button btBack;
	private Button btNext;
	private ItemListener listener;

	public PageV2(float width, float height) {
		this.setSize(width, height);
		tablePage = new Table();
		tablePage.setTransform(true);
		scrollPage = new ScrollPane(tablePage);
		scrollPage.setTransform(true);
		listItemPage = new ArrayList<PageV2.ItemPage>();
		data = new ArrayList<ArrayList<String>>();

		ButtonStyle nextStyle = new ButtonStyle();
		nextStyle.up = new TextureRegionDrawable(
				Assets.instance.getRegion("next_up"));
		nextStyle.down = new TextureRegionDrawable(
				Assets.instance.getRegion("next_down"));
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

		btBack = new Button(nextStyle);
		btBack.setTransform(true);
		btBack.setOrigin(23, 17);
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
		int pageNumber = StringUtil.round((float) (data.size() / 15f));
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
			this.add(btBack).padLeft(10).padRight(10).width(25).height(34);
		}
		this.add(scrollPage);
		if (pageNumber > 1) {
			this.add(btNext).padRight(10).padLeft(10).width(25).height(34);
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

	public void addData(ArrayList<String> data) {
		this.data.add(data);
	}

	public void removeAllPage() {
		data.clear();
		listItemPage.clear();
		tablePage.reset();
		this.clear();
	}

	public ArrayList<ArrayList<String>> getCurrentDataPage() {
		return dataCurrentPage;
	}

	public ArrayList<ArrayList<String>> getDataFromPage(int i) {
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		int begin = (i - 1) * 15;
		int end = begin + 14;
		if (end != 0) {
			for (int j = begin; j <= end; j++) {
				if (this.data.size() > j) {
					ArrayList<String> item = this.data.get(j);
					data.add(item);
				} else {
					break;
				}
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
					Assets.instance.fontFactory.getFont(30, FontType.Regular),
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
								page.getStyle().fontColor = Constants.COLOR_ACTIONBAR;
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
