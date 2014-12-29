package utils.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coder5560.game.listener.OnClickListener;

public class Img extends Image {
	private boolean			ignoreUpdate		= false;
	private OnClickListener	onClickListener;
	private Vector2			offsetBoundClick	= new Vector2(0, 0);
	private float tapScale = 1.2f;
	public Img(Texture texture) {
		super(texture);
		setup();
	}

	public Img(TextureRegion region) {
		super(region);
		setup();
	}

	private void setup() {
		setOriginCenter();
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				final float px = x;
				final float py = y;
				addAction(Actions.sequence(Actions.scaleTo(tapScale, tapScale, .05f),
						Actions.scaleTo(1f, 1f, .1f, Interpolation.swingOut),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								if (onClickListener != null) {
									onClickListener.onClick(px, py);
								}
							}
						})));
			}
		});
	}

	@Override
	public void act(float delta) {
		if (ignoreUpdate)
			return;
		super.act(delta);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && this.getTouchable() != Touchable.enabled)
			return null;
		return x >= -offsetBoundClick.x
				&& x < (getWidth() + offsetBoundClick.x)
				&& y >= -offsetBoundClick.y
				&& y < (getHeight() + offsetBoundClick.y) ? this : null;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	public Img(TextureRegion region, float width, float height) {
		super(region);
		setSize(width, height);
		setOriginCenter();
	}

	public Img(NinePatch ninePatch) {
		super(ninePatch);
		setOriginCenter();
	}

	public Vector2 getPosition() {
		return new Vector2(getX(), getY());
	}

	public void setPosition(Vector2 position) {
		setX(position.x);
		setY(position.y);
	}

	public Rectangle getBound() {
		return new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public void setOriginCenter() {
		setOrigin(getWidth() / 2, getHeight() / 2);
	}

	public Vector2 getCenter() {
		return new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	public float getRight() {
		return getX() + getWidth();
	}

	public boolean isIgnoreUpdate() {
		return ignoreUpdate;
	}

	public void setIgnoreUpdate(boolean ignoreUpdate) {
		this.ignoreUpdate = ignoreUpdate;
	}

	public OnClickListener getOnClickListener() {
		return onClickListener;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public Vector2 getOffsetBoundClick() {
		return offsetBoundClick;
	}

	public void setOffsetBoundClick(Vector2 offsetBoundClick) {
		this.offsetBoundClick = offsetBoundClick;
	}

	public float getTapScale() {
		return tapScale;
	}

	public void setTapScale(float tapScale) {
		this.tapScale = tapScale;
	}

}
