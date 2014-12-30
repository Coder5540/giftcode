package engine.listview;

import utils.elements.Img;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.coder5560.game.assets.Assets;

public class SubItemLabel extends ISubItem {

	public static final int	LEFT	= 0;
	public static final int	CENTER	= 1;

	Label					title;
	Vector2					touchPoint;
	int						align;

	public SubItemLabel(int id, String title, LabelStyle style, float width,
			float height, int align) {
		this.align = align;
		setId(id);
		init(title, style, width, height);
	}

	public SubItemLabel(int id, String title, LabelStyle style, float width,
			float height) {
		setId(id);
		this.align = LEFT;
		init(title, style, width, height);

		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				touchPoint.set(x, y);
				bg.addAction(Actions.alpha(.8f));
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (touchPoint.epsilonEquals(x, y, 20)) {
					if (onClickListener != null)
						onClickListener.onClick(getId());
				}
				bg.addAction(Actions.alpha(1f));
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				if (!touchPoint.epsilonEquals(x, y, 40)) {
					touchPoint.set(0, 0);
					bg.addAction(Actions.alpha(.8f));
				}
				super.touchDragged(event, x, y, pointer);
			}
		});
	}

	public void init(String title, LabelStyle style, float width, float height) {
		touchPoint = new Vector2();
		setSize(width, height);
		bg = new Img(new NinePatch(new NinePatch(
				Assets.instance.ui.reg_ninepatch), new Color(0, 191 / 255f, 1,
				1)));
		bg.setSize(width, height);
		this.title = new Label(title, style);
		if (align == LEFT) {
			this.title.setPosition(56, bg.getY() + bg.getHeight() / 2
					- this.title.getHeight() / 2);
		} else {
			this.title
					.setPosition(
							getWidth() / 2 - this.title.getWidth() / 2,
							bg.getY() + bg.getHeight() / 2
									- this.title.getHeight() / 2);
		}
		addActor(bg);
		addActor(this.title);
		addLine(this, 2, Color.GRAY);
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
}
