package imp.view;

import utils.elements.CustomTable;
import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.StringSystem;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.screen.Toast;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.listener.OnClickListener;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.View;

public class ViewMails extends View {

	private boolean		isDoneAction	= true;
	public CustomTable	root;
	CustomTable			bgDialog;
	private JsonValue	dataResponse	= null;
	private JsonValue	responseSeen	= null;
	Color				colorBg			= new Color(255 / 255f, 255 / 255f,
												255 / 255f, 1f);

	public void buildComponent() {
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch, colorBg)));
		root = new CustomTable();
		root.setSize(getWidth(), getHeight());
		root.defaults().expandX().fillX().height(80).align(Align.left);
		root.top();

		ScrollPane scroll = new ScrollPane(root);
		scroll.setSmoothScrolling(true);
		scroll.setScrollingDisabled(true, false);
		scroll.setFadeScrollBars(true);
		scroll.setScrollBarPositions(false, true);
		scroll.getStyle().vScrollKnob = new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.getNinePathKnob(), Color.BLACK));
		scroll.getStyle().vScrollKnob.setTopHeight(4);
		add(scroll).expand().fill().top();
		createDialogNoMessage();
	}

	void updateData(JsonValue jsonArray) {
		if (jsonArray.size == 0) {
			showNoMessage(null);
			return;
		}
		hideNoMessage(null);
		for (int i = 0; i < jsonArray.size; i++) {
			JsonValue mail = jsonArray.get(i);
			String sender = mail.getString(ExtParamsKey.SENDER);
			String content = mail.getString(ExtParamsKey.CONTENT);
			int id = mail.getInt(ExtParamsKey.ID);
			int read = mail.getInt(ExtParamsKey.IS_READ);
			long time = mail.getLong(ExtParamsKey.GEN_DATE);
			ItemMail itemMail = new ItemMail(id, sender, content,
					(read == 0) ? false : true, time, root.getWidth(), 80);
			root.add(itemMail);
			root.row();
		}
	}

	public void requestMail() {
		Request.getInstance().getMessage(AppPreference.instance.name,
				new HttpResponseListener() {

					@Override
					public void handleHttpResponse(HttpResponse httpResponse) {
						String dataJson = httpResponse.getResultAsString();
						JsonValue respone = new JsonReader().parse(dataJson);
						dataResponse = respone.get(ExtParamsKey.LIST);
					}

					@Override
					public void failed(Throwable t) {

					}

					@Override
					public void cancelled() {
					}
				});
	}

	@Override
	public void show(OnCompleteListener listener) {
		super.show(listener);
		Loading.ins.hide();
		requestMail();
	}

	@Override
	public void hide(OnCompleteListener listener) {
		super.hide(listener);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (dataResponse != null) {
			root.clearChildren();
			updateData(dataResponse);
			dataResponse = null;
		}
		if (responseSeen != null) {
			boolean result = responseSeen.getBoolean(ExtParamsKey.RESULT);
			if (result) {
				int number_unseen = responseSeen
						.getInt(ExtParamsKey.NUMBER_UNSEEN);
				((MainMenuView) getViewController().getView(
						StringSystem.VIEW_MAIN_MENU)).setNotify(number_unseen);
				String message = responseSeen.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(getStage(), message, Toast.LENGTH_SHORT);
			}
			responseSeen = null;
		}
	}

	public void request() {

	}

	@Override
	public void back() {
		super.back();
		getViewController().removeView(getName());
		((TopBarView) getViewController().getView(StringSystem.VIEW_ACTION_BAR))
				.setTopName("Bài 69 Giftcode");
	}

	public boolean isDoneAction() {
		return isDoneAction;
	}

	public void setDoneAction(boolean isDoneAction) {
		this.isDoneAction = isDoneAction;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	void addLine(Table table, float height) {
		Image line = new Image(Assets.instance.ui.reg_ninepatch);
		line.setColor(new Color(100 / 255f, 100 / 255f, 100 / 255f, 0.7f));
		line.setHeight(height);
		line.setWidth(table.getWidth());
		table.add(line).expandX().fillX().height(height).padTop(2).padLeft(10)
				.padRight(10).colspan(4);
		table.row();
	}

	void addLine(Table table, float height, Color color) {
		Image line = new Image(Assets.instance.ui.reg_ninepatch);
		line.setColor(color);
		line.setHeight(height);
		line.setWidth(table.getWidth());
		table.add(line).expandX().fillX().height(height).padTop(2).padLeft(10)
				.padRight(10).colspan(4);
		table.row();
	}

	public void createDialogNoMessage() {
		bgDialog = new CustomTable();
		bgDialog.setSize(getWidth(), getHeight());
		bgDialog.setColor(getColor());
		bgDialog.setTouchable(Touchable.enabled);

		LabelStyle style = new LabelStyle();
		style.font = Assets.instance.fontFactory.getFont(30, FontType.Light);
		style.fontColor = Color.BLACK;
		Label label = new Label("Bạn Không Có Tin Nhắn Nào !", style);
		label.setAlignment(Align.center);
		bgDialog.add(label).expand().fill().center();
		addActor(bgDialog);
		bgDialog.setVisible(false);

	}

	public void showNoMessage(OnCompleteListener onCompleteListeners) {
		bgDialog.toFront();
		bgDialog.setVisible(true);
	}

	public void hideNoMessage(OnCompleteListener onCompleteListeners) {
		bgDialog.toBack();
		bgDialog.setVisible(false);
	}

	class ItemMail extends Group {
		Image			bg;
		Image			icon;
		LabelStyle		styleTitle, styleDesciption, styleTime;
		Label			lbTitle, lbShortDescription, lbTime;
		OnClickListener	onClickListener;
		Color			bgColor, fontColor;
		public int		index;
		private boolean	isRead;

		public ItemMail(final int index, final String title,
				final String description, boolean isRead, final long time,
				final float width, float height) {
			this.setSize(width, height);
			this.index = index;
			this.isRead = isRead;
			fontColor = new Color(Constants.COLOR_ACTIONBAR);
			setRead(isRead);
			styleTitle = new LabelStyle();
			styleTitle.font = Assets.instance.fontFactory.getFont(22,
					FontType.Medium);
			styleTitle.fontColor = fontColor;

			styleDesciption = new LabelStyle();
			styleDesciption.font = Assets.instance.fontFactory.getFont(18,
					FontType.Light);

			styleDesciption.fontColor = fontColor;

			styleTime = new LabelStyle();
			styleTime.font = Assets.instance.fontFactory.getFont(18,
					FontType.Light);
			styleTime.fontColor = fontColor;
			lbTitle = new Label(title, styleTitle);
			lbTitle.setAlignment(Align.center, Align.left);
			lbTitle.setTouchable(Touchable.disabled);

			lbShortDescription = new Label(Factory.getSubString(description,
					2 * width / 3 - 10, styleDesciption.font), styleDesciption);
			lbShortDescription.setAlignment(Align.center, Align.left);
			lbShortDescription.setTouchable(Touchable.disabled);
			lbShortDescription.setWrap(true);

			lbTime = new Label(Factory.getTime(time), styleTime);
			icon = new Image(Assets.instance.ui.reg_ninepatch);
			icon.setColor(Color.GRAY);
			icon.setTouchable(Touchable.disabled);

			bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch4, 6,
					6, 6, 6));

			bg.setColor(bgColor);
			bg.addListener(new ClickListener() {
				boolean	isTouch	= false;
				Vector2	touch	= new Vector2();

				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					touch.set(x, y);
					bg.setColor(new Color(229 / 255f, 229 / 255f, 229 / 255f,
							0.4f));
					isTouch = true;
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					if (!isTouch || touch.isZero())
						return;

					if (onClickListener != null)
						onClickListener.onClick(x, y);
					if (!getIsRead()) {
						requestSeen();
					}
					setRead(true);
					bg.setColor(bgColor);
					ViewMessage viewMessage = new ViewMessage(title,
							description, index, time);
					viewMessage.build(getStage(), getViewController(),
							"view_message", new Rectangle(0, 0,
									Constants.WIDTH_SCREEN,
									Constants.HEIGHT_SCREEN
											- Constants.HEIGHT_ACTIONBAR));
					viewMessage.buildComponent();
					viewMessage.show(null);

				}

				@Override
				public void touchDragged(InputEvent event, float x, float y,
						int pointer) {
					if (!touch.epsilonEquals(x, y, 10)) {
						touch.set(0, 0);
						bg.setColor(bgColor);
					}
					super.touchDragged(event, x, y, pointer);
				}
			});

			addActor(bg);
			// addActor(icon);
			addActor(lbTitle);
			addActor(lbShortDescription);
			addActor(lbTime);
			valid();
		}

		private void setRead(boolean isRead) {
			this.isRead = isRead;
			if (isRead) {
				bgColor = new Color(250 / 255f, 250 / 255f, 250 / 255f, 0.2f);
				fontColor = new Color(Constants.COLOR_ACTIONBAR);
			} else {
				bgColor = Constants.COLOR_ACTIONBAR;
				fontColor = new Color(Color.WHITE);
			}
			updateColor();
		}

		public void updateColor() {
			if (lbTime != null)
				lbTime.setColor(fontColor);
			if (lbTitle != null)
				lbTitle.setColor(fontColor);
			if (lbShortDescription != null)
				lbShortDescription.setColor(fontColor);
			if (bg != null)
				bg.setColor(bgColor);
		}

		boolean getIsRead() {
			return this.isRead;
		}

		public void valid() {
			float w = this.getWidth();
			float h = this.getHeight();
			bg.setSize(w - 10, h);
			bg.setPosition(5, 0);
			lbShortDescription.setWidth(2 * w / 3);

			lbShortDescription.setPosition(10, lbTitle.getHeight() - 20);
			lbTime.setPosition(
					bg.getX() + bg.getWidth() - 20 - lbTime.getWidth(),
					bg.getY() + 4);
			lbTitle.setPosition(bg.getX() + 10, +lbShortDescription.getX() + 5
					+ lbShortDescription.getHeight());
		}

		public void requestSeen() {
			Request.getInstance().requestSeen(AppPreference.instance.name,
					index, new HttpResponseListener() {

						@Override
						public void handleHttpResponse(HttpResponse httpResponse) {
							String dataRespone = httpResponse
									.getResultAsString();
							responseSeen = new JsonReader().parse(dataRespone);
						}

						@Override
						public void failed(Throwable t) {

						}

						@Override
						public void cancelled() {

						}
					});
		}
	}

	@Override
	public String getLabel() {
		return "Hòm Thư";
	}
}
