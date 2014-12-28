package imp.view;

import java.util.HashMap;

import utils.elements.PartnerPicker;
import utils.factory.FontFactory.fontType;
import utils.factory.Style;
import utils.networks.ExtParamsKey;
import utils.networks.Request;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.RoleID;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.View;

public class ViewChangeRole extends View {
	public static int		change_role_false;
	private PartnerPicker	partnerRole;
	private JsonValue		responeChangeRole;
	OnCompleteListener		onCompleteListener;
	JsonValue				userData;
	private int				currentRole;

	public ViewChangeRole(OnCompleteListener onCompleteListener,
			JsonValue userData, int currentRole) {
		super();
		this.currentRole = currentRole;
		this.onCompleteListener = onCompleteListener;
		this.userData = userData;
	}

	public void buildComponent() {
		this.top();
		createPattenRole();
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch)));

		Label lbTitle = new Label("Thay đổi cấp tài khoản", new LabelStyle(
				Assets.instance.fontFactory.getFont(30, fontType.Regular),
				Color.BLUE));

		Label lbRole = new Label("Role : ", new LabelStyle(
				Assets.instance.fontFactory.getFont(20, fontType.Regular),
				Color.GRAY));

		Table tbButton = new Table();
		TextButton btOk = new TextButton("Ok", Style.ins.textButtonStyle);
		TextButton btCancel = new TextButton("Hủy", Style.ins.textButtonStyle);
		tbButton.add(btOk).width(100).height(40);
		tbButton.add(btCancel).width(100).padLeft(5).height(40);

		this.add(lbTitle).padTop(10).colspan(2).row();
		this.add(lbRole).right().padTop(10);
		this.add(partnerRole).left().padLeft(5).padTop(10).width(200)
				.height(35).row();
		this.add(tbButton).padTop(20).colspan(2);
		{

			btOk.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Request.getInstance().changeAgencyRoleName(
							userData.getString(ExtParamsKey.AGENCY_NAME),
							getRoleSelected(), new ChangeRole());
				}
			});
			btCancel.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					back();
				}
			});

		}
	}

	private int getRoleSelected() {
		return  partnerRole.getSelectedIndex();
	}

	private void createPattenRole() {
		partnerRole = new PartnerPicker(Style.ins.selectBoxStyle);
		switch (currentRole) {
			case RoleID.USER_MANAGER:
				return;
			case RoleID.AGENCY_LEVEL2:
				partnerRole.addPartner(0, "Money Manager", "");
				partnerRole.addPartner(1, "Admin", "");
				partnerRole.addPartner(2, "Đại lý cấp 1", "");
				break;
			case RoleID.AGENCY_LEVEL1:
				partnerRole.addPartner(0, "Money Manager", "");
				partnerRole.addPartner(1, "Admin", "");
				break;
			case RoleID.ADMIN:
				partnerRole.addPartner(0, "Money Manager", "");
				break;
			default:
				break;
		}
	}

	@Override
	public void update(float delta) {
		if (responeChangeRole != null) {
			boolean result = responeChangeRole.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				onCompleteListener.done();
			} else {
				onCompleteListener.onError();
			}
			responeChangeRole = null;
		}
	}

	@Override
	public void back() {
		super.back();
		onCompleteListener.onError();
		getViewController().removeView(getName());

	}

	class ChangeRole implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeChangeRole = (new JsonReader()).parse(httpResponse
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
