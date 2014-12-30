package imp.view;

import utils.elements.Img;
import utils.factory.AppPreference;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Log;
import utils.factory.StringSystem;
import utils.factory.Style;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.GameEvent;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.ItemListener;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.View;

public class HomeViewV2 extends View {
	String		response		= "";
	boolean		isLoad			= false;

	Table		content;
	String		username		= "";
	int			role_id;
	Color		colorItem		= new Color(255 / 255f, 255 / 255f, 255 / 255f,
										.4f);
	Color		colorTextTitle	= new Color(0.2f, 0.2f, 0.2f, 1f);
	Color		colorText		= Constants.COLOR_ACTIONBAR;

	public long	tongtiendacap;
	public long	tongtiendanhan;
	public long	tongtiengiftcode;

	public HomeViewV2 buildComponent() {
		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
				new Color(250 / 255f, 250 / 255f, 250 / 255f, 1)));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);
		content = new Table();
		content.setBounds(0, 0, getWidth(), getHeight());
		content.top();
		addActor(content);
		role_id = UserInfo.getInstance().getRoleId();
		setUserName(AppPreference.instance.name);

		return this;
	}

	public void setUserName(String name) {
		this.username = name;
	}

	public void setRoleId(int role_id) {
		this.role_id = role_id;
	}

	public String getUserName() {
		return this.username;
	}

	void addItem(int id, String title, String content, ItemListener listener) {
		ItemTotal item = new ItemTotal(title, content, getWidth(), 80);
		// this.content.add(item).width(item.getWidth()).height(item.getHeight())
		// .padBottom(2).row();
		if (id == 3) {
			if (role_id != 0 && role_id != 4) {
				item.setListener(listener);
			}
		} else {
			item.setListener(listener);
		}
		this.content.add(item).expandX().fillX().height(item.getHeight())
				.padBottom(2).row();
	}

	Actor	actorExit	= new Actor();

	@Override
	public void update(float delta) {
		actorExit.act(delta);
		if (isLoad) {
			content.clear();
			padTop(5);
			JsonValue json = (new JsonReader()).parse(response);
			boolean result = json.getBoolean(ExtParamsKey.RESULT);
			String message = json.getString(ExtParamsKey.MESSAGE);
			if (result) {
				tongtiendanhan = json.getLong(ExtParamsKey.MONEY_RECEIVE);
				tongtiendacap = json.getLong(ExtParamsKey.MONEY_TRANSFER);
				tongtiengiftcode = json.getLong(ExtParamsKey.MONEY_GIFT_CODE);
				long tiencon = json.getLong(ExtParamsKey.MONEY_REMAIN);
				if (role_id != 0) {
					addItem(1, "Tổng tiền đã nhận",
							Factory.getDotMoney(tongtiendanhan) + " "
									+ UserInfo.currency, new ItemListener() {
								@Override
								public void onItemClick() {

									if (getViewController()
											.isContainView(
													StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART)) {
									} else {
										ViewLogChart viewLogReceiveChart = new ViewLogChart();
										viewLogReceiveChart
												.build(getStage(),
														getViewController(),
														StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART,
														new Rectangle(
																0,
																0,
																Constants.WIDTH_SCREEN,
																Constants.HEIGHT_SCREEN
																		- Constants.HEIGHT_ACTIONBAR));
										viewLogReceiveChart
												.buildComponent(ViewLogChart.TYPE_RECEIVE_MONEY);
									}
									((ViewLogChart) getViewController()
											.getView(
													StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART))
											.setTotalMoney(tongtiendanhan);
									((ViewLogChart) getViewController()
											.getView(
													StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART))
											.setUserName(username);
									((ViewLogChart) getViewController()
											.getView(
													StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART))
											.setTitleView("Thống kê tiền đã nhận");
									((ViewLogChart) getViewController()
											.getView(
													StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART))
											.setTotalMoney(tongtiendanhan);
									getViewController()
											.getView(
													StringSystem.VIEW_LOG_RECEIVE_MONEY_CHART)
											.show(null);
								}
							});
				}
				System.out.println("TIEN DA CAP : " + tongtiendacap);
				if (role_id != 3) {
					addItem(2, "Tổng tiền cấp",
							Factory.getDotMoney(Math.abs(tongtiendacap)) + " "
									+ UserInfo.currency, new ItemListener() {
								@Override
								public void onItemClick() {
									if (getViewController()
											.isContainView(
													StringSystem.VIEW_LOG_SEND_MONEY_CHART)) {
									} else {
										ViewLogChart viewLogSendChart = new ViewLogChart();
										viewLogSendChart
												.build(getStage(),
														getViewController(),
														StringSystem.VIEW_LOG_SEND_MONEY_CHART,
														new Rectangle(
																0,
																0,
																Constants.WIDTH_SCREEN,
																Constants.HEIGHT_SCREEN
																		- Constants.HEIGHT_ACTIONBAR));
										viewLogSendChart
												.buildComponent(ViewLogChart.TYPE_SEND_MONEY);
									}
									((ViewLogChart) getViewController()
											.getView(
													StringSystem.VIEW_LOG_SEND_MONEY_CHART))
											.setTotalMoney(tongtiendacap);
									((ViewLogChart) getViewController()
											.getView(
													StringSystem.VIEW_LOG_SEND_MONEY_CHART))
											.setUserName(username);
									((ViewLogChart) getViewController()
											.getView(
													StringSystem.VIEW_LOG_SEND_MONEY_CHART))
											.setTitleView("Thống kê tiền đã chuyển");
									((ViewLogChart) getViewController()
											.getView(
													StringSystem.VIEW_LOG_SEND_MONEY_CHART))
											.setTotalMoney(tongtiendacap);

									getViewController()
											.getView(
													StringSystem.VIEW_LOG_SEND_MONEY_CHART)
											.show(null);
								}
							});
				}

				addItem(3, "Tổng tiền sinh GiftCode",
						Factory.getDotMoney(tongtiengiftcode) + " "
								+ UserInfo.currency, new ItemListener() {
							@Override
							public void onItemClick() {
								if (getViewController()
										.isContainView(
												StringSystem.VIEW_LOG_MONEY_GIFTCODE_CHART)) {
								} else {
									ViewLogChart viewLogGiftCodeChart = new ViewLogChart();
									viewLogGiftCodeChart
											.build(getStage(),
													getViewController(),
													StringSystem.VIEW_LOG_MONEY_GIFTCODE_CHART,
													new Rectangle(
															0,
															0,
															Constants.WIDTH_SCREEN,
															Constants.HEIGHT_SCREEN
																	- Constants.HEIGHT_ACTIONBAR));
									viewLogGiftCodeChart
											.buildComponent(ViewLogChart.TYPE_GIFTCODE);
								}
								((ViewLogChart) getViewController()
										.getView(
												StringSystem.VIEW_LOG_MONEY_GIFTCODE_CHART))
										.setTimeReload(tongtiengiftcode);
								((ViewLogChart) getViewController()
										.getView(
												StringSystem.VIEW_LOG_MONEY_GIFTCODE_CHART))
										.setUserName(username);
								((ViewLogChart) getViewController()
										.getView(
												StringSystem.VIEW_LOG_MONEY_GIFTCODE_CHART))
										.setTitleView("Thống kê tiền GiftCode");
								((ViewLogChart) getViewController()
										.getView(
												StringSystem.VIEW_LOG_MONEY_GIFTCODE_CHART))
										.setTotalMoney(tongtiengiftcode);
								getViewController()
										.getView(
												StringSystem.VIEW_LOG_MONEY_GIFTCODE_CHART)
										.show(null);
							}
						});
				addItem(4, "Tổng tiền còn lại", Factory.getDotMoney(tiencon)
						+ " " + UserInfo.currency, null);
			} else {
				Toast.makeText(getStage(), message, 3f);
			}
			Loading.ins.hide();
			isLoad = false;
		}
		super.update(delta);
	}

	@Override
	public void show(OnCompleteListener listener) {
		// TODO Auto-generated method stub
		getTotalMoneyInfo();
		super.show(listener);
	}

	@Override
	public void onGameEvent(GameEvent gameEvent) {
		if (gameEvent == GameEvent.ONBACK
				&& TraceView.instance.getLastView().equalsIgnoreCase(getName())) {
			getTotalMoneyInfo();
		}
	}

	void getTotalMoneyInfo() {
		Request.getInstance().getTotalMoneyInfo(username,
				new getMoneyInfoListener());
	}

	class ItemTotal extends Group {
		Image	bg;

		public ItemTotal(String title, String content, float width, float height) {
			setSize(width, height);
			bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch, 0,
					0, 0, 1));
			bg.setColor(colorItem);
			bg.setSize(getWidth(), getHeight());
			Image icon = new Image(new Texture(
					Gdx.files.internal("Img/coin.png")));
			icon.setSize(getHeight() / 4, getHeight() / 4);
			// icon.setColor(colorText);

			Label lbtitle = new Label(title, Style.ins.getLabelStyle(20,
					FontType.Light, colorTextTitle));
			Label lbcontent = new Label(content, Style.ins.getLabelStyle(25,
					FontType.Regular, colorText));

			icon.setPosition(25, getHeight() / 2 + 6);
			// icon.setPosition(-icon.getWidth(), -icon.getHeight());
			lbtitle.setPosition(icon.getX() + icon.getWidth() + 12,
					getHeight() / 2 + 3);
			lbcontent.setPosition(lbtitle.getX(),
					getHeight() / 2 - lbcontent.getHeight() - 3);

			Img line = new Img(Assets.instance.ui.reg_ninepatch);
			line.setSize(getWidth() - 2 * lbtitle.getX(), 1);
			line.setColor(0, 191 / 255f, 1f, 1f);
			line.setPosition(lbtitle.getX(), 1);
			addActor(bg);
			addActor(icon);
			addActor(lbtitle);
			addActor(lbcontent);
			addActor(line);

		}

		public void setListener(final ItemListener listener) {
			if (listener != null) {
				addListener(new ClickListener() {
					@Override
					public boolean touchDown(InputEvent event, float x,
							float y, int pointer, int button) {
						Log.d("Clicked");
						bg.setColor(150 / 255f, 150 / 255f, 150 / 255f, 1);
						return super.touchDown(event, x, y, pointer, button);
					}

					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						bg.setColor(colorItem);
						listener.onItemClick();
						super.touchUp(event, x, y, pointer, button);
					}
				});
			}
		}
	}

	@Override
	public void back() {
		if (getName().equalsIgnoreCase(StringSystem.VIEW_HOME)) {
			if (actorExit.getActions().size > 0) {
				Gdx.app.exit();
			} else {
				Toast.makeText(getStage(), "Nhấn thêm lần nữa để thoát !", 0.3f);
				actorExit.addAction(Actions.delay(1f));
			}
			return;
		}
		super.back();
		getViewController().removeView(getName());
	}

	class getMoneyInfoListener implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			// TODO Auto-generated method stub
			response = httpResponse.getResultAsString();
			isLoad = true;
		}

		@Override
		public void failed(Throwable t) {
			// TODO Auto-generated method stub

		}

		@Override
		public void cancelled() {
			// TODO Auto-generated method stub

		}

	}
}
