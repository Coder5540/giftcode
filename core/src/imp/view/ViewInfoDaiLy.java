package imp.view;

import utils.factory.FontFactory.FontType;
import utils.networks.UserInfo;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.views.View;

public class ViewInfoDaiLy extends View {

	ScrollPane scroll;
	Table content;

	@Override
	public String getLabel() {
		return "Thông tin đại lý";
	}

	public void buildComponent() {
		this.top();
		
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch)));
		
		content = new Table();
		content.left();
		scroll = new ScrollPane(content);
		scroll.setOverscroll(false, true);
		content.add(getRow("Tên đại lý", UserInfo.fullName)).left().padTop(10)
				.row();
		content.add(getRow("Địa chỉ đại lý", UserInfo.address)).left().row();
		content.add(getRow("Cấp đại lý", UserInfo.level)).left().row();
		content.add(getRow("Số điện thoại đại lý", UserInfo.phone)).left()
				.row();
		content.add(getRow("Số điện thoại người giới thiệu", UserInfo.phoneNGT))
				.left().row();
		content.add(getRow("Số tiền trong tài khoản", "" + UserInfo.money))
				.left().row();
		content.add(getRow("Email", UserInfo.email)).left().row();
		String imei = UserInfo.imeiDevice.replaceAll(",", "\n");
		content.add(getRow("Imei thiết bị", imei)).left().row();
		String nameDevice = UserInfo.nameDevice.replaceAll(",", "\n");
		content.add(getRow("Tên thiết bị", nameDevice)).left().row();
		String imeiBlock = UserInfo.imeiDeviceBlock.replaceAll(",", "\n");
		content.add(getRow("Imei thiết bị bị khóa", imeiBlock)).left().row();
		String nameBlock = UserInfo.nameDeviceBlock.replaceAll(",", "\n");
		content.add(getRow("Tên thiết bị bị khóa", nameBlock)).left().row();
		String state;
		if (UserInfo.state == 0) {
			state = "Chưa kích hoạt";
		} else if (UserInfo.state == 1) {
			state = "Hoạt động bình thường";
		} else {
			state = "Bị khóa";
		}
		content.add(getRow("Trạng thái", state)).left().row();
		this.add(scroll).width(getWidth());
	}

	Table getRow(String title, String info) {
		Table table = new Table();
		table.left();
		table.padTop(10);
		table.padBottom(10);
		Label lbTitle = new Label(title, new LabelStyle(
				Assets.instance.fontFactory.getFont(17, FontType.Regular),
				Constants.COLOR_ACTIONBAR));
		Label lbInfo = new Label(info, new LabelStyle(
				Assets.instance.fontFactory.getFont(25, FontType.Regular),
				Constants.COLOR_ACTIONBAR));
		table.add(lbTitle).left().padLeft(20).row();
		table.add(lbInfo).left().padLeft(20);
		return table;
	}

	@Override
	public void back() {
		super.back();
		getViewController().removeView(getName());
	}
}
