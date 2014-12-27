package imp.view;

import utils.factory.AppPreference;
import utils.factory.Log;
import utils.factory.PlatformResolver.OnResultListener;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.screen.Toast;

import com.aia.appsreport.component.table.AbstractTable;
import com.aia.appsreport.component.table.ItemAdminRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.DialogCustom;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewAdminRequest extends View {

	private JsonValue respone, responeGetActiveCode;
	Table content = new Table();
	private AbstractTable tableContent;
	String activeCode = "";
	String phone = "";
	boolean isSend = false, isFailGetActive = false;

	@Override
	public String getLabel() {
		return "Đại lý chờ kích hoạt";
	}

	public View buildComponent() {

		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);

		float[] widthCol = { 150, 150, 200, 100, 200, 170, 170, 170, 170, 210 };
		tableContent = new AbstractTable(new Table(), widthCol);
		String[] title = { "Số điện thoại", "Giới thiệu", "Họ tên", "Cấp",
				"Địa chỉ", "Email", "Tên thiết bị", "Imei thiết bị",
				"Tình trạng", "" };
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

						final DialogCustom diaCancel = new DialogCustom("");
						diaCancel.text("Bạn có chắc chắn hủy đăng ký của "
								+ phone + " không?");
						diaCancel.button("Ok", new Runnable() {
							@Override
							public void run() {
								Loading.ins.show(ViewAdminRequest.this);
								Request.getInstance().rejectActive(phone,
										AppPreference.instance.name,
										AppPreference.instance.pass,
										new RejectListener());
								getViewController().getView(
										ViewInfoDaiLySmall.class.getName())
										.hide(null);
							}
						});
						diaCancel.button("Hủy");

						final DialogCustom diaActive = new DialogCustom("");
						diaActive
								.text("Bạn có chắc chắn thêm tài khoản? Một tin nhắn sẽ được tự động gửi đến số điện thoại: "
										+ phone);
						diaActive.button("Ok", new Runnable() {
							@Override
							public void run() {
								getViewController().getView(
										ViewInfoDaiLySmall.class.getName())
										.hide(null);
								if (!isFailGetActive) {
									Loading.ins.show(ViewAdminRequest.this);
									isSend = true;
								} else {
									Toast.makeText(
											getStage(),
											"Lỗi khi lấy active code. Vui lòng thử lại sau",
											Toast.LENGTH_SHORT);
								}
							}
						});
						diaActive.button("Hủy");

						ItemAdminRequest newItem = new ItemAdminRequest(
								tableContent,
								new String[] {
										infoUser.getString(ExtParamsKey.AGENCY_NAME),
										infoUser.getString(ExtParamsKey.REF_CODE),
										infoUser.getString(ExtParamsKey.FULL_NAME),
										infoUser.getString(ExtParamsKey.ROLE_NAME),
										infoUser.getString(ExtParamsKey.ADDRESS),
										infoUser.getString(ExtParamsKey.EMAIL),
										infoUser.getString(ExtParamsKey.DEVICE_NAME),
										infoUser.getString(ExtParamsKey.DEVICE_ID),
										infoUser.getString(ExtParamsKey.STATE) }) {
							@Override
							public void click() {
								View view = ((ViewInfoDaiLySmall) getViewController()
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
												infoUser.getString(ExtParamsKey.DEVICE_ID),
												infoUser.getString(ExtParamsKey.DEVICE_NAME),
												infoUser.getString(ExtParamsKey.STATE));
								TextButton btnActive = new TextButton(
										"Kích hoạt", this.btSet.getStyle());
								btnActive.addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event,
											float x, float y) {
										diaActive.show(getStage());
										final GetActiveCodeListener listener = new GetActiveCodeListener();
										isFailGetActive = false;
										Request.getInstance().getActiveCode(
												AppPreference.instance.name,
												AppPreference.instance.pass,
												phone, listener);

									}
								});
								TextButton btnCancel = new TextButton(
										"Hủy kích hoạt", this.btSet.getStyle());
								btnCancel.addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event,
											float x, float y) {
										diaCancel.show(getStage());
									}
								});
								view.add(btnActive).width(160).height(50)
										.padTop(30);
								view.add(btnCancel).width(160).height(50)
										.padTop(30);
							}
						};
						tableContent.addItem(newItem);
						newItem.btSet.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								diaActive.show(getStage());

								final GetActiveCodeListener listener = new GetActiveCodeListener();
								isFailGetActive = false;
								Request.getInstance().getActiveCode(
										AppPreference.instance.name,
										AppPreference.instance.pass, phone,
										listener);
							}
						});

						newItem.btDel.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								diaCancel.show(getStage());
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
		if (isSend && phone != "") {
			_viewController.getPlatformResolver().sendSMS(phone,
					"Mã kích hoạt tài khoản 8B8 GM của bạn là: " + activeCode,
					new OnResultListener() {
						@Override
						public void onError() {
							Loading.ins.hide();
							Toast.makeText(getStage(),
									"Không gửi được tin nhăn.Vui lòng thử lại",
									Toast.LENGTH_SHORT);
						}

						@Override
						public void onComplete() {
							Loading.ins.hide();
							Toast.makeText(getStage(),
									"Gửi tin nhắn thành công",
									Toast.LENGTH_SHORT);

							Request.getInstance().doneSendingActiveCode(phone,
									AppPreference.instance.name,
									AppPreference.instance.pass,
									new HttpResponseListener() {

										@Override
										public void handleHttpResponse(
												HttpResponse httpResponse) {
											Request.getInstance()
													.getListAgency(
															AppPreference.instance.name,
															AppPreference.instance.pass,
															Constants.agency_type_non_active,
															new Listener());
										}

										@Override
										public void failed(Throwable t) {

										}

										@Override
										public void cancelled() {

										}
									});
						}
					});
			isSend = false;
			phone = "";
			activeCode = "";
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

	class RejectListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			JsonValue respone = (new JsonReader()).parse(httpResponse
					.getResultAsString());
			Toast.makeText(getStage(), respone.getString(ExtParamsKey.MESSAGE),
					Toast.LENGTH_SHORT);
			Loading.ins.hide();
			if (respone.getBoolean(ExtParamsKey.RESULT)) {
				Loading.ins.show(ViewAdminRequest.this);
				Request.getInstance().getListAgency(
						AppPreference.instance.name,
						AppPreference.instance.pass,
						Constants.agency_type_non_active, new Listener());
			}
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

	class SendMessageListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

	class GetActiveCodeListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeGetActiveCode = (new JsonReader()).parse(httpResponse
					.getResultAsString());
			Log.d(responeGetActiveCode.toString());
			Loading.ins.hide();
			if (responeGetActiveCode.getBoolean(ExtParamsKey.RESULT)) {
				phone = responeGetActiveCode
						.getString(ExtParamsKey.AGENCY_NAME);
				activeCode = responeGetActiveCode
						.getString(ExtParamsKey.ACTIVE_CODE);
			} else {
				Toast.makeText(getStage(),
						responeGetActiveCode.getString(ExtParamsKey.MESSAGE),
						Toast.LENGTH_SHORT);
				Loading.ins.hide();
				isFailGetActive = true;
				if (isSend)
					isSend = false;
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
				AppPreference.instance.pass, Constants.agency_type_non_active,
				new Listener());
		Loading.ins.show(this);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible == true) {
			Loading.ins.show(this);
			Request.getInstance().getListAgency(AppPreference.instance.name,
					AppPreference.instance.pass,
					Constants.agency_type_non_active, new Listener());
		}
	}

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
		toBack();
	}
}
