package imp.view;

import utils.elements.ItemDatePartner;
import utils.elements.PartnerSelectBox;
import utils.factory.AppPreference;
import utils.factory.DateTime;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Log;
import utils.factory.Style;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.screen.AbstractGameScreen;

import com.aia.appsreport.component.list.ItemList;
import com.aia.appsreport.component.list.ListDetail;
import com.aia.appsreport.component.table.AbstractTable;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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
import com.coder5560.game.ui.CustomTextField;
import com.coder5560.game.ui.DatePicker;
import com.coder5560.game.ui.ItemListener;
import com.coder5560.game.ui.LabelButton;
import com.coder5560.game.views.View;

public class ViewHistoryCode extends View {
	JsonValue			responeData;
	AbstractTable		content;
	boolean				isLoad					= true;

	DatePicker			dateFrom;
	DatePicker			dateTo;
	CustomTextField		tfSearch;
	TextButton			btnXem;
	Group				gExtendDate;
	boolean				isExtend				= false;
	Image				iconsortby;

	Actor				colHoten;
	Actor				colThoigian;
	int					sortby;
	int					sorttype;
	static final int	HOTEN					= 1;
	static final int	THOIGIAN				= 2;
	static final int	TOPDOWN					= 1;
	static final int	BOTTOMUP				= 2;

	float				widthCol[]				= { 250, 150, 200, 200, 220,
			220, 220, 150, 150					};
	String				title[]					= { "Code ID", "Số điện thoại",
			"Họ tên", "Hoạt động", "Số tiền trước giao dịch",
			"Số tiền sau giao dịch", "Số tiền giao dịch", "Đơn vị", "Thời gian" };

	boolean				isLoadByName;
	String				responseByName;
	boolean				isLoadByRoleId;
	String				reponseByRoleId;

	boolean				isChange;
	String				stateofpartnerFun		= "0";
	String				laststateofpartnerFun	= "0";

	Image				iconextendDate;
	PartnerSelectBox	quickDatePicker;
	boolean				isChangeFun;
	int					stateFun;
	private String		username;

	ListDetail			listDetail;

	public ViewHistoryCode buildComponent() {
		username = AppPreference.instance.name;
		top();
		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
				Color.WHITE));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);

		Group gTop = new Group();
		gTop.setSize(getWidth(), 160);
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
							- gExtendDate.getHeight() - 5, 0.15f));
					gExtendDate.addAction(Actions.alpha(1, 0.075f));
					iconextendDate.addAction(Actions.rotateTo(90, 0.15f));
					isExtend = true;
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});

		gExtendDate = new Group();
		gExtendDate.setSize(getWidth(), 90);
		gExtendDate.setTransform(true);
		Image bgExtendDate = new Image(new NinePatch(
				Assets.instance.ui.reg_ninepatch));
		bgExtendDate.setColor(245 / 255f, 245 / 255f, 245 / 255f, 1);
		bgExtendDate.setSize(gExtendDate.getWidth(), gExtendDate.getHeight());
		Label lbExtendDate = new Label("Chọn nhanh", Style.ins.getLabelStyle(
				20, FontType.Regular, Color.BLACK));
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
			}
		});

		LabelButton btnOk = new LabelButton("Chọn", Style.ins.getLabelStyle(20,
				FontType.Regular, Color.BLACK), 80, 40, LabelButton.CENTER);
		lbExtendDate.setPosition(55,
				gExtendDate.getHeight() / 2 - lbExtendDate.getHeight() / 2);
		quickDatePicker.setPosition(
				lbExtendDate.getX() + lbExtendDate.getWidth() + 25,
				gExtendDate.getHeight() / 2 - quickDatePicker.getHeight() / 2);
		btnOk.setPosition(getWidth() - btnOk.getWidth() - 20,
				quickDatePicker.getY());
		btnOk.setListener(new ItemListener() {

			@Override
			public void onItemClick() {

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
			}
		});
		gExtendDate.addActor(bgExtendDate);
		gExtendDate.addActor(lbExtendDate);
		gExtendDate.addActor(quickDatePicker);

		gExtendDate.setPosition(0, dateFrom.getY());
		gExtendDate.getColor().a = 0f;

		TextFieldStyle style = new TextFieldStyle();
		NinePatch ninepatch = new NinePatch(
				Assets.instance.getRegion("ninepatch_stock"), 4, 4, 4, 4);
		style.background = new NinePatchDrawable(ninepatch);
		style.background.setLeftWidth(10);
		style.cursor = new NinePatchDrawable(new NinePatch(
				Assets.instance.getRegion("bg_white"), Color.GRAY));
		style.font = Assets.instance.fontFactory.getFont(20, FontType.Regular);
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
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		btnXem = new TextButton("Xem", Style.ins.textButtonStyle);
		btnXem.setSize(2 * getWidth() / 5, 40);
		btnXem.setPosition(getWidth() / 2 - btnXem.getWidth() / 2,
				bgTop.getY() + 10);
		btnXem.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Request.getInstance().getCodeCashOutLog(
						AppPreference.instance.name, dateFrom.getDate(),
						dateTo.getDate(), getLogCodeListener);
				AbstractGameScreen.keyboard.hide();
				super.clicked(event, x, y);
			}
		});
		gTop.addActor(bgTop);
		gTop.addActor(tfSearch);
		gTop.addActor(btnXem);
		gTop.addActor(gExtendDate);
		gTop.addActor(bgDatetime);
		gTop.addActor(dateFrom);
		gTop.addActor(dateTo);
		gTop.addActor(bgiconextendDate);
		gTop.addActor(iconextendDate);

		tfSearch.setVisible(false);

		iconsortby = new Image(Assets.instance.getRegion("back"));
		iconsortby.setSize(20, 20);
		iconsortby.setOrigin(iconsortby.getWidth() / 2,
				iconsortby.getHeight() / 2);
		iconsortby.setColor(Color.BLACK);

		content = new AbstractTable(new Table(), widthCol);
		content.setTitle(title);

		listDetail = new ListDetail(new Table(), new Rectangle(0, 0,
				getWidth(), getHeight() - gTop.getHeight()));
		add(gTop).width(gTop.getWidth()).padLeft(-3).row();
		add(listDetail).width(listDetail.getWidth())
				.height(listDetail.getHeight()).row();
		Request.getInstance().getCodeCashOutLog(
				AppPreference.instance.name, dateFrom.getDate(),
				dateTo.getDate(), getLogCodeListener);
		return this;
	}

	LabelStyle	styleLabel			= Style.ins.getLabelStyle(16,
											FontType.Light, Color.BLACK);
	LabelStyle	styleDescription	= Style.ins.getLabelStyle(18,
											FontType.Light,
											Constants.COLOR_ACTIONBAR);

	void loadListDetail(JsonValue data) {
		for (int i = 0; i < data.size; i++) {
			JsonValue dataElement = data.get(i);
			ItemList itemList = new ItemList(listDetail, listDetail.getWidth(),
					80);
			itemList.btnExpand.setY(itemList.item.getY() + 15);
			itemList.btnExpand.setX(itemList.btnExpand.getX() - 5);
			Image iconUser = new Image(Assets.instance.ui.getIconUser());
			iconUser.setSize(18, 18);
			Image icon_coin = new Image(new Texture(
					Gdx.files.internal("Img/coin.png")));
			icon_coin.setSize(18, 18);
			// itemList.addComponent(iconUser, 13, 70);
			itemList.addComponent(
					new Label("Code" + " - "
							+ dataElement.getString(ExtParamsKey.CODE),
							Style.ins.getLabelStyle(22, FontType.Regular,
									Color.WHITE)), 40, 45);
			// itemList.addComponent(icon_coin, 13, 40);
			itemList.addComponent(
					new Label(dataElement.getInt(ExtParamsKey.AMOUNT) + " "
							+ dataElement.getString(ExtParamsKey.CURRENCY),
							Style.ins.getLabelStyle(18, FontType.Regular,
									Color.WHITE)), 40, 15);
			Label lbTime = new Label(Factory.getTime(dataElement
					.getLong(ExtParamsKey.GEN_DATE)), Style.ins.getLabelStyle(
					18, FontType.Regular, Color.WHITE));
			itemList.addComponent(lbTime,
					itemList.getWidth() - lbTime.getWidth() - 10, 45);
			itemList.addSubItem(
					new Label("Người nạp", styleLabel), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(
							dataElement.getString(ExtParamsKey.USER_CASH_OUT),
							styleDescription), itemList
							.getWidth(), 25);
			itemList.addSubItem(
					new Label("ID code", styleLabel), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(dataElement.getString(ExtParamsKey.ID), styleDescription), itemList
							.getWidth(), 25);

			itemList.addSubItem(
					new Label("Tiền trong game", styleLabel), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(""
							+ Factory.getDotMoney(dataElement
									.getInt(ExtParamsKey.MONEY_IN_GAME)),
									styleDescription), itemList
							.getWidth(), 25);

			itemList.addSubItem(
					new Label("Money Before", styleLabel), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(
							""
									+ Factory
											.getDotMoney(dataElement
													.getLong(ExtParamsKey.MONEY_IN_GAME_BEFORE))
									+ " Xu",styleDescription),
					itemList.getWidth(), 25);
			itemList.addSubItem(
					new Label("Money After", styleLabel), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(""
							+ Factory.getDotMoney(dataElement
									.getLong(ExtParamsKey.MONEY_IN_GAME_AFTER))
							+ " Xu", styleDescription),
					itemList.getWidth(), 25);

			listDetail.addItemMenu(itemList);
		}
	}

	public void setDate(String date) {
		dateFrom.setDate(date);
		dateTo.setDate(date);
	}

	@Override
	public void update(float delta) {
		if (responeData != null) {
			JsonValue jsonList = responeData.get(ExtParamsKey.LIST);
			listDetail.setScrollX(0);
			listDetail.setScrollY(0);
			listDetail.table.clear();
			loadListDetail(jsonList);
			responeData = null;
		}

		super.update(delta);
	}

	@Override
	public void show(OnCompleteListener listener) {
		super.show(listener);
	}

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
		getViewController().removeView(getName());
	}

	@Override
	public void back() {
		super.back();
	}

	@Override
	public String getLabel() {
		return "Lịch sử  Code";
	}

	
	
	class getListByName implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responseByName = httpResponse.getResultAsString();
			isLoadByName = true;
		}

		@Override
		public void failed(Throwable t) {
		}

		@Override
		public void cancelled() {
		}

	}

	class getListByRoleId implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responseByName = httpResponse.getResultAsString();
			isLoadByName = true;
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {
		}
	}

	public void setUserName(String username) {
		this.username = username;
	}

	HttpResponseListener getLogCodeListener = new HttpResponseListener() {

		@Override
		public void handleHttpResponse(
				HttpResponse httpResponse) {
			responeData = new JsonReader()
					.parse(httpResponse.getResultAsString());
			Log.d("Response", responeData.toString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}
	};
}

