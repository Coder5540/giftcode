package imp.view;

import utils.elements.Img;
import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Log;
import utils.factory.StringSystem;
import utils.listener.CustomListener;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnClickListener;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.ListMenu;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.View;

public class MainMenuView extends View {
	private Image			tranBg;
	Table					content;
	public int				lastSelect			= 0;
	public ListMenu			menu;
	Table					groupLogout;
	private boolean			ignoreUpdateMove	= true;
	public static boolean	isLoadUserData		= false;
	private JsonValue		responeInfoDaily;
	public boolean			block				= false;

	public MainMenuView buildComponent() {
		Color colorBg = Constants.COLOR_ACTIONBAR;

		getViewController().getGameScreen().setGestureDetector(
				new GestureDetector(customListener));
		tranBg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
				new Color(00, 00, 00, .4f)));
		tranBg.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
				- Constants.HEIGHT_ACTIONBAR);
		tranBg.setVisible(false);
		tranBg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide(null);
			}
		});

		content = new Table();
		content.setSize(Constants.WIDTH_MAINMENU, Constants.HEIGHT_SCREEN);

		content.setPosition(-content.getWidth(), 0);
		content.setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch, colorBg)));
		addActor(tranBg);
		addActor(content);
		buildContent(content);
		return this;

	}

	void buildContent(Table content) {
		float heightLogout = 50;

		menu = new ListMenu(getViewController(), new Table(), new Rectangle(0,
				heightLogout, content.getWidth(), content.getHeight()
						- heightLogout));
		menu.setOnHomeViewClicked(onHomeViewClicked);
		menu.setOnActiveUserClicked(onActiveUserClicked);
		menu.setOnBlockUserClicked(onBlockUserClicked);
		menu.setOnUnActiveUserClicked(onUnActiveUserClicked);
		menu.setOnAvatarClicked(onAvatarClicked);
		menu.setOnAllMailClicked(onAllMailClicked);
		menu.setOnHistoryReceiveClicked(onHistoryReceiveMoney);
		menu.setOnHistorySendMoneyClicked(onHistorySendMoney);
		menu.setOnAddMoneyClicked(onAddMoneyClicked);
		menu.setOnSellGiftCode(onSellGiftCode);
		menu.setOnListGiftcodeClicked(onGiftcodeClicked);
		menu.setOnHistoryGiftcodeClicked(onHistoryGiftcodeClicked);
		menu.setOnNewCodeClicked(onNewCodeClicked);
		menu.setOnHistoryCodeClicked(onHistoryCodeClicked);

		groupLogout = new Table();
		groupLogout.setTouchable(Touchable.enabled);
		groupLogout.setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch)));
		groupLogout.setOrigin(Align.center);
		groupLogout.setSize(content.getWidth(), heightLogout);
		final Img imgLogout = new Img(Assets.instance.ui.getRegionLogout());
		imgLogout.setSize(40, 40);
		imgLogout.setPosition(30, groupLogout.getHeight() / 2, Align.center);
		imgLogout.setColor(Constants.COLOR_ACTIONBAR);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Assets.instance.fontFactory.getFont(26,
				FontType.Light);
		labelStyle.fontColor = new Color(Constants.COLOR_ACTIONBAR);

		final Label label = new Label("Log out", labelStyle);
		label.setPosition(
				imgLogout.getX() + imgLogout.getWidth() / 2 + label.getWidth()
						- 10, imgLogout.getY() + imgLogout.getHeight() / 2,
				Align.center);

		groupLogout.addActor(imgLogout);
		groupLogout.addActor(label);

		label.setTouchable(Touchable.disabled);
		imgLogout.setTouchable(Touchable.disabled);
		groupLogout.setOrigin(Align.center);
		groupLogout.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				imgLogout.clearActions();
				imgLogout.addAction(Actions.sequence(
						Actions.scaleTo(1.2f, 1.2f, .1f),
						Actions.scaleTo(1f, 1f, .1f, Interpolation.swingOut),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								if (onLogoutListener != null) {
									onLogoutListener.onClick(0, 0);
								}
							}
						})));
				label.clearActions();
				label.addAction(Actions.sequence(
						Actions.scaleTo(1.2f, 1.2f, .1f),
						Actions.scaleTo(1f, 1f, .1f, Interpolation.swingOut),
						Actions.run(new Runnable() {
							@Override
							public void run() {
							}
						})));
			}
		});

		content.addActor(menu);
		content.addActor(groupLogout);
	}

	Img getLine(Color color, float height) {
		Img img = new Img(Assets.instance.ui.reg_ninepatch);
		img.setColor(color);
		return img;
	}

	@Override
	public void show(final OnCompleteListener listener) {
		super.show(listener);
		setIgnoreUpdateMove(false);
		tranBg.setVisible(true);
		menu.updateMail();
		content.addAction(Actions.sequence(
				Actions.moveTo(0, 0, 0.5f, Interpolation.pow5Out),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						if (listener != null)
							listener.done();
						setIgnoreUpdateMove(false);
						setViewState(ViewState.SHOW);
						// if (!isLoadUserData)
						Request.getInstance().getInfoDaily(
								AppPreference.instance.getName(),
								new GetInfoDaily());
					}
				})));
	}

	public void getUserData() {
		Request.getInstance().getInfoDaily(AppPreference.instance.getName(),
				new GetInfoDaily());
	}

	@Override
	public void hide(final OnCompleteListener listener) {
		super.hide(listener);
		tranBg.setVisible(false);

		setIgnoreUpdateMove(false);
		content.addAction(Actions.sequence(Actions.moveTo(-content.getWidth(),
				0, 0.5f, Interpolation.pow5Out), Actions.run(new Runnable() {
			@Override
			public void run() {
				if (listener != null)
					listener.done();
				setIgnoreUpdateMove(false);
				setViewState(ViewState.HIDE);
			}
		})));
	}

	public void update(float delta) {
		if (responeInfoDaily != null) {
			Loading.ins.hide();
			Log.d(responeInfoDaily.toString());
			boolean result = responeInfoDaily.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				UserInfo.fullName = responeInfoDaily
						.getString(ExtParamsKey.FULL_NAME);
				UserInfo.address = responeInfoDaily
						.getString(ExtParamsKey.ADDRESS);
				UserInfo.level = responeInfoDaily
						.getString(ExtParamsKey.ROLE_NAME);
				UserInfo.phone = AppPreference.instance.getName();
				UserInfo.phoneNGT = responeInfoDaily
						.getString(ExtParamsKey.REF_CODE);
				UserInfo.money = responeInfoDaily.getLong(ExtParamsKey.AMOUNT);
				UserInfo.currency = responeInfoDaily
						.getString(ExtParamsKey.CURRENCY);
				UserInfo.email = responeInfoDaily.getString(ExtParamsKey.EMAIL);

				UserInfo.imeiDevice = Factory.getDeviceID(responeInfoDaily);
				UserInfo.nameDevice = Factory.getDeviceName(responeInfoDaily);
				UserInfo.imeiDeviceBlock = Factory
						.getDeviceIDBlock(responeInfoDaily);
				UserInfo.nameDeviceBlock = Factory
						.getDeviceNameBlock(responeInfoDaily);
				UserInfo.state = responeInfoDaily.getInt(ExtParamsKey.STATE);

				menu.setUserName(UserInfo.fullName);
				menu.setPhone(UserInfo.phone);
				menu.setMoney(Factory.getDotMoney(UserInfo.money) + " "
						+ UserInfo.currency);
				isLoadUserData = true;
			}
			responeInfoDaily = null;
		}
	};

	Actor	bar, currentView;

	@Override
	public void act(float delta) {
		super.act(delta);
		if (ignoreUpdateMove)
			return;
		if (tranBg.isVisible()) {
			float alpha = (content.getWidth() + content.getX())
					/ content.getWidth();
			if (content.getX() == 0)
				alpha = 1;
			tranBg.setColor(tranBg.getColor().r, tranBg.getColor().g,
					tranBg.getColor().b, alpha);
		}
		if (getViewController() != null) {
			bar = (Actor) (getViewController()
					.getView(StringSystem.VIEW_ACTION_BAR));
			currentView = (Actor) (getViewController()
					.getView(TraceView.instance.getLastView()));

			if (bar != null)
				bar.setPosition(content.getX() + content.getWidth(), bar.getY());
			if (currentView != null)
				currentView.setPosition(content.getX() + content.getWidth(),
						currentView.getY());
		}
	}

	@Override
	public void destroyComponent() {
	}

	@Override
	public void back() {
		hide(null);
	}

	boolean					canPan						= false;
	CustomListener			customListener				= new CustomListener() {
															public boolean touchDown(
																	float x,
																	float y,
																	int pointer,
																	int button) {
																if (content
																		.getX() == -content
																		.getWidth()
																		&& x < 40
																		&& !canPan) {
																	canPan = true;
																	tranBg.setVisible(true);
																}
																return false;
															};

															public boolean pan(
																	float x,
																	float y,
																	float deltaX,
																	float deltaY) {
																if (canPan) {
																	setIgnoreUpdateMove(false);
																	tranBg.setVisible(true);
																	content.setPosition(
																			MathUtils
																					.clamp(content
																							.getX()
																							+ deltaX,
																							-content.getWidth(),
																							0),
																			content.getY());
																	float alpha = (content
																			.getWidth() - content
																			.getX())
																			/ content
																					.getWidth();
																	if (content
																			.getX() == 0)
																		alpha = 1;
																	tranBg.setColor(
																			tranBg.getColor().r,
																			tranBg.getColor().g,
																			tranBg.getColor().b,
																			alpha);
																	return true;
																}
																return false;
															};

															public boolean panStop(
																	float x,
																	float y,
																	int pointer,
																	int button) {
																if (canPan) {
																	float position = content
																			.getX()
																			+ content
																					.getWidth();
																	if (position <= content
																			.getWidth() / 2)
																		hide(null);
																	if (position > content
																			.getWidth() / 2)
																		show(null);
																	canPan = false;
																	return true;
																}
																canPan = false;
																return false;
															};

														};

	public OnClickListener	onActiveUserClicked			= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_ADMIN_ACTIVE)) {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_ADMIN_ACTIVE)
																							.show(null);
																				} else {
																					ViewAdminActive viewAdminActive = new ViewAdminActive();
																					viewAdminActive
																							.build(getStage(),
																									getViewController(),
																									StringSystem.VIEW_ADMIN_ACTIVE,
																									new Rectangle(
																											0,
																											0,
																											Constants.WIDTH_SCREEN,
																											Constants.HEIGHT_SCREEN
																													- Constants.HEIGHT_ACTIONBAR));
																					viewAdminActive
																							.buildComponent();
																					viewAdminActive
																							.show(null);
																				}
																			}
																		});
															}
														};

	public OnClickListener	onUnActiveUserClicked		= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_ADMIN_REQUEST)) {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_ADMIN_REQUEST)
																							.show(null);
																				} else {
																					ViewAdminRequest viewAdminRequest = new ViewAdminRequest();
																					viewAdminRequest
																							.build(getStage(),
																									getViewController(),
																									StringSystem.VIEW_ADMIN_REQUEST,
																									new Rectangle(
																											0,
																											0,
																											Constants.WIDTH_SCREEN,
																											Constants.HEIGHT_SCREEN
																													- Constants.HEIGHT_ACTIONBAR));
																					viewAdminRequest
																							.buildComponent();
																					viewAdminRequest
																							.show(null);
																				}
																			}
																		});

															}
														};

	public OnClickListener	onBlockUserClicked			= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_ADMIN_BLOCK)) {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_ADMIN_BLOCK)
																							.show(null);
																				} else {
																					ViewAdminLock viewAdminLock = new ViewAdminLock();
																					viewAdminLock
																							.build(getStage(),
																									getViewController(),
																									StringSystem.VIEW_ADMIN_BLOCK,
																									new Rectangle(
																											0,
																											0,
																											Constants.WIDTH_SCREEN,
																											Constants.HEIGHT_SCREEN
																													- Constants.HEIGHT_ACTIONBAR));
																					viewAdminLock
																							.buildComponent();
																					viewAdminLock
																							.show(null);
																				}
																			}
																		});
															}
														};

	public OnClickListener	onAvatarClicked				= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());

																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				if (!getViewController()
																						.isContainView(
																								StringSystem.VIEW_HOST_INFO)) {
																					ViewInfoDaiLy viewInfoDaiLy = new ViewInfoDaiLy();
																					viewInfoDaiLy
																							.build(getStage(),
																									getViewController(),
																									StringSystem.VIEW_HOST_INFO,
																									new Rectangle(
																											0,
																											0,
																											Constants.WIDTH_SCREEN,
																											Constants.HEIGHT_SCREEN
																													- Constants.HEIGHT_ACTIONBAR));

																					viewInfoDaiLy
																							.buildComponent();
																					viewInfoDaiLy
																							.show(null);
																				} else {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_HOST_INFO)
																							.show(null);
																				}
																			}
																		});
															}
														};

	public OnClickListener	onAllMailClicked			= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_MAIL)) {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_MAIL)
																							.show(null);
																				} else {
																					ViewMails viewMail = new ViewMails();
																					viewMail.build(
																							getStage(),
																							getViewController(),
																							StringSystem.VIEW_MAIL,
																							new Rectangle(
																									0,
																									0,
																									Constants.WIDTH_SCREEN,
																									Constants.HEIGHT_SCREEN
																											- Constants.HEIGHT_ACTIONBAR));
																					viewMail.buildComponent();
																					viewMail.show(null);
																				}

																			}
																		});
															}
														};

	public OnClickListener	onHistorySendMoney			= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());

																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_LOG_SEND_MONEY_CHART)) {
																				} else {
																					ViewLogChart viewLog = new ViewLogChart();
																					viewLog.build(
																							getStage(),
																							getViewController(),
																							StringSystem.VIEW_LOG_SEND_MONEY_CHART,
																							new Rectangle(
																									0,
																									0,
																									Constants.WIDTH_SCREEN,
																									Constants.HEIGHT_SCREEN
																											- Constants.HEIGHT_ACTIONBAR));
																					viewLog.buildComponent(ViewLogChart.TYPE_SEND_MONEY);
																				}
																				((ViewLogChart) getViewController()
																						.getView(
																								StringSystem.VIEW_LOG_SEND_MONEY_CHART))
																						.setTotalMoney(((HomeViewV2) getViewController()
																								.getView(
																										StringSystem.VIEW_HOME)).tongtiendacap);
																				((ViewLogChart) getViewController()
																						.getView(
																								StringSystem.VIEW_LOG_SEND_MONEY_CHART))
																						.show(null);
																			}
																		});
															}
														};

	public OnClickListener	onHistoryReceiveMoney		= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());

																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART)) {
																				} else {
																					ViewLogChart viewLog = new ViewLogChart();
																					viewLog.build(
																							getStage(),
																							getViewController(),
																							StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART,
																							new Rectangle(
																									0,
																									0,
																									Constants.WIDTH_SCREEN,
																									Constants.HEIGHT_SCREEN
																											- Constants.HEIGHT_ACTIONBAR));
																					viewLog.buildComponent(ViewLogChart.TYPE_RECEIVE_MONEY);
																				}
																				((ViewLogChart) getViewController()
																						.getView(
																								StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART))
																						.setTotalMoney(((HomeViewV2) getViewController()
																								.getView(
																										StringSystem.VIEW_HOME)).tongtiendanhan);
																				((ViewLogChart) getViewController()
																						.getView(
																								StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART))
																						.show(null);
																			}
																		});
															}
														};

	public OnClickListener	onAddMoneyClicked			= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																final ViewCapTienDaiLy viewAddMoney = new ViewCapTienDaiLy();
																viewAddMoney
																		.build(getStage(),
																				getViewController(),
																				StringSystem.VIEW_ADD_MONEY,
																				new Rectangle(
																						0,
																						0,
																						Constants.WIDTH_SCREEN,
																						Constants.HEIGHT_SCREEN
																								- Constants.HEIGHT_ACTIONBAR));
																viewAddMoney
																		.buildComponent();
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				viewAddMoney
																						.show(null);
																			}
																		});

															}
														};

	public OnClickListener	onLogoutListener			= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				getViewController()
																						.getView(
																								StringSystem.VIEW_LOGIN)
																						.show(new OnCompleteListener() {

																							@Override
																							public void onError() {

																							}

																							@Override
																							public void done() {
																								AppPreference.instance
																										.setLogin(
																												false,
																												true);
																								Request.getInstance()
																										.requestQuitApp(
																												UserInfo.phone,
																												new HttpResponseListener() {

																													@Override
																													public void handleHttpResponse(
																															HttpResponse httpResponse) {

																													}

																													@Override
																													public void failed(
																															Throwable t) {

																													}

																													@Override
																													public void cancelled() {

																													}
																												});
																								getViewController()
																										.resetAll();
																							}
																						});
																			}
																		});
															}
														};

	public OnClickListener	onSellGiftCode				= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {

																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_SELL_GIFT_CODE)) {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_SELL_GIFT_CODE)
																							.show(null);
																				} else {
																					ViewSellGiftCode viewSell = new ViewSellGiftCode();
																					viewSell.build(
																							getStage(),
																							getViewController(),
																							StringSystem.VIEW_SELL_GIFT_CODE,
																							new Rectangle(
																									0,
																									0,
																									Constants.WIDTH_SCREEN,
																									Constants.HEIGHT_SCREEN
																											- Constants.HEIGHT_ACTIONBAR));
																					viewSell.buildComponent();
																					viewSell.show(null);
																				}
																			}
																		});
															}
														};
	public OnClickListener	onGiftcodeClicked			= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());

																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				Log.d("Click to use");
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_GIFTCODE)) {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_GIFTCODE)
																							.show(null);
																				} else {
																					ViewGiftCode viewGiftcode = new ViewGiftCode();
																					viewGiftcode
																							.build(getStage(),
																									getViewController(),
																									StringSystem.VIEW_GIFTCODE,
																									new Rectangle(
																											0,
																											0,
																											Constants.WIDTH_SCREEN,
																											Constants.HEIGHT_SCREEN
																													- Constants.HEIGHT_ACTIONBAR));
																					viewGiftcode
																							.buildComponent();
																					viewGiftcode
																							.show(null);
																				}
																			}
																		});
															}
														};
	public OnClickListener	onHistoryGiftcodeClicked	= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());

																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_LOG_GIFTCODE_CHART)) {
																				} else {
																					ViewLogChart viewLog = new ViewLogChart();
																					viewLog.build(
																							getStage(),
																							getViewController(),
																							StringSystem.VIEW_LOG_GIFTCODE_CHART,
																							new Rectangle(
																									0,
																									0,
																									Constants.WIDTH_SCREEN,
																									Constants.HEIGHT_SCREEN
																											- Constants.HEIGHT_ACTIONBAR));
																					viewLog.buildComponent(ViewLogChart.TYPE_GIFTCODE);
																				}
																				((ViewLogChart) getViewController()
																						.getView(
																								StringSystem.VIEW_LOG_GIFTCODE_CHART))
																						.setTotalMoney(((HomeViewV2) getViewController()
																								.getView(
																										StringSystem.VIEW_HOME)).tongtiengiftcode);
																				((ViewLogChart) getViewController()
																						.getView(
																								StringSystem.VIEW_LOG_GIFTCODE_CHART))
																						.show(null);
																			}
																		});
															}
														};
	public OnClickListener	onHomeViewClicked			= new OnClickListener() {

															@Override
															public void onClick(
																	float x,
																	float y) {
																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				getViewController()
																						.resetHome();
																			}
																		});
															}
														};

	OnClickListener			onNewCodeClicked			= new OnClickListener() {
															@Override
															public void onClick(
																	float x,
																	float y) {

																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_NEW_CODE)) {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_NEW_CODE)
																							.show(null);
																				} else {
																					ViewNewCode viewNewCode = new ViewNewCode();
																					viewNewCode
																							.build(getStage(),
																									getViewController(),
																									StringSystem.VIEW_NEW_CODE,
																									new Rectangle(
																											0,
																											0,
																											Constants.WIDTH_SCREEN,
																											Constants.HEIGHT_SCREEN
																													- Constants.HEIGHT_ACTIONBAR));
																					viewNewCode
																							.buildComponent();
																					viewNewCode
																							.show(null);
																				}
																			}
																		});
															}
														};
	OnClickListener			onHistoryCodeClicked		= new OnClickListener() {
															@Override
															public void onClick(
																	float x,
																	float y) {

																Loading.ins
																		.show((Group) getViewController()
																				.getCurrentView());
																getViewController()
																		.getView(
																				StringSystem.VIEW_MAIN_MENU)
																		.hide(new OnCompleteListener() {

																			@Override
																			public void onError() {

																			}

																			@Override
																			public void done() {
																				Loading.ins
																						.hide();
																				if (getViewController()
																						.isContainView(
																								StringSystem.VIEW_HISTORY_CODE)) {
																					getViewController()
																							.getView(
																									StringSystem.VIEW_HISTORY_CODE)
																							.show(null);
																				} else {
																					ViewHistoryCode viewHistoryCode = new ViewHistoryCode();
																					viewHistoryCode
																							.build(getStage(),
																									getViewController(),
																									StringSystem.VIEW_HISTORY_CODE,
																									new Rectangle(
																											0,
																											0,
																											Constants.WIDTH_SCREEN,
																											Constants.HEIGHT_SCREEN
																													- Constants.HEIGHT_ACTIONBAR));
																					viewHistoryCode
																							.buildComponent();
																					viewHistoryCode
																							.show(null);
																				}
																			}
																		});
															}
														};

	class GetInfoDaily implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeInfoDaily = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

	public boolean isIgnoreUpdateMove() {
		return ignoreUpdateMove;
	}

	public void setIgnoreUpdateMove(boolean ignoreUpdateMove) {
		this.ignoreUpdateMove = ignoreUpdateMove;
	}

	public void setNotify(int number_unseen) {
		menu.setNotify(number_unseen);
	}

}
