package imp.view;

import utils.elements.Img;
import utils.factory.FontFactory.fontType;
import utils.factory.StringSystem;
import utils.factory.Style;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.GameEvent;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnClickListener;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.View;

public class TopBarView extends View {
	Img		transparent;
	Img		iconMenu;
	Img		iconBack;
	Label	label;

	public TopBarView() {
	}

	public TopBarView buildComponent() {
		setBackground(new NinePatchDrawable(new NinePatch(Style.ins.np4,
				new Color(0, 191 / 255f, 1, 1))));
		transparent = new Img(Assets.instance.ui.reg_ninepatch);
		transparent.setColor(0 / 255f, 0 / 255f, 0 / 255f, 0.4f);
		iconMenu = new Img(Assets.instance.ui.reg_submenu);
		iconMenu.setSize(60, 60);
		iconBack = new Img(new Texture(Gdx.files.internal("Img/refresh.png")));
		iconBack.setSize(60, 60);

		left();
		add(iconMenu).padLeft(10).left();

		label = new Label(getLabel(),
				Style.ins.getLabelStyle(25, fontType.Bold));
		add(label).expandX().fillX().padLeft(10).left();
		add(iconBack).padRight(20).right();
		buildListener();
		return this;
	}

	public void setTopName(String name) {
		if (label != null)
			label.setText(name);
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
	}

	@Override
	public void destroyComponent() {
	}

	@Override
	public void back() {
	}

	void buildListener() {
		iconBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				getViewController().notifyEvent(GameEvent.ONREFRESH);
			}
		});
		iconMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(float x, float y) {
				if (getViewController().getView(StringSystem.VIEW_MAIN_MENU)
						.getViewState() == ViewState.HIDE) {
					iconMenu.setTouchable(Touchable.disabled);
					getViewController().getView(StringSystem.VIEW_MAIN_MENU)
							.show(new OnCompleteListener() {

								@Override
								public void done() {
									iconMenu.setTouchable(Touchable.enabled);
								}

								@Override
								public void onError() {

								}
							});
					return;
				}
				if (getViewController().getView(StringSystem.VIEW_MAIN_MENU)
						.getViewState() == ViewState.SHOW) {
					iconMenu.setTouchable(Touchable.disabled);
					getViewController().getView(StringSystem.VIEW_MAIN_MENU)
							.hide(new OnCompleteListener() {

								@Override
								public void done() {
									iconMenu.setTouchable(Touchable.enabled);
								}

								@Override
								public void onError() {

								}
							});
				}
			}
		});
	}
}
