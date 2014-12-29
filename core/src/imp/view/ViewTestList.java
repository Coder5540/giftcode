package imp.view;

import utils.factory.Style;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.bgate.engine.list.Item;
import com.bgate.engine.list.ListEvent;
import com.bgate.engine.list.ListItem;
import com.bgate.engine.list.SubItem;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.IViewController;
import com.coder5560.game.views.View;

public class ViewTestList extends View {
	ListItem	listItem;

	@Override
	public void build(Stage stage, IViewController viewController,
			String viewName, Rectangle bound) {
		super.build(stage, viewController, viewName, bound);
		setBackground(new NinePatchDrawable(Style.ins.np2));
	}

	public void buildComponent() {
		final Table root = new Table();
		root.setSize(getWidth(), getHeight());
		root.top();
		root.defaults().expandX().fillX().height(60).align(Align.center);
		final ScrollPane scrollPane = new ScrollPane(root);
		scrollPane.setScrollingDisabled(true, false);
		add(scrollPane).expand().fill().top();

		ListEvent event = new ListEvent() {

			@Override
			public void notifyEvent(int event) {
				root.invalidateHierarchy();
				ViewTestList.this.layout();
			}
		};

		listItem = new ListItem(root, scrollPane);
		for (int i = 0; i < 10; i++) {
			Item item = new Item(event, i, 400, 60,
					Assets.instance.ui.getIconUser(), "Test", "(12)", null,
					new Table());
			listItem.addItem(item);
		}

		Item item = listItem.getItem(2);
		for (int i = 0; i < 5; i++) {
			SubItem subItem = new SubItem(item, 100 * item.getId() + i, 400,
					200, Assets.instance.ui.getIconUser(), "Test", "(12)", null);
			item.addSubItem(subItem);
		}

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
