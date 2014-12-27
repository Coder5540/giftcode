package imp.view;

import org.w3c.dom.UserDataHandler;

import utils.elements.Img;
import utils.factory.FontFactory.fontType;
import utils.factory.Log;
import utils.factory.StringSystem;
import utils.factory.Style;
import utils.listener.OnClickListener;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.RoleID;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.IViewController;
import com.coder5560.game.views.View;

public class ViewUserManager extends View {

	public static final int	ADMIN_CLICK			= 100;
	public static final int	AGENCY_1_CLICK		= 101;
	public static final int	AGENCY_2_CLICK		= 102;
	public static final int	MONEYMANAGE_CLICK	= 99;

	Table					root;
	ScrollPane				scroll;
	JsonValue				responseAllAgency;
	JsonValue				responseCustomAgency;

	Array<User>				listAdmin			= new Array<ViewUserManager.User>();
	Array<User>				listMoneyManager	= new Array<ViewUserManager.User>();
	Array<User>				listAgency1			= new Array<ViewUserManager.User>();
	Array<User>				listAgency2			= new Array<ViewUserManager.User>();

	public ViewUserManager() {
		super();

	}

	@Override
	public String getLabel() {
		return super.getLabel();
	}

	@Override
	public void build(Stage stage, IViewController viewController,
			String viewName, Rectangle bound) {
		super.build(stage, viewController, viewName, bound);
		((MainMenuView) getViewController()
				.getView(StringSystem.VIEW_MAIN_MENU)).setBlock(true);
	}

	public void buildComponent() {
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch, Color.GRAY)));
		buildRoot();
		rebuildUI();
		Log.d("Phone : " + UserInfo.phone);
		Request.getInstance().getAllAgency(UserInfo.phone, -1,
				onAllAgencyListener);
	}

	private void buildRoot() {
		root = new Table();
		root.setSize(getWidth(), getHeight());
		root.top();
		root.defaults().expandX().fillX().height(60).align(Align.center);
		scroll = new ScrollPane(root);
		scroll.setScrollingDisabled(true, false);
		add(scroll).expand().fill().top();

		ItemUser itemMoneyManager = new ItemUser(0, 400, 60,
				Assets.instance.ui.getRegUsermanagement(), "MoneyManager", " ("
						+ 1 + ")", onClickListener, new Table());
		addItemUser(root, itemMoneyManager);

		ItemUser itemUserAdmin = new ItemUser(ADMIN_CLICK, 400, 60,
				Assets.instance.ui.getRegUsermanagement(), "Admin", " (" + 1
						+ ")", onClickListener, new Table());
		addItemUser(root, itemUserAdmin);

		ItemUser itemUserLevel1 = new ItemUser(2, 400, 60,
				Assets.instance.ui.getRegUsermanagement(), "Đại lý cấp 1", " ("
						+ 3 + ")", onClickListener, new Table());
		addItemUser(root, itemUserLevel1);

		for (int i = 0; i < 10; i++) {
			SubItemUser subItemUser = new SubItemUser(itemUserLevel1, 1,
					itemUserLevel1.getWidth() - 40, itemUserLevel1.getHeight(),
					Assets.instance.ui.getRegUsermanagement(), "AAAA", "BBB",
					onClickListener);
			itemUserLevel1.addSubItem(subItemUser);
		}

		ItemUser itemUserLevel2 = new ItemUser(0, 400, 60,
				Assets.instance.ui.getRegUsermanagement(), "Đại lý cấp 2", " ("
						+ 3 + ")", onClickListener, new Table());
		addItemUser(root, itemUserLevel2);

	}

	public void rebuildUI() {
		root.clear();

		ItemUser itemMoneyManager = new ItemUser(MONEYMANAGE_CLICK, 400, 60,
				Assets.instance.ui.getRegUsermanagement(), "MoneyManager", " ("
						+ listMoneyManager.size + ")", onClickListener, new Table());
		addItemUser(root, itemMoneyManager);
		for (int i = 0; i < listMoneyManager.size; i++) {
			SubItemUser subItemUser = new SubItemUser(itemMoneyManager,
					MONEYMANAGE_CLICK * 100 + i + 1,
					itemMoneyManager.getWidth() - 40,
					itemMoneyManager.getHeight(),
					Assets.instance.ui.getRegUsermanagement(),
					listMoneyManager.get(i).name, "("
							+ listMoneyManager.get(i).phone + ")",
					onClickListener);
			itemMoneyManager.addSubItem(subItemUser);
		}

		ItemUser itemAdmin = new ItemUser(MONEYMANAGE_CLICK, 400, 60,
				Assets.instance.ui.getRegUsermanagement(), "Admin", " ("
						+ listAdmin.size + ")", onClickListener, new Table());
		addItemUser(root, itemAdmin);
		for (int i = 0; i < listAdmin.size; i++) {
			SubItemUser subItemUser = new SubItemUser(itemAdmin, ADMIN_CLICK
					* 100 + (i + 1), itemAdmin.getWidth() - 40,
					itemAdmin.getHeight(),
					Assets.instance.ui.getRegUsermanagement(),
					listAdmin.get(i).name, "(" + listAdmin.get(i).phone + ")",
					onClickListener);
			itemAdmin.addSubItem(subItemUser);
		}

		ItemUser itemAgency1 = new ItemUser(AGENCY_1_CLICK, 400, 60,
				Assets.instance.ui.getRegUsermanagement(), "Đại lý cấp 1", " ("
						+ listAgency1.size + ")", onClickListener, new Table());
		addItemUser(root, itemAgency1);
		for (int i = 0; i < listAgency1.size; i++) {
			SubItemUser subItemUser = new SubItemUser(itemAgency1,
					AGENCY_1_CLICK * 100 + i + 1, itemAgency1.getWidth() - 40,
					itemAgency1.getHeight(),
					Assets.instance.ui.getRegUsermanagement(),
					listAgency1.get(i).name, "(" + listAgency1.get(i).phone
							+ ")", onClickListener);
			itemAgency1.addSubItem(subItemUser);
		}

		ItemUser itemAgency2 = new ItemUser(AGENCY_2_CLICK, 400, 60,
				Assets.instance.ui.getRegUsermanagement(), "Đại lý cấp 2", " ("
						+ listAgency2.size + ")", onClickListener, new Table());
		addItemUser(root, itemAgency2);
		for (int i = 0; i < listAgency2.size; i++) {
			SubItemUser subItemUser = new SubItemUser(itemAgency2,
					AGENCY_2_CLICK * 100 + i + 1, itemAgency2.getWidth() - 40,
					itemAgency2.getHeight(),
					Assets.instance.ui.getRegUsermanagement(),
					listAgency2.get(i).name, "(" + listAgency2.get(i).phone
							+ ")", onClickListener);
			itemAgency2.addSubItem(subItemUser);
		}

	}

	public void addItemUser(Table rootTable, ItemUser itemUser) {
		rootTable.add(itemUser).pad(4).row();
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

	private void processResponseAll(JsonValue response) {
		JsonValue list = response.get(ExtParamsKey.LIST);
		listAdmin.clear();
		listAgency1.clear();
		listAgency2.clear();
		listMoneyManager.clear();
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
				listAgency2.add(user);
			}
		}
		rebuildUI();
	}

	private void processResponseCustom() {

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

	OnClickListener	onClickListener	= new OnClickListener() {

										@Override
										public void onClick(int idClicked) {
											Log.d("ID Click" + idClicked);
											// if (idClicked == ADMIN_CLICK) {
											// onAdminClicked();
											// }

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
		if (!getViewController().isContainView(
				ViewInfoDaiLySmall.class.getName())) {
			ViewInfoDaiLySmall view = new ViewInfoDaiLySmall();
			view.build(getStage(), getViewController(),
					ViewInfoDaiLySmall.class.getName(), new Rectangle(0, 0,
							Constants.WIDTH_SCREEN - 30,
							Constants.HEIGHT_SCREEN
									- Constants.HEIGHT_ACTIONBAR * 3));
			view.buildComponent();
		}
		int roleID = user.roleID;
		ViewInfoDaiLySmall view = ((ViewInfoDaiLySmall) getViewController()
				.getView(ViewInfoDaiLySmall.class.getName()));
		view.buildComponent();
		view.show(user.data);
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
					homeViewV2.show(null);
				} else {
					HomeViewV2 homeViewV2 = (HomeViewV2) getViewController()
							.getView(StringSystem.VIEW_HOME_V2);
					homeViewV2.setUserName(user.phone);
					homeViewV2.show(null);
				}
			}
		});
		view.addButton("Cấp quyền", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ViewChangeRole viewChangeRole = new ViewChangeRole(
						new OnCompleteListener() {

							@Override
							public void onError() {
								if (getViewController().isContainView(
										"viewchangerole"))
									getViewController().removeView(
											"viewchangerole");
								Toast.makeText(getStage(),
										"Hủy thay đổi quyền!",
										Toast.LENGTH_SHORT);
							}

							@Override
							public void done() {
								if (getViewController().isContainView(
										"viewchangerole"))
//									getViewController().getView(
//											"viewchangerole").setViewState(
//											ViewState.DISPOSE);
									getViewController().removeView(
											"viewchangerole");
									

								getViewController().getView(
										ViewInfoDaiLySmall.class.getName())
										.back();
								Request.getInstance()
										.getAllAgency(UserInfo.phone, -1,
												onAllAgencyListener);
								Toast.makeText(getStage(),
										"Thay đổi quyền thành công !",
										Toast.LENGTH_SHORT);
							}
						}, user.data);
				viewChangeRole.build(getStage(), getViewController(),
						"viewchangerole", new Rectangle(0, 0,
								Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
										- Constants.HEIGHT_ACTIONBAR));
				viewChangeRole.buildComponent();
				viewChangeRole.show(null);
			}
		});
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public enum ItemUserState {
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

	class ItemUser extends Group {

		int					id;
		Img					icon, bg;
		Label				lbText, lbTextNumber;
		OnClickListener		onClickListener;
		Table				data;

		ItemUserState		itemUserState	= ItemUserState.COLLAPSE;
		Array<SubItemUser>	listSubItem;

		public ItemUser(int id, float width, float height,
				TextureRegion reg_icon, String text, String textnumber,
				final OnClickListener onClickListener, Table data) {
			super();
			this.onClickListener = onClickListener;
			this.setSize(width, height);
			setOrigin(Align.center);
			this.id = id;
			this.icon = new Img(reg_icon);
			this.bg = new Img(Style.ins.np1);
			bg.setColor(Color.BLACK);
			this.lbText = new Label(text, Style.ins.getLabelStyle(22,
					fontType.Bold, Color.WHITE));
			this.lbTextNumber = new Label(textnumber, Style.ins.getLabelStyle(
					20, fontType.Regular, Color.WHITE));

			NinePatch np = Style.ins.np4;
			np.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
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
		}

		public void addSubItem(SubItemUser subItemUser) {
			subItemUser.toFront();
			data.add(subItemUser).width(subItemUser.getWidth())
					.height(subItemUser.getHeight()).padTop(5).row();
			if (listSubItem == null)
				listSubItem = new Array<ViewUserManager.SubItemUser>();
			listSubItem.add(subItemUser);

			itemUserState = ItemUserState.COLLAPSE;
			data.setVisible(false);
			root.getCell(data).height(0);
			root.invalidateHierarchy();
			scroll.invalidate();
			ViewUserManager.this.layout();

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
			bg.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					clearActions();
					addAction(Actions.sequence(Actions
							.scaleTo(1.1f, 1.1f, .05f), Actions.scaleTo(1f, 1f,
							.1f, Interpolation.swingOut), Actions
							.run(new Runnable() {
								@Override
								public void run() {
									Log.d("Click to : " + id);
									if (onClickListener != null) {
										onClickListener.onClick(id);
									}
									if (itemUserState == ItemUserState.EXPAND) {
										itemUserState = ItemUserState.COLLAPSE;
										data.setVisible(false);
										root.getCell(data).height(0);
										root.invalidateHierarchy();
										scroll.invalidate();
										ViewUserManager.this.layout();
									} else {
										itemUserState = ItemUserState.EXPAND;
										if (listSubItem != null) {
											data.setVisible(true);
											data.setHeight(70 * (listSubItem.size + 1) - 5);
											root.getCell(data)
													.height(70 * (listSubItem.size + 1) - 5);
											root.invalidateHierarchy();
											scroll.invalidate();
											ViewUserManager.this.layout();
										}
									}
								}
							})));
				}
			});
		}
	}

	class SubItemUser extends Table {
		int					id;
		Img					icon, bg;
		Label				lbText, lbTextNumber;
		OnClickListener		onClickListener;
		Group				parent;
		ItemUserState		itemUserState	= ItemUserState.COLLAPSE;
		Array<SubItemUser>	listSubItem;

		public SubItemUser(Group parent, int id, float width, float height,
				TextureRegion reg_icon, String text, String textnumber,
				final OnClickListener onClickListener) {
			super();
			setClip(true);
			this.onClickListener = onClickListener;
			this.setTouchable(Touchable.enabled);
			this.setTransform(true);
			this.parent = parent;
			this.setSize(width, height);
			setOrigin(Align.center);
			this.id = id;
			this.icon = new Img(reg_icon);
			this.bg = new Img(Style.ins.np1);
			bg.setColor(Color.BLACK);
			this.lbText = new Label(text, Style.ins.getLabelStyle(22,
					fontType.Bold, Color.WHITE));
			this.lbTextNumber = new Label(textnumber, Style.ins.getLabelStyle(
					20, fontType.Regular, Color.WHITE));

			addActor(bg);
			addActor(icon);
			addActor(lbText);
			addActor(lbTextNumber);
			setUp();
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
							.scaleTo(1.1f, 1.1f, .05f), Actions.scaleTo(1f, 1f,
							.1f, Interpolation.swingOut), Actions
							.run(new Runnable() {
								@Override
								public void run() {
									if (onClickListener != null) {
										onClickListener.onClick(id);
									}
								}
							})));
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
													}

													@Override
													public void failed(
															Throwable t) {

													}

													@Override
													public void cancelled() {

													}

												};

}
