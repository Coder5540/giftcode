package imp.view;

import utils.factory.AppPreference;
import utils.factory.DateTime;
import utils.factory.FontFactory.fontType;
import utils.factory.Log;
import utils.factory.Style;
import utils.keyboard.KeyboardConfig;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.Toast;

import com.aia.appsreport.component.table.AbstractTable;
import com.aia.appsreport.component.table.ItemNonSellGiftCode;
import com.aia.appsreport.component.table.ItemTable;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.DialogCustom;
import com.coder5560.game.ui.ItemListener;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.ui.MoneyPicker;
import com.coder5560.game.ui.Page;
import com.coder5560.game.ui.TextfieldStatic;
import com.coder5560.game.views.View;

public class ViewSellGiftCode extends View {

	MoneyPicker				pkAmount;
	private JsonValue		respone;
	Table					tbGenerate	= new Table(),
			tbGiftCode = new Table();
	Label					lbMoneyInGame;
	float					rateMoney	= 1;
	Label					lbAdminMoney;
	TextfieldStatic			tfNewGiftCode;
	TextButton				btCopy, btSendMessage;
	private AbstractTable	tableNormal;
	private JsonValue		responeNormal;
	private JsonValue		responeReturn;
	private JsonValue		responeGenerateGiftCode;

	private Page			page;

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
		Label lbAmount = new Label("Số tiền", Style.ins.getLabelStyle(22));
		lbAmount.setColor(Color.BLACK);
		lbAdminMoney = new Label("", Style.ins.getLabelStyle(22));
		lbAdminMoney.setColor(Color.BLACK);
		Label lbEqual = new Label("Tương ứng", Style.ins.getLabelStyle(22));
		lbEqual.setColor(Color.BLACK);
		lbMoneyInGame = new Label("chip", Style.ins.getLabelStyle(22));
		lbMoneyInGame.setColor(Color.BLACK);
		pkAmount = new MoneyPicker(Style.ins.selectBoxStyle);

		pkAmount.addPartner(10000);

		TextButtonStyle btStyle = new TextButtonStyle();
		btStyle.up = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				new Color(0, 191 / 255f, 1, 1)));
		btStyle.down = new NinePatchDrawable(new NinePatch(Style.ins.np1,
				new Color(0, 191 / 255f, 1, 0.5f)));
		btStyle.font = Assets.instance.fontFactory.getFont(20, fontType.Medium);
		btStyle.fontColor = Color.WHITE;
		TextButton btOk = new TextButton("Lấy giftcode", btStyle);
		btOk.setSize(370, 55);
		btOk.setPosition(tbGenerate.getX(),
				tbGenerate.getY() - btOk.getHeight() - 20);
		btOk.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Loading.ins.show(ViewSellGiftCode.this);
				Request.getInstance().generateGiftCode(UserInfo.phone,
						pkAmount.getMoney(), UserInfo.currency, 6,
						new HttpResponseListener() {
							@Override
							public void handleHttpResponse(
									HttpResponse httpResponse) {
								Loading.ins.hide();
								Toast.makeText(
										getStage(),
										respone.getString(ExtParamsKey.MESSAGE),
										Toast.LENGTH_SHORT);

								responeGenerateGiftCode = (new JsonReader())
										.parse(httpResponse.getResultAsString());
								Log.d("AAAAA"
										+ responeGenerateGiftCode.toString());

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
		});
		tbGenerate.top().padTop(10);
		tbGenerate.setSize(getWidth(), 140);
		tbGenerate.row();

		tbGenerate.setWidth(getWidth());
		tbGenerate.add(lbAmount).width(130).height(55).left();
		tbGenerate.add(pkAmount).width(190).height(50).left();
		tbGenerate.add(lbAdminMoney).width(110).height(55).padLeft(5).left();

		tbGenerate.row();

		tbGenerate.add(lbEqual).width(130).height(55).left();
		tbGenerate.add(lbMoneyInGame).width(50).height(55).left();

		tbGenerate.add(btOk).colspan(2).width(150).height(55).pad(10);

		tbGenerate.setBackground(new NinePatchDrawable(new NinePatch(
				Style.ins.np2, new Color(0.8f, 0.8f, 0.8f, 1))));

		tbGiftCode.setSize(getWidth(), 150);

		tbGiftCode.setBackground(new NinePatchDrawable(new NinePatch(
				Style.ins.np2, new Color(0.7f, 0.7f, 0.7f, 1))));
		tfNewGiftCode = new TextfieldStatic("Chưa sinh giftcode mới",
				Style.ins.getLabelStyle(15), Color.BLACK,
				tbGiftCode.getWidth() - 20);

		btSendMessage = new TextButton("Gửi tin nhắn", btStyle);
		btSendMessage.setSize(370, 55);
		btSendMessage.setPosition(tbGenerate.getX(), tbGenerate.getY()
				- btSendMessage.getHeight() - 20);
		btSendMessage.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Loading.ins.show(ViewSellGiftCode.this);
			}
		});
		btCopy = new TextButton("Copy", btStyle);
		btCopy.setSize(370, 55);
		btCopy.setPosition(tbGenerate.getX(),
				tbGenerate.getY() - btCopy.getHeight() - 20);
		btCopy.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Loading.ins.show(ViewSellGiftCode.this);
			}
		});

		tbGiftCode.add(tfNewGiftCode).width(getWidth() - 20).top().colspan(2);
		tbGiftCode.row();
		tbGiftCode.add(btSendMessage).width(180).height(45).pad(10);
		tbGiftCode.add(btCopy).width(150).height(45).pad(10);

		page = new Page(getWidth(), 60);
		float[] widthColNormal = { 50, 120, 100, 150, 150, 150, 120 };
		tableNormal = new AbstractTable(new Table(), widthColNormal);
		String[] titleNormal = { "STT", "Gift code", "Tiền", "Tiền trong game",
				"Ngày hết hạn", "" };
		tableNormal.setTitle(titleNormal);
		page.setListener(new ItemListener() {
			@Override
			public void onItemClick() {
				tableNormal.setScrollX(0);
				tableNormal.setScrollY(0);
				tableNormal.addAction(Actions.sequence(
						Actions.alpha(0, 0.5f, Interpolation.exp5Out),
						Actions.alpha(1, 0.5f, Interpolation.exp5Out)));
				tableNormal.removeAll();
				for (int i = 0; i < page.getCurrentDataPage().size(); i++) {
					ItemTable item = page.getCurrentDataPage().get(i);
					tableNormal.addItem(item);
				}
			}
		});
		tableNormal.setScrollX(0);
		tableNormal.setScrollY(0);

		top();
		this.add(tbGenerate).width(tbGenerate.getWidth()).top().padTop(1)
				.height(140);
		row();
		add(tbGiftCode).width(tbGiftCode.getWidth()).height(120).padTop(5)
				.top();
		add(tableNormal).padTop(10).height(580).row();
		add(page);

		hideButton();
		return this;
	}

	public void showButton() {
		btCopy.setVisible(true);
		btSendMessage.setVisible(true);
	}

	public void hideButton() {
		btCopy.setVisible(false);
		btSendMessage.setVisible(false);
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
		if (lbAdminMoney.getText().equals("") && !UserInfo.currency.equals("")) {
			lbAdminMoney
					.setText("/" + UserInfo.money + " " + UserInfo.currency);
		}
		if (responeGenerateGiftCode != null) {
			Loading.ins.hide();
			if (responeGenerateGiftCode.getBoolean(ExtParamsKey.RESULT)) {

				rateMoney = responeGenerateGiftCode
						.getFloat(ExtParamsKey.MONEY_IN_GAME) / 1000;
				lbMoneyInGame.setText((long) rateMoney * pkAmount.getMoney()
						+ " chip");
			} else {
			}
			responeGenerateGiftCode = null;
		}    

		if (respone != null) {
			Loading.ins.hide();
			Boolean isSuccess = respone.getBoolean(ExtParamsKey.RESULT);
			String mess = respone.getString(ExtParamsKey.MESSAGE);
			Toast.makeText(getStage(), mess, Toast.LENGTH_SHORT);
			respone = null;
		}

		if (responeNormal != null) {
			Loading.ins.hide();
			page.removeAllPage();
			tableNormal.removeAll();
			boolean resut = responeNormal.getBoolean(ExtParamsKey.RESULT);
			if (resut) {
				JsonValue list = responeNormal.get(ExtParamsKey.LIST);
				if (list.size > 0) {
					for (int i = 0; i < list.size; i++) {
						JsonValue content = list.get(i);
						final String giftCode = content
								.getString(ExtParamsKey.GIFT_CODE);
						int money = content.getInt(ExtParamsKey.AMOUNT);
						String currency = content
								.getString(ExtParamsKey.CURRENCY);
						int money_in_game = content
								.getInt(ExtParamsKey.MONEY_IN_GAME);
						long time = content.getLong(ExtParamsKey.DATE_EXPIRE);

						final String id = content.getString(ExtParamsKey.ID);
						final ItemNonSellGiftCode item = new ItemNonSellGiftCode(
								tableNormal, new String[] {
										"" + (i + 1),
										giftCode,
										money + " " + currency,
										"" + money_in_game,
										DateTime.getStringDate(time,
												DateTime.FORMAT) });

						item.btReturn.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								super.clicked(event, x, y);
								DialogCustom dl = new DialogCustom("");
								dl.text("Bạn có chắc chắn muốn trả lại Gift Code này");
								dl.button("Ok", new Runnable() {

									@Override
									public void run() {
										Loading.ins.show(ViewSellGiftCode.this);
										// Request.getInstance().returnGiftCode(
										// AppPreference.instance.name,
										// id, new ReturnGiftCode());
									}
								});
								dl.button("Hủy");
								dl.show(getStage());
							}
						});
						page.addData(item);
					}
					page.init();
					for (int i = 0; i < page.getCurrentDataPage().size(); i++) {
						ItemTable item = page.getCurrentDataPage().get(i);
						tableNormal.addItem(item);
					}
				} else {
					Toast.makeText(_stage,
							"Bạn không có gift code nào chưa sử dụng",
							Toast.LENGTH_SHORT);
				}
			} else {
				String mess = responeNormal.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			}
			responeNormal = null;
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
		Loading.ins.show(this);
		lbAdminMoney.setText("/" + UserInfo.money + " " + UserInfo.currency);
		Request.getInstance().getNormalGiftCode(AppPreference.instance.name, 0,
				new GetGiftCodeNormal());
		Request.getInstance().getExchangeInGame(1000, UserInfo.currency,
				new HttpResponseListener() {

					@Override
					public void handleHttpResponse(HttpResponse httpResponse) {
						JsonValue respone = (new JsonReader())
								.parse(httpResponse.getResultAsString());
						if (respone.getBoolean(ExtParamsKey.RESULT)) {
							Log.d(respone.toString());
							rateMoney = respone
									.getFloat(ExtParamsKey.MONEY_IN_GAME) / 1000;
							lbMoneyInGame.setText((long) rateMoney
									* pkAmount.getMoney() + " chip");
							Loading.ins.hide();
						} else {
							Toast.makeText(getStage(),
									respone.getString(ExtParamsKey.MESSAGE),
									Toast.LENGTH_SHORT);
						}
					}

					@Override
					public void failed(Throwable t) {
						Loading.ins.hide();
					}

					@Override
					public void cancelled() {

					}
				});
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

	class GetGiftCodeNormal implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeNormal = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

	class ReturnGiftCode implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeReturn = (new JsonReader()).parse(httpResponse
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
