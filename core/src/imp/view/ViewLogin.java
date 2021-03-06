package imp.view;

import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Log;
import utils.factory.StringSystem;
import utils.factory.Style;
import utils.keyboard.KeyboardConfig;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.LoginCode;
import com.coder5560.game.enums.RoleID;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.DialogCustom;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewLogin extends View {

	private CustomTextField	tfName;
	private CustomTextField	tfPass;
	private JsonValue		respone;
	private JsonValue		responeInfoDaily;
	Label					btnRegister, btnActive;
	View					view;

	boolean					isHandlerResponseLogin		= false;
	boolean					isHandlerResponseGetInfo	= false;
	int						id							= -1;

	public View buildComponent() {
		Request.getInstance().requestQuitApp(AppPreference.instance.getName(),
				new HttpResponseListener() {

					@Override
					public void handleHttpResponse(HttpResponse httpResponse) {

					}

					@Override
					public void failed(Throwable t) {

					}

					@Override
					public void cancelled() {

					}
				});

		AppPreference.instance.setLogin(false, true);
		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);
		bg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				getStage().setKeyboardFocus(null);
				AbstractGameScreen.keyboard.show(false);
			}
		});

		Image titleLogin = new Image(Assets.instance.ui.getLogo());
		titleLogin.setSize(400, 40);
		titleLogin.setPosition(getWidth() / 2 - titleLogin.getWidth() / 2,
				4 * getHeight() / 5);
		addActor(titleLogin);
		titleLogin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				getStage().setKeyboardFocus(null);
				AbstractGameScreen.keyboard.show(false);
			}
		});
		TextFieldStyle tfStyleName = Style.ins.getTextFieldStyle(43,
				Assets.instance.fontFactory.getFont(25, FontType.Light));
		TextFieldStyle tfStylePass = Style.ins.getTextFieldStyle(43,
				Assets.instance.fontFactory.getFont(25, FontType.Light));

		tfName = new CustomTextField("", tfStyleName);
		tfPass = new CustomTextField("", tfStylePass);
		tfName.setMessageText("Username");
		tfPass.setMessageText("Password");
		tfPass.setPasswordCharacter('*');
		tfPass.setPasswordMode(true);
		tfName.setSize(370, 60);
		tfPass.setSize(370, 60);
		tfName.setPosition(Constants.WIDTH_SCREEN / 2 - tfName.getWidth() / 2,
				titleLogin.getY() - tfName.getHeight() - 120);
		tfName.setText(AppPreference.instance.getName());
		tfPass.setPosition(tfName.getX(), tfName.getY() - tfPass.getHeight()
				- 40);
		tfName.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tfPass.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tfName.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				registerKeyboard(tfName, "tfName", KeyboardConfig.NORMAL);
				return true;
			}
		});

		tfPass.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				registerKeyboard(tfPass, "tfPass", KeyboardConfig.PASSWORD);
				return true;
			}
		});
		Image iconUser = new Image(Assets.instance.getRegion("icon_user"));
		iconUser.setSize(15, 21);
		Image iconLock = new Image(Assets.instance.getRegion("icon khoa"));
		iconLock.setSize(15, 21);

		iconUser.setPosition(tfName.getX() + 15,
				tfName.getY() + tfName.getHeight() / 2 - iconUser.getHeight()
						/ 2);

		iconLock.setPosition(tfPass.getX() + 15,
				tfPass.getY() + tfPass.getHeight() / 2 - iconLock.getHeight()
						/ 2);

		TextButtonStyle btStyle = new TextButtonStyle();
		btStyle.up = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				Constants.COLOR_ACTIONBAR));
		Color color = new Color(Constants.COLOR_ACTIONBAR);
		color.a = 0.5f;
		btStyle.down = new NinePatchDrawable(
				new NinePatch(Style.ins.np1, color));
		btStyle.font = Assets.instance.fontFactory.getFont(20, FontType.Medium);
		btStyle.fontColor = Color.WHITE;
		TextButton btOk = new TextButton("Đăng nhập", btStyle);
		btOk.setSize(370, 55);
		btOk.setPosition(tfName.getX(), tfPass.getY() - tfPass.getHeight() - 36);
		btOk.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MainMenuView.isLoadUserData = false;
				String username = tfName.getText();
				String pass = tfPass.getText();
				if (username.equalsIgnoreCase("") || pass.equalsIgnoreCase("")) {
					Toast.makeText(getStage(),
							"Vui lòng nhập đầy đủ thông tin",
							Toast.LENGTH_SHORT);
				} else if (!Factory.validPhone(tfName.getText())) {
					Toast.makeText(
							getStage(),
							"Số điện thoại của bạn không đúng. Vui lòng nhập lại!",
							Toast.LENGTH_SHORT);
				} else {
					setTouchable(Touchable.disabled);
					Loading.ins.show(ViewLogin.this);
					String deviceName = getViewController().getGameParent()
							.getPlatformResolver().getDeviceName();
					String deviceID = getViewController().getGameParent()
							.getPlatformResolver().getDeviceID();
					Request.getInstance().login(username, pass, deviceID,
							deviceName, _loginListener);
					AbstractGameScreen.keyboard.show(false);
					AppPreference.instance.setName(username, false);
					AppPreference.instance.setPass(pass, false);
				}
			}
		});

		Label btnForgotPass = new Label("Quên mật khẩu ?", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, FontType.Light),
				Constants.COLOR_ACTIONBAR));
		btnForgotPass.setPosition(btOk.getX(), btOk.getY() - 70);
		btnForgotPass.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
			}
		});
		btnRegister = new Label("Đăng ký", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, FontType.Light),
				Constants.COLOR_ACTIONBAR)) {
			@Override
			public Actor hit(float x, float y, boolean touchable) {
				if (x < btnRegister.getWidth() + 10 && x > -10
						&& y < btnRegister.getHeight() + 10 && y > -10)
					return btnRegister;
				return null;
			}
		};
		btnRegister.setPosition(
				btOk.getX() + btOk.getWidth() - btnRegister.getWidth(),
				btnForgotPass.getY());
		btnRegister.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (getViewController().isContainView(
						StringSystem.VIEW_REGISTER)) {
					getViewController().getView(StringSystem.VIEW_REGISTER)
							.show(null);
				} else {
					ViewRegister viewRegister = new ViewRegister();
					viewRegister.build(getStage(), getViewController(),
							StringSystem.VIEW_REGISTER, new Rectangle(0, 0,
									Constants.WIDTH_SCREEN,
									Constants.HEIGHT_SCREEN));
					viewRegister.buildComponent();
					viewRegister.show(null);
				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				btnRegister.setColor(Color.GRAY);
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				btnRegister.setColor(Constants.COLOR_ACTIONBAR);
				super.touchUp(event, x, y, pointer, button);
			}
		});
		btnActive = new Label("Kích hoạt tài khoản", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, FontType.Light),
				Constants.COLOR_ACTIONBAR)) {
			@Override
			public Actor hit(float x, float y, boolean touchable) {
				if (x < btnActive.getWidth() + 10 && x > -10
						&& y < btnActive.getHeight() + 10 && y > -10)
					return btnActive;
				return null;
			}
		};
		btnActive.setPosition(btOk.getX(), btnForgotPass.getY() - 40);
		btnActive.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (getViewController().isContainView(
						StringSystem.VIEW_WAIT_ACCEPT)) {
					getViewController().getView(StringSystem.VIEW_WAIT_ACCEPT)
							.show(null);
				} else {
					ViewWaitAccept viewWaitAccept = new ViewWaitAccept();
					viewWaitAccept.build(getStage(), getViewController(),
							StringSystem.VIEW_WAIT_ACCEPT, new Rectangle(0, 0,
									Constants.WIDTH_SCREEN,
									Constants.HEIGHT_SCREEN));
					viewWaitAccept.buildComponent();
					viewWaitAccept.show(null);
				}
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				btnActive.setColor(Color.GRAY);
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				btnActive.setColor(Constants.COLOR_ACTIONBAR);
				super.touchUp(event, x, y, pointer, button);
			}
		});

		addActor(tfName);
		addActor(tfPass);
		addActor(iconUser);
		addActor(iconLock);
		addActor(btOk);
		addActor(btnRegister);
		addActor(btnActive);
		addActor(btnForgotPass);
		return this;
	}

	public void registerKeyboard(final TextField tf, String tfname, int config) {
		// AbstractGameScreen.keyboard.registerTextField(tf, tfname, config,
		// KeyboardConfig.SINGLE_LINE);
	}

	Actor	actorExit	= new Actor();

	@Override
	public void update(float deltaTime) {
		actorExit.act(deltaTime);
		if (view != null && view.getViewState() == ViewState.HIDE) {
			view.show(null);
			return;
		}

		if (respone != null) {
			// Loading.ins.hide();
			Boolean isSuccess = respone.getBoolean(ExtParamsKey.RESULT);
			String mess = respone.getString(ExtParamsKey.MESSAGE);
			if (isSuccess) {
				JsonValue per = respone.get(ExtParamsKey.PERMISSION);
				UserInfo.phone = tfName.getText();
				for (int i = 0; i < per.size; i++) {
					int index = per.getInt(i);
					UserInfo.getInstance().setPermisstion(index);
				}
				int role_id = respone.getInt(ExtParamsKey.ROLE_ID);
				UserInfo.getInstance().setRoleId(role_id);
				int id = respone.getInt(ExtParamsKey.ROLE_ID);
				AppPreference.instance.type = id;
				AppPreference.instance.flush();

				Request.getInstance().getInfoDaily(
						AppPreference.instance.getName(), onGetinfoListener);

			} else {

				Loading.ins.hide();
				setTouchable(Touchable.enabled);
				if (respone.has(ExtParamsKey.LOGIN_CODE)) {
					int code = respone.getInt(ExtParamsKey.LOGIN_CODE);
					if (code != LoginCode.DEVICE_NOT_IN_LIST_CAN_REGISTER
							.ordinal()) {
						final DialogCustom dia = new DialogCustom("");
						dia.text(mess);
						dia.button("Ok", new Runnable() {
							@Override
							public void run() {
								Loading.ins.hide();
								dia.hide();
							}
						});
						dia.show(getStage());
					} else {
						final DialogCustom dia = new DialogCustom("");
						dia.text(mess);
						dia.button("Ok", new Runnable() {
							@Override
							public void run() {
								Loading.ins.show(ViewLogin.this);
								String username = AppPreference.instance.name;
								String pass = AppPreference.instance.pass;
								String deviceID = getViewController()
										.getGameParent().getPlatformResolver()
										.getDeviceID();
								String deviceName = getViewController()
										.getGameParent().getPlatformResolver()
										.getDeviceName();
								respone = null;
								Request.getInstance().registerDevice(username,
										pass, deviceID, deviceName,
										_registerDeviceListener);
								Loading.ins.show(ViewLogin.this);
							}
						});
						dia.button("Cancel", new Runnable() {
							@Override
							public void run() {
								dia.hide();
							}
						});
						dia.show(getStage());

					}

				} else {
					final DialogCustom dia = new DialogCustom("");
					dia.text(mess);
					dia.button("Ok", new Runnable() {
						@Override
						public void run() {
							Loading.ins.show(ViewLogin.this);
							String username = AppPreference.instance.name;
							String pass = AppPreference.instance.pass;
							String deviceID = getViewController()
									.getGameParent().getPlatformResolver()
									.getDeviceID();
							String deviceName = getViewController()
									.getGameParent().getPlatformResolver()
									.getDeviceName();
							Request.getInstance().registerDevice(username,
									pass, deviceID, deviceName,
									_registerDeviceListener);
						}
					});
					dia.show(getStage());
				}

			}
			isHandlerResponseLogin = true;
			respone = null;
		}

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
				UserInfo.money = responeInfoDaily.getInt(ExtParamsKey.AMOUNT);
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
			}
			responeInfoDaily = null;
			isHandlerResponseGetInfo = true;
		}

		if (isHandlerResponseLogin && isHandlerResponseGetInfo) {
			handlerCreatView();
			isHandlerResponseGetInfo = false;
			isHandlerResponseLogin = false;
		}
	}

	private void handlerCreatView() {
		AppPreference.instance.setLogin(true, true);
		TopBarView topBarView = new TopBarView();
		topBarView.build(getStage(), getViewController(),
				StringSystem.VIEW_ACTION_BAR, new Rectangle(0,
						Constants.HEIGHT_SCREEN - Constants.HEIGHT_ACTIONBAR,
						Constants.WIDTH_SCREEN, Constants.HEIGHT_ACTIONBAR));
		topBarView.buildComponent();

		final MainMenuView mainMenu = new MainMenuView();
		mainMenu.build(getStage(), getViewController(),
				StringSystem.VIEW_MAIN_MENU, new Rectangle(0, 0,
						Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
		mainMenu.buildComponent();

		if (UserInfo.getInstance().getRoleId() == RoleID.USER_MANAGER) {
			ViewUserManager view = new ViewUserManager();
			view.build(getStage(), getViewController(), StringSystem.VIEW_HOME,
					new Rectangle(0, 0, Constants.WIDTH_SCREEN,
							Constants.HEIGHT_SCREEN
									- Constants.HEIGHT_ACTIONBAR));
			view.buildComponent();
			view.show(new OnCompleteListener() {
				@Override
				public void onError() {
				}

				@Override
				public void done() {
					mainMenu.hide(null);
					mainMenu.getUserData();
					getViewController().getView(StringSystem.VIEW_LOGIN).hide(
							null);
				}
			});
		} else {
			HomeViewV2 homeViewV2 = new HomeViewV2();
			homeViewV2.build(getStage(), getViewController(),
					StringSystem.VIEW_HOME, new Rectangle(0, 0,
							Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
									- Constants.HEIGHT_ACTIONBAR));
			homeViewV2.buildComponent();
			homeViewV2.show(

			new OnCompleteListener() {
				@Override
				public void onError() {
				}

				@Override
				public void done() {
					mainMenu.hide(null);
					mainMenu.getUserData();
					getViewController().getView(StringSystem.VIEW_LOGIN).hide(
							null);
				}
			});
		}
		Loading.ins.hide();
	}

	@Override
	public void hide(OnCompleteListener listener) {
		setVisible(false);
	}

	public void back() {
		if (Loading.ins.isLoading) {
			Loading.ins.hide();
			return;
		}

		if (view != null && view.getViewState() == ViewState.SHOW) {
			view.hide(null);
			return;
		}

		if (actorExit.getActions().size > 0) {
			Gdx.app.exit();
		} else {
			Toast.makeText(getStage(), "Nhấn thêm lần nữa để thoát !", 0.3f);
			actorExit.addAction(Actions.delay(1f));
		}
	};

	private void switchView() {
		Request.getInstance().login(
				AppPreference.instance.getName(),
				AppPreference.instance.getPass(),
				getViewController().getGameParent().getPlatformResolver()
						.getDeviceID(),
				getViewController().getGameParent().getPlatformResolver()
						.getDeviceName(), _loginListener);
	}

	HttpResponseListener	_registerDeviceListener	= new HttpResponseListener() {

														@Override
														public void handleHttpResponse(
																HttpResponse httpResponse) {
															JsonValue value = (new JsonReader())
																	.parse(httpResponse
																			.getResultAsString());
															if (value
																	.getBoolean(ExtParamsKey.RESULT)) {
																switchView();
															} else {
																Toast.makeText(
																		getStage(),
																		value.getString(ExtParamsKey.MESSAGE),
																		Toast.LENGTH_SHORT);
																Loading.ins
																		.hide();
															}
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

	HttpResponseListener	_loginListener			= new HttpResponseListener() {

														@Override
														public void handleHttpResponse(
																HttpResponse httpResponse) {
															respone = (new JsonReader())
																	.parse(httpResponse
																			.getResultAsString());
														}

														@Override
														public void failed(
																Throwable t) {
														}

														@Override
														public void cancelled() {
														}
													};

	HttpResponseListener	onGetinfoListener		= new HttpResponseListener() {

														@Override
														public void handleHttpResponse(
																HttpResponse httpResponse) {
															responeInfoDaily = (new JsonReader())
																	.parse(httpResponse
																			.getResultAsString());
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
