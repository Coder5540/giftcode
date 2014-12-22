package com.coder5560.game.ui;


import utils.factory.Style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.ObjectMap;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;

public class DialogCustom extends MyDialog {

	ObjectMap<Object, Runnable> runMap = new ObjectMap<Object, Runnable>();
	int i = 0;
	Image back;

	public DialogCustom(String title, Skin skin) {
		super(title, skin);
	}

	public DialogCustom(String title) {
		super(title, Style.ins.windowStyle);
		if (buttonStyle == null) {
			buttonStyle = Style.ins.textButtonStyle;
		}
		getButtonTable().pad(10, 20, 10, 20);
	}

	public static void showOk(String str, Stage stage) {
		DialogCustom dia = new DialogCustom("");
		dia.text(str);
		dia.button("Ok");
		dia.show(stage);
	}

	public DialogCustom(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
	}

	@Override
	public MyDialog show(Stage stage) {
		back = new Image(new NinePatch(new NinePatch(
				Assets.instance.getRegion("ninepatch2"), 4, 4, 4, 4),
				Color.BLACK));
		back.getColor().a = 0.8f;

		back.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN);
		stage.addActor(back);
		return super.show(stage);
	}

	@Override
	public void hide() {
		back.remove();
		super.hide();
	}

	public static TextButtonStyle buttonStyle = null;

	public void button(String text, Runnable run) {

		button(text, i, Style.ins.textButtonStyle);
		runMap.put(i, run);
		i++;
	}

	@Override
	public MyDialog button(String text) {
		return super.button(text, Style.ins.textButtonStyle);
	}

	@Override
	public MyDialog text(String text) {
		return text(text,
				new LabelStyle(Assets.instance.fontFactory.getFont(20),
						Color.BLACK));
	}

	@Override
	protected void result(Object object) {
		try {
			runMap.get(object).run();
			runMap.remove(object);
		} catch (Exception e) {

		}
	}
}
