package imp.view;

import utils.factory.AppPreference;
import utils.factory.FontFactory.fontType;
import utils.factory.Style;
import utils.keyboard.KeyboardConfig;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
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
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.View;

public class ViewWaitAccept extends View {

	private CustomTextField	tfPhone;
	private CustomTextField	tfActiveCode;
	private JsonValue		respone;
	Label					btnRegister, btnLogin;

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
				getHeight() - titleLogin.getHeight() - 80);
		addActor(titleLogin);
		titleLogin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				getStage().setKeyboardFocus(null);
				AbstractGameScreen.keyboard.show(false);
			}
		});
		Label lbNotice = new Label(
				"Vui lòng chờ mã kích hoạt gửi đến số điện thoại bạn đăng ký. Khi nhận được hãy nhập mã kích hoạt vào ô bên dưới.",
				Style.ins.getLabelStyle(20, fontType.Light));
		lbNotice.setColor(Color.BLACK);
		lbNotice.setWrap(true);
		lbNotice.setWidth(Constants.WIDTH_SCREEN - 50);
		lbNotice.setPosition(getWidth() / 2 - lbNotice.getTextBounds().width
				/ 2, 550);
		TextFieldStyle tfStyleName = Style.ins.getTextFieldStyle(10,
				Assets.instance.fontFactory.getFont(25, fontType.Light));
		TextFieldStyle tfStylePass = Style.ins.getTextFieldStyle(10,
				Assets.instance.fontFactory.getFont(25, fontType.Light));

		tfPhone = new CustomTextField("", tfStyleName);
		tfActiveCode = new CustomTextField("", tfStylePass);
		tfPhone.setMessageText("Số điện thoại");
		tfActiveCode.setMessageText("Mã kích hoạt");
		tfPhone.setSize(370, 60);
		tfActiveCode.setSize(370, 60);
		tfPhone.setPosition(
				Constants.WIDTH_SCREEN / 2 - tfPhone.getWidth() / 2, 440);
		tfPhone.setText(AppPreference.instance.name);
		tfActiveCode.setPosition(tfPhone.getX(), 360);
		tfPhone.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tfActiveCode.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tfPhone.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				registerKeyboard(tfPhone, KeyboardConfig.NORMAL);
				return true;
			}
		});

		tfActiveCode.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				registerKeyboard(tfActiveCode, KeyboardConfig.NORMAL);
				return true;
			}
		});

		TextButtonStyle btStyle = new TextButtonStyle();
		btStyle.up = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				new Color(0, 191 / 255f, 1, 1)));
		btStyle.down = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				new Color(0, 191 / 255f, 1, 0.5f)));
		btStyle.font = Assets.instance.fontFactory.getFont(20, fontType.Medium);
		btStyle.fontColor = Color.WHITE;
		TextButton btOk = new TextButton("Kích hoạt", btStyle);
		btOk.setSize(370, 55);
		btOk.setPosition(tfPhone.getX(),
				tfActiveCode.getY() - tfActiveCode.getHeight() - 20);
		btOk.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				String username = tfPhone.getText();
				String pass = tfActiveCode.getText();
				AppPreference.instance.name = tfPhone.getText();
				AppPreference.instance.save();
				if (username.equalsIgnoreCase("") || pass.equalsIgnoreCase("")) {
					Toast.makeText(getStage(),
							"Vui lòng nhập đầy đủ thông tin",
							Toast.LENGTH_SHORT);
				} else {
					AbstractGameScreen.keyboard.show(false);
					Request.getInstance().active(tfPhone.getText(),
							tfActiveCode.getText(), new LoginListener());
					setTouchable(Touchable.disabled);
					Loading.ins.show(ViewWaitAccept.this);
				}
				super.clicked(event, x, y);
			}
		});

		btnRegister = new Label("Đăng ký mới", new LabelStyle(
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
				ViewRegister viewRegister = new ViewRegister();
				viewRegister.build(getStage(), _viewController,
						"view_register",
						new Rectangle(0, 0, Constants.WIDTH_SCREEN,
								Constants.HEIGHT_SCREEN));
				viewRegister.buildComponent().show(null);
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

		btnLogin = new Label("Đăng nhập", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, fontType.Light),
				new Color(0, 191 / 255f, 1, 1))) {
			@Override
			public Actor hit(float x, float y, boolean touchable) {
				if (x < btnLogin.getWidth() + 10 && x > -10
						&& y < btnLogin.getHeight() + 10 && y > -10)
					return btnLogin;
				return null;
			}
		};
		btnLogin.setPosition(btOk.getX(), 200);
		btnLogin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				_viewController.getView("view_login").show(null);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				btnLogin.setColor(Color.GRAY);
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				btnLogin.setColor(new Color(0, 191 / 255f, 1, 1));
				super.touchUp(event, x, y, pointer, button);
			}
		});

		this.addActor(tfPhone);
		this.addActor(tfActiveCode);
		this.addActor(lbNotice);
		this.addActor(btOk);
		addActor(btnRegister);
		addActor(btnLogin);
		return this;
	}

	public void registerKeyboard(TextField tf, int config) {
//		AbstractGameScreen.keyboard.registerTextField(tf, config,
//				KeyboardConfig.SINGLE_LINE);
	}

	@Override
	public void update(float deltaTime) {
		if (respone != null) {
			setTouchable(Touchable.enabled);
			Loading.ins.hide();
			Boolean isSuccess = respone.getBoolean(ExtParamsKey.RESULT);
			String mess = respone.getString(ExtParamsKey.MESSAGE);
			Toast.makeText(getStage(), mess, Toast.LENGTH_SHORT);
			if (isSuccess) {
				_viewController.getView("view_login").show(null);
				AppPreference.instance.isWaitActiveCode = false;
				AppPreference.instance.save();
			}
			respone = null;
		}
	}

	class LoginListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			respone = (new JsonReader())
					.parse(httpResponse.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {
		}

		@Override
		public void cancelled() {

		}
	}

	@Override
	public void hide(OnCompleteListener listener) {
		TraceView.instance.removeView(this.getName());
		getViewController().removeView(getName());
	}
}
