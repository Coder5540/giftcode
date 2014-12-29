package imp.view;

import utils.factory.FontFactory.FontType;
import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.TextfieldStatic;
import com.coder5560.game.views.IViewController;
import com.coder5560.game.views.View;

public class CopyOfViewInfoDaiLySmall extends View {

	TextfieldStatic	lbTfTitle[];
	TextfieldStatic	lbTfInfo[];
	Image			bg;
	Image			btnClose;
	Label			lbTitle;
	Table			tbTopbar;
	Table			tbContent;
	Table			tbBtn;

	public CopyOfViewInfoDaiLySmall() {
	}

	@Override
	public void build(Stage stage, IViewController viewController,
			String viewName, Rectangle bound) {
		super.build(stage, viewController, viewName, bound);
		buildComponent();
		debugAll();
	}

	private void buildComponent() {
		buildTextField();
		setPosition(Constants.WIDTH_SCREEN / 2, Constants.HEIGHT_SCREEN / 2,
				Align.center);
		{
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
		}

		{
			btnClose = new Image(Assets.instance.getRegion("close"));
			btnClose.setSize(40, 40);
			btnClose.setPosition(getWidth() - btnClose.getWidth() - 20,
					getHeight() - btnClose.getHeight() - 20);
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
		}

		{
			lbTitle = new Label("Thông tin đại lý", new LabelStyle(
					Assets.instance.fontFactory.getFont(30, FontType.Medium),
					Color.BLUE));
			lbTitle.setSize(getWidth(), 60);
			lbTitle.setWrap(true);
			lbTitle.setAlignment(Align.center);
			lbTitle.setPosition(0, getHeight() - lbTitle.getHeight());
		}
		

		{
			tbContent = new Table();
			tbContent.setSize(getWidth(), getHeight() - 120);
			tbContent.setPosition(0, lbTitle.getY() - tbContent.getHeight());
			for (int i = 0; i < 10; i++) {
				tbContent.add(lbTfTitle[i]).padTop(5);
				tbContent.add(lbTfInfo[i]).padLeft(5).padTop(5).row();
			}
		}

		{
			top();
			setBackground(new NinePatchDrawable(new NinePatch(Style.ins.np2,
					new Color(0.8f, 0.8f, 0.8f, 1))));
			add(lbTitle).expandX().fillX().height(lbTitle.getHeight()).row();
			add(tbContent).expandX().fillX().expandY().height(tbContent.getHeight()).padTop(40).row();
			
			addActor(btnClose);
			setTransform(true);
		}
	}

	private void buildTextField() {
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
	}

	public View show(String... info) {
		clear();
		tbContent.clear();
		tbContent.top();
		for (int i = 0; i < 10; i++) {
			tbContent.add(lbTfTitle[i]).padTop(5);
			tbContent.add(lbTfInfo[i]).padLeft(5).padTop(5).row();
		}
		getStage().addActor(bg);
		add(lbTitle).expandX().fillX().height(lbTitle.getHeight()).row();
		add(tbContent).expandX().fillX().expandY().height(tbContent.getHeight()).padTop(40).row();
		addActor(btnClose);
		toFront();
		setOrigin(Align.center);
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

		bg.setVisible(true);
		setVisible(true);
		super.show(null);
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
	public String getLabel() {
		return "Thông tin đại lý";
	}

	@Override
	public void back() {
		super.back();
		getViewController().removeView(getName());
	}
}
