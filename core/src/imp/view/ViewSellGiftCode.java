package imp.view;

import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.fontType;
import utils.factory.Style;
import utils.keyboard.KeyboardConfig;
import utils.networks.ExtParamsKey;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.Toast;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewSellGiftCode extends View {

	private CustomTextField	tfAmount, tfCount, tfAmountGame;
	private JsonValue		respone;
	Table					content	= new Table();
	Label					lbCurrency;

	@Override
	public String getLabel() {
		return "Bán Gift Code";
	}

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
		TextFieldStyle tfStyleName = Style.ins.getTextFieldStyle(8,
				Assets.instance.fontFactory.getFont(25, fontType.Light));
		Label lbAmount = new Label("Số tiền", Style.ins.getLabelStyle(22));
		lbAmount.setColor(Color.BLACK);
		lbCurrency = new Label(UserInfo.currency + "",
				Style.ins.getLabelStyle(22));
		lbCurrency.setColor(Color.BLACK);
		Label lbCount = new Label("Số lượng", Style.ins.getLabelStyle(22));
		lbCount.setColor(Color.BLACK);
		Label lbEqual = new Label("Tương ứng", Style.ins.getLabelStyle(22));
		lbEqual.setColor(Color.BLACK);
		Label lbGameCurrency = new Label("chip", Style.ins.getLabelStyle(22));
		lbGameCurrency.setColor(Color.BLACK);
		tfAmount = new CustomTextField("", tfStyleName);
		tfAmountGame = new CustomTextField("", tfStyleName);
		tfCount = new CustomTextField("", tfStyleName);

		tfCount.setMessageText("<50");

		tfAmount.setText(_viewController.getPlatformResolver().getCountryCode());

		registerKeyboard(tfAmount, KeyboardConfig.NUMBER);
		registerKeyboard(tfAmountGame, KeyboardConfig.NORMAL);
		registerKeyboard(tfCount, KeyboardConfig.NUMBER);

		TextButtonStyle btStyle = new TextButtonStyle();
		btStyle.up = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				new Color(0, 191 / 255f, 1, 1)));
		btStyle.down = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				new Color(0, 191 / 255f, 1, 0.5f)));
		btStyle.font = Assets.instance.fontFactory.getFont(20, fontType.Medium);
		btStyle.fontColor = Color.WHITE;
		TextButton btOk = new TextButton("Lấy giftcode", btStyle);
		btOk.setSize(370, 55);
		btOk.setPosition(content.getX(), content.getY() - btOk.getHeight() - 20);
		btOk.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				String username = tfAmount.getText();
				String pass = tfCount.getText();
				AppPreference.instance.name = tfAmount.getText();
				AppPreference.instance.save();
				if (username.equalsIgnoreCase("") || pass.equalsIgnoreCase("")) {
					Toast.makeText(getStage(),
							"Vui lòng nhập đầy đủ thông tin",
							Toast.LENGTH_SHORT);
				} else if (!Factory.validPhone(tfAmount.getText())) {
					Toast.makeText(
							getStage(),
							"Số điện thoại của bạn không đúng. Vui lòng nhập lại!",
							Toast.LENGTH_SHORT);
				} else if (!Factory.validPhone(tfCount.getText())) {
					Toast.makeText(
							getStage(),
							"Số điện thoại giới thiệu không đúng. Vui lòng nhập lại!",
							Toast.LENGTH_SHORT);
				} else {
					AbstractGameScreen.keyboard.show(false);
					Loading.ins.show(ViewSellGiftCode.this);
				}
				super.clicked(event, x, y);
			}
		});
		content.top().padTop(10);
		content.setSize(getWidth(), getHeight());
		content.row();

		content.setWidth(getWidth());
		content.add(lbAmount).width(130).height(55).left();
		content.add(tfAmount).width(110).height(50).left();
		content.add(lbCurrency).width(50).height(55).left();
		content.add(lbCount).width(110).height(55).padLeft(20);
		content.add(tfCount).width(50).height(50);

		content.row();

		content.add(lbEqual).width(130).height(55).left();
		content.add(tfAmountGame).width(110).height(50).left();
		content.add(lbGameCurrency).width(50).height(55).left();

		content.add(btOk).colspan(2).width(150).height(55);

		this.add(content).colspan(2).width(content.getWidth())
				.height(content.getHeight());

		return this;
	}

	public void registerKeyboard(final TextField tf, final int config) {
		tf.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tf.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				AbstractGameScreen.keyboard.registerTextField(tf, config,
						KeyboardConfig.SINGLE_LINE);
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
	public void show(OnCompleteListener listener) {
		super.show(listener);
		lbCurrency.setText("" + UserInfo.currency);
		// Request.getInstance().getExchangeInGame(amount, currency, listener);
	}

	@Override
	public void hide(OnCompleteListener listener) {
		setVisible(false);
	}
}
