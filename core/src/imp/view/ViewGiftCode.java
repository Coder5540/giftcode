package imp.view;

import utils.elements.PartnerPicker;
import utils.factory.AppPreference;
import utils.factory.DateTime;
import utils.factory.FontFactory.FontType;
import utils.factory.Style;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.Toast;

import com.aia.appsreport.component.table.AbstractTable;
import com.aia.appsreport.component.table.ItemGiftCodeNormal;
import com.aia.appsreport.component.table.ItemGiftCodeUsed;
import com.aia.appsreport.component.table.ItemTable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.ui.DialogCustom;
import com.coder5560.game.ui.ItemListener;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.ui.Page;
import com.coder5560.game.views.View;

public class ViewGiftCode extends View {

	private Group groupPartner;
	private PartnerPicker partnerGiftCode;
	private PartnerPicker partnerState;
	private Label lbTitle;

	private AbstractTable tableNormal;
	private AbstractTable tableUsed;
	private Page page;

	private ItemGiftCodeNormal currentItem;

	private JsonValue responeNormal;
	private JsonValue responeReturn;
	private JsonValue responeChangeState;
	private JsonValue responeUsed;
	private Table tbContent;

	public void buildComponent() {
		this.top();
		setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch)));
		partnerGiftCode = new PartnerPicker(Style.ins.selectBoxStyle);
		partnerGiftCode.addPartner(0, "Chưa sử dụng", "");
		partnerGiftCode.addPartner(1, "Đã sử dụng", "");
		partnerGiftCode.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Loading.ins.show(ViewGiftCode.this);
				if (partnerGiftCode.getSelectedIndex() == 0) {
					partnerState.setVisible(true);
					partnerGiftCode.addAction(Actions.moveTo(
							(groupPartner.getWidth()
									- partnerGiftCode.getWidth()
									- partnerState.getWidth() - 20) / 2,
							partnerGiftCode.getY(), 0.5f,
							Interpolation.exp10Out));
					Request.getInstance().getNormalGiftCode(
							AppPreference.instance.name,
							partnerState.getPartnerId(),
							new GetGiftCodeNormal());
				} else {
					partnerState.setVisible(false);
					partnerGiftCode.addAction(Actions.moveTo(
							groupPartner.getWidth() / 2
									- partnerGiftCode.getWidth() / 2,
							partnerGiftCode.getY(), 0.5f,
							Interpolation.exp10Out));
					Request.getInstance().getUsedGiftCode(
							AppPreference.instance.name, new GetGiftCodeUsed());
				}
			}
		});

		partnerState = new PartnerPicker(Style.ins.selectBoxStyle);
		partnerState.addPartner(-1, "Tất cả", "");
		partnerState.addPartner(0, "Chưa bán", "");
		partnerState.addPartner(1, "Đã bán", "");
		partnerState.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Loading.ins.show(ViewGiftCode.this);
				Request.getInstance().getNormalGiftCode(
						AppPreference.instance.name,
						partnerState.getPartnerId(), new GetGiftCodeNormal());
			}
		});

		partnerGiftCode.setSize(170, 40);
		partnerState.setSize(170, 40);

		groupPartner = new Group();
		groupPartner.setSize(getWidth(), 80);
		Image bgTop = new Image(new NinePatch(new NinePatch(
				Assets.instance.ui.reg_ninepatch3, 6, 6, 6, 6), new Color(
				245 / 255f, 245 / 255f, 245 / 255f, 1)));
		bgTop.setSize(groupPartner.getWidth(), groupPartner.getHeight());
		groupPartner.addActor(bgTop);
		groupPartner.addActor(partnerGiftCode);
		groupPartner.addActor(partnerState);
		partnerGiftCode.setPosition(
				(groupPartner.getWidth() - partnerGiftCode.getWidth()
						- partnerState.getWidth() - 20) / 2,
				groupPartner.getHeight() / 2 - partnerGiftCode.getHeight() / 2);
		partnerState.setPosition(
				partnerGiftCode.getX() + partnerGiftCode.getWidth() + 20,
				partnerGiftCode.getY());

		lbTitle = new Label("DANH SÁCH GIFT CODE CHƯA SỬ DỤNG", new LabelStyle(
				Assets.instance.fontFactory.getFont(26, FontType.Medium),
				Color.BLUE));
		lbTitle.setVisible(false);

		float[] widthColNormal = { 50, 150, 120, 100, 150, 150, 150, 260 };
		tableNormal = new AbstractTable(new Table(), widthColNormal);
		String[] titleNormal = { "STT", "ID", "Gift code", "Tiền",
				"Tiền trong game", "Ngày hết hạn", "Trạng thái", "" };
		tableNormal.setTitle(titleNormal);

		float[] widthColUsed = { 50, 150, 120, 100, 150, 150, 150, 150, 150 };
		tableUsed = new AbstractTable(new Table(), widthColUsed);
		String[] titleUsed = { "STT", "ID", "Gift code", "Tiền",
				"Tiền trong game", "Ngày hết hạn", "Ngày sử dụng", "Người nạp",
				"Trạng thái" };
		tableUsed.setTitle(titleUsed);

		page = new Page(getWidth(), 60);

		tbContent = new Table();

		this.add(groupPartner).row();
		this.add(lbTitle).padTop(20).row();
		this.add(tbContent);

		Loading.ins.show(this);
		Request.getInstance().getNormalGiftCode(AppPreference.instance.name,
				-1, new GetGiftCodeNormal());
	}

	@Override
	public void update(float delta) {
		if (responeNormal != null) {
			Loading.ins.hide();
			page.removeAllPage();
			tableNormal.removeAll();
			boolean resut = responeNormal.getBoolean(ExtParamsKey.RESULT);
			if (resut) {
				JsonValue list = responeNormal.get(ExtParamsKey.LIST);
				if (list.size > 0) {
					for (int i = 0; i < list.size; i++) {
						JsonValue content = list.get(i);
						final String giftCode = content
								.getString(ExtParamsKey.GIFT_CODE);
						long money = content.getLong(ExtParamsKey.AMOUNT);
						String currency = content
								.getString(ExtParamsKey.CURRENCY);
						long money_in_game = content
								.getLong(ExtParamsKey.MONEY_IN_GAME);
						long time = content.getLong(ExtParamsKey.DATE_EXPIRE);
						final int isSold = content.getInt(ExtParamsKey.IS_SOLD);
						final String id = content.getString(ExtParamsKey.ID);
						final ItemGiftCodeNormal item = new ItemGiftCodeNormal(
								tableNormal, isSold, new String[] {
										"" + (i + 1),
										id,
										giftCode,
										money + " " + currency,
										"" + money_in_game,
										DateTime.getStringDate(time,
												DateTime.FORMAT) });
						item.btCopy.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								super.clicked(event, x, y);
								Gdx.app.getClipboard().setContents(
										"CODE : " + giftCode + " ID : " + id);
								Toast.makeText(getStage(),
										"Đã copy vào bộ nhớ đệm",
										Toast.LENGTH_SHORT);
							}
						});
						if (isSold == 0) {
							item.btSell.addListener(new ClickListener() {
								@Override
								public void clicked(InputEvent event, float x,
										float y) {
									super.clicked(event, x, y);
									DialogCustom dl = new DialogCustom("");
									dl.text("Bạn có chắc chắn muốn đã bán Gift Code này");
									dl.button("Ok", new Runnable() {
										@Override
										public void run() {
											Loading.ins.show(ViewGiftCode.this);
											currentItem = item;
											Request.getInstance()
													.setStateGiftCode(
															AppPreference.instance.name,
															giftCode,
															1,
															new ChangeStateGiftCode());
										}
									});
									dl.button("Hủy");
									dl.show(getStage());
								}
							});
						}

						item.btReturn.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x,
									float y) {
								super.clicked(event, x, y);
								DialogCustom dl = new DialogCustom("");
								dl.text("Bạn có chắc chắn muốn trả lại Gift Code này");
								dl.button("Ok", new Runnable() {

									@Override
									public void run() {
										Loading.ins.show(ViewGiftCode.this);
										Request.getInstance().returnGiftCode(
												AppPreference.instance.name,
												id, new ReturnGiftCode());
									}
								});
								dl.button("Hủy");
								dl.show(getStage());
							}
						});
						page.addData(item);
					}
					page.init();
					for (int i = 0; i < page.getCurrentDataPage().size(); i++) {
						ItemTable item = page.getCurrentDataPage().get(i);
						tableNormal.addItem(item);
					}
				} else {
					Toast.makeText(_stage,
							"Bạn không có gift code nào chưa sử dụng",
							Toast.LENGTH_SHORT);
				}
				configContent(tableNormal);
			} else {
				String mess = responeNormal.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			}
			responeNormal = null;
		}

		if (responeUsed != null) {
			Loading.ins.hide();
			page.removeAllPage();
			tableUsed.removeAll();
			boolean resut = responeUsed.getBoolean(ExtParamsKey.RESULT);
			if (resut) {
				JsonValue list = responeUsed.get(ExtParamsKey.LIST);
				if (list.size > 0) {
					for (int i = 0; i < list.size; i++) {
						JsonValue content = list.get(i);
						final String giftCode = content
								.getString(ExtParamsKey.GIFT_CODE);
						long money = content.getLong(ExtParamsKey.AMOUNT);
						String currency = content
								.getString(ExtParamsKey.CURRENCY);
						long money_in_game = content
								.getLong(ExtParamsKey.MONEY_IN_GAME);
						long timeExpire = content
								.getLong(ExtParamsKey.DATE_EXPIRE);
						long timeUsed = content
								.getLong(ExtParamsKey.DATE_EXPIRE);
						String user = content
								.getString(ExtParamsKey.USER_RECHARGE);
						int isUse = content.getInt(ExtParamsKey.IS_USE);
						String state;
						if (isUse == 0) {
							state = "Hết hạn sử dụng";
						} else if (isUse == 1) {
							state = "Đã được sử dụng";
						} else {
							state = "Trả lại";
						}
						final String id = content.getString(ExtParamsKey.ID);
						ItemGiftCodeUsed item = new ItemGiftCodeUsed(tableUsed,
								new String[] {
										"" + (i + 1),
										id,
										giftCode,
										money + "" + currency,
										"" + money_in_game,
										DateTime.getStringDate(timeExpire,
												DateTime.FORMAT),
										DateTime.getStringDate(timeUsed,
												DateTime.FORMAT), user, state });
						page.addData(item);
					}
					page.init();
					for (int i = 0; i < page.getCurrentDataPage().size(); i++) {
						ItemTable item = page.getCurrentDataPage().get(i);
						tableUsed.addItem(item);
					}
				} else {
					Toast.makeText(_stage,
							"Bạn không có gift code nào đã sử dụng",
							Toast.LENGTH_SHORT);
				}
				configContent(tableUsed);
			} else {
				String mess = responeUsed.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			}
			responeUsed = null;
		}

		if (responeChangeState != null) {
			Loading.ins.hide();
			boolean resut = responeChangeState.getBoolean(ExtParamsKey.RESULT);
			if (resut) {
				currentItem.changeStateSold();
			}
			String mess = responeChangeState.getString(ExtParamsKey.MESSAGE);
			Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			responeChangeState = null;
		}

		if (responeReturn != null) {
			boolean resut = responeReturn.getBoolean(ExtParamsKey.RESULT);
			if (resut) {
				UserInfo.money = responeReturn
						.getLong(ExtParamsKey.UPDATE_MONEY);
				Request.getInstance().getNormalGiftCode(
						AppPreference.instance.name,
						partnerState.getPartnerId(), new GetGiftCodeNormal());
			} else {
				Loading.ins.hide();
			}
			String mess = responeReturn.getString(ExtParamsKey.MESSAGE);
			Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			responeReturn = null;
		}
	}

	@Override
	public void back() {
		super.back();
		getViewController().removeView(getName());
	}

	private void configContent(final AbstractTable table) {
		tbContent.clear();
		if (table.equals(tableNormal)) {
			lbTitle.setText("DANH SÁCH GIFT CODE CHƯA SỬ DỤNG");
		} else {
			lbTitle.setText("DANH SÁCH GIFT CODE ĐÃ SỬ DỤNG");
		}
		lbTitle.setVisible(true);
		page.setListener(new ItemListener() {
			@Override
			public void onItemClick() {
				table.setScrollX(0);
				table.setScrollY(0);
				table.addAction(Actions.sequence(
						Actions.alpha(0, 0.2f, Interpolation.exp5Out),
						Actions.alpha(1, 0.2f, Interpolation.exp5Out)));
				table.removeAll();
				for (int i = 0; i < page.getCurrentDataPage().size(); i++) {
					ItemTable item = page.getCurrentDataPage().get(i);
					table.addItem(item);
				}
			}
		});
		table.setScrollX(0);
		table.setScrollY(0);
		tbContent.add(table).padTop(10).height(580).row();
		tbContent.add(page);
	}

	@Override
	public String getLabel() {
		return "Danh sách Giftcode";
	}

	class GetGiftCodeUsed implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeUsed = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

	class GetGiftCodeNormal implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeNormal = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

	class ReturnGiftCode implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeReturn = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

	class ChangeStateGiftCode implements HttpResponseListener {

		@Override
		public void handleHttpResponse(HttpResponse httpResponse) {
			responeChangeState = (new JsonReader()).parse(httpResponse
					.getResultAsString());
		}

		@Override
		public void failed(Throwable t) {

		}

		@Override
		public void cancelled() {

		}

	}

}
