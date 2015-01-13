package imp.view;

import utils.elements.GiftCode;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Log;
import utils.factory.Style;
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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.ui.MoneyPicker;
import com.coder5560.game.ui.TextfieldStatic;
import com.coder5560.game.views.View;

public class ViewNewCode extends View {

	MoneyPicker			pkAmount;
	Table				tbGenerate		= new Table();
	Table				tbGiftCode		= new Table();
	Label				lbMoneyRealMoney;
	float				rateMoney		= 1;
	TextfieldStatic		lbNewCode;
	TextButton			btCopy;
	private JsonValue	responeNewCode;
	private JsonValue	responseListMoney;
	private JsonValue	responeExchange;

	GiftCode			currentGiftCode	= new GiftCode("", "");

	@Override
	public String getLabel() {
		return "Sinh code đổi thưởng";
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
		Label lbAmount = new Label("Tiền trong game",
				Style.ins.getLabelStyle(22));
		lbAmount.setColor(Color.BLACK);
		Label lbEqual = new Label("Tiền thật", Style.ins.getLabelStyle(22));
		lbEqual.setColor(Color.BLACK);
		lbMoneyRealMoney = new Label("", Style.ins.getLabelStyle(22));
		lbMoneyRealMoney.setColor(Color.BLACK);
		pkAmount = new MoneyPicker(Style.ins.selectBoxStyle);
		pkAmount.setCustomCurrency("Xu");
		pkAmount.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (pkAmount.getSize() == 0)
					return;
				lbMoneyRealMoney.setText(Factory
						.getDotMoney((long) (1 / rateMoney * pkAmount
								.getMoney()))
						+ " " + UserInfo.currency);
			}
		});

		TextButtonStyle btStyle = new TextButtonStyle();
		btStyle.up = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				Constants.COLOR_ACTIONBAR));
		Color colorDown = new Color(Constants.COLOR_ACTIONBAR);
		colorDown.a = 0.5f;

		btStyle.down = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				colorDown));
		btStyle.font = Assets.instance.fontFactory.getFont(20, FontType.Medium);
		btStyle.fontColor = Color.WHITE;
		TextButton btGetGiftCode = new TextButton("Lấy Code", btStyle);
		btGetGiftCode.setSize(370, 55);
		btGetGiftCode.setPosition(tbGenerate.getX(), tbGenerate.getY()
				- btGetGiftCode.getHeight() - 20);
		btGetGiftCode.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (pkAmount.getSize() == 0) {
					return;
				}
				Loading.ins.show(ViewNewCode.this);
				Request.getInstance().generateCodeCashOut(UserInfo.phone,
						pkAmount.getMoney(), new GetNewCode());
			}
		});
		tbGenerate.top().padTop(10);
		tbGenerate.setSize(getWidth(), 200);
		tbGenerate.row();

		tbGenerate.setWidth(getWidth());
		tbGenerate.add(lbAmount).width(lbAmount.getWidth()).height(55)
				.padLeft(10).left();
		tbGenerate.add(pkAmount).width(160).height(40).padLeft(10).left();

		tbGenerate.row();

		tbGenerate.add(lbEqual).width(lbAmount.getWidth()).height(55)
				.padLeft(10).left();
		tbGenerate.add(lbMoneyRealMoney).width(50).height(55).left()
				.padLeft(10);
		tbGenerate.row();
		tbGenerate.add(btGetGiftCode).colspan(4).width(130).height(50).pad(10);

		tbGenerate.setBackground(new NinePatchDrawable(new NinePatch(
				Style.ins.np2, new Color(0.7f, 0.7f, 0.7f, 1))));

		tbGiftCode.setSize(getWidth(), 150);

		tbGiftCode.setBackground(new NinePatchDrawable(new NinePatch(
				Style.ins.np2, new Color(0.75f, 0.75f, 0.75f, 1))));
		lbNewCode = new TextfieldStatic("Chưa sinh code mới",
				Style.ins.getLabelStyle(20), Color.BLACK,
				tbGiftCode.getWidth() - 10);
		lbNewCode.setContent("Chưa sinh code mới", Align.center);

		btCopy = new TextButton("Copy", btStyle);
		btCopy.setOrigin(Align.center);
		btCopy.setSize(370, 55);
		btCopy.setTransform(true);
		btCopy.setPosition(tbGenerate.getX(),
				tbGenerate.getY() - btCopy.getHeight() - 20);
		btCopy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.getClipboard().setContents(lbNewCode.getContent());
				Toast.makeText(getStage(), "Đã copy vào bộ nhớ đệm",
						Toast.LENGTH_SHORT);
			}
		});
		tbGiftCode.add(lbNewCode).width(lbNewCode.getWidth()).center().top()
				.colspan(2);
		tbGiftCode.row();
		// tbGiftCode.add(btReturn).width(180).height(45).pad(10);
		tbGiftCode.add(btCopy).width(180).height(45).pad(10).colspan(2);

		top();
		this.add(tbGenerate).width(tbGenerate.getWidth()).top().padTop(1)
				.height(tbGenerate.getHeight());
		row();
		add(tbGiftCode).width(tbGiftCode.getWidth()).height(120).padTop(5)
				.top();
		row();

		hideButton();
		return this;
	}

	public void showButton() {
		btCopy.setVisible(false);
		btCopy.clearActions();
		btCopy.getColor().a = 0.8f;
		btCopy.setScale(0f);
		btCopy.addAction(Actions.parallel(
				Actions.sequence(Actions.visible(true),
						Actions.scaleTo(1, 1, 0.2f, Interpolation.fade)),
				Actions.fadeIn(0.2f, Interpolation.fade)));
	}

	public void hideButton() {
		btCopy.clearActions();
		btCopy.addAction(Actions.sequence(
				Actions.scaleTo(0.5f, 0.5f, 0.2f, Interpolation.fade),
				Actions.hide()));
		btCopy.addAction(Actions.fadeOut(0.2f, Interpolation.fade));
	}

	public void registerKeyboard(final TextField tf, final int config) {
		tf.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tf.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}
		});
	}

	@Override
	public void update(float deltaTime) {
		if (responeNewCode != null) {
			if (responeNewCode.getBoolean(ExtParamsKey.RESULT)) {
				currentGiftCode = new GiftCode(
						responeNewCode.getString(ExtParamsKey.CODE),
						responeNewCode.getString(ExtParamsKey.ID));
				lbNewCode.setContent("Code: " + currentGiftCode.code + " Id: "
						+ currentGiftCode.id, Align.center);
				showButton();
			}
			responeNewCode = null;
		}
		if (responseListMoney != null) {
			Log.d("Response return is catch");
			Loading.ins.hide();
			boolean result = responseListMoney.getBoolean(ExtParamsKey.RESULT);
			String mess = responseListMoney.getString(ExtParamsKey.MESSAGE);
			if (result) {
				JsonValue list = responseListMoney.get(ExtParamsKey.LIST);
				pkAmount.reset();
				for (int i = 0; i < list.size; i++) {
					pkAmount.addPartner(list.getInt(i));
				}
				lbMoneyRealMoney.setText(Factory
						.getDotMoney((long) (1 / rateMoney * pkAmount
								.getMoney()))
						+ " " + UserInfo.currency);
			} else {
				Toast.makeText(getStage(), mess, Toast.LENGTH_SHORT);
			}

			responseListMoney = null;
		}

		if (responeExchange != null) {
			if (responeExchange.getBoolean(ExtParamsKey.RESULT)) {
				Log.d(responeExchange.toString());
				rateMoney = responeExchange
						.getFloat(ExtParamsKey.MONEY_IN_GAME) / 1000;
				lbMoneyRealMoney.setText(Factory
						.getDotMoney((long) (1 / rateMoney * pkAmount
								.getMoney()))
						+ " " + UserInfo.currency);
				Loading.ins.hide();
			} else {
				Toast.makeText(getStage(),
						responeExchange.getString(ExtParamsKey.MESSAGE),
						Toast.LENGTH_SHORT);
			}
			responeExchange = null;
		}

	}

	@Override
	public void show(OnCompleteListener listener) {
		super.show(listener);
		Loading.ins.show(this);

		Request.getInstance().getListMoneyCashOut(UserInfo.phone,
				UserInfo.currency, new GetListMoney());
	}

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(null);
		getViewController().removeView(getName());
	}

	@Override
	public void back() {
		super.back();
	}

	class GetNewCode implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			Loading.ins.hide();
			responeNewCode = (new JsonReader()).parse(httpResponse
					.getResultAsString());
			Toast.makeText(getStage(),
					responeNewCode.getString(ExtParamsKey.MESSAGE),
					Toast.LENGTH_SHORT);
			Log.d(responeNewCode.toString());
		}

		@Override
		public void failed(Throwable t) {
			Loading.ins.hide();
		}

		@Override
		public void cancelled() {
			Loading.ins.hide();
		}
	}

	class GetListMoney implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responseListMoney = (new JsonReader()).parse(httpResponse
					.getResultAsString());
			Log.d(responseListMoney.toString());
			Request.getInstance().getExchangeInGame(1000, UserInfo.currency,
					new HttpResponseListener() {
						@Override
						public void handleHttpResponse(HttpResponse httpResponse) {
							responeExchange = (new JsonReader())
									.parse(httpResponse.getResultAsString());
						}

						@Override
						public void failed(Throwable t) {
							Loading.ins.hide();
						}

						@Override
						public void cancelled() {
							Loading.ins.hide();
						}
					});

		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}
	}

}
