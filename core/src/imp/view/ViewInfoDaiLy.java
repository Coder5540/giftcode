package imp.view;

import utils.factory.FontFactory.fontType;
import utils.networks.UserInfo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.ui.TextfieldStatic;
import com.coder5560.game.views.View;

public class ViewInfoDaiLy extends View {

	TextfieldStatic	lbTfTitle[];
	TextfieldStatic	lbTfInfo[];

	public ViewInfoDaiLy() {
		Loading.ins.hide();
		this.top();
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch)));

		lbTfTitle = new TextfieldStatic[10];
		lbTfInfo = new TextfieldStatic[10];
		lbTfTitle[0] = new TextfieldStatic("Tên đại lý", Color.GRAY, 220);
		lbTfTitle[1] = new TextfieldStatic("Địa chỉ đại lý", Color.GRAY, 220);
		lbTfTitle[2] = new TextfieldStatic("Cấp đại lý", Color.GRAY, 220);
		lbTfTitle[3] = new TextfieldStatic("Số điện thoại đại lý", Color.GRAY,
				220);
		lbTfTitle[4] = new TextfieldStatic("Số điện thoại người giới thiệu",
				Color.GRAY, 220);
		lbTfTitle[5] = new TextfieldStatic("Số tiền trong tài khoản",
				Color.GRAY, 220);
		lbTfTitle[6] = new TextfieldStatic("Email", Color.GRAY, 220);
		lbTfTitle[7] = new TextfieldStatic("Imei thiết bị", Color.GRAY, 220);
		lbTfTitle[8] = new TextfieldStatic("Tên thiết bị", Color.GRAY, 220);
		lbTfTitle[9] = new TextfieldStatic("Trạng thái", Color.GRAY, 220);

		lbTfInfo[0] = new TextfieldStatic(UserInfo.fullName, Color.BLACK, 220);
		lbTfInfo[1] = new TextfieldStatic(UserInfo.address, Color.BLACK, 220);
		lbTfInfo[2] = new TextfieldStatic(UserInfo.level, Color.BLACK, 220);
		lbTfInfo[3] = new TextfieldStatic(UserInfo.phone, Color.BLACK, 220);
		lbTfInfo[4] = new TextfieldStatic(UserInfo.phoneNGT, Color.BLACK, 220);
		lbTfInfo[5] = new TextfieldStatic("" + UserInfo.money + " "
				+ UserInfo.currency, Color.BLACK, 220);
		lbTfInfo[6] = new TextfieldStatic(UserInfo.email, Color.BLACK, 220);
		lbTfInfo[7] = new TextfieldStatic(UserInfo.imeiDevice, Color.BLACK, 220);
		lbTfInfo[8] = new TextfieldStatic(UserInfo.nameDevice, Color.BLACK, 220);
		lbTfInfo[9] = new TextfieldStatic("", Color.BLACK, 220);
		if (UserInfo.state == 0) {
			lbTfInfo[9].setContent("Chưa kích hoạt");
		} else if (UserInfo.state == 1) {
			lbTfInfo[9].setContent("Hoạt động bình thường");
		} else {
			lbTfInfo[9].setContent("Bị khóa");
		}

		for (int i = 0; i < 10; i++) {
			if (lbTfTitle[i].getHeight() < lbTfInfo[i].getHeight()) {
				lbTfTitle[i].setHeight(lbTfInfo[i].getHeight());
			} else {
				lbTfInfo[i].setHeight(lbTfTitle[i].getHeight());
			}
		}

		lbTfInfo[4].setHeight(lbTfTitle[4].getHeight());
		Label lbTitle = new Label("Thông tin đại lý", new LabelStyle(
				Assets.instance.fontFactory.getFont(30, fontType.Medium),
				Color.BLUE));
		this.add(lbTitle).padTop(10).padBottom(10).colspan(2).row();
		for (int i = 0; i < 10; i++) {
			this.add(lbTfTitle[i]).padTop(5);
			this.add(lbTfInfo[i]).padLeft(5).padTop(5).row();
		}
	}
}
