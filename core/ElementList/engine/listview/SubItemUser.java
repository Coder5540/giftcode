package engine.listview;

import utils.elements.Img;
import utils.listener.OnClickListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.coder5560.game.assets.Assets;

public class SubItemUser extends ISubItem {
	Img						bg;
	private OnClickListener	onClickListener;
	private int				id;

	public SubItemUser(int idEvent, String title, LabelStyle style,
			float width, float height) {
		this.id = idEvent;
		init(title, style, width, height);
		addListener(new InputListener() {
			Vector2	touchPoint	= new Vector2();

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
					if (onClickListener != null) {
						onClickListener.onClick(id);
					}
					bg.addAction(Actions.alpha(1f));
				}
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				if (!touchPoint.epsilonEquals(x, y, 40)) {
					touchPoint.set(0, 0);
				}
				bg.addAction(Actions.alpha(1f));
				super.touchDragged(event, x, y, pointer);
			}
		});
	}

	public void init(String title, LabelStyle style, float width, float height) {
		setSize(width, height);
		bg = new Img(new NinePatch(Assets.instance.ui.reg_ninepatch));
		bg.setColor(0, 191 / 255f, 1f, 1f);
		bg.setSize(width, height);
		addActor(bg);
		addLine(this, 2, Color.GRAY);
	}

}