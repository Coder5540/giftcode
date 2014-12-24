package imp.view;

import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.Log;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.DialogCustom;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewAdminActive extends View {

	private JsonValue respone;
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
				210 };
		tableContent = new AbstractTable(new Table(), widthCol);
		String[] title = { "Số điện thoại", "Họ tên", "Số tiền", "Giới thiệu",
				"Cấp", "Địa chỉ", "Email", "Tên thiết bị", "Imei thiết bị",
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

						final DialogCustom dia = new DialogCustom("");
						dia.text("Bạn có chắc chắn khóa tài khoản " + phone
								+ " không?");
						dia.button("Ok", new Runnable() {
							@Override
							public void run() {
								getViewController().getView(
										ViewInfoDaiLySmall.class.getName())
										.hide(null);
								Loading.ins.show(ViewAdminActive.this);
								Request.getInstance().chaneStateAdmin(phone,
										AppPreference.instance.name,
										AppPreference.instance.pass,
										Constants.agency_type_lock,
										new LockListener());
							}
						});
						dia.button("Hủy");

						final ItemAdminActive newItem = new ItemAdminActive(
								tableContent,
								new String[] {
										infoUser.getString(ExtParamsKey.AGENCY_NAME),
										infoUser.getString(ExtParamsKey.FULL_NAME),
										Factory.getStrMoney(infoUser
												.getInt(ExtParamsKey.AMOUNT))
												+ " "
												+ infoUser
														.getString(ExtParamsKey.CURRENCY),
										infoUser.getString(ExtParamsKey.REF_CODE),
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
								TextButton btnLock = new TextButton("Khóa",
										this.btLock.getStyle());
								btnLock.addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event,
											float x, float y) {
										dia.show(getStage());
									}
								});
								view.add(btnLock).colspan(2).width(160)
										.height(50).pad(30);

							}
						};
						tableContent.addItem(newItem);

						newItem.btLock.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								dia.show(getStage());
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

	// @Override
	// public void setVisible(boolean visible) {
	// super.setVisible(visible);
	// if (visible == true) {
	// Loading.ins.show(this);
	// Request.getInstance().getListAgency(AppPreference.instance.name,
	// AppPreference.instance.pass, Constants.agency_type_active,
	// new Listener());
	// }
	// }

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
		toBack();
	}

}
