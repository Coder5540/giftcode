package engine.listview;

import utils.listener.OnClickListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.coder5560.game.assets.Assets;

public abstract class ISubItem extends Table{

	public Image					bg;
	public OnClickListener	onClickListener;
	public int				id;
	public float tapScale = 1.1f;

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.clearListeners();
		this.onClickListener = onClickListener;
	}

	public void addLine(Group group, float height, Color color) {
		Image line = new Image(Assets.instance.ui.reg_ninepatch);
		line.setColor(color);
		line.setHeight(height);
		line.setWidth(group.getWidth());
		group.addActor(line);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getTapScale() {
		return tapScale;
	}

	public void setTapScale(float tapScale) {
		this.tapScale = tapScale;
	}

	
	
}
