package imp.view;

import utils.elements.CustomTable;
import utils.elements.Img;
import utils.factory.FontFactory.fontType;
import utils.factory.StringSystem;
import utils.networks.UserInfo;
import utils.screen.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.listener.OnClickListener;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.views.IViewController;
import com.coder5560.game.views.View;

public class HomeView extends View {
	CustomTable	content;
	ScrollPane	scrollPane;

	public HomeView() {
	}

	public HomeView buildComponent() {
		content = new CustomTable();
		content.setSize(getWidth(), getHeight());
		content.top();
		scrollPane = new ScrollPane(content);
		scrollPane.setSmoothScrolling(true);
		scrollPane.setScrollingDisabled(true, false);
		add(scrollPane).expand().fill().top();
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch, Color.WHITE)));
		content.defaults().width(220).height(220);
		buildBypermission();
		return this;
	}

	public void buildBypermission() {
		int[] permission = UserInfo.getInstance().getPermission().permission;
		for (int i = 0; i < permission.length; i++) {
			if (UserInfo.getInstance().getPermission().isHasPermission(i)
					&& !getString(i).equalsIgnoreCase("")) {
				IconFunction iconFunction = new IconFunction(
						getViewController(), 220, 220, new Texture(
								Gdx.files.internal("Img/Add-User-icon.png")),
						getString(i));
				content.add(iconFunction).pad(20);
				if (content.getChildren().size % 2 == 0
						&& content.getChildren().size > 0) {
					content.row();
				}
			}
		}
	}
	@Override
	public String getLabel() {
		return super.getLabel();
	}
//	public void buildBypermission() {
//		int[] permission = UserInfo.getInstance().getPermission().permission;
//		for (int i = 0; i < permission.length; i++) {
//			if (UserInfo.getInstance().getPermission().isHasPermission(i)
//					&& !getString(i).equalsIgnoreCase("")) {
//				IconFunction iconFunction = new IconFunction(
//						getViewController(), 220, 220, new Texture(
//								Gdx.files.internal("Img/Add-User-icon.png")),
//								getString(i));
//				content.add(iconFunction).pad(20);
//				if (content.getChildren().size % 2 == 0
//						&& content.getChildren().size > 0) {
//					content.row();
//				}
//			}
//		}
//	}

	private String getString(int i) {
		if (i == 0)
			return StringSystem.FUNCTION_USERMANAGEMENT_ACTIVE;
		if (i == 1)
			return StringSystem.FUNCTION_USERMANAGEMENT_UN_ACTIVE;
		if (i == 2)
			return StringSystem.FUNCTION_USERMANAGEMENT_BLOCK;
		if (i == 3)
			return StringSystem.FUNCTION_USERMANAGEMENT_ADD_MONEY;
		if (i == 4)
			return StringSystem.FUNCTION_MAIL_ALL_MAIL;
		if (i == 5)
			return StringSystem.FUNCTION_LOG_SEND_MONEY;
		if (i == 6)
			return StringSystem.FUNCTION_LOG_RECIEVE_MONEY;
		if (i == 7)
			return StringSystem.FUNCTION_GIFT_CODE_SELL_GIFTCODE;
		if (i == 8)
			return StringSystem.FUNCTION_GIFT_CODE_LIST_GIFTCODE;
		return "";
	}
	@Override
	public void show(OnCompleteListener listener) {
		super.show(listener);
	}

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
	}

	Actor	actorExit	= new Actor();

	@Override
	public void update(float delta) {
		actorExit.act(delta);
	}

	@Override
	public void back() {
		if (actorExit.getActions().size > 0) {
			Gdx.app.exit();
		} else {
			Toast.makeText(getStage(), "Nhấn thêm lần nữa để thoát !", 0.3f);
			actorExit.addAction(Actions.delay(1f));
		}
	}
}

class IconFunction extends Group {
	Img		bg, icon, bgTitle;
	Label	lbTitle;

	public IconFunction(final IViewController viewController, float width,
			float height, Texture iconTexture, final String title) {
		setSize(width, height);
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Assets.instance.fontFactory
				.getFont(14, fontType.Bold);
		lbTitle = new Label(title, labelStyle);
		labelStyle.fontColor = Color.BLACK;

		bg = new Img(new NinePatch(Assets.instance.ui.reg_ninepatch4,
				Color.GRAY));
		bg.setSize(width, height);
		icon = new Img(iconTexture);
		icon.setSize(3 * width / 4, 3 * height / 4);
		bgTitle = new Img(Assets.instance.ui.reg_ninepatch);
		bgTitle.setTouchable(Touchable.disabled);
		bgTitle.setColor(240 / 255f, 240 / 255f, 240 / 255f, 0.75f);
		bgTitle.setSize(width - 12, 20 + lbTitle.getHeight());
		this.addActor(bg);
		this.addActor(icon);
		this.addActor(bgTitle);
		this.addActor(lbTitle);
		icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(float x, float y) {
				MainMenuView mainMenuView = (MainMenuView) viewController
						.getView(StringSystem.VIEW_MAIN_MENU);
				if (mainMenuView != null) {
					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_USERMANAGEMENT_ACTIVE)) {
						mainMenuView.onActiveUserClicked.onClick(x, y);
					}
					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_USERMANAGEMENT_UN_ACTIVE)) {
						mainMenuView.onUnActiveUserClicked.onClick(x, y);
					}
					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_USERMANAGEMENT_BLOCK)) {
						mainMenuView.onBlockUserClicked.onClick(x, y);
					}
					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_USERMANAGEMENT_ADD_MONEY)) {
						mainMenuView.onAddMoneyClicked.onClick(x, y);
					}

					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_LOG_SEND_MONEY)) {
						mainMenuView.onHistorySendMoney.onClick(x, y);
					}
					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_LOG_RECIEVE_MONEY)) {
						mainMenuView.onHistoryReceiveMoney.onClick(x, y);
					}

					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_MAIL_ALL_MAIL)) {
						mainMenuView.onAllMailClicked.onClick(x, y);
					}

					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_GIFT_CODE_LIST_GIFTCODE)) {
						mainMenuView.onGiftcodeClicked.onClick(x, y);
					}
					if (title
							.equalsIgnoreCase(StringSystem.FUNCTION_GIFT_CODE_SELL_GIFTCODE)) {
						mainMenuView.onSellGiftCode.onClick(x, y);
					}

				}
			}
		});
		valid();
	}
	
	
	

	public void valid() {
		icon.setPosition(bg.getWidth() / 2 - icon.getWidth() / 2,
				bg.getHeight() / 2 - icon.getHeight() / 2);
		bgTitle.setPosition(bg.getX() + 6, bg.getY() + 10);
		lbTitle.setPosition(bgTitle.getX() + 5,
				bgTitle.getX() + bgTitle.getHeight() / 2 - lbTitle.getHeight()
						/ 2);
	}
}
