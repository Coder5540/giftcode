package imp.view;

import utils.factory.FontFactory.fontType;
import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.listener.OnClickListener;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.listener.OnSelectListener;
import com.coder5560.game.ui.CustomTextButton;
import com.coder5560.game.views.IViewController;
import com.coder5560.game.views.View;

public class BlockView extends View {

	// public static final int LOCK_ACCOUNT = 0;
	public static final int	LOCK_DEVICE_1	= 0;
	public static final int	LOCK_DEVICE_2	= 1;
	public static final int	LOCK_DEVICE_3	= 2;

	Image					bg;
	Table					content;
	String					phone;
	Array<String>			imeis;
	OnSelectListener		onSelectListener;
	/*
	 * == 1 : khoa
	 * 
	 * == 2 : mo khoa
	 */
	int						type			= 1;

	public BlockView(int type, String phone, Array<String> imeis) {
		super();
		this.type = type;
		this.phone = phone;
		this.imeis = imeis;
	}

	@Override
	public String getLabel() {
		return type == 1 ? "Khóa thiết bị" : "Mở khóa thiết bị";
	}

	@Override
	public void build(Stage stage, IViewController viewController,
			String viewName, Rectangle bound) {
		super.build(stage, viewController, viewName, bound);
	}

	public void buildComponent() {
		{
			bg = new Image(Assets.instance.ui.reg_ninepatch);
			bg.setColor(0.1f, 0.1f, 0.1f, 0.2f);
			bg.setSize(getWidth(), getHeight());
			bg.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					hide(null);
					getViewController().removeView(getName());
				};
			});
		}
		LabelStyle style = Style.ins.getLabelStyle(20, fontType.Medium,
				Color.BLACK);
		// Label lbBlockAll = new Label("Khóa tài khoản", style);
		// lbBlockAll.setAlignment(Align.left);
		Label lbBlockDevice = new Label(type == 1 ? "Khóa thiết bị"
				: "Mở khóa thiết bị", style);
		lbBlockDevice.setAlignment(Align.left);
		// CustomTextButton btnLockAccount = getTextButton(phone, style,
		// Style.ins.np2, Color.GREEN, Color.BLACK, new OnClickListener() {
		//
		// @Override
		// public void onClick(float x, float y) {
		// if (onSelectListener != null) {
		// onSelectListener.onSelect(LOCK_ACCOUNT);
		// }
		// }
		// });
		// btnLockAccount.setSize(getWidth(), 60);
		// btnLockAccount.setColor(Color.GREEN);

		content = new Table();
		content.setTouchable(Touchable.enabled);
		content.defaults().expandX().fillX().height(60);
		content.top();
		NinePatch np = Style.ins.np4;
		np.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
		content.setBackground(new NinePatchDrawable(np));
		content.setSize(2 * getWidth() / 3, 60 * (2 + imeis.size));

		// content.add(lbBlockAll).expand().padLeft(10).height(60).left().row();
		// content.add(btnLockAccount).padLeft(10).height(60).row().padTop(10);
		content.add(lbBlockDevice).padLeft(10).height(60).left().row()
				.padBottom(10);

		for (int i = 0; i < imeis.size; i++) {
			final int index = i;
			CustomTextButton customTextImei = getTextButton(imeis.get(0),
					style, Style.ins.np2, Color.GREEN, Color.BLACK,
					new OnClickListener() {
						@Override
						public void onClick(float x, float y) {
							if (onSelectListener != null) {
								onSelectListener.onSelect(index);
							}
						}
					});
			customTextImei.setSize(getWidth(), 60);
			customTextImei.setColor(Color.GREEN);
			content.add(customTextImei).padLeft(10).height(60).row()
					.padBottom(10);
		}

		bg.setPosition(0, 0);
		content.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
		addActor(bg);
		addActor(content);
	}

	public CustomTextButton getTextButton(String text, LabelStyle style,
			NinePatch ninePatch, Color buttonColor, Color textColor,
			final OnClickListener listener) {
		Label lbText = new Label(text, style);
		lbText.setAlignment(Align.center);
		lbText.setColor(textColor);
		final CustomTextButton textButton = new CustomTextButton(ninePatch,
				lbText);
		textButton.setColor(buttonColor);
		if (listener == null)
			textButton.setTouchable(Touchable.disabled);
		else {
			textButton.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					textButton.setScale(0.99f);
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					textButton.setScale(1f);
					listener.onClick(x, y);
					event.stop();
				}
			});
		}
		return textButton;

	}

	@Override
	public void show(OnCompleteListener listener) {

		super.show(listener);
	}

	@Override
	public void hide(OnCompleteListener listener) {

		super.hide(listener);
	}

	@Override
	public void update(float delta) {

		super.update(delta);
	}

	@Override
	public void back() {

		super.back();
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		this.onSelectListener = onSelectListener;
	}

}
