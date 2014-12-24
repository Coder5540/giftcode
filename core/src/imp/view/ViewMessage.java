package imp.view;

import utils.factory.FontFactory.fontType;
import utils.factory.StringSystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.View;

public class ViewMessage extends View {
	Image	bg;
	Table	tbContent;
	Label	lbShortDescription;
	int		id;
	String	content;
	String	sender;

	public ViewMessage(String sender, String content, int id) {
		super();
		this.content = content;
		this.id = id;
		this.sender = sender;
	}

	public void buildComponent() {
		LabelStyle style = new LabelStyle();
		style.font = Assets.instance.fontFactory.getFont(20, fontType.Medium);
		style.fontColor = new Color(Color.BLACK);
		lbShortDescription = new Label(content, style);
		lbShortDescription.setAlignment(Align.top, Align.left);
		lbShortDescription.setTouchable(Touchable.disabled);
		lbShortDescription.setWidth(getWidth() - 40);
		lbShortDescription.setWrap(true);

		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch, Color.WHITE)));
		top();
		add(lbShortDescription).expand().fill().align(Align.top).padLeft(20)
				.padTop(20).padRight(20);
		setTouchable(Touchable.enabled);
	}

	@Override
	public void show(final OnCompleteListener listener) {
		_viewController.setCurrentView(this);
		_viewController.toFront(name);
		addAction(Actions.sequence(Actions.alpha(1f, .2f),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						setTouchable(Touchable.enabled);
						setViewState(ViewState.SHOW);
						if (listener != null)
							listener.done();
					}
				})));
		((TopBarView) getViewController().getView(StringSystem.VIEW_ACTION_BAR))
				.setTopName(getLabel());
	}

	@Override
	public void hide(final OnCompleteListener onCompleteListener) {
		addAction(Actions.sequence(Actions.alpha(0f, 0.1f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						if (onCompleteListener != null)
							onCompleteListener.done();
						TraceView.instance.removeView(getName());
						getViewController().removeView(getName());
						((TopBarView) getViewController().getView(
								StringSystem.VIEW_ACTION_BAR))
								.setTopName("Hòm Thư");
					}
				})));
	}

	@Override
	public void update(float delta) {

		super.update(delta);
	}

	@Override
	public void back() {
		hide(null);
	}

	@Override
	public String getLabel() {
		return sender;
	}

}
