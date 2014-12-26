package imp.view;

import utils.factory.FontFactory.fontType;
import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.TextfieldStatic;
import com.coder5560.game.views.View;

public class ViewInfoDaiLySmall extends View {

	TextfieldStatic	lbTfTitle[];
	TextfieldStatic	lbTfInfo[];
	Image			bg;
	Image			btnClose;
	Label			lbTitle;

	public ViewInfoDaiLySmall() {
		this.top();
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

		lbTfTitle = new TextfieldStatic[10];
		lbTfInfo = new TextfieldStatic[10];

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
		lbTfTitle[9] = new TextfieldStatic("Trạng thái",
				Style.ins.getLabelStyle(15), Color.BLACK, 190);

		for (int i = 0; i < 10; i++)
			lbTfInfo[i] = new TextfieldStatic("", Style.ins.getLabelStyle(15),
					Color.BLACK, 190);

		lbTfInfo[4].setHeight(lbTfTitle[4].getHeight());

		lbTitle = new Label("Thông tin đại lý", new LabelStyle(
				Assets.instance.fontFactory.getFont(30, fontType.Medium),
				Color.BLUE));
		this.add(lbTitle).padTop(10).padBottom(10).colspan(2).row();
		for (int i = 0; i < 10; i++) {
			this.add(lbTfTitle[i]).padTop(5);
			this.add(lbTfInfo[i]).padLeft(5).padTop(5).row();
		}
		setTransform(true);
	}

	public View show(String... info) {
		clear();
		addActor(btnClose);
		this.add(lbTitle).padTop(10).padBottom(10).colspan(2).row();
		for (int i = 0; i < 10; i++) {
			this.add(lbTfTitle[i]).padTop(5);
			this.add(lbTfInfo[i]).padLeft(5).padTop(5).row();
		}

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
		for (int i = 0; i < 10; i++)
			lbTfInfo[i].setContent(info[i]);
		int state = Integer.parseInt(info[9]);
		if (state == 0) {
			lbTfInfo[9].setContent("Chưa kích hoạt");
		} else if (state == 1) {
			lbTfInfo[9].setContent("Hoạt động bình thường");
		} else {
			lbTfInfo[9].setContent("Bị khóa");
		}
		super.show(null);
		clearActions();
		setScale(0.8f);
		getColor().a = 0.8f;
		addAction(Actions.scaleTo(1, 1, 0.2f, Interpolation.fade));
		addAction(Actions.fadeIn(0.2f, Interpolation.fade));
		return this;
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
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
		bg.setVisible(false);
		clearActions();
		addAction(Actions.sequence(
				Actions.scaleTo(0.5f, 0.5f, 0.2f, Interpolation.fade),
				Actions.hide()));
		addAction(Actions.fadeOut(0.2f, Interpolation.fade));
	}

	@Override
	public void back() {
		super.back();
		getViewController().removeView(getName());
	}
}
