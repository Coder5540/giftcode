package imp.view;

import utils.factory.StringSystem;

import com.aia.appsreport.component.chart.ColumnChartComponent;
import com.aia.appsreport.component.chart.ColumnChartGroup;
import com.aia.appsreport.component.chart.ColumnComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.ui.ColumnChartView;
import com.coder5560.game.views.View;

public class ViewLogMoneyChart extends View {
	String responese = "";
	boolean isLoad = true;
	ScrollPane scroll;
	Table content;
	ColumnChartView columnchart;

	public ViewLogMoneyChart buildComponent() {
		Image bg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch));
		bg.setSize(getWidth(), getHeight());
		addActor(bg);
		columnchart = new ColumnChartView(3 * getHeight() / 4);
		columnchart.numbertype = 2;
		content = new Table();
		scroll = new ScrollPane(content);
		scroll.setBounds(0, 0, getWidth(), 3 * getHeight() / 4 + 100);
		content.left();
		addActor(scroll);
		scroll.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		return this;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		if (isLoad) {
			int size = 7;
			String titledown[] = new String[size];
			Color[] color = { Color.RED, Color.BLUE };
			String[] dir = { "Chuyển tiền", "Nhận tiền" };
			columnchart.chartback.resetData();
			columnchart.columnChart.resetData();
			content.clear();
			for (int i = 0; i < size; i++) {
				long money_send = MathUtils.random(500, 1500);
				long money_receive = MathUtils.random(500, 1500);
				String date = "12-12-2012";
				ColumnChartGroup colGroup = new ColumnChartGroup();
				ColumnChartComponent colSend = new ColumnChartComponent();

				colSend.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (_viewController
								.isContainView(StringSystem.VIEW_LOG_SEND_MONEY)) {
							((ViewLogTransferMoney) _viewController
									.getView(StringSystem.VIEW_LOG_SEND_MONEY))
									.setDate(1);
						} else {
							ViewLogTransferMoney viewLogTransferMoney = new ViewLogTransferMoney();
							viewLogTransferMoney
									.build(getStage(),
											_viewController,
											StringSystem.VIEW_LOG_SEND_MONEY,
											new Rectangle(
													0,
													0,
													Constants.WIDTH_SCREEN,
													Constants.HEIGHT_SCREEN
															- Constants.HEIGHT_ACTIONBAR));
							viewLogTransferMoney
									.buildComponent(ViewLogTransferMoney.TYPE_SEND);
						}
						((ViewLogTransferMoney) _viewController
								.getView(StringSystem.VIEW_LOG_SEND_MONEY))
								.show(null);
						super.clicked(event, x, y);
					}
				});
				colSend.addColumnComponent(new ColumnComponent(
						(int) money_send, color[0]));
				ColumnChartComponent colReceive = new ColumnChartComponent();
				colReceive.addColumnComponent(new ColumnComponent(
						(int) money_receive, color[1]));
				colReceive.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						System.out.println("click to receive");
						if (_viewController
								.isContainView(StringSystem.VIEW_LOG_RECEIVE_MONEY)) {
							((ViewLogTransferMoney) _viewController
									.getView(StringSystem.VIEW_LOG_RECEIVE_MONEY))
									.setDate(1);
						} else {
							ViewLogTransferMoney viewLogTransferMoney = new ViewLogTransferMoney();
							viewLogTransferMoney
									.build(getStage(),
											_viewController,
											StringSystem.VIEW_LOG_RECEIVE_MONEY,
											new Rectangle(
													0,
													0,
													Constants.WIDTH_SCREEN,
													Constants.HEIGHT_SCREEN
															- Constants.HEIGHT_ACTIONBAR));
							viewLogTransferMoney
									.buildComponent(ViewLogTransferMoney.TYPE_RECEIVE);
						}
						((ViewLogTransferMoney) _viewController
								.getView(StringSystem.VIEW_LOG_RECEIVE_MONEY))
								.show(null);
						super.clicked(event, x, y);
					};
				});
				colGroup.addComponent(colSend);
				colGroup.addComponent(colReceive);
				columnchart.columnChart.addColumnChartComponent(colGroup);
				titledown[i] = date;
			}
			columnchart.columnChart.offsetX = 50;
			columnchart.validateComponent(160, titledown, dir, color);
			content.add(columnchart);
			isLoad = false;
		}
		super.update(delta);
	}

}
