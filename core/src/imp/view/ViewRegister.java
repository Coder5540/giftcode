package imp.view;

import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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
import com.coder5560.game.views.View;

public class ViewRegister extends View {

	private CustomTextField tfPhone, tfphoneIntro, tfFullName, tfAddress,
			tfEmail;
	private CustomTextField tfPass, tfRePass;
	private JsonValue respone;
	Table content = new Table();
	ScrollPane scrTextField;
	Label btnRegister;

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

		Image titleLogin = new Image(Assets.instance.ui.getLogo());
		titleLogin.setSize(400, 40);
		titleLogin.setPosition(getWidth() / 2 - titleLogin.getWidth() / 2,
				getHeight() - titleLogin.getHeight() - 50);
		addActor(titleLogin);
		titleLogin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				getStage().setKeyboardFocus(null);
				AbstractGameScreen.keyboard.show(false);
			}
		});
		TextFieldStyle tfStyleName = Style.ins.getTextFieldStyle(8,
				Assets.instance.fontFactory.getFont(25, FontType.Light));
		TextFieldStyle tfStylePass = Style.ins.getTextFieldStyle(8,
				Assets.instance.fontFactory.getFont(25, FontType.Light));
		Label phoneNumber = new Label("Số điện thoại",
				Style.ins.getLabelStyle(22));
		phoneNumber.setColor(Color.BLACK);
		Label passWord = new Label("Mật khẩu", Style.ins.getLabelStyle(22));
		passWord.setColor(Color.BLACK);
		Label rePassWord = new Label("Nhập lại", Style.ins.getLabelStyle(22));
		rePassWord.setColor(Color.BLACK);
		Label phoneIntro = new Label("SĐT giới thiệu",
				Style.ins.getLabelStyle(22));
		phoneIntro.setColor(Color.BLACK);
		Label fullName = new Label("Họ tên", Style.ins.getLabelStyle(22));
		fullName.setColor(Color.BLACK);
		Label adddress = new Label("Địa chỉ", Style.ins.getLabelStyle(22));
		adddress.setColor(Color.BLACK);
		Label email = new Label("Email", Style.ins.getLabelStyle(22));
		email.setColor(Color.BLACK);
		tfPhone = new CustomTextField("", tfStyleName);
		tfAddress = new CustomTextField("", tfStyleName);
		tfEmail = new CustomTextField("", tfStyleName);
		tfFullName = new CustomTextField("", tfStyleName);
		tfphoneIntro = new CustomTextField("", tfStyleName);
		tfPass = new CustomTextField("", tfStylePass);
		tfRePass = new CustomTextField("", tfStylePass);

		// tfPhone.setMessageText("số điện thoại của bạn");
		// tfPass.setMessageText("mật khẩu");
		tfPass.setPasswordCharacter('*');
		tfPass.setPasswordMode(true);
		tfRePass.setPasswordCharacter('*');
		tfRePass.setPasswordMode(true);

		tfPhone.setText(_viewController.getPlatformResolver().getCountryCode());

		registerKeyboard(tfPhone, KeyboardConfig.NUMBER);
		registerKeyboard(tfAddress, KeyboardConfig.NORMAL);
		registerKeyboard(tfEmail, KeyboardConfig.NORMAL);
		registerKeyboard(tfFullName, KeyboardConfig.NORMAL);
		registerKeyboard(tfphoneIntro, KeyboardConfig.NUMBER);

		registerKeyboard(tfPass, KeyboardConfig.PASSWORD);
		registerKeyboard(tfRePass, KeyboardConfig.PASSWORD);

		TextButton btOk = new TextButton("Đăng ký", Style.ins.textButtonStyle);
		btOk.setSize(370, 55);
		btOk.setPosition(content.getX(), content.getY() - btOk.getHeight() - 20);
		btOk.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				String username = tfPhone.getText();
				String pass = tfPass.getText();
				AppPreference.instance.name = tfPhone.getText();
				AppPreference.instance.save();
				if (username.equalsIgnoreCase("") || pass.equalsIgnoreCase("")) {
					Toast.makeText(getStage(),
							"Vui lòng nhập đầy đủ thông tin",
							Toast.LENGTH_SHORT);
				} else if (!Factory.validPhone(tfPhone.getText())) {
					Toast.makeText(
							getStage(),
							"Số điện thoại của bạn không đúng. Vui lòng nhập lại!",
							Toast.LENGTH_SHORT);
				} else if (!Factory.validPhone(tfphoneIntro.getText())) {
					Toast.makeText(
							getStage(),
							"Số điện thoại giới thiệu không đúng. Vui lòng nhập lại!",
							Toast.LENGTH_SHORT);
				} else if (!tfPass.getText().equals(tfRePass.getText())) {
					Toast.makeText(getStage(),
							"Mật khẩu không khớp nhau. Vui lòng nhập lại!",
							Toast.LENGTH_SHORT);
					tfPass.setText("");
					tfRePass.setText("");
				} else if (!Factory.validEmail(tfEmail.getText())) {
					Toast.makeText(getStage(),
							"Email không đúng. Vui lòng nhập lại",
							Toast.LENGTH_SHORT);
				} else {
					AbstractGameScreen.keyboard.show(false);
					Request.getInstance().register(tfPhone.getText(),
							tfPass.getText(), tfphoneIntro.getText(),
							tfFullName.getText(), tfAddress.getText(),
							tfEmail.getText(), new LoginListener());
					Loading.ins.show(ViewRegister.this);
				}
				super.clicked(event, x, y);
			}
		});
		content.add(titleLogin).colspan(2).width(titleLogin.getWidth())
				.height(titleLogin.getHeight()).pad(50);
		content.setSize(getWidth(), getHeight());
		content.row();
		Table tbTextField = new Table();
		scrTextField = new ScrollPane(tbTextField);
		scrTextField.setSize(getWidth(), 480);
		tbTextField.setWidth(getWidth());
		float tfwidth = 280;
		tbTextField.add(phoneNumber).width(phoneNumber.getWidth()).expandX()
				.right().pad(10);
		tbTextField.add(tfPhone).width(tfwidth).left().pad(10);
		tbTextField.row();
		tbTextField.add(passWord).right().pad(10);
		tbTextField.add(tfPass).left().pad(10).width(tfwidth);

		tbTextField.row();
		tbTextField.add(rePassWord).right().pad(10);
		tbTextField.add(tfRePass).left().pad(10).width(tfwidth);

		tbTextField.row();
		tbTextField.add(phoneIntro).right().pad(10);
		tbTextField.add(tfphoneIntro).left().pad(10).width(tfwidth);

		tbTextField.row();
		tbTextField.add(fullName).right().pad(10);
		tbTextField.add(tfFullName).left().pad(10).width(tfwidth);

		tbTextField.row();
		tbTextField.add(adddress).right().pad(10);
		tbTextField.add(tfAddress).left().pad(10).width(tfwidth);

		tbTextField.row();
		tbTextField.add(email).right().pad(10);
		tbTextField.add(tfEmail).left().pad(10).width(tfwidth);
		tbTextField.row();
		tbTextField.add(new Actor()).colspan(2).padBottom(300);

		this.content.add(scrTextField).colspan(2)
				.width(scrTextField.getWidth())
				.height(scrTextField.getHeight());

		this.content.row();
		this.content.add(btOk).width(375).height(55).colspan(2).padTop(10);

		btnRegister = new Label("Đăng nhập", new LabelStyle(
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
				btOk.getX() + btOk.getWidth() - btnRegister.getWidth(), 200);
		btnRegister.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getViewController().getView("view_login").show(null);
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

		content.row();
		this.content.add(btnRegister).colspan(2).width(btnRegister.getWidth())
				.height(btnRegister.getHeight()).padBottom(100).padTop(15);
		// this.content.debug();
		addActor(this.content);
		return this;
	}

	public void registerKeyboard(final TextField tf, final int config) {
		tf.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tf.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				Vector2 tfPos = new Vector2(0, 0);
				tf.localToAscendantCoordinates(scrTextField, tfPos);
				float yDef = tfPos.y - scrTextField.getScrollY();
				Vector2 tfPosStageScroll = new Vector2(0, 0);
				scrTextField.localToStageCoordinates(tfPosStageScroll);
				yDef += tfPosStageScroll.y;
				if (yDef < Constants.HEIGHT_SCREEN / 2 + 50) {
					scrTextField.setScrollY(Constants.HEIGHT_SCREEN / 2 + 50
							- yDef);
				}
				// AbstractGameScreen.keyboard.registerTextField(tf, config,
				// KeyboardConfig.SINGLE_LINE);
				return true;
			}
		});
	}

	@Override
	public void update(float deltaTime) {
		if (respone != null) {
			Loading.ins.hide();
			Boolean isSuccess = respone.getBoolean(ExtParamsKey.RESULT);
			String mess = respone.getString(ExtParamsKey.MESSAGE);
			Toast.makeText(getStage(), mess, Toast.LENGTH_SHORT);
			if (isSuccess) {
				ViewWaitAccept viewWaitAccept = new ViewWaitAccept();
				viewWaitAccept.build(getStage(), _viewController,
						ViewWaitAccept.class.getName(),
						new Rectangle(0, 0, Constants.WIDTH_SCREEN,
								Constants.HEIGHT_SCREEN));
				viewWaitAccept.buildComponent().show(null);

				AppPreference.instance.name = tfPhone.getText();
				AppPreference.instance.isWaitActiveCode = true;
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
		setVisible(false);
	}

	@Override
	public void back() {
		super.back();
		getViewController().removeView(getName());
	}
}
