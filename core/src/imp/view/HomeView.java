package imp.view;

import utils.elements.GalleryViewHorizontal;
import utils.factory.FontFactory.fontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.UIUtils;
import com.coder5560.game.views.View;

public class HomeView extends View {
	GalleryViewHorizontal	galleryViewHorizontal;

	public HomeView() {
	}

	public HomeView buildComponent() {
		setBackground(new NinePatchDrawable(new NinePatch(Assets.instance.ui.reg_ninepatch)));
		galleryViewHorizontal = new GalleryViewHorizontal(this, 1);
		galleryViewHorizontal.pages.setOverscroll(false, false);
		for (int i = 0; i < 10; i++) {
			Table page = galleryViewHorizontal.newPage();
			Label lb = UIUtils.getLabel("View " + i,
					Assets.instance.fontFactory.getFont(30, fontType.Light),
					Color.WHITE);
			lb.setAlignment(Align.center);
			page.add(lb).expand().fill().center().padTop(220);
		}
		createListener();
		return this;
	}

	void createListener() {
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
	}

	@Override
	public void destroyComponent() {
	}

	@Override
	public void back() {
	}

	public void setFocus(int i) {
		galleryViewHorizontal.pages.focusOnPage(i);
	}

	public int getFocus() {
		return galleryViewHorizontal.pages.getCurrentPage();
	}
}
