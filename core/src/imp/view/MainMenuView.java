package imp.view;

import utils.elements.Img;
import utils.factory.FontFactory.fontType;
import utils.factory.Log;
import utils.factory.StringSystem;
import utils.listener.CustomListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;
import com.coder5560.game.enums.ViewState;
import com.coder5560.game.listener.OnClickListener;
import com.coder5560.game.listener.OnCompleteListener;
import com.coder5560.game.ui.ListMenu;
import com.coder5560.game.ui.Loading;
import com.coder5560.game.views.TraceView;
import com.coder5560.game.views.View;

public class MainMenuView extends View {
	private Image	tranBg;
	Table			content;
	public int		lastSelect			= 0;
	public ListMenu	menu;
	Group			groupLogout;
	private boolean	ignoreUpdateMove	= true;

	public MainMenuView buildComponent() {
		Color colorBg = new Color(100 / 255f, 100 / 255f, 100 / 255f, 1f);

		getViewController().getGameScreen().setGestureDetector(
				new GestureDetector(customListener));
		tranBg = new Image(new NinePatch(Assets.instance.ui.reg_ninepatch,
				new Color(00, 00, 00, .4f)));
		tranBg.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN
				- Constants.HEIGHT_ACTIONBAR);
		tranBg.setVisible(true);
		tranBg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide(null);
			}
		});

		content = new Table();
		content.setSize(Constants.WIDTH_MAINMENU, Constants.HEIGHT_SCREEN);

		content.setPosition(-content.getWidth(), 0);
		content.setBackground(new NinePatchDrawable(new NinePatch(
				Assets.instance.ui.reg_ninepatch, colorBg)));
		addActor(tranBg);
		addActor(content);
		buildContent(content);
		return this;

	}

	void buildContent(Table content) {
		float heightLogout = 60;

		menu = new ListMenu(getViewController(), new Table(), new Rectangle(0,
				heightLogout, content.getWidth(), content.getHeight()
						- heightLogout));

		menu.setOnActiveUserClicked(onActiveUserClicked);
		menu.setOnBlockUserClicked(onBlockUserClicked);
		menu.setOnUnActiveUserClicked(onUnActiveUserClicked);
		menu.setOnAvatarClicked(onAvatarClicked);
		menu.setOnAllMailClicked(onAllMailClicked);
		menu.setOnHistoryTransitionClicked(onHistory);
		menu.setOnAddMoneyClicked(onAddMoneyClicked);

		groupLogout = new Group();
		groupLogout.setOrigin(Align.center);
		groupLogout.setSize(content.getWidth(), heightLogout);
		Img imgLogout = new Img(Assets.instance.ui.getRegionLogout());
		imgLogout.setSize(50, 50);
		imgLogout.setPosition(100, groupLogout.getHeight() / 2, Align.center);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = Assets.instance.fontFactory.getFont(26,
				fontType.Medium);
		labelStyle.fontColor = Color.WHITE;

		Label label = new Label("Log out", labelStyle);
		label.setPosition(
				imgLogout.getX() + imgLogout.getWidth() / 2 + label.getWidth()
						- 20, imgLogout.getY() + imgLogout.getHeight() / 2,
				Align.center);

		groupLogout.addActor(imgLogout);
		groupLogout.addActor(label);

		label.setTouchable(Touchable.disabled);
		imgLogout.setTouchable(Touchable.disabled);
		groupLogout.setOrigin(Align.center);
		groupLogout.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				groupLogout.clearActions();
				groupLogout.addAction(Actions.sequence(
						Actions.scaleTo(1.2f, 1.2f, .1f),
						Actions.scaleTo(1f, 1f, .1f, Interpolation.swingOut),
						Actions.run(new Runnable() {
							@Override
							public void run() {
								if (onLogoutListener != null) {
									onLogoutListener.onClick(0, 0);
									groupLogout.addAction(Actions.sequence(
											Actions.touchable(Touchable.disabled),
											Actions.delay(
													.1f,
													Actions.touchable(Touchable.enabled))));
								}
							}
						})));
			}
		});
		content.addActor(menu);
		content.addActor(groupLogout);
	}

	Img getLine(Color color, float height) {
		Img img = new Img(Assets.instance.ui.reg_ninepatch);
		img.setColor(color);
		return img;
	}

	@Override
	public void show(final OnCompleteListener listener) {
		super.show(listener);
		setIgnoreUpdateMove(false);
		tranBg.setVisible(true);
		tranBg.setTouchable(Touchable.disabled);
		menu.updateMail();
		content.addAction(Actions.sequence(
				Actions.moveTo(0, 0, 0.5f, Interpolation.pow5Out),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						if (listener != null)
							listener.done();
						setIgnoreUpdateMove(true);
						tranBg.setTouchable(Touchable.enabled);
						setViewState(ViewState.SHOW);
					}
				})));
	}

	@Override
	public void hide(final OnCompleteListener listener) {
		super.hide(listener);
		tranBg.setVisible(false);
		setIgnoreUpdateMove(false);
		content.addAction(Actions.sequence(Actions.moveTo(-content.getWidth(),
				0, 0.5f, Interpolation.pow5Out), Actions.run(new Runnable() {
			@Override
			public void run() {
				if (listener != null)
					listener.done();
				setIgnoreUpdateMove(true);
				setViewState(ViewState.HIDE);

			}
		})));
	}

	Actor	bar, currentView;

	@Override
	public void act(float delta) {
		super.act(delta);
		if (ignoreUpdateMove)
			return;
		if (tranBg.isVisible()) {
			float alpha = (content.getWidth() + content.getX())
					/ content.getWidth();
			if (content.getX() == 0)
				alpha = 1;
			tranBg.setColor(tranBg.getColor().r, tranBg.getColor().g,
					tranBg.getColor().b, alpha);
		}
		if (getViewController() != null) {
			if (bar == null)
				bar = (Actor) (getViewController()
						.getView(StringSystem.VIEW_ACTION_BAR));
			if (currentView == null)
				currentView = (Actor) (getViewController()
						.getView(TraceView.instance.getLastView()));

			if (bar != null)
				bar.setPosition(content.getX() + content.getWidth(), bar.getY());
			if (currentView != null)
				currentView.setPosition(content.getX() + content.getWidth(),
						currentView.getY());
		}
	}

	@Override
	public void destroyComponent() {
	}

	@Override
	public void back() {
		hide(null);
	}

	boolean			canPan					= false;
	CustomListener	customListener			= new CustomListener() {
												public boolean touchDown(
														float x, float y,
														int pointer, int button) {
													if (content.getX() == -content
															.getWidth()
															&& x < 10
															&& !canPan) {
														canPan = true;
														tranBg.setVisible(true);
													}
													if (content.getX() == 0) {
														canPan = true;
													}
													return false;
												};

												public boolean pan(float x,
														float y, float deltaX,
														float deltaY) {
													if (canPan) {
														setIgnoreUpdateMove(false);
														tranBg.setVisible(true);
														content.setPosition(
																MathUtils
																		.clamp(content
																				.getX()
																				+ deltaX,
																				-content.getWidth(),
																				0),
																content.getY());
														float alpha = (content
																.getWidth() - content
																.getX())
																/ content
																		.getWidth();
														if (content.getX() == 0)
															alpha = 1;
														tranBg.setColor(
																tranBg.getColor().r,
																tranBg.getColor().g,
																tranBg.getColor().b,
																alpha);
														return true;
													}
													return false;
												};

												public boolean panStop(float x,
														float y, int pointer,
														int button) {
													if (canPan) {
														float position = content
																.getX()
																+ content
																		.getWidth();
														if (position <= content
																.getWidth() / 2)
															hide(null);
														if (position > content
																.getWidth() / 2)
															show(null);
														canPan = false;
														return true;
													}
													return false;
												};

											};

	OnClickListener	onActiveUserClicked		= new OnClickListener() {

												@Override
												public void onClick(float x,
														float y) {
													Loading.ins
															.show((Group) getViewController()
																	.getCurrentView());
													getViewController()
															.getView(
																	StringSystem.VIEW_MAIN_MENU)
															.hide(new OnCompleteListener() {

																@Override
																public void onError() {

																}

																@Override
																public void done() {
																	Loading.ins
																			.hide();
																	getViewController()
																			.getView(
																					"view_admin_acive")
																			.show(null);
																}
															});
												}
											};

	OnClickListener	onUnActiveUserClicked	= new OnClickListener() {

												@Override
												public void onClick(float x,
														float y) {
													Loading.ins
															.show((Group) getViewController()
																	.getCurrentView());
													getViewController()
															.getView(
																	StringSystem.VIEW_MAIN_MENU)
															.hide(new OnCompleteListener() {

																@Override
																public void onError() {

																}

																@Override
																public void done() {
																	getViewController()
																			.getView(
																					"view_admin_request")
																			.show(null);
																}
															});

												}
											};

	OnClickListener	onBlockUserClicked		= new OnClickListener() {

												@Override
												public void onClick(float x,
														float y) {
													Loading.ins
															.show((Group) getViewController()
																	.getCurrentView());
													getViewController()
															.getView(
																	StringSystem.VIEW_MAIN_MENU)
															.hide(new OnCompleteListener() {

																@Override
																public void onError() {

																}

																@Override
																public void done() {
																	Loading.ins
																			.hide();
																	getViewController()
																			.getView(
																					"view_admin_lock")
																			.show(null);
																}
															});
												}
											};

	OnClickListener	onAvatarClicked			= new OnClickListener() {

												@Override
												public void onClick(float x,
														float y) {
													Loading.ins
															.show((Group) getViewController()
																	.getCurrentView());
													getViewController()
															.getView(
																	StringSystem.VIEW_MAIN_MENU)
															.hide(new OnCompleteListener() {

																@Override
																public void onError() {

																}

																@Override
																public void done() {
																	ViewInfoDaiLy viewInfoDaiLy = new ViewInfoDaiLy();
																	viewInfoDaiLy
																			.build(getStage(),
																					getViewController(),
																					"info_daily",
																					new Rectangle(
																							0,
																							0,
																							Constants.WIDTH_SCREEN,
																							Constants.HEIGHT_SCREEN
																									- Constants.HEIGHT_ACTIONBAR));

																	viewInfoDaiLy
																			.show(null);
																}
															});
												}
											};

	OnClickListener	onAllMailClicked		= new OnClickListener() {

												@Override
												public void onClick(float x,
														float y) {
													Loading.ins
															.show((Group) getViewController()
																	.getCurrentView());
													getViewController()
															.getView(
																	StringSystem.VIEW_MAIN_MENU)
															.hide(new OnCompleteListener() {

																@Override
																public void onError() {

																}

																@Override
																public void done() {
																	getViewController()
																			.getView(
																					StringSystem.VIEW_MAIL)
																			.show(null);
																}
															});
												}
											};

	OnClickListener	onHistory				= new OnClickListener() {

												@Override
												public void onClick(float x,
														float y) {
													Log.d("OnHistory");
													Loading.ins
															.show((Group) getViewController()
																	.getCurrentView());
													final ViewLog viewLog = new ViewLog();
													viewLog.build(
															getStage(),
															getViewController(),
															StringSystem.VIEW_LOG,
															new Rectangle(
																	0,
																	0,
																	Constants.WIDTH_SCREEN,
																	Constants.HEIGHT_SCREEN
																			- Constants.HEIGHT_ACTIONBAR));
													viewLog.buildComponent();
													viewLog.show(null);
													getViewController()
															.getView(
																	StringSystem.VIEW_MAIN_MENU)
															.hide(new OnCompleteListener() {

																@Override
																public void onError() {

																}

																@Override
																public void done() {
																	viewLog.show(null);
																}
															});
												}
											};

	OnClickListener	onAddMoneyClicked		= new OnClickListener() {

												@Override
												public void onClick(float x,
														float y) {
													final ViewCapTienDaiLy viewAddMoney = new ViewCapTienDaiLy();
													viewAddMoney
															.build(getStage(),
																	getViewController(),
																	StringSystem.VIEW_ADD_MONEY,
																	new Rectangle(
																			0,
																			0,
																			Constants.WIDTH_SCREEN,
																			Constants.HEIGHT_SCREEN
																					- Constants.HEIGHT_ACTIONBAR));
													viewAddMoney
															.buildComponent();
													getViewController()
															.getView(
																	StringSystem.VIEW_MAIN_MENU)
															.hide(new OnCompleteListener() {

																@Override
																public void onError() {

																}

																@Override
																public void done() {
																	viewAddMoney
																			.show(null);
																}
															});

												}
											};

	OnClickListener	onLogoutListener		= new OnClickListener() {

												@Override
												public void onClick(float x,
														float y) {
												}
											};

	public boolean isIgnoreUpdateMove() {
		return ignoreUpdateMove;
	}

	public void setIgnoreUpdateMove(boolean ignoreUpdateMove) {
		this.ignoreUpdateMove = ignoreUpdateMove;
	}

	public void setNotify(int number_unseen) {
		menu.setNotify(number_unseen);
	}

}
