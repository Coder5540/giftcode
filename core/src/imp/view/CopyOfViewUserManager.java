package imp.view;

import utils.elements.Img;
import utils.factory.FontFactory.FontType;
import utils.factory.Log;
import utils.factory.StringSystem;
import utils.factory.Style;
import utils.listener.OnClickListener;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.Toast;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.GameEvent;
import com.coder5560.game.enums.RoleID;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.DialogCustom;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.View;

public class CopyOfViewUserManager extends View {

	public static final int	ADMIN_CLICK			= 100;
	public static final int	AGENCY_1_CLICK		= 101;
	public static final int	AGENCY_2_CLICK		= 102;
	public static final int	MONEYMANAGE_CLICK	= 99;
	public static final int	CHILDREN_CLICK		= 98;

	// Table root;
	ScrollPane				scroll;
	JsonValue				responseAllAgency;
	JsonValue				responseCustomAgency;

	Array<User>				listAdmin			= new Array<CopyOfViewUserManager.User>();
	Array<User>				listMoneyManager	= new Array<CopyOfViewUserManager.User>();
	Array<User>				listAgency1			= new Array<CopyOfViewUserManager.User>();
	Array<User>				listAgency2			= new Array<CopyOfViewUserManager.User>();
	Array<User>				listChildren		= new Array<CopyOfViewUserManager.User>();

	Color					bgItemColor			= new Color(
														Constants.COLOR_ACTIONBAR);
	Color					textItemColor		= new Color(
														Constants.COLOR_ACTIONBAR);
	Color					dataItemColor		= new Color(
														Constants.COLOR_ACTIONBAR);
	Color					iconItemColor		= new Color(
														Constants.COLOR_ACTIONBAR);
	Tree					tree;
	boolean					isClickExpand		= false;

	public CopyOfViewUserManager() {
		super();
		dataItemColor.a = 0;
		bgItemColor.a = 0;
	}

	public void buildComponent() {
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch)));
		Table topData = new Table();
		add(topData).width(getWidth()).height(4).top().row();
		buildRoot();
		Request.getInstance().getAllAgency(UserInfo.phone, -1,
				onAllAgencyListener);
		Loading.ins.show(this);
	}

	public void addItemUser(Table rootTable, ItemUser itemUser, float padLeft,
			float padRight) {
		rootTable.add(itemUser).padBottom(2).row();
		rootTable.add(itemUser.data).height(itemUser.data.getHeight())
				.padLeft(padLeft).padRight(padRight).row();
	}

	public void addItemUser(Table rootTable, ItemUser itemUser) {
		rootTable.add(itemUser).padBottom(2).row();
		rootTable.add(itemUser.data).height(itemUser.data.getHeight())
				.padLeft(rootTable.getWidth() / 2 - itemUser.getWidth() / 2)
				.padRight(rootTable.getWidth() / 2 - itemUser.getWidth() / 2)
				.row();
	}

	@Override
	public void show(OnCompleteListener listener) {

		super.show(listener);
	}

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
	}

	Actor	actorExit	= new Actor();

	@Override
	public void update(float delta) {
		actorExit.act(delta);
		processResponse();
		super.update(delta);
	}

	private void processResponse() {
		if (responseAllAgency != null) {
			processResponseAll(responseAllAgency);
			responseAllAgency = null;
		}
		if (responseCustomAgency != null) {
			processResponseCustom();
			responseCustomAgency = null;
		}
	}

	public void rebuildUI() {
		for (int i = 0; i < listMoneyManager.size; i++) {
			SubItemUser subItemUser = new SubItemUser(MONEYMANAGE_CLICK * 100
					+ i + 1, 440, 60,
					Assets.instance.ui.getRegUsermanagement(),
					listMoneyManager.get(i).name, "("
							+ listMoneyManager.get(i).phone + ")",
					onClickListener);
			UserNode node = new UserNode(subItemUser);
			nodeMoneyManager.add(node);
		}
		nodeMoneyManager.getItem().lbTextNumber.setText("("
				+ listMoneyManager.size + ")" + "");
		nodeAdminAll.getItem().lbTextNumber.setText("(" + listAdmin.size + ")"
				+ "");

		for (int i = 0; i < listChildren.size; i++) {
			SubItemUser subItemUser = new SubItemUser(CHILDREN_CLICK * 100 + i
					+ 1, 440, 60, Assets.instance.ui.getRegUsermanagement(),
					listChildren.get(i).name, "(" + listChildren.get(i).phone
							+ ")", onClickListener);
			UserNode node = new UserNode(subItemUser);
			nodeChildren.add(node);
		}
		nodeChildren.getItem().lbTextNumber.setText("(" + listChildren.size
				+ ")" + "");
		nodeChildren.getItem().lbTextNumber.setText("(" + listChildren.size
				+ ")" + "");

		for (int i = 0; i < listAdmin.size; i++) {
			// Log.d("add admin" + user.data.getString(ExtParamsKey.REF_CODE)
			// + " manager phone=" + listMoneyManager.get(i).phone);
			SubItemUser subItemUserAdmin = new SubItemUser(ADMIN_CLICK * 100
					+ (i + 1), 440, 60,
					Assets.instance.ui.getRegUsermanagement(),
					listAdmin.get(i).name, "(" + listAdmin.get(i).phone + ")",
					onClickListener);
			UserNode nodeAdmin = new UserNode(subItemUserAdmin);
			nodeAdminAll.add(nodeAdmin);
			for (int j = 0; j < listAgency1.size; j++) {
				if (listAdmin.get(i).phone.equals(listAgency1.get(j).data
						.getString(ExtParamsKey.REF_CODE))) {
					SubItemUser subItemUserAg1 = new SubItemUser(AGENCY_1_CLICK
							* 100 + (j + 1), 440, 60,
							Assets.instance.ui.getRegUsermanagement(),
							listAgency1.get(j).name, "("
									+ listAgency1.get(j).phone + ")",
							onClickListener);
					UserNode nodeAg1 = new UserNode(subItemUserAg1);
					nodeAdmin.add(nodeAg1);

					for (int t = 0; t < listAgency2.size; t++) {
						if (listAgency1.get(j).phone
								.equals(listAgency2.get(t).data
										.getString(ExtParamsKey.REF_CODE))) {
							SubItemUser subItemUserAg2 = new SubItemUser(
									AGENCY_2_CLICK * 100 + (t + 1), 440, 60,
									Assets.instance.ui.getRegUsermanagement(),
									listAgency2.get(t).name, "("
											+ listAgency2.get(t).phone + ")",
									onClickListener);
							UserNode nodeAg2 = new UserNode(subItemUserAg2);
							nodeAg1.add(nodeAg2);
						}
					}
				}
			}
		}

		for (int i = 0; i < tree.getNodes().size; i++) {
			final Node node = tree.getNodes().get(i);
			node.getActor().addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					node.setExpanded(!node.isExpanded());
				}
			});
			for (final Node node2 : node.getChildren()) {
				if (node2.getChildren().size > 0) {
					((UserNode) node2).getItem().setExpandVisiable(true);
					((UserNode) node2).getItem().setExpandListener(
							new ClickListener() {
								@Override
								public void clicked(InputEvent event, float x,
										float y) {
									node2.setExpanded(!node2.isExpanded());
									((UserNode) node2).getItem().setExpand(
											node2.isExpanded());
									isClickExpand = true;
								}
							});
				}

				for (final Node node3 : node2.getChildren()) {
					if (node3.getChildren().size > 0) {
						((UserNode) node3).getItem().setExpandVisiable(true);
						((UserNode) node3).getItem().setExpandListener(
								new ClickListener() {
									@Override
									public void clicked(InputEvent event,
											float x, float y) {
										node3.setExpanded(!node3.isExpanded());
										((UserNode) node3).getItem().setExpand(
												node3.isExpanded());
										isClickExpand = true;
									}
								});
					}
				}
			}
		}
		nodeMoneyManager.setExpanded(true);
		nodeAdminAll.setExpanded(true);
	}

	UserNode	nodeMoneyManager, nodeAdminAll, nodeChildren;

	private void buildRoot() {

		final ItemUser itemMoneyManager = new ItemUser(0, 440, 60,
				Assets.instance.ui.getRegUsermanagement(), "MoneyManager", " ("
						+ 1 + ")", onClickListener, new Table());
		nodeMoneyManager = new UserNode(itemMoneyManager);
		final ItemUser itemUserAdmin = new ItemUser(ADMIN_CLICK, 440, 60,
				Assets.instance.ui.getRegUsermanagement(), "Admin", " (" + 1
						+ ")", onClickListener, new Table());
		nodeAdminAll = new UserNode(itemUserAdmin);

		final ItemUser itemUserChildren = new ItemUser(CHILDREN_CLICK, 440, 60,
				Assets.instance.ui.getRegUsermanagement(), "Children", " (" + 1
						+ ")", onClickListener, new Table());
		nodeChildren = new UserNode(itemUserChildren);

		tree = new Tree(Style.ins.treeStyle);
		tree.setIconSpacing(20, 0);
		tree.setStyle(Style.ins.treeStyle);

		tree.add(nodeMoneyManager);
		tree.add(nodeAdminAll);
		tree.add(nodeChildren);

		tree.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {

				for (int i = 0; i < tree.getNodes().size; i++) {
					UserNode node = (UserNode) tree.getNodes().get(i);
					if (tree.getSelection().contains(node)) {
						node.getItem().setWhite();
					} else {
						node.getItem().setColorful();
					}
					if (node.getChildren().size > 0) {
						if (!node.isExpanded())
							tree.getSelection().remove(node);
					}
					for (int j = 0; j < node.getChildren().size; j++) {
						UserNode node2 = (UserNode) node.getChildren().get(j);
						if (tree.getSelection().contains(node2)) {
							node2.getItem().setWhite();
						} else {
							node2.getItem().setColorful();
						}
						if (node2.getChildren().size > 0) {
							if (!node2.isExpanded())
								tree.getSelection().remove(node2);
						}

						for (int k = 0; k < node2.getChildren().size; k++) {
							UserNode node3 = (UserNode) node2.getChildren()
									.get(k);
							if (tree.getSelection().contains(node3)) {
								node3.getItem().setWhite();
							} else {
								node3.getItem().setColorful();
							}
							for (int l = 0; l < node3.getChildren().size; l++) {
								UserNode node4 = (UserNode) node3.getChildren()
										.get(l);
								if (tree.getSelection().contains(node4)) {
									node4.getItem().setWhite();
								} else {
									node4.getItem().setColorful();
								}
							}
						}
					}
				}
			}
		});
		scroll = new ScrollPane(tree);
		scroll.setScrollingDisabled(true, false);
		add(scroll).expand().fill().top().left();
	}

	private void processResponseAll(JsonValue response) {
		JsonValue list = response.get(ExtParamsKey.LIST);
		listAdmin.clear();
		listAgency1.clear();
		listAgency2.clear();
		listMoneyManager.clear();
		listChildren.clear();
		for (int i = 0; i < list.size; i++) {
			JsonValue userData = list.get(i);
			String userName = userData.getString(ExtParamsKey.FULL_NAME);
			String phone = userData.getString(ExtParamsKey.AGENCY_NAME);
			int roleID = userData.getInt(ExtParamsKey.ROLE_ID);
			User user = new User(userData, roleID, userName, phone);
			
			
			if (roleID == RoleID.ADMIN) {
				listAdmin.add(user);
			} else if (roleID == RoleID.MONEY_MANAGER) {
				listMoneyManager.add(user);
			} else if (roleID == RoleID.AGENCY_LEVEL1) {
				listAgency1.add(user);
			} else if (roleID == RoleID.AGENCY_LEVEL2) {
				String refrenceCode = userData.getString(ExtParamsKey.REF_CODE);
				if (refrenceCode.equalsIgnoreCase(UserInfo.getInstance().phone)) {
					listChildren.add(user);
				} else {
					listAgency2.add(user);
				}
			}
		}
		rebuildUI();
	}

	private void processResponseCustom() {
		if (responseCustomAgency != null) {
			String userName = responseCustomAgency
					.getString(ExtParamsKey.FULL_NAME);
			String phone = responseCustomAgency
					.getString(ExtParamsKey.AGENCY_NAME);
			final int roleID = responseCustomAgency
					.getInt(ExtParamsKey.ROLE_ID);
			final User user = new User(responseCustomAgency, roleID, userName,
					phone);
			if (!getViewController().isContainView(
					ViewInfoDaiLySmall.class.getName())) {
				ViewInfoDaiLySmall view = new ViewInfoDaiLySmall();
				view.build(getStage(), getViewController(),
						ViewInfoDaiLySmall.class.getName(), new Rectangle(0, 0,
								Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
										- Constants.HEIGHT_ACTIONBAR));
				view.buildComponent();
			}
			ViewInfoDaiLySmall view = ((ViewInfoDaiLySmall) getViewController()
					.getView(ViewInfoDaiLySmall.class.getName()));
			view.buildComponent();
			view.setInfo(user.data);
			view.clearButton();
			view.addButton("Lịch sử giao dịch", new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!getViewController().isContainView(
							StringSystem.VIEW_HOME_V2)) {
						// khoi tao
						HomeViewV2 homeViewV2 = new HomeViewV2();
						homeViewV2.build(getStage(), getViewController(),
								StringSystem.VIEW_HOME_V2, new Rectangle(0, 0,
										Constants.WIDTH_SCREEN,
										Constants.HEIGHT_SCREEN
												- Constants.HEIGHT_ACTIONBAR));
						homeViewV2.buildComponent();
						homeViewV2.setUserName(user.phone);
						Log.d("role_id : " + user.roleID);
						homeViewV2.setRoleId(user.roleID);
						Log.d("User Phone " + user.phone);
						homeViewV2.show(null);
					} else {
						HomeViewV2 homeViewV2 = (HomeViewV2) getViewController()
								.getView(StringSystem.VIEW_HOME_V2);
						homeViewV2.setUserName(user.phone);
						homeViewV2.setRoleId(user.roleID);
						Log.d("User Phone 2 :" + user.phone);
						homeViewV2.show(null);
					}
				}
			});
			view.addButton("Cấp quyền", new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					onClickedChangeRole(user, roleID);
				}
			});
			view.show(null);
			responseCustomAgency = null;
		}
	}

	@Override
	public void back() {
		if (actorExit.getActions().size > 0) {
			Gdx.app.exit();
		} else {
			Toast.makeText(getStage(), "Nhấn thêm lần nữa để thoát !", 0.3f);
			actorExit.addAction(Actions.delay(1f));
		}
	}

	@Override
	public void onReload() {
		// Request.getInstance().getAllAgency(UserInfo.phone, -1,
		// onAllAgencyListener);
	}

	public void onGameEvent(GameEvent gameEvent) {
		if (gameEvent == GameEvent.ONBACK
				&& TraceView.instance.getLastView().equalsIgnoreCase(getName())) {
			Request.getInstance().getAllAgency(UserInfo.phone, -1,
					onAllAgencyListener);
		}
		// if (gameEvent == GameEvent.ONREFRESH
		// && TraceView.instance.getLastView().equalsIgnoreCase(getName())) {
		// getViewController().getView(StringSystem.VIEW_HOME).show(null);
		// // if (getViewController().isContainView(
		// // ViewInfoDaiLySmall.class.getName())) {
		// // getViewController().removeView(
		// // ViewInfoDaiLySmall.class.getName());
		// // }
		// // if (getViewController().isContainView("viewchangerole")) {
		// // getViewController().removeView("viewchangerole");
		// // }
		// }
	};

	OnClickListener	onClickListener	= new OnClickListener() {

										@Override
										public void onClick(int idClicked) {
											// if (idClicked == ADMIN_CLICK) {
											// onAdminClicked();
											// }

											if (CHILDREN_CLICK * 100 < idClicked
													&& idClicked < MONEYMANAGE_CLICK * 100) {
												showUserInfo(listChildren
														.get((idClicked
																% CHILDREN_CLICK - 1)));
											}

											if (MONEYMANAGE_CLICK * 100 < idClicked
													&& idClicked < ADMIN_CLICK * 100) {
												showUserInfo(listMoneyManager
														.get((idClicked
																% MONEYMANAGE_CLICK - 1)));
											}

											if (ADMIN_CLICK * 100 < idClicked
													&& idClicked < AGENCY_1_CLICK * 100) {
												showUserInfo(listAdmin
														.get((idClicked
																% ADMIN_CLICK - 1)));
											}

											if (AGENCY_1_CLICK * 100 < idClicked
													&& idClicked < AGENCY_2_CLICK * 100) {
												showUserInfo(listAgency1
														.get((idClicked
																% AGENCY_1_CLICK - 1)));
											}
											if (AGENCY_2_CLICK * 100 < idClicked) {
												showUserInfo(listAgency2
														.get((idClicked
																% AGENCY_2_CLICK - 1)));
											}
										}
									};

	private void showUserInfo(final User user) {
		if (isClickExpand) {
			isClickExpand = false;
			return;
		}
		Request.getInstance().getInfoDaily(user.phone, onCustomAgency);
		Loading.ins.show(CopyOfViewUserManager.this);
	}

	private void onClickedChangeRole(final User user, int roleID) {
		if (roleID == RoleID.MONEY_MANAGER) {
			final DialogCustom dia = new DialogCustom("");
			dia.text("Không thể thay đổi quyền của Money Manager");
			dia.button("Ok", new Runnable() {
				@Override
				public void run() {

				}
			});
			dia.show(getStage());
			return;
		}
		if (roleID == RoleID.ADMIN) {
			final DialogCustom dia = new DialogCustom("");
			dia.text("Không thể thay đổi quyền của Admin");
			dia.button("Ok", new Runnable() {
				@Override
				public void run() {

				}
			});
			dia.show(getStage());
			return;
		}

		ViewChangeRole viewChangeRole = new ViewChangeRole(
				new OnCompleteListener() {

					@Override
					public void onError() {
						if (getViewController().isContainView("viewchangerole"))
							getViewController().removeView("viewchangerole");
						Toast.makeText(getStage(), "Hủy thay đổi quyền!",
								Toast.LENGTH_SHORT);
					}

					@Override
					public void done() {
						if (getViewController().isContainView("viewchangerole"))
							getViewController().removeView("viewchangerole");

						getViewController().getView(
								ViewInfoDaiLySmall.class.getName()).back();
						Request.getInstance().getAllAgency(UserInfo.phone, -1,
								onAllAgencyListener);
						Toast.makeText(getStage(),
								"Thay đổi quyền thành công !",
								Toast.LENGTH_SHORT);
					}
				}, user.data, roleID);
		viewChangeRole.build(getStage(), getViewController(), "viewchangerole",
				new Rectangle(0, 0, Constants.WIDTH_SCREEN,
						Constants.HEIGHT_SCREEN - Constants.HEIGHT_ACTIONBAR));
		viewChangeRole.buildComponent();
		viewChangeRole.show(new OnCompleteListener() {

			@Override
			public void onError() {

			}

			@Override
			public void done() {
				if (!getViewController().isContainView(
						ViewInfoDaiLySmall.class.getName())) {
					getViewController().removeView(
							ViewInfoDaiLySmall.class.getName());
				}
			}
		});
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public enum ItemState {
		EXPAND, COLLAPSE;
	}

	class User {
		JsonValue	data;
		String		name;
		String		phone;
		int			roleID;

		public User(JsonValue data, int roleID, String name, String phone) {
			super();
			this.roleID = roleID;
			this.data = data;
			this.name = name;
			this.phone = phone;
		}
	}

	class ImageExpand extends Image {
		public ImageExpand(TextureRegion region) {
			super(region);
		}

		@Override
		public Actor hit(float x, float y, boolean touchable) {
			if (touchable && this.getTouchable() != Touchable.enabled)
				return null;
			return x >= -30 && x < this.getWidth() + 30 && y >= -30
					&& y < this.getHeight() + 30 ? this : null;
		}
	}

	public abstract class Item extends Group {
		Img				icon, bg;
		Label			lbText, lbTextNumber;
		OnClickListener	onClickListener;
		ImageExpand		expand;

		public Item() {
			expand = new ImageExpand(Assets.instance.getRegion("down"));
		}

		public void setWhite() {
			this.icon.setColor(Color.WHITE);
			lbText.setColor(Color.WHITE);
			lbTextNumber.setColor(Color.WHITE);
			if (expand != null)
				expand.setColor(Color.WHITE);
		}

		public void setColorful() {
			this.icon.setColor(iconItemColor);
			lbText.setColor(textItemColor);
			lbTextNumber.setColor(textItemColor);
			if (expand != null)
				expand.setColor(iconItemColor);
		}

		public void setAction(boolean isAction) {
		}

		public void setExpandListener(ClickListener listener) {
			expand.clearListeners();
			expand.addListener(listener);
		}

		public void setExpand(boolean isExpand) {
			if (isExpand) {
				expand.addAction(Actions
						.rotateTo(0, 0.2f, Interpolation.bounce));
			} else {
				expand.addAction(Actions.rotateTo(90, 0.2f,
						Interpolation.bounce));
			}
		}

		public void setExpandVisiable(boolean isEx) {
			expand.setVisible(isEx);
		}

		public abstract void click();
	}

	class ItemUser extends Item {
		int					id;
		Table				data;

		ItemState			itemUserState	= ItemState.COLLAPSE;
		Array<SubItemUser>	listSubItem;

		public ItemUser(int id, float width, float height,
				TextureRegion reg_icon, String text, String textnumber,
				final OnClickListener onClickListener, Table data) {
			super();
			// this.onClickListener = onClickListener;
			this.setSize(width, height);
			setOrigin(Align.center);
			this.id = id;
			this.icon = new Img(reg_icon);
			this.bg = new Img(new NinePatch(Assets.instance.ui.reg_ninepatch));
			bg.setTapScale(1.1f);
			bg.setColor(bgItemColor);

			this.lbText = new Label(text, Style.ins.getLabelStyle(22,
					FontType.Regular, Color.WHITE));
			this.lbTextNumber = new Label(textnumber, Style.ins.getLabelStyle(
					20, FontType.Regular, Color.WHITE));

			NinePatch np = new NinePatch(Assets.instance.ui.reg_ninepatch);
			np.setColor(dataItemColor);
			this.data = data;
			data.setTouchable(Touchable.childrenOnly);
			this.data.setBackground(new NinePatchDrawable(np));
			this.data.right().top();
			addActor(bg);
			addActor(icon);
			addActor(lbText);
			addActor(lbTextNumber);
			setUp();
			data.setVisible(false);
			setColorful();
		}

		public void setUp() {
			bg.setSize(getWidth(), getHeight());
			bg.setPosition(0, 0);
			icon.setSize(4 * getHeight() / 5, 4 * getHeight() / 5);
			icon.setPosition(getHeight() / 10, getHeight() / 10);
			icon.setColor(iconItemColor);
			bg.setColor(bgItemColor);
			lbText.setPosition(icon.getX(Align.right) + 20,
					icon.getY(Align.center) - lbText.getHeight() / 2);
			lbTextNumber.setPosition(lbText.getX(Align.right) + 20,
					icon.getY(Align.center) - lbText.getHeight() / 2);

			icon.setTouchable(Touchable.disabled);
			lbText.setTouchable(Touchable.disabled);
			lbTextNumber.setTouchable(Touchable.disabled);
			bg.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					clearActions();
					addAction(Actions.sequence(Actions
							.scaleTo(1.1f, 1.1f, .05f), Actions.scaleTo(1f, 1f,
							.1f, Interpolation.swingOut)));
				}
			});
		}

		@Override
		public void click() {
		}
	}

	class SubItemUser extends Item {

		int					id;
		Group				parent;
		ItemState			itemUserState	= ItemState.COLLAPSE;
		Array<SubItemUser>	listSubItem;
		boolean				isAction		= true;

		public SubItemUser(int id, float width, float height,
				TextureRegion reg_icon, String text, String textnumber,
				final OnClickListener onClickListener) {
			super();
			setClip(true);
			this.onClickListener = onClickListener;
			this.setTouchable(Touchable.enabled);
			this.setTransform(true);
			this.setSize(width, height);
			setOrigin(Align.center);
			this.id = id;
			this.icon = new Img(reg_icon);
			this.icon.setColor(iconItemColor);
			this.icon.setSize(0.8f * icon.getWidth(), 0.8f * icon.getHeight());
			this.bg = new Img(Assets.instance.ui.reg_ninepatch);
			bg.setColor(bgItemColor);
			this.lbText = new Label(text, Style.ins.getLabelStyle(22,
					FontType.Light, Color.WHITE));
			this.lbTextNumber = new Label(textnumber, Style.ins.getLabelStyle(
					20, FontType.Light, Color.WHITE));
			addActor(bg);
			addActor(icon);
			addActor(lbText);
			addActor(lbTextNumber);

			expand.setOrigin(Align.center);
			expand.setRotation(90);
			expand.setY(getHeight() / 2 - expand.getHeight() / 2);

			addActor(expand);
			expand.setVisible(false);
			setUp();
			setColorful();

		}

		@Override
		public void act(float delta) {
			super.act(delta);
			Vector2 pos = new Vector2(Constants.WIDTH_SCREEN
					- expand.getWidth() - 5, 0);
			this.stageToLocalCoordinates(pos);
			expand.setX(pos.x);
		}

		@Override
		public void setExpand(boolean isExpand) {
			super.setExpand(isExpand);
		}

		public void setAction(boolean isAction) {
			this.isAction = isAction;
		}

		@Override
		public void click() {
			onClickListener.onClick(id);
		}

		public void setUp() {
			bg.setSize(getWidth(), getHeight());
			bg.setPosition(0, 0);
			icon.setSize(4 * getHeight() / 5, 4 * getHeight() / 5);
			icon.setPosition(getHeight() / 10, getHeight() / 10);
			lbText.setPosition(icon.getX(Align.right) + 20,
					icon.getY(Align.center) - lbText.getHeight() / 2);
			lbTextNumber.setPosition(lbText.getX(Align.right) + 20,
					icon.getY(Align.center) - lbText.getHeight() / 2);

			icon.setTouchable(Touchable.disabled);
			lbText.setTouchable(Touchable.disabled);
			lbTextNumber.setTouchable(Touchable.disabled);
			this.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Log.d("Click to sub : " + id);
					SubItemUser.this.clearActions();
					SubItemUser.this.addAction(Actions.sequence(Actions
							.scaleTo(1f, 1f, .1f, Interpolation.swingOut),
							Actions.run(new Runnable() {
								@Override
								public void run() {
									if (onClickListener != null && isAction) {
										onClickListener.onClick(id);
									}
								}
							})));
				}

				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					SubItemUser.this.addAction(Actions.sequence(Actions
							.scaleTo(1.1f, 1.1f, .05f)));
					return super.touchDown(event, x, y, pointer, button);
				}
			});
		}
	}

	HttpResponseListener	onAllAgencyListener	= new HttpResponseListener() {

													@Override
													public void handleHttpResponse(
															HttpResponse httpResponse) {
														responseAllAgency = new JsonReader()
																.parse(httpResponse
																		.getResultAsString());

														boolean result = responseAllAgency
																.getBoolean(ExtParamsKey.RESULT);
														if (!result) {
															String data = responseAllAgency
																	.getString(ExtParamsKey.MESSAGE);
															Toast.makeText(
																	getStage(),
																	data,
																	Toast.LENGTH_SHORT);
														}

														Log.d("All Agency : "
																+ responseAllAgency
																		.toString());
														Loading.ins.hide();
													}

													@Override
													public void failed(
															Throwable t) {

													}

													@Override
													public void cancelled() {

													}

												};

	HttpResponseListener	onCustomAgency		= new HttpResponseListener() {

													@Override
													public void handleHttpResponse(
															HttpResponse httpResponse) {
														responseCustomAgency = new JsonReader()
																.parse(httpResponse
																		.getResultAsString());

														boolean result = responseCustomAgency
																.getBoolean(ExtParamsKey.RESULT);
														if (!result) {
															String data = responseCustomAgency
																	.getString(ExtParamsKey.MESSAGE);
															Toast.makeText(
																	getStage(),
																	data,
																	Toast.LENGTH_SHORT);
														}

														Loading.ins.hide();
														Log.d("All Agency : "
																+ responseCustomAgency
																		.toString());
													}

													@Override
													public void failed(
															Throwable t) {
														Loading.ins.hide();
													}

													@Override
													public void cancelled() {
														Loading.ins.hide();
													}

												};

	public class UserNode extends Node {
		public Item	item;

		public UserNode(Item item) {
			super(item);
			this.item = item;
		}

		public Item getItem() {
			return item;
		}
	}
}
