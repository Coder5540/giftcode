package imp.view;

import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Style;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.Toast;

import com.aia.appsreport.component.list.ItemList;
import com.aia.appsreport.component.list.ListDetail;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.listener.OnSelectListener;
import com.coder5560.game.ui.DialogCustom;
import com.coder5560.game.ui.ItemListener;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.ui.PageV3;
import com.coder5560.game.views.View;

public class ViewAdminActive extends View {

	private JsonValue respone;
	private JsonValue responeLock;
	private JsonValue responeUnlock;

	PageV3 pages;
	ListDetail listDetail;

	@Override
	public String getLabel() {
		return "Đại lý đang hoạt động";
	}

	public View buildComponent() {

		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);

		pages = new PageV3(getWidth(), 50);
		pages.init();
		pages.setListener(new ItemListener() {

			@Override
			public void onItemClick() {
				listDetail.addAction(Actions.sequence(
						Actions.alpha(0, 0.2f, Interpolation.exp5Out),
						Actions.run(new Runnable() {

							@Override
							public void run() {
								listDetail.setScrollX(0);
								listDetail.setScrollY(0);
								listDetail.table.clear();
								loadListDetail();
							}
						}), Actions.alpha(1, 0.2f, Interpolation.exp5Out)));
			}
		});
		pages.setPosition(0, 0);
		top();
		listDetail = new ListDetail(new Table(), new Rectangle(0,
				pages.getHeight(), getWidth(), getHeight() - pages.getHeight()));
		listDetail.table.top();
		add(listDetail).width(listDetail.getWidth())
				.height(listDetail.getHeight()).padTop(10).row();
		add(pages).width(getWidth());
		return this;
	}

	void loadListByName() {
		JsonValue json = respone;
		boolean result = json.getBoolean(ExtParamsKey.RESULT);
		String message = json.getString(ExtParamsKey.MESSAGE);

		if (result) {
			pages.removeAllPage();
			listDetail.table.clear();
			JsonValue content = json.get(ExtParamsKey.LIST);
			if (content.size > 0) {
				for (int i = 0; i < content.size; i++) {
					final JsonValue infoUser = content.get(i);
					pages.addData(infoUser);
				}
				pages.init();
				loadListDetail();
			} else {
				Toast.makeText(getStage(), "Không có dữ liệu !!!", 2f);
			}
		} else {
			Toast.makeText(getStage(), message, 3f);
		}
		Loading.ins.hide();
	}

	void loadListDetail() {
		for (int i = 0; i < pages.getCurrentDataPage().size(); i++) {
			final JsonValue data = pages.getCurrentDataPage().get(i);
			ItemList itemList = new ItemList(listDetail, listDetail.getWidth(),
					100);
			Table header = new Table();
			header.setSize(listDetail.getWidth(), 100);
			header.add(
					new Label(data.getString(ExtParamsKey.FULL_NAME), Style.ins
							.getLabelStyle(25, FontType.Regular, Color.WHITE)))
					.left().padLeft(20);
			header.add(
					new Label(
							"-" + data.getString(ExtParamsKey.ROLE_NAME) + "",
							Style.ins.getLabelStyle(20, FontType.Light,
									Color.WHITE))).expandX().right()
					.padRight(50);
			header.row();
			header.add(
					new Label(Factory.getDotMoney(Long.parseLong(data
							.getString(ExtParamsKey.AMOUNT)))
							+ " "
							+ data.getString(ExtParamsKey.CURRENCY), Style.ins
							.getLabelStyle(30, FontType.Bold, Color.WHITE)))
					.left().padLeft(20).padTop(10).colspan(2);

			itemList.addComponent(header, 0, 0);

			itemList.addSubItem(
					new Label("Số điện thoại", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(data.getString(ExtParamsKey.AGENCY_NAME),
							Style.ins.getLabelStyle(18, FontType.Light,
									Constants.COLOR_ACTIONBAR)), itemList
							.getWidth(), 25);

			itemList.addSubItem(
					new Label("Địa chỉ đại lý", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(data.getString(ExtParamsKey.ADDRESS), Style.ins
							.getLabelStyle(18, FontType.Light,
									Constants.COLOR_ACTIONBAR)), itemList
							.getWidth(), 25);
			itemList.addSubItem(
					new Label("Email", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			itemList.addSubItem(
					new Label(data.getString(ExtParamsKey.EMAIL), Style.ins
							.getLabelStyle(18, FontType.Light,
									Constants.COLOR_ACTIONBAR)), itemList
							.getWidth(), 25);
			itemList.addSubItem(new Label("Số điện thoại người giới thiệu",
					Style.ins.getLabelStyle(15, FontType.Light, Color.BLACK)),
					itemList.getWidth(), 25);
			itemList.addSubItem(
					new Label(data.getString(ExtParamsKey.REF_CODE), Style.ins
							.getLabelStyle(18, FontType.Light,
									Constants.COLOR_ACTIONBAR)), itemList
							.getWidth(), 25);
			itemList.addSubItem(
					new Label("Email thiết bị", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			String id[] = Factory.getDeviceID(data).split(",");
			for (String text : id) {
				if (text != "") {
					Label label = new Label(text, Style.ins.getLabelStyle(18,
							FontType.Light, Constants.COLOR_ACTIONBAR));
					itemList.addSubItem(label, itemList.getWidth(), 25);
				}
			}
			itemList.addSubItem(
					new Label("Tên thiết bị", Style.ins.getLabelStyle(15,
							FontType.Light, Color.BLACK)), itemList.getWidth(),
					25);
			String is = Factory.getDeviceName(data);
			id = Factory.getDeviceName(data).split(",");
			for (String text : id) {
				if (text != "") {
					Label label = new Label(text, Style.ins.getLabelStyle(18,
							FontType.Light, Constants.COLOR_ACTIONBAR));
					itemList.addSubItem(label, itemList.getWidth(), 25);
				}
			}
			itemList.addSubItem(
					new Label("Email thiết bị bị khóa", Style.ins
							.getLabelStyle(15, FontType.Light, Color.BLACK)),
					itemList.getWidth(), 25);
			id = Factory.getDeviceIDBlock(data).split(",");
			for (String text : id) {
				if (text != "") {
					Label label = new Label(text, Style.ins.getLabelStyle(18,
							FontType.Light, Constants.COLOR_ACTIONBAR));
					itemList.addSubItem(label, itemList.getWidth(), 25);
				}
			}
			itemList.addSubItem(
					new Label("Tên thiết bị bị khóa", Style.ins.getLabelStyle(
							15, FontType.Light, Color.BLACK)), itemList
							.getWidth(), 25);
			id = Factory.getDeviceNameBlock(data).split(",");
			for (String text : id) {
				if (text != "") {
					Label label = new Label(text, Style.ins.getLabelStyle(18,
							FontType.Light, Constants.COLOR_ACTIONBAR));
					itemList.addSubItem(label, itemList.getWidth(), 25);
				}
			}
			Table grButton = new Table();
			grButton.left();
			addButton("Khóa", new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					onBtnLockClicked(data.getString(ExtParamsKey.AGENCY_NAME),
							data);
				}
			}, grButton);
			addButton("Mở khóa", new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					onBtnUnlockClicked(
							data.getString(ExtParamsKey.AGENCY_NAME), data);
				}
			}, grButton);
			itemList.addSubItem(grButton, itemList.getWidth(), 70);
			listDetail.addItemMenu(itemList);
		}
	}

	public void addButton(String text, ClickListener listener, Table gr) {
		TextButton btn = new TextButton(text, Style.ins.textButtonStyle);
		btn.addListener(listener);
		if (btn.getLabel().getTextBounds().width > 100)
			gr.add(btn).padTop(5).padLeft(8).padRight(8)
					.width(btn.getLabel().getTextBounds().width + 10)
					.height(50);
		else
			gr.add(btn).padTop(5).padLeft(8).padRight(8).width(100).height(50);
	}

	@Override
	public void update(float deltaTime) {
		updateLock();
		if (respone != null) {
			loadListByName();
			// Loading.ins.hide();
			// Log.d("" + respone.toString());
			// Boolean isSuccess = respone.getBoolean(ExtParamsKey.RESULT);
			// String mess = respone.getString(ExtParamsKey.MESSAGE);
			// Toast.makeText(getStage(), mess, Toast.LENGTH_SHORT);
			// if (isSuccess) {
			// tableContent.removeAll();
			// JsonValue content = respone.get(ExtParamsKey.LIST);
			// if (content.size > 0) {
			//
			// } else {
			// Toast.makeText(getStage(), "Không tìm thấy tài khoản nào",
			// Toast.LENGTH_SHORT);
			// }
			// }
			respone = null;
		}
	}

	class Listener implements HttpResponseListener {

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

	class LockListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			Loading.ins.hide();
			JsonValue respone = (new JsonReader()).parse(httpResponse
					.getResultAsString());
			Toast.makeText(getStage(), respone.getString(ExtParamsKey.MESSAGE),
					Toast.LENGTH_SHORT);
			if (respone.getBoolean(ExtParamsKey.RESULT)) {
				Loading.ins.show(ViewAdminActive.this);
				Request.getInstance().getListAgency(
						AppPreference.instance.name,
						AppPreference.instance.pass,
						Constants.agency_type_active, new Listener());
			}
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
		Request.getInstance().getListAgency(AppPreference.instance.name,
				AppPreference.instance.pass, Constants.agency_type_active,
				new Listener());
		Loading.ins.show(this);
	}

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
		toBack();
	}

	String blockName = "";
	Array<String> IDs = new Array<String>();
	Array<String> Names = new Array<String>();

	void onBtnLockClicked(String phone, JsonValue infoUser) {
		Array<String> imeis = new Array<String>();
		JsonValue deviceIDs = infoUser.get(ExtParamsKey.DEVICE_ID);
		JsonValue deviceNames = infoUser.get(ExtParamsKey.DEVICE_NAME);
		blockName = phone;

		if (deviceIDs.size == 0 || deviceNames.size == 0
				|| Factory.getDeviceID(infoUser).equalsIgnoreCase("")
				|| Factory.getDeviceName(infoUser).equalsIgnoreCase("")) {
			showDialogWhenNoDevice("User không có thiết bị nào chưa khóa!");
			return;
		}

		for (int i = 0; i < deviceIDs.size; i++) {
			String imei = deviceNames.getString(i) + " - "
					+ deviceIDs.getString(i);
			imeis.add(imei);
			IDs.add(deviceIDs.getString(i));
			Names.add(deviceNames.getString(i));
		}

		BlockView blockView = new BlockView(1, phone, imeis);
		blockView.build(getStage(), getViewController(), "block",
				new Rectangle(0, 0, Constants.WIDTH_SCREEN,
						Constants.HEIGHT_SCREEN - Constants.HEIGHT_ACTIONBAR));
		blockView.buildComponent();
		blockView.show(null);
		blockView.setOnSelectListener(onSelectLock);
	}

	void showDialogWhenNoDevice(String message) {
		final DialogCustom dia = new DialogCustom("");
		dia.text(message);
		dia.button("Ok", new Runnable() {
			@Override
			public void run() {

			}
		});
		dia.show(getStage());
	}

	private void onBtnUnlockClicked(String phone, JsonValue infoUser) {
		Array<String> imeis = new Array<String>();
		JsonValue deviceIDs = infoUser.get(ExtParamsKey.DEVICE_ID_BLOCK);
		JsonValue deviceNames = infoUser.get(ExtParamsKey.DEVICE_NAME_BLOCK);
		blockName = phone;
		if (deviceIDs.size == 0
				|| deviceNames.size == 0
				|| Factory.getListByKey(ExtParamsKey.DEVICE_ID_BLOCK, infoUser)
						.equalsIgnoreCase("")
				|| Factory.getListByKey(ExtParamsKey.DEVICE_NAME_BLOCK,
						infoUser).equalsIgnoreCase("")) {
			showDialogWhenNoDevice("User không có thiết bị nào bị khóa!");
			return;
		}

		for (int i = 0; i < deviceIDs.size; i++) {
			String imei = deviceNames.getString(i) + " - "
					+ deviceIDs.getString(i);
			imeis.add(imei);
			IDs.add(deviceIDs.getString(i));
			Names.add(deviceNames.getString(i));
		}

		BlockView blockView = new BlockView(2, phone, imeis);
		blockView.build(getStage(), getViewController(), "block",
				new Rectangle(0, 0, Constants.WIDTH_SCREEN,
						Constants.HEIGHT_SCREEN - Constants.HEIGHT_ACTIONBAR));
		blockView.buildComponent();
		blockView.show(null);
		blockView.setOnSelectListener(onSelectUnLock);
	}

	public void updateLock() {
		if (responeLock != null) {
			boolean result = responeLock.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				final String message = responeLock
						.getString(ExtParamsKey.MESSAGE);
				getViewController().getView(ViewInfoDaiLySmall.class.getName())
						.hide(new OnCompleteListener() {

							@Override
							public void onError() {

							}

							@Override
							public void done() {
								Toast.makeText(getStage(), message,
										Toast.LENGTH_SHORT);
							}
						});
				;

			} else {
				Toast.makeText(getStage(), "Lock device fail",
						Toast.LENGTH_SHORT);
			}
			responeLock = null;
		}
		if (responeUnlock != null) {
			boolean result = responeUnlock.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				final String message = responeUnlock
						.getString(ExtParamsKey.MESSAGE);
				getViewController().getView(ViewInfoDaiLySmall.class.getName())
						.hide(new OnCompleteListener() {

							@Override
							public void onError() {

							}

							@Override
							public void done() {
								Toast.makeText(getStage(), message,
										Toast.LENGTH_SHORT);
							}
						});
				;

			} else {
				Toast.makeText(getStage(), "Unlock device fail",
						Toast.LENGTH_SHORT);
			}
			responeUnlock = null;
		}
	}

	OnSelectListener onSelectLock = new OnSelectListener() {

		@Override
		public void onSelect(final int i) {
			final DialogCustom dia = new DialogCustom("");
			dia.text("Bạn có chắc chắn khóa thiết bị " + Names.get(i)
					+ " có IMEI : " + IDs.get(i) + " không?");
			dia.button("Ok", new Runnable() {
				@Override
				public void run() {
					try {
						Loading.ins.show(ViewAdminActive.this);
						getViewController().removeView("block");
						Request.getInstance().lockLoginDevice(UserInfo.phone,
								blockName, IDs.get(i), Names.get(i),
								new HttpResponseListener() {

									@Override
									public void handleHttpResponse(
											HttpResponse httpResponse) {
										Loading.ins.hide();
										responeLock = new JsonReader()
												.parse(httpResponse
														.getResultAsString());
									}

									@Override
									public void failed(Throwable t) {

									}

									@Override
									public void cancelled() {

									}
								});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			dia.button("Hủy");
			dia.show(getStage());
		}
	};
	OnSelectListener onSelectUnLock = new OnSelectListener() {

		@Override
		public void onSelect(final int i) {
			final DialogCustom dia = new DialogCustom("");
			dia.text("Bạn có chắc chắn mở khóa thiết bị " + Names.get(i)
					+ " có IMEI : " + IDs.get(i) + " không?");
			dia.button("Ok", new Runnable() {
				@Override
				public void run() {
					try {
						Loading.ins.show(ViewAdminActive.this);
						getViewController().removeView("block");
						Request.getInstance().unLockLoginDevice(UserInfo.phone,
								blockName, IDs.get(i), Names.get(i),
								new HttpResponseListener() {

									@Override
									public void handleHttpResponse(
											HttpResponse httpResponse) {
										Loading.ins.hide();
										responeUnlock = new JsonReader()
												.parse(httpResponse
														.getResultAsString());
									}

									@Override
									public void failed(Throwable t) {

									}

									@Override
									public void cancelled() {

									}
								});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			dia.button("Hủy");
			dia.show(getStage());
		}
	};
}
