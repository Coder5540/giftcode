package imp.view;

import utils.factory.Style;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.IViewController;
import com.coder5560.game.views.View;

import engine.listview.ListView;

public class ViewTestList extends View {
	ListView	listView;

	@Override
	public void build(Stage stage, IViewController viewController,
			String viewName, Rectangle bound) {
		super.build(stage, viewController, viewName, bound);
		setBackground(new NinePatchDrawable(Style.ins.np2));
	}

	void setHeightForCell(Table tb, Actor a, float height) {
		tb.getCell(a).height(height);
		tb.invalidateHierarchy();
		tb.layout();
	}

	public void buildComponent() {
		listView = new ListView(getViewController(), new Table(),
				new Rectangle(0, 0, getWidth(), getHeight()));
		addActor(listView);
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
