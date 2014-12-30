package com.coder5560.game.ui;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.coder5560.game.assets.Assets;

public class LabelButton extends Group {

	public static final int LEFT = 0;
	public static final int CENTER = 1;

	public Image bg;
	public Image			bgFocus;
	Label title;
	Vector2 touchPoint;
	int align;

	public LabelButton(String title, LabelStyle style, float width,
			float height, int align) {
		this.align = align;
		touchPoint = new Vector2();
		setSize(width, height);
		bg = new Image(new NinePatch(new NinePatch(
				Assets.instance.ui.reg_ninepatch1, 4, 4, 4, 4), new Color(
				245 / 255f, 245 / 255f, 245 / 255f, 1)));
		bg.setSize(width, height);
		bgFocus = new Image(new NinePatch(new NinePatch(
				Assets.instance.ui.reg_ninepatch1, 4, 4, 4, 4), new Color(
				220 / 255f, 220 / 255f, 220 / 255f, 1)));
		bgFocus.getColor().a = 0;
		bgFocus.setSize(width, height);

		this.title = new Label(title, style);
		if (align == LEFT) {
			this.title.setPosition(30, bg.getY() + bg.getHeight() / 2
					- this.title.getHeight() / 2);
		} else {
			this.title
					.setPosition(
							getWidth() / 2 - this.title.getWidth() / 2,
							bg.getY() + bg.getHeight() / 2
									- this.title.getHeight() / 2);
		}
		addActor(bg);
		addActor(bgFocus);
		addActor(this.title);
	}

	public LabelButton(String title, LabelStyle style, float width, float height) {
		this.align = LEFT;
		touchPoint = new Vector2();
		setSize(width, height);
		bg = new Image(new NinePatch(new NinePatch(
				Assets.instance.ui.reg_ninepatch1, 4, 4, 4, 4), new Color(
				245 / 255f, 245 / 255f, 245 / 255f, 1)));
		bg.setSize(width, height);
		bgFocus = new Image(new NinePatch(new NinePatch(
				Assets.instance.ui.reg_ninepatch1, 4, 4, 4, 4), new Color(
				220 / 255f, 220 / 255f, 220 / 255f, 1)));
		bgFocus.getColor().a = 0;
		bgFocus.setSize(width, height);

		this.title = new Label(title, style);
		if (align == LEFT) {
			this.title.setPosition(30, bg.getY() + bg.getHeight() / 2
					- this.title.getHeight() / 2);
		} else {
			this.title
					.setPosition(
							getWidth() / 2 - this.title.getWidth() / 2,
							bg.getY() + bg.getHeight() / 2
									- this.title.getHeight() / 2);
		}
		// addActor(bg);
		addActor(bgFocus);
		addActor(this.title);
	}

	public void setTitle(String title) {
		this.title.setText(title);
		if (align == LEFT) {
			this.title.setPosition(30, bg.getY() + bg.getHeight() / 2
					- this.title.getHeight() / 2);
		} else {
			this.title.setPosition(getWidth() / 2
					- this.title.getTextBounds().width / 2, getHeight() / 2
					- this.title.getHeight() / 2);
		}
	}

	public void setListener(final ItemListener listener) {
		this.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				touchPoint.set(x, y);
				bgFocus.getColor().a = 1;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				bgFocus.getColor().a = 0;
				if (touchPoint.epsilonEquals(x, y, 20)) {
					listener.onItemClick();
				}
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				if (!touchPoint.epsilonEquals(x, y, 40)) {
					bgFocus.getColor().a = 0;
					touchPoint.set(0, 0);
				}
				super.touchDragged(event, x, y, pointer);
			}
		});
	}
}
