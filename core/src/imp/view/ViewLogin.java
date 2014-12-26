package imp.view;

import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.fontType;
import utils.factory.Log;
import utils.factory.StringSystem;
import utils.factory.Style;
import utils.keyboard.KeyboardConfig;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.Toast;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.listener.OnResponseListener;
import com.coder5560.game.ui.CustomDialog;
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewLogin extends View {

	private CustomTextField	tfName;
	private CustomTextField	tfPass;
	private JsonValue		respone;
	Label					btnRegister, btnActive;
	CustomDialog			dialogConfirm;

	public View buildComponent() {
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

		Image titleLogin = new Image(Assets.instance.getRegion("8B8 GIFT CODE"));
		titleLogin.setSize(400, 40);
		titleLogin.setPosition(getWidth() / 2 - titleLogin.getWidth() / 2,
				4* getHeight()/5 );
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
				Assets.instance.fontFactory.getFont(25, fontType.Light));
		TextFieldStyle tfStylePass = Style.ins.getTextFieldStyle(43,
				Assets.instance.fontFactory.getFont(25, fontType.Light));

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
				new Color(0, 191 / 255f, 1, 1)));
		btStyle.down = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				new Color(0, 191 / 255f, 1, 0.5f)));
		btStyle.font = Assets.instance.fontFactory.getFont(20, fontType.Medium);
		btStyle.fontColor = Color.WHITE;
		TextButton btOk = new TextButton("Đăng nhập", btStyle);
		btOk.setSize(370, 55);
		btOk.setPosition(tfName.getX(), tfPass.getY() - tfPass.getHeight() - 20);
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
				Assets.instance.fontFactory.getFont(20, fontType.Light),
				new Color(0, 191 / 255f, 1, 1)));
		btnForgotPass.setPosition(btOk.getX(), btOk.getY() - 70);
		btnForgotPass.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
			}
		});
		btnRegister = new Label("Đăng ký", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, fontType.Light),
				new Color(0, 191 / 255f, 1, 1))) {
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
				btnRegister.setColor(new Color(0, 191 / 255f, 1, 1));
				super.touchUp(event, x, y, pointer, button);
			}
		});
		btnActive = new Label("Kích hoạt tài khoản", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, fontType.Light),
				new Color(0, 191 / 255f, 1, 1))) {
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
				btnActive.setColor(new Color(0, 191 / 255f, 1, 1));
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

		//

		return this;
	}

	public void registerKeyboard(final TextField tf, String tfname, int config) {
		AbstractGameScreen.keyboard.registerTextField(tf, tfname, config,
				KeyboardConfig.SINGLE_LINE);
	}

	@Override
	public void update(float deltaTime) {
		if (respone != null) {
			Loading.ins.hide();
			Boolean isSuccess = respone.getBoolean(ExtParamsKey.RESULT);
			String mess = respone.getString(ExtParamsKey.MESSAGE);
			Toast.makeText(getStage(), mess, Toast.LENGTH_SHORT);
			if (isSuccess) {
				JsonValue per = respone.get(ExtParamsKey.PERMISSION);
				for (int i = 0; i < per.size; i++) {
					int index = per.getInt(i);
					UserInfo.getInstance().setPermisstion(index);
				}
				int role_id = respone.getInt(ExtParamsKey.ROLE_ID);
				UserInfo.getInstance().setRoleId(role_id);
				HomeView homeView = new HomeView();
				homeView.build(getStage(), getViewController(),
						StringSystem.VIEW_HOME, new Rectangle(0, 0,
								Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
										- Constants.HEIGHT_ACTIONBAR));
				homeView.buildComponent();
				TopBarView topBarView = new TopBarView();
				topBarView.build(getStage(), getViewController(),
						StringSystem.VIEW_ACTION_BAR, new Rectangle(0,
								Constants.HEIGHT_SCREEN
										- Constants.HEIGHT_ACTIONBAR,
								Constants.WIDTH_SCREEN,
								Constants.HEIGHT_ACTIONBAR));
				topBarView.buildComponent();

				final MainMenuView mainMenu = new MainMenuView();
				mainMenu.build(getStage(), getViewController(),
						StringSystem.VIEW_MAIN_MENU,
						new Rectangle(0, 0, Constants.WIDTH_SCREEN,
								Constants.HEIGHT_SCREEN));
				mainMenu.buildComponent();
				int id = respone.getInt(ExtParamsKey.ROLE_ID);
				AppPreference.instance.type = id;
				AppPreference.instance.flush();
				homeView.show(new OnCompleteListener() {
					@Override
					public void onError() {
					}

					@Override
					public void done() {
						mainMenu.hide(null);
						getViewController().getView(StringSystem.VIEW_LOGIN)
								.hide(null);
					}
				});

			} else {
				// login success but the device is not active. show dialog
				// confirm
				setTouchable(Touchable.enabled);
				dialogConfirm = new CustomDialog();
				dialogConfirm.build(getStage(), getViewController(), "dialog",
						new Rectangle(0, 0, Constants.WIDTH_SCREEN,
								Constants.HEIGHT_SCREEN));
				dialogConfirm.buildComponent("dialog", new Rectangle(0, 0,
						Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
				dialogConfirm.setContent(mess);
				dialogConfirm.show(null);
				dialogConfirm.setOnResponseListener(new OnResponseListener() {

					@Override
					public void onOk(String name, String quality) {

					}

					@Override
					public void onOk() {
						Log.d("onOk");
						String username = AppPreference.instance.name;
						String pass = AppPreference.instance.pass;
						String deviceID = getViewController().getGameParent()
								.getPlatformResolver().getDeviceID();
						String deviceName = getViewController().getGameParent()
								.getPlatformResolver().getDeviceName();
						Request.getInstance().registerDevice(username, pass,
								deviceID, deviceName, _registerDeviceListener);
					}

					@Override
					public void onCancel() {
						Log.d("onCancel");
					}
				});
			}
			respone = null;
		}
	}

	@Override
	public void hide(OnCompleteListener listener) {
		setVisible(false);
	}

	public void back() {
		Log.d("Call back on Login View");
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
																	.getBoolean(ExtParamsKey.RESULT))
																switchView();
														}

														@Override
														public void failed(
																Throwable t) {

														}

														@Override
														public void cancelled() {

														}
													};

	HttpResponseListener	_loginListener			= new HttpResponseListener() {

														@Override
														public void handleHttpResponse(
																HttpResponse httpResponse) {
															respone = (new JsonReader())
																	.parse(httpResponse
																			.getResultAsString());
															Log.d("OnLogin result",
																	respone.toString());
														}

														@Override
														public void failed(
																Throwable t) {
															Log.d("On Login Fail :",
																	t.getMessage());
														}

														@Override
														public void cancelled() {
															Log.d("On Login Cancel :",
																	"Login is cancel");
														}
													};

}
