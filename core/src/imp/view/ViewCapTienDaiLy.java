package imp.view;

import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.fontType;
import utils.factory.Style;
import utils.keyboard.KeyboardConfig;
import utils.keyboard.VirtualKeyboard.OnHideListener;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.Toast;

import com.aia.appsreport.component.table.AbstractTable;
import com.aia.appsreport.component.table.ItemInfoDaily;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.DialogCustom;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.ui.TextfieldStatic;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.View;

public class ViewCapTienDaiLy extends View {

	private ViewDetail viewDetail;
	private CustomTextField tfPerson;

	private AbstractTable tableDaily;

	private JsonValue responeGetDaily;
	private JsonValue responeGetDailyLower;
	private JsonValue responeCheck;

	@Override
	public String getLabel() {
		return "Cấp tiền cho đại lý";
	}

	public void buildComponent() {
		this.top();
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch)));

		Label lbPerson = new Label("Người nhận tiền", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, fontType.Regular),
				Color.BLACK));
		tfPerson = new CustomTextField("", Style.ins.getTextFieldStyle(8,
				Assets.instance.fontFactory.getFont(25, fontType.Light)));
		tfPerson.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tfPerson.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				AbstractGameScreen.keyboard.registerTextField(tfPerson,
						KeyboardConfig.NORMAL, KeyboardConfig.SINGLE_LINE);
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		final CustomTextField tfMoney = new CustomTextField(
				"",
				Style.ins.getTextFieldStyle(8,
						Assets.instance.fontFactory.getFont(25, fontType.Light)));
		tfMoney.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tfMoney.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				AbstractGameScreen.keyboard.registerTextField(tfMoney,
						KeyboardConfig.NORMAL, KeyboardConfig.SINGLE_LINE);
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		TextButton btSend = new TextButton("Tìm kiếm",
				Style.ins.textButtonStyle);
		btSend.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (tfPerson.getText().equalsIgnoreCase("")) {
					AbstractGameScreen.keyboard.hide();
					Toast.makeText(_stage, "Vui lòng nhập đủ thông tin",
							Toast.LENGTH_SHORT);
				} else {
					AbstractGameScreen.keyboard.hide();
					Loading.ins.show(ViewCapTienDaiLy.this);
					Request.getInstance().checkDaily(
							AppPreference.instance.name, tfPerson.getText(),
							new CheckDaily());
				}
			}
		});

		Label lbTitle = new Label("Danh sách các đại lý cấp dưới",
				new LabelStyle(Assets.instance.fontFactory.getFont(30,
						fontType.Medium), Color.BLUE));

		float[] widthCol = { 50, 180, 150, 98 };
		tableDaily = new AbstractTable(new Table(), widthCol);
		String[] title = { "STT", "Tên đại lý", "Số điện thoại", "" };
		tableDaily.setTitle(title);

		viewDetail = new ViewDetail();

		this.padTop(15);
		this.add(lbPerson).right();
		this.add(tfPerson).padLeft(10).width(250).height(35).left().row();
		this.add(btSend).padTop(10).width(120).height(40).colspan(2).row();
		this.add(lbTitle).padTop(15).colspan(2).row();
		this.add(tableDaily).padTop(10).colspan(2);
		this.addActor(viewDetail);
		Request.getInstance().getLowerDaily(AppPreference.instance.name,
				AppPreference.instance.pass, -1, new GetDailyLower());
	}

	@Override
	public void update(float delta) {
		viewDetail.update(delta);
		if (responeGetDailyLower != null) {
			Loading.ins.hide();
			boolean result = responeGetDailyLower
					.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				JsonValue listDaily = responeGetDailyLower
						.get(ExtParamsKey.LIST);
				if (listDaily.size > 0) {
					for (int i = 0; i < listDaily.size; i++) {
						final String nameDaily = responeGetDaily
								.getString(ExtParamsKey.FULL_NAME);
						final String address = responeGetDaily
								.getString(ExtParamsKey.ADDRESS);
						final String level = responeGetDaily
								.getString(ExtParamsKey.ROLE_NAME);
						final String phone = responeGetDaily
								.getString(ExtParamsKey.AGENCY_NAME);
						final String sdtGt = responeGetDaily
								.getString(ExtParamsKey.REF_CODE);
						final int money = responeGetDaily
								.getInt(ExtParamsKey.AMOUNT);
						final String currency = responeGetDaily
								.getString(ExtParamsKey.CURRENCY);
						final String email = responeGetDaily
								.getString(ExtParamsKey.EMAIL);
						final String deviceId = responeGetDaily
								.getString(ExtParamsKey.DEVICE_ID);
						final String deviceName = responeGetDaily
								.getString(ExtParamsKey.DEVICE_NAME);
						int state = responeGetDaily.getInt(ExtParamsKey.STATE);
						final String realState;
						if (state == 0) {
							realState = "Chưa kích hoạt";
						} else if (state == 1) {
							realState = "Hoạt động bình thường";
						} else {
							realState = "Bị khóa";
						}
						ItemInfoDaily item = new ItemInfoDaily(tableDaily,
								new String[] { nameDaily, phone });
						item.btSee.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								super.clicked(event, x, y);
								String[] info = { nameDaily, address, level,
										tfPerson.getText(), sdtGt,
										money + " " + currency, email,
										deviceId, deviceName, realState };
								viewDetail.money = money;
								viewDetail.setInfo(info);
								viewDetail.show(null);
							}
						});
						tableDaily.addItem(item);
					}
				} else {
					Toast.makeText(_stage, "Không có đại lý cấp dưới nào",
							Toast.LENGTH_SHORT);
				}
			} else {
				String mess = responeGetDailyLower
						.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			}
			responeGetDailyLower = null;
		}

		if (responeCheck != null) {
			boolean result = responeCheck.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				Request.getInstance().getInfoDaily(tfPerson.getText(),
						new GetDaily());
			} else {
				Loading.ins.hide();
				String mess = responeCheck.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			}
			responeCheck = null;
		}

		if (responeGetDaily != null) {
			Loading.ins.hide();
			boolean result = responeGetDaily.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				String nameDaily = responeGetDaily
						.getString(ExtParamsKey.FULL_NAME);
				String address = responeGetDaily
						.getString(ExtParamsKey.ADDRESS);
				String capDaily = responeGetDaily
						.getString(ExtParamsKey.ROLE_NAME);
				String sdtGt = responeGetDaily.getString(ExtParamsKey.REF_CODE);
				int money = responeGetDaily.getInt(ExtParamsKey.AMOUNT);
				String currency = responeGetDaily
						.getString(ExtParamsKey.CURRENCY);
				String email = responeGetDaily.getString(ExtParamsKey.EMAIL);
				String deviceId = responeGetDaily
						.getString(ExtParamsKey.DEVICE_ID);
				String deviceName = responeGetDaily
						.getString(ExtParamsKey.DEVICE_NAME);
				int state = responeGetDaily.getInt(ExtParamsKey.STATE);
				String stringState;
				if (state == 0) {
					stringState = "Chưa kích hoạt";
				} else if (state == 1) {
					stringState = "Hoạt động bình thường";
				} else {
					stringState = "Bị khóa";
				}
				
				String[] info = { nameDaily, address, capDaily,
						tfPerson.getText(), sdtGt, money + " " + currency,
						email, deviceId, deviceName, stringState };
				viewDetail.money = money;
				viewDetail.setInfo(info);
				viewDetail.show(null);
			} else {
				String mess = responeGetDaily.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			}
			responeGetDaily = null;
		}

	}

	class GetDailyLower implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeGetDailyLower = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}
	}

	class CheckDaily implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeCheck = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}
	}

	class GetDaily implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeGetDaily = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}
	}

	class ViewDetail extends View {

		Table tbContent;
		ScrollPane scroll;
		JsonValue responeSendMoney;
		Label[] lbTitle;
		TextfieldStatic[] lbInfo;
		int money;
		CustomTextField tfMoney;
		TextArea taNote;
		Image bg;

		public ViewDetail() {
			setVisible(false);
			setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
					- Constants.HEIGHT_ACTIONBAR);
			this.top();
			bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
					new Color(1, 1, 1, 1)));
			bg.setSize(getWidth(), getHeight());
			Label lbHeader = new Label("Thông tin đại lý", new LabelStyle(
					Assets.instance.fontFactory.getFont(30, fontType.Medium),
					Color.BLUE));
			lbTitle = new Label[12];
			lbInfo = new TextfieldStatic[10];

			String[] title = { "Tên đại lý", "Địa chỉ đại lý", "Cấp đại lý",
					"Số điện thoại đại lý", "Số điện thoại người giới thiệu",
					"Số tiền trong tài khoản", "Email", "Imei thiết bị",
					"Tên thiết bị", "Trạng thái", "Số tiền cần chuyển",
					"Ghi chú" };
			for (int i = 0; i < lbTitle.length; i++) {
				lbTitle[i] = new Label("", new LabelStyle(
						Assets.instance.fontFactory.getFont(20,
								fontType.Regular), Color.GRAY));
				lbTitle[i].setWrap(true);
				lbTitle[i].setWidth(220);
				lbTitle[i].setText(title[i]);
			}

			for (int i = 0; i < lbInfo.length; i++) {
				lbInfo[i] = new TextfieldStatic("", Color.BLACK, 270);
			}
			lbInfo[4].setHeight(lbTitle[4].getTextBounds().height + 10);
			tfMoney = new CustomTextField("", Style.ins.getTextFieldStyle(8,
					Assets.instance.fontFactory.getFont(25, fontType.Light)));
			tfMoney.setOnscreenKeyboard(AbstractGameScreen.keyboard);
			tfMoney.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					AbstractGameScreen.keyboard.registerTextField(tfMoney,
							KeyboardConfig.NUMBER, KeyboardConfig.SINGLE_LINE);
					AbstractGameScreen.keyboard
							.setHideListener(new OnHideListener() {

								@Override
								public void hide() {
									getCell(scroll).height(getHeight());
									invalidate();
									layout();
								}
							});
					getCell(scroll).height(
							getHeight()
									- AbstractGameScreen.keyboard
											.getRealHeight());
					invalidate();
					layout();
					scroll.setScrollY(scroll.getMaxY());
					return super.touchDown(event, x, y, pointer, button);
				}
			});

			Label lbCurrency = new Label(UserInfo.currency, new LabelStyle(
					Assets.instance.fontFactory.getFont(20, fontType.Regular),
					Color.BLACK));

			taNote = new TextArea("", Style.ins.getTextFieldStyle(8,
					Assets.instance.fontFactory.getFont(25, fontType.Light)));
			taNote.setOnscreenKeyboard(AbstractGameScreen.keyboard);
			taNote.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					AbstractGameScreen.keyboard.registerTextField(taNote,
							KeyboardConfig.NORMAL, KeyboardConfig.MULTI_LINE);
					AbstractGameScreen.keyboard
							.setHideListener(new OnHideListener() {
								@Override
								public void hide() {
									getCell(scroll).height(getHeight());
									invalidate();
									layout();
								}
							});
					getCell(scroll).height(
							getHeight()
									- AbstractGameScreen.keyboard
											.getRealHeight());
					invalidate();
					layout();
					scroll.setScrollY(scroll.getMaxY());
					return super.touchDown(event, x, y, pointer, button);
				}
			});

			Table tbButton = new Table();
			TextButton btSend = new TextButton("Ok", Style.ins.textButtonStyle);
			TextButton btCancel = new TextButton("Hủy",
					Style.ins.textButtonStyle);
			tbButton.add(btSend).width(100).height(40);
			tbButton.add(btCancel).padLeft(5).width(100).height(40);
			btSend.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (!Factory.isNumeric(tfMoney.getText())) {
						Toast.makeText(getStage(),
								"Vui lòng nhập đúng số tiền",
								Toast.LENGTH_SHORT);
					} else {
						AbstractGameScreen.keyboard.hide();
						DialogCustom dl = new DialogCustom("");
						dl.text("Bạn có chắc chắn muốn chuyển "
								+ tfMoney.getText() + " USD " + " cho "
								+ tfPerson.getText());
						dl.button("Ok", new Runnable() {
							@Override
							public void run() {
								Loading.ins.show(ViewDetail.this);
								Request.getInstance().sendMoney(
										AppPreference.instance.name,
										AppPreference.instance.pass,
										tfPerson.getText(), tfMoney.getText(),
										"USD", taNote.getText(),
										new SendMoney());
							}
						});
						dl.button("Hủy");
						dl.show(getStage());
					}
					super.clicked(event, x, y);
				}
			});
			btCancel.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					hide(null);
					AbstractGameScreen.keyboard.hide();
				}
			});

			tbContent = new Table();
			scroll = new ScrollPane(tbContent);

			this.addActor(bg);
			this.add(scroll);
			tbContent.add(lbHeader).padTop(10).colspan(3).padBottom(20).row();
			for (int i = 0; i < 10; i++) {
				tbContent.add(lbTitle[i]).width(180).padTop(5);
				tbContent.add(lbInfo[i]).padLeft(5).padTop(5).colspan(2).left()
						.row();
			}
			tbContent.add(lbTitle[10]).width(180).padTop(5);
			tbContent.add(tfMoney).left().width(200).padTop(5).height(35);
			tbContent.add(lbCurrency).padLeft(5).row();
			tbContent.add(lbTitle[11]).width(180).padTop(5);
			tbContent.add(taNote).width(270).padTop(5).height(100).left()
					.colspan(2).row();
			tbContent.add(tbButton).padTop(20).colspan(3);
		}

		void setInfo(String[] info) {
			for (int i = 0; i < lbInfo.length; i++) {
				lbInfo[i].setContent(info[i]);
				tbContent.getCell(lbInfo[i]).height(lbInfo[i].getHeight());
				invalidate();
			}
		}

		@Override
		public void update(float delta) {
			if (responeSendMoney != null) {
				Loading.ins.hide();
				boolean result = responeSendMoney
						.getBoolean(ExtParamsKey.RESULT);
				if (result) {
					int moneyTrans = responeSendMoney
							.getInt(ExtParamsKey.MONEY_TRANSFER);
					money += moneyTrans;
					lbInfo[5].setContent("" + money);
				}
				String mess = responeSendMoney.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(getStage(), mess, Toast.LENGTH_SHORT);
				responeSendMoney = null;
			}
		}

		@Override
		public void show(OnCompleteListener listener) {
			toFront();
			this.setViewState(ViewState.SHOW);
			setVisible(true);
			tfMoney.setText("");
			taNote.setText("");
			this.setPosition(getWidth(), 0);
			this.clearActions();
			this.addAction(Actions.moveTo(0, 0, 0.5f, Interpolation.exp10Out));
		}

		@Override
		public void hide(OnCompleteListener listener) {
			this.setViewState(ViewState.HIDE);
			this.addAction(Actions.sequence(
					Actions.moveTo(getWidth(), 0, 0.5f, Interpolation.exp10Out),
					Actions.visible(false)));
		}

		class SendMoney implements HttpResponseListener {

			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				responeSendMoney = (new JsonReader()).parse(httpResponse
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

	@Override
	public void hide(OnCompleteListener listener) {
		setViewState(ViewState.HIDE);
		setTouchable(Touchable.disabled);
		TraceView.instance.removeView(this.getName());
		getViewController().removeView(name);
	}
}
