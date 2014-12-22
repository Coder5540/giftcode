package imp.view;

import utils.factory.AppPreference;
import utils.factory.FontFactory.fontType;
import utils.factory.Factory;
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
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewLogin extends View {

	private CustomTextField	tfName;
	private CustomTextField	tfPass;
	private JsonValue		respone, responeInfoDaily;
	Label					btnRegister, btnActive;

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
				getHeight() - titleLogin.getHeight() - 180);
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
				440);
		tfName.setText(AppPreference.instance.getName());
		tfPass.setPosition(tfName.getX(), 360);
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
				String username = tfName.getText();
				String pass = tfPass.getText();
				AppPreference.instance.setName(tfName.getText(), false);
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
					AbstractGameScreen.keyboard.show(false);
					Request.getInstance().login(tfName.getText(),
							tfPass.getText(), new LoginListener());
					// Request.getInstance().login("841257523333", "123456",
					// new LoginListener());
					setTouchable(Touchable.disabled);
					Loading.ins.show(ViewLogin.this);
				}
				super.clicked(event, x, y);
			}
		});

		Label btnForgotPass = new Label("Quên mật khẩu ?", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, fontType.Light),
				new Color(0, 191 / 255f, 1, 1)));
		btnForgotPass.setPosition(btOk.getX(), 200);
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
				btOk.getX() + btOk.getWidth() - btnRegister.getWidth(), 200);
		btnRegister.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getViewController().getView("view_register").show(null);
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
		btnActive.setPosition(btOk.getX(), 150);
		btnActive.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getViewController().getView(ViewWaitAccept.class.getName())
						.show(null);
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

		this.addActor(tfName);
		this.addActor(tfPass);
		addActor(iconUser);
		addActor(iconLock);
		this.addActor(btOk);
		addActor(btnRegister);
		addActor(btnActive);
		addActor(btnForgotPass);
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
				int role_id = respone.getInt(ExtParamsKey.ROLE_ID);
				UserInfo.getInstance().setRoleId(role_id);
				TopBarView topBarView = new TopBarView();
				topBarView.build(getStage(), _viewController,
						StringSystem.VIEW_ACTION_BAR, new Rectangle(0,
								Constants.HEIGHT_SCREEN
										- Constants.HEIGHT_ACTIONBAR,
								Constants.WIDTH_SCREEN,
								Constants.HEIGHT_ACTIONBAR));
				topBarView.buildComponent();

				MainMenuView mainMenu = new MainMenuView();
				mainMenu.build(getStage(), _viewController,
						StringSystem.VIEW_MAIN_MENU,
						new Rectangle(0, 0, Constants.WIDTH_SCREEN,
								Constants.HEIGHT_SCREEN));
				mainMenu.buildComponent();

				ViewAdminLock viewAdminLock = new ViewAdminLock();
				viewAdminLock.build(getStage(), _viewController,
						"view_admin_lock", new Rectangle(0, 0,
								Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
										- Constants.HEIGHT_ACTIONBAR));
				viewAdminLock.buildComponent();

				ViewAdminRequest viewAdminRequest = new ViewAdminRequest();
				viewAdminRequest.build(getStage(), _viewController,
						"view_admin_request", new Rectangle(0, 0,
								Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
										- Constants.HEIGHT_ACTIONBAR));
				viewAdminRequest.buildComponent();

				ViewInfoDaiLySmall viewInfo = new ViewInfoDaiLySmall();
				viewInfo.build(getStage(), getViewController(),
						ViewInfoDaiLySmall.class.getName(), new Rectangle(0, 0,
								400, 600));

				getViewController().getView("view_login").destroyComponent();
				getViewController().getView("view_register").destroyComponent();
				getViewController().getView(StringSystem.VIEW_WAIT_ACCEPT)
						.destroyComponent();

				int id = respone.getInt(ExtParamsKey.ROLE_ID);
				AppPreference.instance.type = id;
				AppPreference.instance.save();
				AppPreference.instance.setPass(tfPass.getText(),false);
				// Log.d("Data resonse is caught");
				// _viewController.getView("view_admin_acive").show(null);
				// getViewController().getView(StringSystem.VIEW_ACTION_BAR).show(
				// null);
				// getViewController().getView(StringSystem.VIEW_MAIN_MENU).show(
				// null);

				Request.getInstance().getInfoDaily(AppPreference.instance.getName(),
						new GetInfoDaily());
			} else {
				setTouchable(Touchable.enabled);
			}
			respone = null;

		}

		if (responeInfoDaily != null) {
			Loading.ins.hide();
			boolean result = responeInfoDaily.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				UserInfo.fullName = responeInfoDaily
						.getString(ExtParamsKey.FULL_NAME);
				UserInfo.address = responeInfoDaily
						.getString(ExtParamsKey.ADDRESS);
				UserInfo.level = responeInfoDaily
						.getString(ExtParamsKey.ROLE_NAME);
				UserInfo.phone = tfName.getText();
				UserInfo.phoneNGT = responeInfoDaily
						.getString(ExtParamsKey.REF_CODE);
				UserInfo.money = responeInfoDaily.getInt(ExtParamsKey.AMOUNT);
				UserInfo.currency = responeInfoDaily
						.getString(ExtParamsKey.CURRENCY);
				UserInfo.email = responeInfoDaily.getString(ExtParamsKey.EMAIL);
				UserInfo.imeiDevice = responeInfoDaily
						.getString(ExtParamsKey.DEVICE_ID);
				UserInfo.nameDevice = responeInfoDaily
						.getString(ExtParamsKey.DEVICE_NAME);
				UserInfo.state = responeInfoDaily.getInt(ExtParamsKey.STATE);

				ViewAdminActive viewAdminActive = new ViewAdminActive();
				viewAdminActive.build(getStage(), getViewController(),
						"view_admin_acive", new Rectangle(0, 0,
								Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
										- Constants.HEIGHT_ACTIONBAR));
				viewAdminActive.buildComponent();
				if (getViewController().isContainView("view_admin_active")) {
					Log.d("contain View");
				} else {
					Log.d("Aaaaaaaaaaaaaaaaaaaa");
				}
				viewAdminActive.show(null);
			} else {
				String mess = responeInfoDaily.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			}
			responeInfoDaily = null;
		}
	}

	class LoginListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			respone = (new JsonReader())
					.parse(httpResponse.getResultAsString());
			Log.d(respone.toString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

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

}
