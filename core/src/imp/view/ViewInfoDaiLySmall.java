package imp.view;

import utils.factory.FontFactory.fontType;
import utils.factory.Factory;
import utils.factory.PlatformResolver.OnResultListener;
import utils.factory.Style;
import utils.listener.OnClickListener;
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
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.listener.OnResponseListener;
import com.coder5560.game.ui.TextfieldStatic;
import com.coder5560.game.views.View;

public class ViewInfoDaiLySmall extends View {

	TextfieldStatic	lbTfTitle[];
	TextfieldStatic	lbTfInfo[];
	Image			bg;
	Image			btnClose;
	Label			lbTitle;
	ScrollPane		scroll;
	Table			content, btn;

	public void buildComponent() {
		this.top();
		content = new Table();
		content.setWidth(getWidth());
		scroll = new ScrollPane(content);
		scroll.setSize(getWidth(), getHeight() - 200);
		btn = new Table();

		bg = new Image(Assets.instance.ui.reg_ninepatch);
		bg.setColor(Color.BLACK);
		bg.getColor().a = 0.6f;
		bg.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
				- Constants.HEIGHT_ACTIONBAR);
		bg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide(null);
			}
		});
		setBackground(new NinePatchDrawable(new NinePatch(Style.ins.np2,
				new Color(0.8f, 0.8f, 0.8f, 1))));

		btnClose = new Image(Assets.instance.getRegion("close"));
		btnClose.setSize(50, 50);
		btnClose.setOrigin(Align.center);
		btnClose.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide(null);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				btnClose.addAction(Actions.scaleTo(1.2f, 1.2f, 0.3f));
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		addActor(btnClose);

		lbTfTitle = new TextfieldStatic[12];
		lbTfInfo = new TextfieldStatic[12];

		lbTfTitle[0] = new TextfieldStatic("Tên đại lý",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[1] = new TextfieldStatic("Địa chỉ đại lý",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[2] = new TextfieldStatic("Cấp đại lý",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[3] = new TextfieldStatic("Số điện thoại đại lý",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[4] = new TextfieldStatic("Số điện thoại người giới thiệu",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[5] = new TextfieldStatic("Số tiền trong tài khoản",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[6] = new TextfieldStatic("Email",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[7] = new TextfieldStatic("Imei thiết bị",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[8] = new TextfieldStatic("Tên thiết bị",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[9] = new TextfieldStatic("Imei bị khóa",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[10] = new TextfieldStatic("Tên bị khóa",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);
		lbTfTitle[11] = new TextfieldStatic("Trạng thái",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);

		for (int i = 0; i < lbTfInfo.length; i++)
			lbTfInfo[i] = new TextfieldStatic("", Style.ins.getLabelStyle(15),
					Color.BLACK, 190);

		lbTfInfo[4].setHeight(lbTfTitle[4].getHeight());

		lbTitle = new Label("Thông tin đại lý", new LabelStyle(
				Assets.instance.fontFactory.getFont(30, fontType.Medium),
				Color.BLUE));
		this.add(lbTitle).padTop(10).padBottom(10).colspan(2).row();
		for (int i = 0; i < lbTfInfo.length; i++) {
			content.add(lbTfTitle[i]).padTop(5);
			content.add(lbTfInfo[i]).padLeft(5).padTop(5).row();
		}
		this.add(scroll).width(getWidth()).height(scroll.getHeight());
		setTransform(true);
	}

	public ViewInfoDaiLySmall show(JsonValue infoUser) {
		String[] a = new String[] {
				infoUser.getString(ExtParamsKey.AGENCY_NAME),
				infoUser.getString(ExtParamsKey.FULL_NAME),
				Factory.getDotMoney(infoUser.getLong(ExtParamsKey.AMOUNT)) + " "
						+ infoUser.getString(ExtParamsKey.CURRENCY),
				infoUser.getString(ExtParamsKey.REF_CODE),
				infoUser.getString(ExtParamsKey.ROLE_NAME),
				infoUser.getString(ExtParamsKey.ADDRESS),
				infoUser.getString(ExtParamsKey.EMAIL),
				Factory.getDeviceName(infoUser), Factory.getDeviceID(infoUser),
				Factory.getDeviceNameBlock(infoUser),
				Factory.getDeviceIDBlock(infoUser),
				infoUser.getString(ExtParamsKey.STATE) };
		show(a);
		return this;
	}

	public ViewInfoDaiLySmall show(String... info) {
		clear();
		content.clear();
		addActor(btnClose);

		setOrigin(Align.center);
		getStage().addActor(bg);
		bg.setVisible(true);
		btnClose.setPosition(getWidth() - btnClose.getWidth() - 10, getHeight()
				- btnClose.getHeight() - 10);
		toFront();
		setVisible(true);
		btnClose.setScale(1);
		setPosition(Constants.WIDTH_SCREEN / 2, Constants.HEIGHT_SCREEN / 2,
				Align.center);
		for (int i = 0; i < info.length; i++)
			lbTfInfo[i].setContent(info[i]);
		int state = Integer.parseInt(info[info.length - 1]);
		if (state == 0) {
			lbTfInfo[info.length - 1].setContent("Chưa kích hoạt");
		} else if (state == 1) {
			lbTfInfo[info.length - 1].setContent("Hoạt động bình thường");
		} else {
			lbTfInfo[info.length - 1].setContent("Bị khóa");
		}

		this.add(lbTitle).padTop(10).padBottom(10).row();
		for (int i = 0; i < info.length; i++) {
			content.add(lbTfTitle[i]).padTop(5);
			content.add(lbTfInfo[i]).padLeft(5).padTop(5).row();
		}
		this.add(scroll).width(getWidth()).height(scroll.getHeight());
		row();
		this.add(btn).width(getWidth()).height(50).padTop(30);
		numButton++;

		super.show(null);
		clearActions();
		setScale(0.8f);
		getColor().a = 0.8f;
		addAction(Actions.scaleTo(1, 1, 0.2f, Interpolation.fade));
		addAction(Actions.fadeIn(0.2f, Interpolation.fade));
		return this;
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
		if (numButton % 3 == 1)
			this.btn.row();

	}

	@Override
	public void show(OnCompleteListener listener) {
		super.show(listener);
		bg.setVisible(true);
		setVisible(true);
		btnClose.setScale(1);
		clearActions();
		setScale(0.8f);
		getColor().a = 0.8f;
		addAction(Actions.scaleTo(1, 1, 0.2f, Interpolation.fade));
		addAction(Actions.fadeIn(0.2f, Interpolation.fade));
	}

	@Override
	public void hide(final OnCompleteListener listener) {
		super.hide(null);
		bg.setVisible(false);
		clearActions();
		addAction(Actions.sequence(Actions.parallel(Actions.sequence(
				Actions.scaleTo(0.5f, 0.5f, 0.2f, Interpolation.fade),
				Actions.hide()), Actions.fadeOut(0.2f, Interpolation.fade)),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						if (listener != null)
							listener.done();
					}
				})));
		btn.clear();
		numButton = 0;
	}

	@Override
	public void back() {
		super.back();
		getViewController().removeView(getName());
	}

}