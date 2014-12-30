package imp.view;

import java.util.ArrayList;

import utils.elements.PartnerPicker;
import utils.factory.AppPreference;
import utils.factory.DateTime;
import utils.factory.Factory;
import utils.factory.FontFactory.FontType;
import utils.factory.Style;
import utils.networks.ExtParamsKey;
import utils.networks.Request;
import utils.networks.UserInfo;
import utils.screen.Toast;

import com.aia.appsreport.component.list.ItemList;
import com.aia.appsreport.component.list.ListDetail;
import com.aia.appsreport.component.table.ItemGiftCodeNormal;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.ui.ItemListener;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.ui.PageV2;
import com.coder5560.game.views.View;

public class ViewGiftCode extends View {

	private Group groupPartner;
	private PartnerPicker partnerGiftCode;
	private PartnerPicker partnerState;
	private Label lbTitle;

	private ListDetail listDetail;
	private PageV2 page;

	private ItemGiftCodeNormal currentItem;

	private JsonValue responeNormal;
	private JsonValue responeReturn;
	private JsonValue responeChangeState;
	private JsonValue responeUsed;

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
		// groupPartner.addActor(bgTop);
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
				Constants.COLOR_ACTIONBAR));

		listDetail = new ListDetail(new Table(), new Rectangle());
		page = new PageV2(getWidth(), 60);
		page.setListener(new ItemListener() {

			@Override
			public void onItemClick() {
				listDetail.setScrollX(0);
				listDetail.setScrollY(0);
				listDetail.addAction(Actions.sequence(
						Actions.alpha(0, 0.3f, Interpolation.exp10Out),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								listDetail.table.clear();
								loadListDetail();
							}
						}), Actions.alpha(1, 0.3f, Interpolation.exp10Out)));

			}
		});

		this.add(groupPartner).row();
		this.add(lbTitle).padTop(20).row();
		this.add(listDetail).width(getWidth()).height(540).row();
		this.add(page).padTop(5).padBottom(5);

		Loading.ins.show(this);
		Request.getInstance().getNormalGiftCode(AppPreference.instance.name,
				-1, new GetGiftCodeNormal());
	}

	@Override
	public void update(float delta) {
		if (responeNormal != null) {
			Loading.ins.hide();
			page.removeAllPage();
			listDetail.table.clear();
			boolean resut = responeNormal.getBoolean(ExtParamsKey.RESULT);
			if (resut) {
				JsonValue list = responeNormal.get(ExtParamsKey.LIST);
				if (list.size > 0) {
					for (int i = 0; i < list.size; i++) {
						JsonValue content = list.get(i);
						String id = content.getString(ExtParamsKey.ID);
						String giftCode = content
								.getString(ExtParamsKey.GIFT_CODE);
						long money = content.getLong(ExtParamsKey.AMOUNT);
						String currency = content
								.getString(ExtParamsKey.CURRENCY);
						long money_in_game = content
								.getLong(ExtParamsKey.MONEY_IN_GAME);
						long timeExpire = content
								.getLong(ExtParamsKey.DATE_EXPIRE);
						long timeGen = content.getLong(ExtParamsKey.GEN_DATE);
						int isSold = content.getInt(ExtParamsKey.IS_SOLD);
						// final ItemGiftCodeNormal item = new
						// ItemGiftCodeNormal(
						// tableNormal, isSold, new String[] {
						// "" + (i + 1),
						// id,
						// giftCode,
						// money + " " + currency,
						// "" + money_in_game,
						// DateTime.getStringDate(time,
						// DateTime.FORMAT) });
						// item.btCopy.addListener(new ClickListener() {
						// @Override
						// public void clicked(InputEvent event, float x,
						// float y) {
						// super.clicked(event, x, y);
						// Gdx.app.getClipboard().setContents(
						// "CODE : " + giftCode + " ID : " + id);
						// Toast.makeText(getStage(),
						// "Đã copy vào bộ nhớ đệm",
						// Toast.LENGTH_SHORT);
						// }
						// });
						// if (isSold == 0) {
						// item.btSell.addListener(new ClickListener() {
						// @Override
						// public void clicked(InputEvent event, float x,
						// float y) {
						// super.clicked(event, x, y);
						// DialogCustom dl = new DialogCustom("");
						// dl.text("Bạn có chắc chắn muốn đã bán Gift Code này");
						// dl.button("Ok", new Runnable() {
						// @Override
						// public void run() {
						// Loading.ins.show(ViewGiftCode.this);
						// currentItem = item;
						// Request.getInstance()
						// .setStateGiftCode(
						// AppPreference.instance.name,
						// giftCode,
						// 1,
						// new ChangeStateGiftCode());
						// }
						// });
						// dl.button("Hủy");
						// dl.show(getStage());
						// }
						// });
						// }
						//
						// item.btReturn.addListener(new ClickListener() {
						// @Override
						// public void clicked(InputEvent event, float x,
						// float y) {
						// super.clicked(event, x, y);
						// DialogCustom dl = new DialogCustom("");
						// dl.text("Bạn có chắc chắn muốn trả lại Gift Code này");
						// dl.button("Ok", new Runnable() {
						//
						// @Override
						// public void run() {
						// Loading.ins.show(ViewGiftCode.this);
						// Request.getInstance().returnGiftCode(
						// AppPreference.instance.name,
						// id, new ReturnGiftCode());
						// }
						// });
						// dl.button("Hủy");
						// dl.show(getStage());
						// }
						// });
						ArrayList<String> data = new ArrayList<String>();
						data.add(id);
						data.add(giftCode);
						data.add(money + " " + currency);
						data.add("" + money_in_game);
						data.add(DateTime.getStringDate(timeGen,
								DateTime.FORMAT));
						data.add(DateTime.getStringDate(timeExpire,
								DateTime.FORMAT));
						data.add("" + isSold);
						page.addData(data);
					}
					page.init();
					loadListDetail();
				} else {
					Toast.makeText(_stage,
							"Bạn không có gift code nào chưa sử dụng",
							Toast.LENGTH_SHORT);
				}
			} else {
				String mess = responeNormal.getString(ExtParamsKey.MESSAGE);
				Toast.makeText(_stage, mess, Toast.LENGTH_SHORT);
			}
			responeNormal = null;
		}

		if (responeUsed != null) {
			Loading.ins.hide();
			page.removeAllPage();
			listDetail.table.clear();
			boolean resut = responeUsed.getBoolean(ExtParamsKey.RESULT);
			if (resut) {
				JsonValue list = responeUsed.get(ExtParamsKey.LIST);
				if (list.size > 0) {
					for (int i = 0; i < list.size; i++) {
						JsonValue content = list.get(i);
						String id = content.getString(ExtParamsKey.ID);
						String giftCode = content
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
						// ItemGiftCodeUsed item = new
						// ItemGiftCodeUsed(tableUsed,
						// new String[] {
						// "" + (i + 1),
						// id,
						// giftCode,
						// money + "" + currency,
						// "" + money_in_game,
						// DateTime.getStringDate(timeExpire,
						// DateTime.FORMAT),
						// DateTime.getStringDate(timeUsed,
						// DateTime.FORMAT), user, state });
						ArrayList<String> data = new ArrayList<String>();
						data.add(id);
						data.add(giftCode);
						data.add(Factory.getDotMoney(money) + " " + currency);
						data.add("" + money_in_game);
						data.add(DateTime.getStringDate(timeExpire,
								DateTime.FORMAT));
						data.add(DateTime.getStringDate(timeUsed,
								DateTime.FORMAT));
						data.add(user);
						data.add("" + isUse);
						page.addData(data);
					}
					page.init();
					loadListDetail();
				} else {
					Toast.makeText(_stage,
							"Bạn không có gift code nào đã sử dụng",
							Toast.LENGTH_SHORT);
				}
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

	private void loadListDetail() {
		for (int i = 0; i < page.getCurrentDataPage().size(); i++) {
			final ArrayList<String> data = page.getCurrentDataPage().get(i);
			ItemList itemList = new ItemList(listDetail, listDetail.getWidth(),
					100);
			if (partnerGiftCode.getSelectedIndex() == 0) {
				lbTitle.setText("DANH SÁCH GIFTCODE CHƯA SỬ DỤNG");
				itemList.addComponent(
						new Label("ID " + ": " + data.get(0), Style.ins
								.getLabelStyle(20, FontType.Regular,
										Color.WHITE)), 20, 60);
				itemList.addComponent(
						new Label("GIFTCODE " + ": " + data.get(1), Style.ins
								.getLabelStyle(20, FontType.Regular,
										Color.WHITE)), 20, 35);
				itemList.addComponent(
						new Label("MONEY " + ": " + data.get(2), Style.ins
								.getLabelStyle(20, FontType.Regular,
										Color.WHITE)), 20, 10);
				itemList.addSubItem(getRow("ID", data.get(0)));
				itemList.addSubItem(getRow("GIFT CODE", data.get(1)));
				itemList.addSubItem(getRow("TIỀN", data.get(2)));
				itemList.addSubItem(getRow("TIỀN TRONG GAME", data.get(3)));
				itemList.addSubItem(getRow("NGÀY TẠO", data.get(4)));
				itemList.addSubItem(getRow("NGÀY HẾT HẠN", data.get(5)));
				int isSold = Integer.valueOf(data.get(6));
				String state;
				if (isSold == 0) {
					state = "Chưa bán";
				} else {
					state = "Đã bán";
				}
				itemList.addSubItem(getRow("TRẠNG THÁI", state));
			} else {
				lbTitle.setText("DANH SÁCH GIFTCODE ĐÃ SỬ DỤNG");
				itemList.addComponent(
						new Label("ID " + ": " + data.get(0), Style.ins
								.getLabelStyle(20, FontType.Regular,
										Color.WHITE)), 20, 60);
				itemList.addComponent(
						new Label("GIFTCODE " + ": " + data.get(1), Style.ins
								.getLabelStyle(20, FontType.Regular,
										Color.WHITE)), 20, 35);
				itemList.addComponent(
						new Label("MONEY " + ": " + data.get(2), Style.ins
								.getLabelStyle(20, FontType.Regular,
										Color.WHITE)), 20, 10);
				itemList.addSubItem(getRow("ID", data.get(0)));
				itemList.addSubItem(getRow("GIFT CODE", data.get(1)));
				itemList.addSubItem(getRow("TIỀN", data.get(2)));
				itemList.addSubItem(getRow("TIỀN TRONG GAME", data.get(3)));
				itemList.addSubItem(getRow("NGÀY HẾT HẠN", data.get(4)));
				itemList.addSubItem(getRow("NGÀY SỬ DỤNG", data.get(5)));
				itemList.addSubItem(getRow("NGƯỜI SỬ DỤNG", data.get(6)));
				int isUse = Integer.valueOf(data.get(7));
				String state;
				if (isUse == 0) {
					state = "Hết hạn sử dụng";
				} else if (isUse == 1) {
					state = "Đã được sử dụng";
				} else {
					state = "Trả lời";
				}
				itemList.addSubItem(getRow("TRẠNG THÁI", state));
			}
			listDetail.addItemMenu(itemList);
		}
	}

	Table getRow(String title, String info) {
		Table table = new Table();
		table.setHeight(70);
		table.left();
		Label lbTitle = new Label(title, new LabelStyle(
				Assets.instance.fontFactory.getFont(17, FontType.Regular),
				new Color(207 / 255f, 207 / 255f, 207 / 255f, 1)));
		Label lbInfo = new Label(info, new LabelStyle(
				Assets.instance.fontFactory.getFont(25, FontType.Regular),
				Constants.COLOR_ACTIONBAR));
		table.add(lbTitle).left().padLeft(20).row();
		table.add(lbInfo).left().padLeft(20);
		return table;
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
