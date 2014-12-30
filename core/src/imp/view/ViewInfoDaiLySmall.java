package imp.view;

import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Style;
import utils.networks.ExtParamsKey;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.View;

public class ViewInfoDaiLySmall extends View {

	Image		bg;
	Image		btnClose;
	ScrollPane	scroll;
	Table		content;
	Table		contentScroll, btn;

	RowInfo[]	rowInfo;

	public void buildComponent() {
		this.top();
		contentScroll = new Table();
		contentScroll.left();
		scroll = new ScrollPane(contentScroll);
		scroll.setOverscroll(false, true);
		btn = new Table();

		bg = new Image(Assets.instance.ui.reg_ninepatch);
		bg.setColor(Color.BLACK);
		bg.getColor().a = 0.3f;
		bg.setSize(getWidth(), getHeight());
		bg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back();
			}
		});

		rowInfo = new RowInfo[12];
		rowInfo[0] = new RowInfo("Tên đại lý", "");
		rowInfo[1] = new RowInfo("Địa chỉ đại lý", "");
		rowInfo[2] = new RowInfo("Cấp đại lý", "");
		rowInfo[3] = new RowInfo("Số điện thoại đại lý", "");
		rowInfo[4] = new RowInfo("Số điện thoại người giới thiệu", "");
		rowInfo[5] = new RowInfo("Số tiền trong tài khoản", "");
		rowInfo[6] = new RowInfo("Email", "");
		rowInfo[7] = new RowInfo("Imei thiết bị", "");
		rowInfo[8] = new RowInfo("Tên thiết bị", "");
		rowInfo[9] = new RowInfo("Imei thiết bị bị khóa", "");
		rowInfo[10] = new RowInfo("Tên thiết bị bị khóa", "");
		rowInfo[11] = new RowInfo("Trạng thái", "");

		for (int i = 0; i < rowInfo.length; i++) {
			contentScroll.add(rowInfo[i]).left().row();
		}

		content = new Table();
		content.setTransform(true);
		content.setSize(getWidth() - 30, getHeight() - 100);
		content.setPosition(getWidth() / 2 - content.getWidth() / 2,
				getHeight() / 2 - content.getHeight() / 2);
		content.setOrigin(Align.center);
		Image bgContent = new Image(new NinePatch(Style.ins.np2));
		bgContent.setSize(content.getWidth(), content.getHeight());
		content.addActor(bgContent);

		btnClose = new Image(Assets.instance.getRegion("close"));
		btnClose.setSize(50, 50);
		btnClose.setPosition(content.getWidth() - btnClose.getWidth() - 10,
				content.getHeight() - btnClose.getHeight() - 10);
		btnClose.setOrigin(Align.center);
		btnClose.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back();
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				btnClose.addAction(Actions.scaleTo(1.2f, 1.2f, 0.3f));
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		content.addActor(btnClose);

		Label lbTitle = new Label("Thông tin đại lý", new LabelStyle(
				Assets.instance.fontFactory.getFont(30, FontType.Medium),
				new Color(0, 191 / 255f, 1, 1)));
		content.add(lbTitle).padTop(10).padBottom(10).row();
		content.add(scroll).width(content.getWidth()).row();
		content.add(btn).padTop(5).padBottom(5);

		this.addActor(bg);
		this.addActor(content);
		// content.add(scroll).width(getWidth()).height(getHeight() -
		// 200).row();
		// content.add(btn).width(getWidth()).padTop(20);
	}

	public void setInfo(String[] info) {
		for (int i = 0; i < rowInfo.length; i++) {
			rowInfo[i].setInfo(info[i]);
		}
	}

	public void setInfo(JsonValue infoUser) {
		String[] a = new String[] {
				infoUser.getString(ExtParamsKey.AGENCY_NAME),
				infoUser.getString(ExtParamsKey.FULL_NAME),
				Factory.getDotMoney(infoUser.getLong(ExtParamsKey.AMOUNT))
						+ " " + infoUser.getString(ExtParamsKey.CURRENCY),
				infoUser.getString(ExtParamsKey.REF_CODE),
				infoUser.getString(ExtParamsKey.ROLE_NAME),
				infoUser.getString(ExtParamsKey.ADDRESS),
				infoUser.getString(ExtParamsKey.EMAIL),
				Factory.getDeviceName(infoUser).replaceAll(",", "\n"),
				Factory.getDeviceID(infoUser).replaceAll(",", "\n"),
				Factory.getDeviceNameBlock(infoUser).replaceAll(",", "\n"),
				Factory.getDeviceIDBlock(infoUser).replaceAll(",", "\n"),
				infoUser.getString(ExtParamsKey.STATE) };
		setInfo(a);
	}

	int	numButton	= 0;

	public void addButton(String text, ClickListener listener) {
		TextButton btn = new TextButton(text, Style.ins.textButtonStyle);
		btn.addListener(listener);
		if (btn.getLabel().getTextBounds().width > 100)
			this.btn.add(btn).padTop(5).padLeft(8).padRight(8)
					.width(btn.getLabel().getTextBounds().width + 10)
					.height(50);
		else
			this.btn.add(btn).padTop(5).padLeft(8).padRight(8).width(100)
					.height(50);
		numButton++;
		if (numButton % 3 == 0)
			this.btn.row();
	}

	public void clearButton() {
		btn.clear();
		numButton = 0;
	}

	@Override
	public void show(OnCompleteListener listener) {
		super.show(listener);
		setVisible(true);
		content.clearActions();
		content.setScale(0.5f);
		content.getColor().a = 0.5f;
		content.addAction(Actions.parallel(
				Actions.scaleTo(1, 1, 0.2f, Interpolation.swingOut),
				Actions.fadeIn(0.2f, Interpolation.fade)));
	}

	@Override
	public void hide(final OnCompleteListener listener) {
		super.hide(null);
		content.addAction(Actions.sequence(Actions.parallel(
				Actions.scaleTo(0.5f, 0.5f, 0.2f, Interpolation.fade),
				Actions.fadeOut(0.2f, Interpolation.fade)), Actions
				.run(new Runnable() {
					@Override
					public void run() {
						setVisible(false);
						if (listener != null)
							listener.done();
					}
				})));
	}

	@Override
	public void back() {
		super.back();
		getViewController().removeView(getName());
	}

	class RowInfo extends Table {

		Label	lbInfo;

		public RowInfo(String title, String info) {
			left();
			padTop(10);
			padBottom(10);
			Label lbTitle = new Label(title, new LabelStyle(
					Assets.instance.fontFactory.getFont(17, FontType.Regular),
					new Color(207 / 255f, 207 / 255f, 207 / 255f, 1)));
			lbInfo = new Label(info, new LabelStyle(
					Assets.instance.fontFactory.getFont(25, FontType.Regular),
					Constants.COLOR_ACTIONBAR));
			add(lbTitle).left().padLeft(20).row();
			add(lbInfo).left().padLeft(20);
		}

		void setInfo(String info) {
			this.lbInfo.setText(info);
		}
	}

}