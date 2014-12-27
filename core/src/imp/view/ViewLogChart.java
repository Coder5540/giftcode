package imp.view;

import imp.view.HomeViewV2.getMoneyInfoListener;

import java.util.ArrayList;

import utils.elements.ItemDatePartner;
import utils.elements.PartnerPicker;
import utils.elements.PartnerSelectBox;
import utils.factory.AppPreference;
import utils.factory.DateTime;
import utils.factory.FontFactory.fontType;
import utils.factory.StringSystem;
import utils.factory.StringUtil;
import utils.factory.Style;
import utils.keyboard.KeyboardConfig;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.AbstractGameScreen;
import utils.screen.Toast;

import com.aia.appsreport.component.chart.ColumnChartComponent;
import com.aia.appsreport.component.chart.ColumnChartGroup;
import com.aia.appsreport.component.chart.ColumnComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.ColumnChartView;
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.DatePicker;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewLogChart extends View {
	int						typeView;
	public static final int	TYPE_SEND_MONEY			= 0;
	public static final int	TYPE_RECEIVE_MONEY		= 1;
	public static final int	TYPE_GIFTCODE			= 2;

	String					response				= "";
	boolean					isLoadTotal				= false;
	String					responese				= "";
	boolean					isLoad					= false;
	ScrollPane				scroll;
	Table					content;
	Group					gTop;
	DatePicker				dateFrom;
	DatePicker				dateTo;
	CustomTextField			tfSearch;
	PartnerPicker			partner;
	PartnerPicker			partnerFun;
	TextButton				btnXem;
	Group					gExtendDate;
	boolean					isExtend				= false;
	boolean					isChange;
	String					stateofpartnerFun		= "0";
	String					laststateofpartnerFun	= "0";

	Image					iconextendDate;
	PartnerSelectBox		quickDatePicker;
	boolean					isChangeFun;
	int						stateFun;

	Label					titleView;

	Table					gBottom;
	Image					icon;
	Label					lbTitle;
	Label					lbContent;

	long					totalMoney				= 0;
	String username = "";

	public ViewLogChart buildComponent(int type) {
		this.typeView = type;
		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);

		gTop = new Group();
		gTop.setSize(getWidth(), 110);
		Image bgTop = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch3, 6, 6, 6, 6));
		bgTop.setSize(getWidth() + 4, gTop.getHeight());
		bgTop.setColor(245 / 255f, 245 / 255f, 245 / 255f, 1);
		Image bgDatetime = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch, new Color(245 / 255f,
						245 / 255f, 245 / 255f, 1)));
		bgDatetime.setSize(getWidth(), 60);
		bgDatetime.setPosition(0, gTop.getHeight() - bgDatetime.getHeight());
		dateFrom = new DatePicker();
		dateTo = new DatePicker();
		dateFrom.setDate(DateTime.getDateBefor("dd", 7),
				DateTime.getDateBefor("MM", 7),
				DateTime.getDateBefor("yyyy", 7));
		dateTo.setDate(DateTime.getCurrentDate("dd"),
				DateTime.getCurrentDate("MM"), DateTime.getCurrentDate("yyyy"));
		dateFrom.setSize(Constants.WIDTH_SCREEN / 2 - 5, 50);
		dateTo.setSize(Constants.WIDTH_SCREEN / 2 - 5, 50);
		dateFrom.setStartString("");
		dateTo.setStartString("");
		dateFrom.setPosition(30, bgTop.getHeight() - dateFrom.getHeight() - 5);
		dateTo.setPosition(dateFrom.getX() + dateFrom.getWidth() - 25,
				bgTop.getHeight() - dateTo.getHeight() - 5);
		final Image bgiconextendDate = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch));
		bgiconextendDate.setSize(50, 50);
		bgiconextendDate.setPosition(getWidth() - bgiconextendDate.getWidth(),
				dateFrom.getY());
		bgiconextendDate.setColor(245 / 255f, 245 / 255f, 245 / 255f, 1);
		iconextendDate = new Image(new TextureRegion(new Texture(
				Gdx.files.internal("Img/play.png"))));
		iconextendDate.setSize(25, 25);
		iconextendDate.setOrigin(iconextendDate.getWidth() / 2,
				iconextendDate.getHeight() / 2);
		iconextendDate.setColor(Color.GRAY);
		iconextendDate.getColor().a = 0.5f;
		iconextendDate.setPosition(
				getWidth() - iconextendDate.getWidth() - 10,
				dateFrom.getY() + dateFrom.getHeight() / 2
						- iconextendDate.getHeight() / 2);
		iconextendDate.setRotation(270);
		bgiconextendDate.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				iconextendDate.getColor().a = 0.25f;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				iconextendDate.getColor().a = 0.5f;
				if (isExtend) {
					gExtendDate.clearActions();
					iconextendDate.clearActions();
					gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY(),
							0.15f));
					gExtendDate.addAction(Actions.alpha(0, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(270, 0.15f));
					isExtend = false;
				} else {
					gExtendDate.clearActions();
					iconextendDate.clearActions();
					gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY()
							- gExtendDate.getHeight() - 5, 0.15f));
					gExtendDate.addAction(Actions.alpha(1, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(90, 0.15f));
					isExtend = true;
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		iconextendDate.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				iconextendDate.getColor().a = 0.25f;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				iconextendDate.getColor().a = 0.5f;
				if (isExtend) {
					gExtendDate.clearActions();
					iconextendDate.clearActions();
					gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY(),
							0.15f));
					gExtendDate.addAction(Actions.alpha(0, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(270, 0.15f));
					isExtend = false;
				} else {
					gExtendDate.clearActions();
					iconextendDate.clearActions();
					gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY()
							- gExtendDate.getHeight(), 0.15f));
					gExtendDate.addAction(Actions.alpha(1, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(90, 0.15f));
					isExtend = true;
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		gExtendDate = new Group();
		gExtendDate.setSize(getWidth(), gTop.getHeight() - dateFrom.getY());
		gExtendDate.setTransform(true);
		Image bgExtendDate = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch));
		bgExtendDate.setColor(245 / 255f, 245 / 255f, 245 / 255f, 1);
		bgExtendDate.setSize(gExtendDate.getWidth(), gExtendDate.getHeight());
		Label lbExtendDate = new Label("Chọn nhanh", Style.ins.getLabelStyle(
				20, fontType.Regular, Color.BLACK));
		quickDatePicker = new PartnerSelectBox(Style.ins.selectBoxStyle);
		quickDatePicker.setSize(220, 40);
		quickDatePicker.addPartner(new ItemDatePartner(0));
		quickDatePicker.addPartner(new ItemDatePartner(1));
		quickDatePicker.addPartner(new ItemDatePartner(2));
		quickDatePicker.addPartner(new ItemDatePartner(3));
		quickDatePicker.addPartner(new ItemDatePartner(7));
		quickDatePicker.addPartner(new ItemDatePartner(14));
		quickDatePicker.addPartner(new ItemDatePartner(21));
		quickDatePicker.addPartner(new ItemDatePartner(28));
		quickDatePicker.setSelectedIndex(4);
		lbExtendDate.setPosition(55,
				gExtendDate.getHeight() / 2 - lbExtendDate.getHeight() / 2);
		quickDatePicker.setPosition(
				lbExtendDate.getX() + lbExtendDate.getWidth() + 25,
				gExtendDate.getHeight() / 2 - quickDatePicker.getHeight() / 2);
		quickDatePicker.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gExtendDate.clearActions();
				iconextendDate.clearActions();
				gExtendDate.addAction(Actions.moveTo(0, dateFrom.getY(), 0.15f));
				gExtendDate.addAction(Actions.alpha(0, 0.075f));
				iconextendDate.addAction(Actions.rotateTo(270, 0.15f));
				isExtend = false;
				dateFrom.setDate(DateTime
						.getDateBefor("dd", ((ItemDatePartner) quickDatePicker
								.getSelected()).daybefor), DateTime
						.getDateBefor("MM", ((ItemDatePartner) quickDatePicker
								.getSelected()).daybefor), DateTime
						.getDateBefor("yyyy",
								((ItemDatePartner) quickDatePicker
										.getSelected()).daybefor));
				dateTo.setDate(DateTime.getCurrentDate("dd"),
						DateTime.getCurrentDate("MM"),
						DateTime.getCurrentDate("yyyy"));
				getListTotalMoney();
			}
		});
		gExtendDate.addActor(bgExtendDate);
		gExtendDate.addActor(lbExtendDate);
		gExtendDate.addActor(quickDatePicker);

		gExtendDate.setPosition(0, dateFrom.getY());
		gExtendDate.getColor().a = 0f;

		partnerFun = new PartnerPicker(Style.ins.selectBoxStyle);
		partnerFun.setSize(2 * getWidth() / 5, 38);
		partnerFun.setPosition(getWidth() / 2 - partnerFun.getWidth() / 2,
				dateFrom.getY() - 5 - partnerFun.getHeight());
		if (UserInfo.getInstance().getRoleId() == 3) {
			partnerFun.addPartner(0, AppPreference.instance.name, "-1");
			stateofpartnerFun = "-1";
		} else {
			stateofpartnerFun = "0";
			partnerFun.addPartner(0, "Tất cả", "0");
			partnerFun.addPartner(1, "Xem theo SDT", "1");
			partnerFun.addPartner(2, "Xem theo cấp", "2");
		}
		TextFieldStyle style = new TextFieldStyle();
		NinePatch ninepatch = new NinePatch(
				Assets.instance.getRegion("ninepatch_stock"), 4, 4, 4, 4);
		style.background = new NinePatchDrawable(ninepatch);
		style.background.setLeftWidth(10);
		style.cursor = new NinePatchDrawable(new NinePatch(
				Assets.instance.getRegion("bg_white"), Color.GRAY));
		style.font = Assets.instance.fontFactory.getFont(20, fontType.Regular);
		style.fontColor = Color.BLACK;

		tfSearch = new CustomTextField("", style);
		tfSearch.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		tfSearch.setMessageText("Số điện thoại");
		tfSearch.setSize(2 * getWidth() / 5, 40);
		tfSearch.setPosition(getWidth() / 2 - tfSearch.getWidth() / 2,
				dateTo.getY() - tfSearch.getHeight() - 5);
		tfSearch.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				AbstractGameScreen.keyboard.registerTextField(tfSearch,
						"tfSearch", KeyboardConfig.NORMAL,
						KeyboardConfig.SINGLE_LINE);
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		partner = new PartnerPicker(Style.ins.selectBoxStyle);
		partner.setSize(2 * getWidth() / 5, 38);
		partner.setPosition(getWidth() / 2 - partner.getWidth() / 2,
				dateTo.getY() - 5 - partner.getHeight());
		btnXem = new TextButton("Xem", Style.ins.textButtonStyle);
		btnXem.setSize(2 * getWidth() / 5, 40);
		btnXem.setPosition(getWidth() / 2 - btnXem.getWidth() / 2,
				bgTop.getY() + 10);
		btnXem.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getListTotalMoney();
				AbstractGameScreen.keyboard.hide();
				super.clicked(event, x, y);
			}
		});
		gTop.addActor(bgTop);
		gTop.addActor(btnXem);
		gTop.addActor(gExtendDate);
		gTop.addActor(dateFrom);
		gTop.addActor(dateTo);
		gTop.addActor(bgiconextendDate);
		gTop.addActor(iconextendDate);

		tfSearch.setVisible(false);
		partner.setVisible(false);
		top();
		add(gTop).width(gTop.getWidth()).padLeft(-3).row();

		titleView = new Label("Báo cáo doanh thu", Style.ins.getLabelStyle(25,
				fontType.Regular, Color.GRAY));
		add(titleView).padTop(15).center().row();

		gBottom = new Table();
		gBottom.setBounds(0, 0, getWidth(), 60);
		gBottom.setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch4, 6, 6, 6, 6)));

		content = new Table();
		scroll = new ScrollPane(content);
		scroll.setBounds(0, 0, getWidth(), getHeight() - gTop.getHeight()
				- gBottom.getHeight() - titleView.getHeight());
		content.left();
		add(scroll).padTop(5).width(scroll.getWidth())
				.height(scroll.getHeight());

		addActor(gBottom);

		return this;
	}
	
	public void setUserName(String name){
		this.username = name;
	}

	public void setTitleView(String title) {
		titleView.setText(title);
	}

	public void setTotalMoney(long money) {
		totalMoney = money;
	}

	@Override
	public void back() {
		// TODO Auto-generated method stub
		getViewController().removeView(getName());
		super.back();
	}

	@Override
	public void show(OnCompleteListener listener) {
		getListTotalMoney();
		super.show(listener);
	}

	void getTotalMoneyInfo() {
		Request.getInstance().getTotalMoneyInfo(username,
				new getMoneyInfoListener());
	}

	void getListTotalMoney() {
		Request.getInstance().getListTotalMoney(username,
				dateFrom.getDate(), dateTo.getDate(),
				new getTotalMoneyListener());
		gBottom.setVisible(false);
		Loading.ins.show(this);
	}

	@Override
	public void update(float delta) {

		if (isLoadTotal) {
			JsonValue json = (new JsonReader()).parse(response);
			boolean result = json.getBoolean(ExtParamsKey.RESULT);
			String message = json.getString(ExtParamsKey.MESSAGE);
			if (result) {
				if (typeView == TYPE_RECEIVE_MONEY) {
					totalMoney = json.getLong(ExtParamsKey.MONEY_RECEIVE);
				} else if (typeView == TYPE_SEND_MONEY) {
					totalMoney = json.getLong(ExtParamsKey.MONEY_TRANSFER);
				} else if (typeView == TYPE_GIFTCODE) {
					totalMoney = json.getLong(ExtParamsKey.MONEY_GIFT_CODE);
				}
			} else {
				Toast.makeText(_stage, message, 3f);
			}
			isLoadTotal = false;
		}

		if (isLoad) {
			JsonValue json = (new JsonReader()).parse(responese);
			boolean result = json.getBoolean(ExtParamsKey.RESULT);
			String message = json.getString(ExtParamsKey.MESSAGE);
			if (result) {
				Toast.makeText(getStage(), message, 3f);
				content.clear();
				gBottom.clear();
				ColumnChartView columnchart = new ColumnChartView(
						scroll.getHeight() - 50);
				JsonValue arr = json.get(ExtParamsKey.LIST);
				int size = arr.size;
				ArrayList<String> titledown = new ArrayList<String>();
				ArrayList<Color> color = new ArrayList<Color>();
				ArrayList<String> dir = new ArrayList<String>();

				if (typeView == TYPE_SEND_MONEY) {
					dir.add("Tiền đã chuyển");
					color.add(new Color(255 / 255f, 48 / 255f, 48 / 255f, 1));
					columnchart.numbertype = 1;
				} else if (typeView == TYPE_RECEIVE_MONEY) {
					columnchart.numbertype = 1;
					dir.add("Tiền đã nhận");
					color.add(new Color(72 / 255f, 118 / 255f, 255 / 255f, 1));
				} else if (typeView == TYPE_GIFTCODE) {
					columnchart.numbertype = 1;
					dir.add("Tiền bán GiftCode");
					color.add(new Color(124 / 255f, 252 / 255f, 0 / 255f, 1));
				}
				if (size == 0) {
					Toast.makeText(getStage(), "Không có dữ liệu", 3);
					isLoad = false;
					return;
				}

				long totalMoneySend = 0;
				long totalMoneyReceive = 0;
				long totalMoneyGiftCode = 0;

				for (int i = 0; i < size; i++) {
					JsonValue content = arr.get(i);
					final String date = content.getString(ExtParamsKey.DATE);
					ColumnChartGroup colGroup = new ColumnChartGroup();

					if (typeView == TYPE_SEND_MONEY) {
						if (UserInfo.getInstance().getRoleId() != 3) {
							long money_send = Math.abs(content
									.getLong(ExtParamsKey.MONEY_TRANSFER));
							totalMoneySend += money_send;
							ColumnChartComponent colSend = new ColumnChartComponent();
							colSend.addListener(new ClickListener() {
								@Override
								public void clicked(InputEvent event, float x,
										float y) {
									if (_viewController
											.isContainView(StringSystem.VIEW_LOG_SEND_MONEY)) {
										((ViewLogTransferMoney) _viewController
												.getView(StringSystem.VIEW_LOG_SEND_MONEY))
												.setDate(date);
										((ViewLogTransferMoney) _viewController
												.getView(StringSystem.VIEW_LOG_SEND_MONEY))
												.setFun(1);
									} else {
										ViewLogTransferMoney viewLogTransferMoney = new ViewLogTransferMoney();
										viewLogTransferMoney
												.build(getStage(),
														_viewController,
														StringSystem.VIEW_LOG_SEND_MONEY,
														new Rectangle(
																0,
																0,
																Constants.WIDTH_SCREEN,
																Constants.HEIGHT_SCREEN
																		- Constants.HEIGHT_ACTIONBAR));
										viewLogTransferMoney
												.buildComponent(ViewLogTransferMoney.TYPE_SEND);
										viewLogTransferMoney.setDate(date);
										viewLogTransferMoney.setFun(1);
									}
									((ViewLogTransferMoney) _viewController
											.getView(StringSystem.VIEW_LOG_SEND_MONEY)).setUserName(username);
									((ViewLogTransferMoney) _viewController
											.getView(StringSystem.VIEW_LOG_SEND_MONEY))
											.show(null);
									super.clicked(event, x, y);
								}
							});
							colSend.addColumnComponent(new ColumnComponent(
									money_send, color.get(0)));
							colGroup.addComponent(colSend);
						}
					}
					if (typeView == TYPE_RECEIVE_MONEY) {
						if (UserInfo.getInstance().getRoleId() != 0) {
							long money_receive = Math.abs(content
									.getLong(ExtParamsKey.MONEY_RECEIVE));
							totalMoneyReceive += money_receive;
							ColumnChartComponent colReceive = new ColumnChartComponent();
							colReceive.addColumnComponent(new ColumnComponent(
									money_receive, color.get(0)));
							colReceive.addListener(new ClickListener() {
								@Override
								public void clicked(InputEvent event, float x,
										float y) {
									System.out.println("click to receive");
									if (_viewController
											.isContainView(StringSystem.VIEW_LOG_RECEIVE_MONEY)) {
										((ViewLogTransferMoney) _viewController
												.getView(StringSystem.VIEW_LOG_RECEIVE_MONEY))
												.setDate(date);
										((ViewLogTransferMoney) _viewController
												.getView(StringSystem.VIEW_LOG_SEND_MONEY))
												.setFun(1);
									} else {
										ViewLogTransferMoney viewLogTransferMoney = new ViewLogTransferMoney();
										viewLogTransferMoney
												.build(getStage(),
														_viewController,
														StringSystem.VIEW_LOG_RECEIVE_MONEY,
														new Rectangle(
																0,
																0,
																Constants.WIDTH_SCREEN,
																Constants.HEIGHT_SCREEN
																		- Constants.HEIGHT_ACTIONBAR));
										viewLogTransferMoney
												.buildComponent(ViewLogTransferMoney.TYPE_RECEIVE);
										viewLogTransferMoney.setDate(date);
										viewLogTransferMoney.setFun(1);
									}
									((ViewLogTransferMoney) _viewController
											.getView(StringSystem.VIEW_LOG_RECEIVE_MONEY)).setUserName(username);
									((ViewLogTransferMoney) _viewController
											.getView(StringSystem.VIEW_LOG_RECEIVE_MONEY))
											.show(null);
									super.clicked(event, x, y);
								};
							});
							colGroup.addComponent(colReceive);
						}
					}

					if (typeView == TYPE_GIFTCODE) {
						if (UserInfo.getInstance().getRoleId() != 0) {
							long money_giftcode = Math.abs(content
									.getLong(ExtParamsKey.MONEY_GIFT_CODE));
							totalMoneyGiftCode += money_giftcode;
							ColumnChartComponent colGiftCode = new ColumnChartComponent();
							colGiftCode.addColumnComponent(new ColumnComponent(
									money_giftcode, color.get(0)));
							colGiftCode.addListener(new ClickListener() {
								@Override
								public void clicked(InputEvent event, float x,
										float y) {
									if (_viewController
											.isContainView(StringSystem.VIEW_LOG_MONEY_GIFTCODE)) {
										((ViewLogGiftCode) _viewController
												.getView(StringSystem.VIEW_LOG_MONEY_GIFTCODE))
												.setDate(date);
										((ViewLogGiftCode) _viewController
												.getView(StringSystem.VIEW_LOG_MONEY_GIFTCODE))
												.setFun(1);
									} else {
										ViewLogGiftCode viewlogGiftCode = new ViewLogGiftCode();
										viewlogGiftCode
												.build(getStage(),
														_viewController,
														StringSystem.VIEW_LOG_MONEY_GIFTCODE,
														new Rectangle(
																0,
																0,
																Constants.WIDTH_SCREEN,
																Constants.HEIGHT_SCREEN
																		- Constants.HEIGHT_ACTIONBAR));
										viewlogGiftCode.buildComponent();
										viewlogGiftCode.setDate(date);
										viewlogGiftCode.setFun(1);
									}
									((ViewLogGiftCode) _viewController
											.getView(StringSystem.VIEW_LOG_MONEY_GIFTCODE)).setUserName(username);
									((ViewLogGiftCode) _viewController
											.getView(StringSystem.VIEW_LOG_MONEY_GIFTCODE))
											.show(null);
									super.clicked(event, x, y);
								}
							});
							colGroup.addComponent(colGiftCode);
						}
					}

					columnchart.columnChart.addColumnChartComponent(colGroup);
					titledown.add(date);
				}
				float widthCol = 90;
				columnchart.columnChart.offsetX = 30;
				columnchart.validateComponent(widthCol, titledown, dir, color);
				content.add(columnchart);

				String title = "";
				if (typeView == TYPE_SEND_MONEY) {
					title = "Tổng tiền: "
							+ StringUtil.getDotMoney(totalMoneySend)
							+ UserInfo.currency + "/"
							+ StringUtil.getDotMoney(totalMoney)
							+ UserInfo.currency;
				} else if (typeView == TYPE_RECEIVE_MONEY) {
					title = "Tổng tiền: "
							+ StringUtil.getDotMoney(totalMoneyReceive)
							+ UserInfo.currency + "/"
							+ StringUtil.getDotMoney(totalMoney)
							+ UserInfo.currency;
				} else {
					title = "Tổng tiền: "
							+ StringUtil.getDotMoney(totalMoneyGiftCode)
							+ UserInfo.currency + "/"
							+ StringUtil.getDotMoney(totalMoney)
							+ UserInfo.currency;
				}
				Image icon = new Image(new Texture(
						Gdx.files.internal("Img/icon_tong.png")));
				icon.setSize(40, 40);

				Label lbTitle = new Label(title, Style.ins.getLabelStyle(20,
						fontType.Regular, Color.BLACK));

				gBottom.top().left();
				gBottom.add(icon);
				gBottom.add(lbTitle).padLeft(2);
				gBottom.setVisible(true);
			} else {
				Toast.makeText(getStage(), message, 3f);
			}
			Loading.ins.hide();
			isLoad = false;
		}
		super.update(delta);
	}

	class getTotalMoneyListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responese = httpResponse.getResultAsString();
			isLoad = true;
		}

		@Override
		public void failed(Throwable t) {
			// TODO Auto-generated method stub

		}

		@Override
		public void cancelled() {
			// TODO Auto-generated method stub

		}

	}

	class getMoneyInfoListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			// TODO Auto-generated method stub
			response = httpResponse.getResultAsString();
			isLoadTotal = true;
		}

		@Override
		public void failed(Throwable t) {
			// TODO Auto-generated method stub

		}

		@Override
		public void cancelled() {
			// TODO Auto-generated method stub

		}

	}

}
