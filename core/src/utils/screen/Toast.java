package utils.screen;

import static com.badlogic.gdx.math.Interpolation.fade;
import static com.badlogic.gdx.math.Interpolation.swingOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;

import utils.factory.FontFactory.fontType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;

public class Toast {
	public enum ALIGN {
		BOTTOM, MID, TOP
	}

	private ALIGN align = ALIGN.BOTTOM;
	public static int LENGTH_LONG = 4;
	public static int LENGTH_SHORT = 2;
	public static Toast instance = null;
	private Label toastLabel = null;
	public Table toast = null;
	private Stage stage;
	private Image back;

	public Toast(Stage stage) {
		super();
		this.stage = stage;
		// init();

	}

	public void render(float delta) {
		if (stage != null) {
			toast.act(delta);
			stage.getBatch().begin();
			toast.draw(stage.getBatch(), 1f);
			stage.getBatch().end();
		}
	}

	private void init() {
		if (toast == null) {
			LabelStyle labelStyle = new LabelStyle();
			NinePatch patch = new NinePatch(Assets.instance.ui.reg_ninepatch,
					4, 4, 4, 4);
			patch.setColor(Color.BLACK);
			back = new Image(patch);
			back.getColor().a = 0.9f;
			labelStyle.font = Assets.instance.fontFactory.getFont(20,
					fontType.Light);

			labelStyle.fontColor = Color.WHITE;
			toastLabel = new Label("", labelStyle);
			toastLabel.setAlignment(0);
			toastLabel.setPosition(5, 5);
			toast = new Table();
			toast.setTransform(true);
			toast.addActor(back);
			toast.addActor(toastLabel);
		}
		toast.clearActions();
		toast.setScale(0);
	}

	public void builToast() {
		init();
	}

	public void setAlign(ALIGN align) {
		this.align = align;
	}

	public void toast(String message) {
		toast.toFront();
		toastLabel.setText(message);
		toastLabel.pack();
		toastLabel.setWrap(true);

		back.setSize(toastLabel.getWidth() + 10, toastLabel.getHeight() + 10);
		positingForActor(
				new float[][] {
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								110 },
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								140 },
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								170 },
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								200 },
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								200 } }, toast);
		toast.setOrigin(toastLabel.getWidth() / 2, toastLabel.getHeight() / 2);
		toast.clearActions();
		toast.addAction(sequence(alpha(0), alpha(1, 0.2f, fade),
				scaleTo(1, 1, 0.2f, swingOut), delay(3), alpha(1),
				alpha(0, 0.3f, fade), scaleBy(-1, -1)));

	}

	boolean isEnd = true;

	public void toast(String message, float duration) {
		if (message == "" || message.equals(toastLabel.getText()))
			return;
		for (ToastInfo info : listToast) {
			if (info.text.equals(message))
				return;
		}
		if (toast.getActions().size != 0 && !isEnd) {
			listToast.add(new ToastInfo(stage, message, 1, align));
			return;
		}
		isEnd = false;
		toast.toFront();
		toastLabel.setText(message);
		toastLabel.setWrap(true);
		toastLabel.setWidth(Constants.WIDTH_SCREEN - 50);
		toastLabel.setHeight(toastLabel.getHeight());
		// toastLabel.pack();
		back.setY(-toastLabel.getTextBounds().height / 2 - 5);
		back.setX(toastLabel.getWidth() / 2 - toastLabel.getTextBounds().width
				/ 2 - 5);
		back.setSize(toastLabel.getTextBounds().width + 20,
				toastLabel.getTextBounds().height + 25);
		positingForActor(
				new float[][] {
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								110 },
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								140 },
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								170 },
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								200 },
						{ (Constants.WIDTH_SCREEN - toastLabel.getWidth()) / 2,
								200 } }, toast);
		toast.setOrigin(toastLabel.getWidth() / 2, toastLabel.getHeight() / 2);
		toast.clearActions();
		toast.addAction(sequence(alpha(0), alpha(1, 0.2f, fade),
				scaleTo(1, 1, 0.2f, swingOut), delay(duration), alpha(1),
				alpha(0, 0.3f, fade), scaleBy(-1, -1),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						isEnd = true;
						if (listToast.size() > 0) {
							makeText(listToast.get(0).stage,
									listToast.get(0).text,
									listToast.get(0).duration,
									listToast.get(0).align);
							listToast.remove(0);
						}
					}
				})));
	}

	public void positingForActor(float[][] position, Actor actor) {
		float y = 110;
		switch (align) {
		case MID:
			y = Constants.HEIGHT_SCREEN / 2;
			break;
		case BOTTOM:
			y = 110;
			break;
		case TOP:
			y = Constants.HEIGHT_SCREEN - 100;
			break;
		default:
			break;
		}

		actor.setPosition(position[0][0], y);
	}

	public static void makeText(Stage context, String text, float duration) {
		if (instance == null) {
			instance = new Toast(context);
			instance.builToast();
		}
		instance.stage = context;
		instance.toast(text, duration);
	}

	public static void makeText(Stage context, String text, float duration,
			ALIGN align) {
		if (instance == null) {
			instance = new Toast(context);
			instance.builToast();
		}
		instance.align = align;
		instance.stage = context;
		instance.toast(text, duration);
	}

	ArrayList<ToastInfo> listToast = new ArrayList<Toast.ToastInfo>();

	public class ToastInfo {
		Stage stage;
		String text;
		float duration;
		ALIGN align;

		public ToastInfo(Stage context, String text, float duration, ALIGN align) {
			this.stage = context;
			this.text = text;
			this.duration = duration;
			this.align = align;
		}
	}
}
