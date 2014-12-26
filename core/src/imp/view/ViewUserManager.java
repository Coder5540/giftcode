package imp.view;

import utils.elements.Img;
import utils.factory.FontFactory.fontType;
import utils.factory.Style;
import utils.listener.OnClickListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.IViewController;
import com.coder5560.game.views.View;

public class ViewUserManager extends View {
	Table	root;

	@Override
	public String getLabel() {
		return super.getLabel();
	}

	@Override
	public void build(Stage stage, IViewController viewController,
			String viewName, Rectangle bound) {
		super.build(stage, viewController, viewName, bound);
	}

	public void buildComponent() {
		buildRoot();
	}

	private void buildRoot() {
		root = new Table();
		root.setSize(getWidth(), getHeight());
		root.defaults().expand().fillX().height(60);
		for (int i = 0; i < 10; i++) {
			ItemUser itemUser = new ItemUser(i, 400, 60,
					Assets.instance.ui.getRegUsermanagement(), "Admin", " (1)",
					null, new Group());
			root.add(itemUser);
		}
		add(root).expand().fill();
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

}

class ItemUser extends Group {

	int				id;
	Img				icon, bg;
	Label			lbText, lbTextNumber;
	OnClickListener	onClickListener;
	Group			data;

	public enum ItemUserState {
		UP, DOWN;
	}

	ItemUserState	itemUserState	= ItemUserState.UP;

	public ItemUser(int id, float width, float height, TextureRegion reg_icon,
			String text, String textnumber,
			final OnClickListener onClickListener, Group data) {
		super();
		this.id = id;
		this.icon = new Img(reg_icon);
		this.bg = new Img(Style.ins.np1);
		bg.setColor(Color.BLACK);
		this.lbText = new Label(text, Style.ins.getLabelStyle(22,
				fontType.Bold, Color.WHITE));
		this.lbTextNumber = new Label(textnumber, Style.ins.getLabelStyle(20,
				fontType.Regular, Color.WHITE));
		this.data = data;

		addActor(bg);
		addActor(icon);
		addActor(lbText);
		addActor(lbTextNumber);
	}

	public void setUp() {
		bg.setSize(getWidth(), getHeight());
		icon.setSize(4 * getHeight() / 5, 4 * getHeight() / 5);
		bg.setPosition(0, 0);
		icon.setPosition(getHeight() / 10, getHeight() / 10);
		lbText.setPosition(icon.getX(Align.right) + 20, icon.getY(Align.center)
				- lbText.getHeight() / 2);
		lbTextNumber.setPosition(lbText.getX(Align.right) + 20,
				icon.getY(Align.center) - lbText.getHeight() / 2);
		bg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addAction(Actions.sequence(Actions.scaleTo(1.2f, 1.2f, .05f),
						Actions.scaleTo(1f, 1f, .1f, Interpolation.swingOut),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								if (onClickListener != null) {
									onClickListener.onClick(id);
								}
								if (itemUserState == ItemUserState.UP) {
									itemUserState = ItemUserState.DOWN;
								} else {
									itemUserState = ItemUserState.UP;
								}
							}
						})));
			}
		});
	}

}
