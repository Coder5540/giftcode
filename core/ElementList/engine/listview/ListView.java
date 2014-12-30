package engine.listview;

import java.util.ArrayList;

import utils.factory.FontFactory.FontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.views.IViewController;

public class ListView extends ScrollPane {

	Table			table;
	IViewController	controller;
	ArrayList<Item>	listItem	= new ArrayList<ListView.Item>();

	LabelStyle		lbStyle;

	INotifyEvent	notifyEvent	= new INotifyEvent() {

									@Override
									public void broadcastEvent(Item item,
											NotifyEvent notifyEvent) {
										if (notifyEvent == NotifyEvent.COLLLAPSE) {
											setHeightForCell(item,
													item.getMinHeigh());
										}
										if (notifyEvent == NotifyEvent.EXPAND) {
											setHeightForCell(item,
													item.getMaxHeigh());
										}

									}
								};

	public ListView(IViewController controllerView, final Table table,
			Rectangle bound) {
		super(table);
		this.controller = controllerView;
		this.table = table;
		setBounds(bound.x, bound.y, bound.width, bound.height);
		table.top();
		lbStyle = new LabelStyle(Assets.instance.fontFactory.getFont(20,
				FontType.Regular), Color.WHITE);

		Color colorItemMenu = new Color(0, 191 / 255f, 1, 1);
		Color colorText = Color.WHITE;

		Item itemManager = new Item(notifyEvent,
				Assets.instance.ui.getRegUsermanagement(),
				"QUẢN LÝ ĐẠI LÝ CẤP DƯỚI", getWidth(), 50, colorItemMenu,
				colorText);
		createItemManager(itemManager);
		addLine(table, 2, colorItemMenu);
		addItem(itemManager, 0);
	}

	private void createItemManager(Item itemManager) {

		SubItemUser userActive = new SubItemUser(1, "Đã kích hoạt", lbStyle,
				getWidth(), 60);
		SubItemLabel userUnactive = new SubItemLabel(2, "Chưa kích hoạt",
				lbStyle, getWidth(), 45);
		SubItemUser userBlock = new SubItemUser(4, "Khóa", lbStyle, getWidth(),
				45);
		SubItemUser addMoney = new SubItemUser(3, "Cấp tiền", lbStyle,
				getWidth(), 100);

		itemManager.addSubItem(userActive);
		itemManager.addSubItem(userUnactive);
		itemManager.addSubItem(userBlock);
		itemManager.addSubItem(addMoney);

	}

	public void addLine(Table table, float height, Color color) {
		Image line = new Image(Assets.instance.ui.reg_ninepatch);
		line.setColor(color);
		line.setHeight(height);
		line.setWidth(table.getWidth());
		table.add(line).expandX().fillX().height(height).padTop(10);
		table.row();
	}

	void addItem(Item item, int permisionItem) {
		if (true) {
			table.add(item).top().row();
			listItem.add(item);
		}
	}

	public void setHeightForCell(Actor a, float height) {
		table.getCell(a).height(height);
		table.invalidate();
		layout();
	}

	class Item extends Group {
		Group				item;
		Image				bg;
		Image				btnExpand;
		ArrayList<ISubItem>	subItems;
		boolean				isValidate;
		boolean				isExpand;
		float				maxheight;
		float				minheight;
		float				currentheight;
		public INotifyEvent	notifyEvent;

		public Item(final INotifyEvent notifyEvent, TextureRegion region,
				String title, float width, float height, Color color,
				Color textColor) {
			this.notifyEvent = notifyEvent;
			subItems = new ArrayList<ISubItem>();
			setSize(width, height);
			item = new Group();
			item.setSize(width, height);
			bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
					color));
			bg.setColor(new Color(1f, 1f, 1, 1f));
			bg.setSize(item.getWidth(), item.getHeight());

			Label lbtitle = new Label(title, new LabelStyle(
					Assets.instance.fontFactory.getFont(16, FontType.Bold),
					textColor));
			btnExpand = new Image(Assets.instance.getRegion("down"));
			btnExpand.setSize(15, 10);
			btnExpand.setColor(textColor);
			btnExpand.setOrigin(btnExpand.getWidth() / 2,
					btnExpand.getHeight() / 2);

			Image icon = new Image(region);
			icon.setSize(45, 45);

			icon.setPosition(7,
					bg.getY() + bg.getHeight() / 2 - icon.getHeight() / 2);
			lbtitle.setPosition(icon.getX() + icon.getWidth() + 5, bg.getY()
					+ bg.getHeight() / 2 - lbtitle.getHeight() / 2);
			btnExpand.setPosition(
					item.getX() + item.getWidth() - btnExpand.getWidth() - 10,
					bg.getY() + bg.getHeight() / 2 - btnExpand.getHeight() / 2);

			item.addActor(bg);
			item.addActor(icon);
			item.addActor(lbtitle);
			item.addActor(btnExpand);

			maxheight = height;
			minheight = height;
			currentheight = height;

			item.addListener(new ClickListener() {
				Vector2	touchPoint	= new Vector2();

				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					touchPoint.set(x, y);
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					if (touchPoint.epsilonEquals(x, y, 20)) {
						onSizeChange();
					}
					super.touchUp(event, x, y, pointer, button);
				}

				@Override
				public void touchDragged(InputEvent event, float x, float y,
						int pointer) {
					if (!touchPoint.epsilonEquals(x, y, 40)) {
						touchPoint.set(0, 0);
					}
					super.touchDragged(event, x, y, pointer);
				}
			});
		}

		public void onSizeChange() {
			if (!isExpand) {
				setHeight(maxheight);
				showSubItem();
				notifyEvent.broadcastEvent(Item.this, NotifyEvent.EXPAND);
				btnExpand.addAction(Actions.rotateTo(180, 0.2f,
						Interpolation.exp10));
				isExpand = true;
			} else {
				setHeight(minheight);
				hideSubItem();
				isExpand = false;
			}
		}

		public void addSubItem(ISubItem subButton) {
			subButton.setVisible(false);
			subButton.setPosition(item.getX(), item.getY());
			this.subItems.add(subButton);
			isValidate = false;
			this.subItems.get(this.subItems.size() - 1).setTouchable(
					Touchable.disabled);
			maxheight += subButton.getHeight();
		}

		public void validatePosition() {
			item.setPosition(getX(), getY());
			for (int i = subItems.size() - 1; i >= 0; i--) {
				addActor(subItems.get(i));
				subItems.get(i).setPosition(getX(), getY());
			}
			addActor(item);
		}

		@Override
		public void act(float delta) {
			if (!isValidate) {
				clear();
				validatePosition();
				isValidate = true;
			}
			super.act(delta);
		}

		public void showSubItem() {
			item.setY(getHeight() - item.getHeight());
			float x = item.getX();
			float y = item.getY();
			item.clearActions();
			for (int i = 0; i < subItems.size(); i++) {
				subItems.get(i).clearActions();
				subItems.get(i).setVisible(true);
				subItems.get(i).setPosition(x, y);
				subItems.get(i).addAction(
						Actions.moveTo(x, y - subItems.get(i).getHeight(),
								0.3f, Interpolation.exp5Out));
				y -= subItems.get(i).getHeight();
				subItems.get(i).setTouchable(Touchable.enabled);
			}
		}

		public void hideSubItem() {
			for (int i = 0; i < subItems.size(); i++) {
				subItems.get(i).addAction(
						Actions.sequence(Actions.moveTo(item.getX(),
								item.getY(), 0.25f, Interpolation.exp10In),
								Actions.visible(false)));
				subItems.get(i).setTouchable(Touchable.disabled);
			}
			item.addAction(Actions.sequence(Actions.delay(0.25f),
					Actions.run(new Runnable() {
						@Override
						public void run() {
							item.setY(getHeight() - item.getHeight());
							notifyEvent.broadcastEvent(Item.this,
									NotifyEvent.COLLLAPSE);
							for (int i = 0; i < subItems.size(); i++) {
								subItems.get(i).setPosition(item.getX(),
										item.getY());
							}
							btnExpand.addAction(Actions.rotateTo(0, 0.2f,
									Interpolation.exp10));
						}
					})));
		}

		public float getMinHeigh() {
			return minheight;
		}

		public float getMaxHeigh() {
			return maxheight;
		}

	}

}
