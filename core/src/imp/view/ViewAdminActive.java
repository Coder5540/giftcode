package imp.view;

import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.Log;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.Toast;

import com.aia.appsreport.component.table.AbstractTable;
import com.aia.appsreport.component.table.ItemAdminActive;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewAdminActive extends View {

	private JsonValue respone;
	private JsonValue responeLock;
	private JsonValue responeUnlock;
	Table content = new Table();
	private AbstractTable tableContent;

	@Override
	public String getLabel() {
		return "Đại lý đang hoạt động";
	}

	public View buildComponent() {

		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);

		float[] widthCol = { 150, 150, 150, 200, 100, 200, 170, 170, 170, 170,
				170, 170, 210 };
		tableContent = new AbstractTable(new Table(), widthCol);
		String[] title = { "Số điện thoại", "Họ tên", "Số tiền", "Giới thiệu",
				"Cấp", "Địa chỉ", "Email", "Thiết bị hoạt động",
				"Imei hoạt động", "Thiết bị khóa", "Imei khóa", "Tình trạng",
				"" };
		tableContent.setTitle(title);

		this.add(tableContent).padTop(5).width(getWidth())
				.height(getHeight() - 100).row();

		if (!getViewController().isContainView(
				ViewInfoDaiLySmall.class.getName())) {
			ViewInfoDaiLySmall view = new ViewInfoDaiLySmall();
			view.build(getStage(), getViewController(),
					ViewInfoDaiLySmall.class.getName(), new Rectangle(0, 0,
							Constants.WIDTH_SCREEN - 30,
							Constants.HEIGHT_SCREEN
									- Constants.HEIGHT_ACTIONBAR * 3));
			view.buildComponent();
		}
		return this;
	}

	@Override
	public void update(float deltaTime) {
		updateLock();
		if (respone != null) {
			Loading.ins.hide();
			Log.d("" + respone.toString());
			Boolean isSuccess = respone.getBoolean(ExtParamsKey.RESULT);
			String mess = respone.getString(ExtParamsKey.MESSAGE);
			Toast.makeText(getStage(), mess, Toast.LENGTH_SHORT);
			if (isSuccess) {
				tableContent.removeAll();
				JsonValue content = respone.get(ExtParamsKey.LIST);
				if (content.size > 0) {
					for (int i = 0; i < content.size; i++) {
						final JsonValue infoUser = content.get(i);
						final String phone = infoUser
								.getString(ExtParamsKey.AGENCY_NAME);

						final ItemAdminActive newItem = new ItemAdminActive(
								tableContent,
								new String[] {
										infoUser.getString(ExtParamsKey.AGENCY_NAME),
										infoUser.getString(ExtParamsKey.FULL_NAME),
										Factory.getDotMoney(infoUser
												.getLong(ExtParamsKey.AMOUNT))
												+ " "
												+ infoUser
														.getString(ExtParamsKey.CURRENCY),
										infoUser.getString(ExtParamsKey.REF_CODE),
										infoUser.getString(ExtParamsKey.ROLE_NAME),
										infoUser.getString(ExtParamsKey.ADDRESS),
										infoUser.getString(ExtParamsKey.EMAIL),
										Factory.getDeviceName(infoUser),
										Factory.getDeviceID(infoUser),
										Factory.getDeviceNameBlock(infoUser),
										Factory.getDeviceIDBlock(infoUser),
										infoUser.getString(ExtParamsKey.STATE) }) {
							@Override
							public void click() {
								if (!getViewController().isContainView(
										ViewInfoDaiLySmall.class.getName())) {
									ViewInfoDaiLySmall view = new ViewInfoDaiLySmall();
									view.build(
											getStage(),
											getViewController(),
											ViewInfoDaiLySmall.class.getName(),
											new Rectangle(
													0,
													0,
													Constants.WIDTH_SCREEN - 30,
													Constants.HEIGHT_SCREEN
															- Constants.HEIGHT_ACTIONBAR
															* 3));
								}

								ViewInfoDaiLySmall view = ((ViewInfoDaiLySmall) getViewController()
										.getView(
												ViewInfoDaiLySmall.class
														.getName()))
										.show(infoUser
												.getString(ExtParamsKey.FULL_NAME),
												infoUser.getString(ExtParamsKey.ADDRESS),
												infoUser.getString(ExtParamsKey.ROLE_NAME),
												infoUser.getString(ExtParamsKey.AGENCY_NAME),
												infoUser.getString(ExtParamsKey.REF_CODE),
												infoUser.getString(ExtParamsKey.AMOUNT),
												infoUser.getString(ExtParamsKey.EMAIL),
												Factory.getDeviceName(infoUser),
												Factory.getDeviceID(infoUser),
												Factory.getDeviceNameBlock(infoUser),
												Factory.getDeviceIDBlock(infoUser),

												infoUser.getString(ExtParamsKey.STATE));
								// TextButton btnLock = new TextButton("Khóa",
								// this.btLock.getStyle());
								// btnLock.addListener(new ClickListener() {
								// @Override
								// public void clicked(InputEvent event,
								// float x, float y) {
								// onBtnLockClicked(phone, infoUser);
								// }
								// });
								// view.add(btnLock).width(160).height(50).pad(30);
								//
								// TextButton btnUnlock = new TextButton(
								// "Mở khóa", this.btLock.getStyle());
								// btnUnlock.addListener(new ClickListener() {
								// @Override
								// public void clicked(InputEvent event,
								// float x, float y) {
								// onBtnUnlockClicked(phone, infoUser);
								// }
								// });
								// view.add(btnUnlock).width(160).height(50)
								// .pad(30);

								view.addButton("Khóa", new ClickListener() {
									@Override
									public void clicked(InputEvent event,
											float x, float y) {
										onBtnLockClicked(phone, infoUser);
									}
								});
								view.addButton("Mở khóa", new ClickListener() {
									@Override
									public void clicked(InputEvent event,
											float x, float y) {
										onBtnUnlockClicked(phone, infoUser);
									}
								});
							}
						};
						tableContent.addItem(newItem);

						newItem.btLock.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								onBtnLockClicked(phone, infoUser);
							}
						});
						newItem.btUnlock.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								onBtnUnlockClicked(phone, infoUser);
							}
						});
					}
				} else {
					Toast.makeText(getStage(), "Không tìm thấy tài khoản nào",
							Toast.LENGTH_SHORT);
				}
			}
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
