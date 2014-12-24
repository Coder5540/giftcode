package com.coder5560.game.ui;

import java.util.ArrayList;

import utils.factory.AppPreference;
import utils.factory.FontFactory.fontType;
import utils.factory.Log;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.listener.OnClickListener;
import com.coder5560.game.views.IViewController;

public class ListMenu extends ScrollPane {

	Table					table;
	IViewController			controller;
	ArrayList<ItemMenu>		listItem	= new ArrayList<ListMenu.ItemMenu>();
	private OnClickListener	onBlockUserClicked;
	private OnClickListener	onUnActiveUserClicked;
	private OnClickListener	onActiveUserClicked;
	private OnClickListener	onAvatarClicked;
	private OnClickListener	onAllMailClicked;
	private OnClickListener	onHistoryTransitionClicked;
	private OnClickListener	onAddMoneyClicked;
	private OnClickListener	onSellGiftCode;
	private OnClickListener	onUsedGiftCode;
	private OnClickListener	onUnUseGiftCode;

	LabelStyle				lbStyle;
	private IconMail		iconMail;
	public Label			lbName;

	public ListMenu(IViewController controllerView, final Table table,
			Rectangle bound) {
		super(table);
		this.controller = controllerView;
		this.table = table;
		setBounds(bound.x, bound.y, bound.width, bound.height);
		table.top();
		lbStyle = new LabelStyle(Assets.instance.fontFactory.getFont(20,
				fontType.Bold), Color.WHITE);
		Group user = new Group();
		createAvatar(user);
		table.add(user).row();
		addLine(table, 2);

		ItemMenu itemManager = new ItemMenu(
				Assets.instance.ui.getRegUsermanagement(),
				"QUẢN LÝ ĐẠI LÝ CẤP DƯỚI", getWidth(), 50);
		createItemManager(itemManager);
		addItem(itemManager, 0);

		addLine(table, 2);
		iconMail = new IconMail(45, 45);
		ItemMenu itemMail = new ItemMenu(iconMail, "HÒM THƯ", getWidth(), 50);
		createItemMail(itemMail);
		addItem(itemMail, 1);

		addLine(table, 2);
		ItemMenu itemLog = new ItemMenu(Assets.instance.ui.getIconTransition(),
				"LỊCH SỬ GIAO DỊCH", getWidth(), 50);
		createItemHistoryTransitio(itemLog);
		addItem(itemLog, 2);

		addLine(table, 2);
		ItemMenu itemGiftCode = new ItemMenu(
				Assets.instance.ui.getIconTransition(), "GIFT CODE",
				getWidth(), 50);
		createItemGiftcode(itemGiftCode);
		addItem(itemGiftCode, 3);

	}

	private void createItemGiftcode(ItemMenu itemGiftCode) {
		LabelButton sellGiftCode = new LabelButton("Bán giftcode", lbStyle,
				getWidth(), 45);
		sellGiftCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				if (onSellGiftCode != null) {
					onSellGiftCode.onClick(x, y);
				}
			}
		});

		LabelButton usedGiftCode = new LabelButton("Giftcode đã sử dụng",
				lbStyle, getWidth(), 45);
		usedGiftCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				if (onUsedGiftCode != null) {
					onUsedGiftCode.onClick(x, y);
				}
			}
		});
		LabelButton unUseGiftCode = new LabelButton("Giftcode chưa sử dụng",
				lbStyle, getWidth(), 45);
		unUseGiftCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				if (onUnUseGiftCode != null) {
					onUnUseGiftCode.onClick(x, y);
				}
			}
		});

		itemGiftCode.addComponent(sellGiftCode);
		itemGiftCode.addComponent(usedGiftCode);
		itemGiftCode.addComponent(unUseGiftCode);
	}

	private void createItemHistoryTransitio(ItemMenu itemLog) {
		LabelButton log = new LabelButton("Lịch sử", lbStyle, getWidth(), 45);
		log.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				if (onHistoryTransitionClicked != null) {
					onHistoryTransitionClicked.onClick(x, y);
				}
			}
		});
		itemLog.addComponent(log);
	}

	public void updateMail() {
		lbName.setText(UserInfo.fullName);
		Request.getInstance().getMessageUnseen(
				AppPreference.instance.getName(), new HttpResponseListener() {

					@Override
					public void handleHttpResponse(HttpResponse httpResponse) {
						String dataRespone = httpResponse.getResultAsString();
						Log.d("MAIL REQUEST", dataRespone);
						JsonValue responeJson = new JsonReader()
								.parse(dataRespone);
						boolean result = responeJson
								.getBoolean(ExtParamsKey.RESULT);
						if (result) {
							int number_unseen = responeJson
									.getInt(ExtParamsKey.NUMBER_UNSEEN);
							iconMail.setNotify(number_unseen);
						}
					}

					@Override
					public void failed(Throwable t) {

					}

					@Override
					public void cancelled() {

					}
				});
	}

	private void createAvatar(Group user) {
		user.setSize(getWidth(), 200);
		final Image bgFocus = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch, Color.GRAY));
		bgFocus.setSize(user.getWidth(), user.getHeight());
		bgFocus.getColor().a = 0;
		Image avatar = new Image(Assets.instance.ui.getAvatar());
		avatar.setSize(120, 120);
		avatar.setPosition(20, user.getHeight() / 2 - avatar.getHeight() / 2);
		lbName = new Label(UserInfo.fullName, new LabelStyle(
				Assets.instance.fontFactory.getFont(20, fontType.Light),
				Color.WHITE));
		lbName.setPosition(avatar.getX() + avatar.getWidth() + 5,
				user.getHeight() / 2 - lbName.getHeight() / 2);
		user.addActor(bgFocus);
		user.addActor(avatar);
		user.addActor(lbName);
		user.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (onAvatarClicked != null) {
					onAvatarClicked.onClick(x, y);
				}
			}
		});
		user.addListener(new InputListener() {
			Vector2	touchPoint	= new Vector2();

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				touchPoint.set(x, y);
				bgFocus.getColor().a = 1;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				bgFocus.getColor().a = 0;
				if (touchPoint.epsilonEquals(x, y, 20)) {

				}
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				if (!touchPoint.epsilonEquals(x, y, 40)) {
					bgFocus.getColor().a = 0;
					touchPoint.set(0, 0);
				}
				super.touchDragged(event, x, y, pointer);
			}
		});
	}

	private void createItemManager(ItemMenu itemManager) {

		LabelButton userActive = new LabelButton("Đã kích hoạt", lbStyle,
				getWidth(), 45);
		userActive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				if (onActiveUserClicked != null)
					onActiveUserClicked.onClick(x, y);
			}
		});
		LabelButton userUnactive = new LabelButton("Chưa kích hoạt", lbStyle,
				getWidth(), 45);
		userUnactive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				if (onUnActiveUserClicked != null)
					onUnActiveUserClicked.onClick(x, y);
			}
		});
		LabelButton userBlock = new LabelButton("Khóa", lbStyle, getWidth(), 45);
		userBlock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				if (onBlockUserClicked != null)
					onBlockUserClicked.onClick(x, y);
			}
		});

		LabelButton addMoney = new LabelButton("Cấp tiền", lbStyle, getWidth(),
				45);
		addMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				if (onAddMoneyClicked != null)
					onAddMoneyClicked.onClick(x, y);
			}
		});
		itemManager.addComponent(userActive);
		itemManager.addComponent(userUnactive);
		itemManager.addComponent(userBlock);
		itemManager.addComponent(addMoney);

	}

	private void createItemMail(ItemMenu itemMail) {
		LabelButton allMail = new LabelButton("Toàn bộ thư", lbStyle,
				getWidth(), 45);
		allMail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(float x, float y) {
				if (onAllMailClicked != null)
					onAllMailClicked.onClick(x, y);

			}
		});
		itemMail.addComponent(allMail);
	}

	void addLine(Table table, float height) {
		Image line = new Image(Assets.instance.ui.reg_ninepatch);
		line.setColor(new Color(0, 220 / 255f, 0, 1f));
		line.setHeight(height);
		line.setWidth(table.getWidth());
		table.add(line).expandX().fillX().height(height).padTop(10);
		table.row();
	}

	void addItem(ItemMenu item, int permisionItem) {
		if (true) {
			table.add(item).top().row();
			listItem.add(item);
		}
	}

	void setHeightForCell(Actor a, float height) {
		table.getCell(a).height(height);
		table.invalidate();
		layout();
	}

	public void setNotify(int number) {
		iconMail.setNotify(number);
	}

	class ItemMenu extends Group {
		Group					item;
		Image					bg;
		Image					bgFocus;
		Image					btnExpand;
		ArrayList<LabelButton>	subButton;
		boolean					isValidate;
		boolean					isExpand;
		float					maxheight;
		float					minheight;
		float					currentheight;
		Vector2					touchPoint;

		// Image bg_content;

		public ItemMenu(TextureRegion region, String title, float width,
				float height) {
			touchPoint = new Vector2();
			subButton = new ArrayList<LabelButton>();
			setSize(width, height);
			item = new Group();
			item.setSize(width, height);
			bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch, 1,
					1, 1, 1));
			bg.setColor(new Color(250 / 255f, 250 / 255f, 250 / 255f, 1f));
			bg.setSize(item.getWidth(), item.getHeight());

			bgFocus = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
			bgFocus.setColor(new Color(220 / 255f, 220 / 255f, 220 / 255f, 0));
			bgFocus.setSize(item.getWidth(), item.getHeight());

			Label lbtitle = new Label(title, new LabelStyle(
					Assets.instance.fontFactory.getFont(15, fontType.Medium),
					new Color(0, 191 / 255f, 1, 1)));
			btnExpand = new Image(Assets.instance.getRegion("down"));
			btnExpand.setSize(15, 10);
			btnExpand
					.setColor(new Color(130 / 255f, 130 / 255f, 130 / 255f, 1));
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
			// item.addActor(bg_content);
			item.addActor(bgFocus);
			item.addActor(icon);
			item.addActor(lbtitle);
			item.addActor(btnExpand);

			maxheight = height;
			minheight = height;
			currentheight = height;

			item.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					touchPoint.set(x, y);
					bgFocus.getColor().a = 1;
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					bgFocus.getColor().a = 0;
					if (touchPoint.epsilonEquals(x, y, 20)) {
						if (!isExpand) {
							setHeight(maxheight);
							showSubButton();
							setHeightForCell(ItemMenu.this, maxheight);
							btnExpand.addAction(Actions.rotateTo(180, 0.2f,
									Interpolation.exp10));
							isExpand = true;
						} else {
							setHeight(minheight);
							hideSubButton();
							isExpand = false;
						}
					}
					super.touchUp(event, x, y, pointer, button);
				}

				@Override
				public void touchDragged(InputEvent event, float x, float y,
						int pointer) {
					if (!touchPoint.epsilonEquals(x, y, 40)) {
						bgFocus.getColor().a = 0f;
						touchPoint.set(0, 0);
					}
					super.touchDragged(event, x, y, pointer);
				}
			});
		}

		public ItemMenu(IconMail iconMail, String title, float width, int height) {

			touchPoint = new Vector2();
			subButton = new ArrayList<LabelButton>();
			setSize(width, height);
			item = new Group();
			item.setSize(width, height);
			bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch, 1,
					1, 1, 1));
			bg.setColor(new Color(250 / 255f, 250 / 255f, 250 / 255f, 1f));
			bg.setSize(item.getWidth(), item.getHeight());

			bgFocus = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
			bgFocus.setColor(new Color(220 / 255f, 220 / 255f, 220 / 255f, 0));
			bgFocus.setSize(item.getWidth(), item.getHeight());

			Label lbtitle = new Label(title, new LabelStyle(
					Assets.instance.fontFactory.getFont(15, fontType.Medium),
					new Color(0, 191 / 255f, 1, 1)));
			btnExpand = new Image(Assets.instance.getRegion("down"));
			btnExpand.setSize(15, 10);
			btnExpand
					.setColor(new Color(130 / 255f, 130 / 255f, 130 / 255f, 1));
			btnExpand.setOrigin(btnExpand.getWidth() / 2,
					btnExpand.getHeight() / 2);

			iconMail.setPosition(7,
					bg.getY() + bg.getHeight() / 2 - iconMail.getHeight() / 2);
			lbtitle.setPosition(iconMail.getX() + iconMail.getWidth() + 5,
					bg.getY() + bg.getHeight() / 2 - lbtitle.getHeight() / 2);
			btnExpand.setPosition(
					item.getX() + item.getWidth() - btnExpand.getWidth() - 10,
					bg.getY() + bg.getHeight() / 2 - btnExpand.getHeight() / 2);
			item.addActor(bg);
			// item.addActor(bg_content);
			item.addActor(bgFocus);
			item.addActor(iconMail);
			item.addActor(lbtitle);
			item.addActor(btnExpand);

			maxheight = height;
			minheight = height;
			currentheight = height;

			item.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					touchPoint.set(x, y);
					bgFocus.getColor().a = 1;
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					bgFocus.getColor().a = 0;
					if (touchPoint.epsilonEquals(x, y, 20)) {
						if (!isExpand) {
							setHeight(maxheight);
							showSubButton();
							setHeightForCell(ItemMenu.this, maxheight);
							btnExpand.addAction(Actions.rotateTo(180, 0.2f,
									Interpolation.exp10));
							isExpand = true;
						} else {
							setHeight(minheight);
							hideSubButton();
							isExpand = false;
						}
					}
					super.touchUp(event, x, y, pointer, button);
				}

				@Override
				public void touchDragged(InputEvent event, float x, float y,
						int pointer) {
					if (!touchPoint.epsilonEquals(x, y, 40)) {
						bgFocus.getColor().a = 0f;
						touchPoint.set(0, 0);
					}
					super.touchDragged(event, x, y, pointer);
				}
			});

		}

		public void addComponent(LabelButton subButton) {
			subButton.setVisible(false);
			subButton.setPosition(item.getX(), item.getY());
			this.subButton.add(subButton);
			isValidate = false;
			this.subButton.get(this.subButton.size() - 1).setTouchable(
					Touchable.disabled);
			maxheight += subButton.getHeight();
		}

		public void validate() {
			item.setPosition(getX(), getY());
			for (int i = subButton.size() - 1; i >= 0; i--) {
				addActor(subButton.get(i));
				subButton.get(i).setPosition(getX(), getY());
			}
			addActor(item);
		}

		@Override
		public void act(float delta) {
			if (!isValidate) {
				clear();
				validate();
				isValidate = true;
			}
			super.act(delta);
		}

		public void showSubButton() {
			item.setY(getHeight() - item.getHeight());
			float x = item.getX();
			float y = item.getY();
			item.clearActions();
			for (int i = 0; i < subButton.size(); i++) {
				subButton.get(i).clearActions();
				subButton.get(i).setVisible(true);
				subButton.get(i).setPosition(x, y);
				subButton.get(i).addAction(
						Actions.moveTo(x, y - subButton.get(i).getHeight(),
								0.3f, Interpolation.exp5Out));
				y -= subButton.get(i).getHeight();
				subButton.get(i).setTouchable(Touchable.enabled);
			}
		}

		public void hideSubButton() {
			for (int i = 0; i < subButton.size(); i++) {
				subButton.get(i).addAction(
						Actions.sequence(Actions.moveTo(item.getX(),
								item.getY(), 0.25f, Interpolation.exp10In),
								Actions.visible(false)));
				subButton.get(i).setTouchable(Touchable.disabled);
			}

			item.addAction(Actions.sequence(Actions.delay(0.25f),
					Actions.run(new Runnable() {
						@Override
						public void run() {
							item.setY(getHeight() - item.getHeight());
							setHeightForCell(ItemMenu.this, minheight);
							for (int i = 0; i < subButton.size(); i++) {
								subButton.get(i).setPosition(item.getX(),
										item.getY());
							}
							btnExpand.addAction(Actions.rotateTo(0, 0.2f,
									Interpolation.exp10));
						}
					})));
		}
	}

	public class LabelButton extends Group {

		public static final int	LEFT	= 0;
		public static final int	CENTER	= 1;

		private Image			bg;
		public Image			bgFocus;
		Label					title;
		Vector2					touchPoint;
		int						align;
		private OnClickListener	onClickListener;

		public LabelButton(String title, LabelStyle style, float width,
				float height, int align) {
			this.align = align;
			init(title, style, width, height);
		}

		public LabelButton(String title, LabelStyle style, float width,
				float height) {
			this.align = LEFT;
			init(title, style, width, height);

			addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					touchPoint.set(x, y);
					bgFocus.getColor().a = 1;
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					bgFocus.getColor().a = 0;
					if (touchPoint.epsilonEquals(x, y, 20)) {
						if (onClickListener != null)
							onClickListener.onClick(x, y);
						for (ItemMenu item : listItem) {
							for (LabelButton label : item.subButton) {
								label.bgFocus.getColor().a = 0;
							}
						}
					}
					super.touchUp(event, x, y, pointer, button);
				}

				@Override
				public void touchDragged(InputEvent event, float x, float y,
						int pointer) {
					if (!touchPoint.epsilonEquals(x, y, 40)) {
						bgFocus.getColor().a = 0;
						touchPoint.set(0, 0);
					}
					super.touchDragged(event, x, y, pointer);
				}
			});
		}

		public void init(String title, LabelStyle style, float width,
				float height) {
			touchPoint = new Vector2();
			setSize(width, height);
			bg = new Image(new NinePatch(new NinePatch(
					Assets.instance.ui.reg_ninepatch, 4, 4, 4, 4), new Color(
					100 / 255f, 100 / 255f, 100 / 255f, 1)));
			bg.setSize(width, height);
			bgFocus = new Image(new NinePatch(new NinePatch(
					Assets.instance.ui.reg_ninepatch, 4, 4, 4, 4), new Color(
					220 / 255f, 220 / 255f, 220 / 255f, 1)));
			bgFocus.getColor().a = 0;
			bgFocus.setSize(width - 20, height);

			this.title = new Label(title, style);
			if (align == LEFT) {
				this.title.setPosition(30, bg.getY() + bg.getHeight() / 2
						- this.title.getHeight() / 2);
			} else {
				this.title.setPosition(getWidth() / 2 - this.title.getWidth()
						/ 2,
						bg.getY() + bg.getHeight() / 2 - this.title.getHeight()
								/ 2);
			}
			addActor(bg);
			bgFocus.setX(10);
			addActor(bgFocus);
			addActor(this.title);
		}

		public void setTitle(String title) {
			this.title.setText(title);
			if (align == LEFT) {
				this.title.setPosition(30, bg.getY() + bg.getHeight() / 2
						- this.title.getHeight() / 2);
			} else {
				this.title.setPosition(
						getWidth() / 2 - this.title.getTextBounds().width / 2,
						getHeight() / 2 - this.title.getHeight() / 2);
			}
		}

		public OnClickListener getOnClickListener() {
			return onClickListener;
		}

		public void setOnClickListener(OnClickListener onClickListener) {
			this.onClickListener = onClickListener;
		}
	}

	class IconMail extends Group {
		private Image	icon;
		private Image	bgNotify;
		private Label	notify;
		LabelStyle		lbNotify;
		private int		unRead	= 0;

		public IconMail(float width, float height) {
			super();
			this.setSize(width, height);
			create();
			addActor(icon);
			addActor(bgNotify);
			addActor(notify);
			setNotify(4);
		}

		void create() {
			lbNotify = new LabelStyle();
			lbNotify.font = Assets.instance.fontFactory.getFont(15,
					fontType.Bold);
			notify = new Label("" + unRead, lbNotify);
			notify.setVisible(true);
			icon = new Image(Assets.instance.ui.getRegionMail());
			icon.setOrigin(Align.center);
			icon.setColor(new Color(0, 220 / 255f, 0f, 1f));
			bgNotify = new Image(Assets.instance.ui.getCircle());
			bgNotify.setOrigin(Align.center);
			bgNotify.setColor(new Color(255 / 255f, 0f, 0f, 1f));
			notify.setAlignment(Align.center);
			valid();
		}

		public void valid() {
			float w = getWidth();
			float h = getHeight();
			icon.setSize(w, h);
			bgNotify.setSize(w / 2, h / 2);
			bgNotify.setPosition(w - bgNotify.getWidth(),
					h - bgNotify.getHeight());
			notify.setPosition(bgNotify.getX() + bgNotify.getWidth() / 2,
					bgNotify.getY() + bgNotify.getHeight() / 2, Align.center);
		}

		public void setNotify(int number) {
			notify.setText("" + number);
			this.unRead = number;
			if (number == 0) {
				notify.setVisible(false);
				bgNotify.setVisible(false);
			}
			valid();
		}

	}

	public void setOnBlockUserClicked(OnClickListener onBlockUserClicked) {
		this.onBlockUserClicked = onBlockUserClicked;
	}

	public void setOnUnActiveUserClicked(OnClickListener onUnActiveUserClicked) {
		this.onUnActiveUserClicked = onUnActiveUserClicked;
	}

	public void setOnActiveUserClicked(OnClickListener onActiveUserClicked) {
		this.onActiveUserClicked = onActiveUserClicked;
	}

	public void setController(IViewController controller) {
		this.controller = controller;
	}

	public void setOnAvatarClicked(OnClickListener onAvatarClicked) {
		this.onAvatarClicked = onAvatarClicked;
	}

	public void setOnAllMailClicked(OnClickListener onMailClicked) {
		this.onAllMailClicked = onMailClicked;
	}

	public void setOnHistoryTransitionClicked(
			OnClickListener onHistoryTransitionClicked) {
		this.onHistoryTransitionClicked = onHistoryTransitionClicked;
	}

	public void setOnAddMoneyClicked(OnClickListener onAddMoneyClicked) {
		this.onAddMoneyClicked = onAddMoneyClicked;
	}

	public void setOnSellGiftCode(OnClickListener onSellGiftCode) {
		this.onSellGiftCode = onSellGiftCode;
	}

	public void setOnUsedGiftCode(OnClickListener onUsedGiftCode) {
		this.onUsedGiftCode = onUsedGiftCode;
	}

	public void setOnUnUseGiftCode(OnClickListener onUnUseGiftCode) {
		this.onUnUseGiftCode = onUnUseGiftCode;
	}

}
