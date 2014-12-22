package utils.factory;

import java.util.HashMap;

import utils.factory.FontFactory.fontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;

public class Style {
	public static Style ins = new Style();
	public LabelStyle labelStyle20;
	public LabelStyle labelStyle30;
	public SelectBoxStyle selectBoxStyle;
	public ScrollPaneStyle scrollPanelType;
	public TextButtonStyle textButtonStyle;
	public WindowStyle windowStyle;
	public NinePatch np1, np2, np3, np4, np5;
	public TextFieldStyle tfStyle;

	HashMap<String, LabelStyle> styles = new HashMap<String, LabelStyle>();

	public Style() {
		np1 = new NinePatch(Assets.instance.getRegion("textfield"), 4, 4, 4, 4);
		np2 = new NinePatch(Assets.instance.getRegion("ninepatch2"), 4, 4, 4, 4);
		np3 = new NinePatch(Assets.instance.getRegion("ninepatch3"), 4, 4, 4, 4);
		np4 = new NinePatch(Assets.instance.getRegion("ninepatch4"), 4, 4, 4, 4);
		np5 = new NinePatch(Assets.instance.getRegion("ninepatch5"), 4, 4, 4, 4);

		labelStyle20 = new LabelStyle(Assets.instance.fontFactory.getFont(20),
				Color.WHITE);
		labelStyle30 = new LabelStyle(Assets.instance.fontFactory.getFont(30),
				Color.WHITE);

		scrollPanelType = new ScrollPaneStyle();
		scrollPanelType.background = new NinePatchDrawable(new NinePatch(
				Assets.instance.getRegion("ninepatch2"), 4, 4, 4, 4));

//		selectBoxStyle = new SelectBoxStyle();
//		selectBoxStyle.font = Assets.instance.fontFactory.getFont(20);
//		selectBoxStyle.fontColor = Color.BLACK;
//		selectBoxStyle.background = new NinePatchDrawable(new NinePatch(
//				Assets.instance.getRegion("textfield"), 4, 4, 4, 4));
//		selectBoxStyle.scrollStyle = scrollPanelType;
//		selectBoxStyle.listStyle = new ListStyle(
//				Assets.instance.fontFactory.getFont(20), Color.BLACK,
//				Color.GRAY, new NinePatchDrawable(new NinePatch(
//						Assets.instance.getRegion("textfield"), 4, 4, 4, 4)));

		selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.font = Assets.instance.fontFactory.getFont(20);
		selectBoxStyle.fontColor = Color.BLACK;
		NinePatch ninepatch = new NinePatch(
				Assets.instance.getRegion("selectboxback"), 8, 17, 15, 15);
		ninepatch.setColor(new Color(255 / 255f, 255 / 255f, 255 / 255f, 1));
		// selectBoxStyle.background = new NinePatchDrawable(new NinePatch(
		// Assets.instance.getRegion("textfield"), 4, 4, 4, 4));
		selectBoxStyle.background = new NinePatchDrawable(ninepatch);
		selectBoxStyle.scrollStyle = scrollPanelType;
		selectBoxStyle.listStyle = new ListStyle(
				Assets.instance.fontFactory.getFont(20), Color.BLACK,
				Color.GRAY, new NinePatchDrawable(new NinePatch(
						Assets.instance.getRegion("textfield"), 4, 4, 4, 4)));

		// textButtonStyle = new TextButtonStyle();
		// textButtonStyle.up = new NinePatchDrawable(new NinePatch(new
		// NinePatch(
		// Assets.instance.getRegion("ninepatch4"), 4, 4, 4, 4),
		// new Color(200 / 255f, 200 / 255f, 210 / 255f, 1)));
		// textButtonStyle.down = new NinePatchDrawable(new NinePatch(
		// new NinePatch(Assets.instance.getRegion("ninepatch2"), 4, 4, 4,
		// 4), new Color(180 / 255f, 180 / 255f, 180 / 255f, 1)));
		// textButtonStyle.font = Assets.instance.fontFactory.getFont(20);
		// textButtonStyle.fontColor = Color.BLACK;

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = new NinePatchDrawable(new NinePatch(np1,
				new Color(0, 191 / 255f, 1, 1)));
		textButtonStyle.down = new NinePatchDrawable(new NinePatch(np1,
				new Color(0, 191 / 255f, 1, 0.5f)));
		textButtonStyle.font = Assets.instance.fontFactory.getFont(20,
				fontType.Medium);
		textButtonStyle.fontColor = Color.WHITE;
		windowStyle = new WindowStyle(Assets.instance.fontFactory.getFont(20),
				Color.BLACK, scrollPanelType.background);

		tfStyle = new TextFieldStyle();
		tfStyle.background = new NinePatchDrawable(new NinePatch(new NinePatch(
				Assets.instance.ui.reg_ninepatch1, 6, 6, 6, 6), new Color(
				245 / 255f, 245 / 255f, 245 / 255f, 1)));
		tfStyle.background.setLeftWidth(6);
		tfStyle.cursor = new NinePatchDrawable(new NinePatch(
				Assets.instance.getRegion("bg_white"), new Color(245 / 255f,
						191 / 255f, 1, 1)));
		tfStyle.font = Assets.instance.fontFactory.getFont(20, fontType.Light);
		tfStyle.fontColor = Color.BLACK;
	}

	public LabelStyle getLabelStyle(int size, fontType type) {
		if (styles.get("" + size + type.toString()) == null) {
			styles.put("" + size + type.toString(), new LabelStyle(
					Assets.instance.fontFactory.getFont(size, type),
					Color.WHITE));
		}
		return styles.get("" + size + type.toString());
	}
	public LabelStyle getLabelStyle(int size, fontType type, Color color) {
		if (styles.get("" + size + type.toString()+ color) == null) {
			styles.put("" + size + type.toString()+ color, new LabelStyle(
					Assets.instance.fontFactory.getFont(size, type),color));
		}
		return styles.get("" + size + type.toString()+ color);
	}

	public LabelStyle getLabelStyle(int size) {
		if (styles.get("" + size + fontType.Regular.toString()) == null) {
			styles.put(
					"" + size + fontType.Regular.toString(),
					new LabelStyle(Assets.instance.fontFactory.getFont(size,
							fontType.Regular), Color.WHITE));
		}
		return styles.get("" + size + fontType.Regular.toString());
	}

	public TextFieldStyle getTextFieldStyle(int padLeft, BitmapFont font) {
		TextFieldStyle style = new TextFieldStyle();
		NinePatch ninePatchNameForcus = new NinePatch(
				Assets.instance.getRegion("rec_vien"), 1, 1, 1, 1);
		NinePatch ninePatchNameDisable = new NinePatch(
				Assets.instance.getRegion("rec_vien"), 1, 1, 1, 1);
		ninePatchNameDisable.setColor(new Color(200 / 255f, 200 / 255f,
				200 / 255f, 1));
		ninePatchNameForcus.setColor(new Color(0 / 255f, 180 / 255f,
				200 / 255f, 1));
		style.background = new NinePatchDrawable(ninePatchNameDisable);
		style.background.setLeftWidth(padLeft);
		style.focusedBackground = new NinePatchDrawable(ninePatchNameForcus);
		style.focusedBackground.setLeftWidth(padLeft);
		style.disabledBackground = new NinePatchDrawable(ninePatchNameDisable);
		style.disabledBackground.setLeftWidth(padLeft);
		style.cursor = new NinePatchDrawable(new NinePatch(
				Assets.instance.getRegion("bg_white"), new Color(0, 191 / 255f,
						1, 1)));

		style.font = font;
		style.fontColor = Color.BLACK;
		return style;
	}
}
