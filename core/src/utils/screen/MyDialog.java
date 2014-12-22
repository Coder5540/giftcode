package utils.screen;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import utils.factory.Style;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.coder5560.game.assets.Assets;
import com.coder5560.game.enums.Constants;

/**
 * Displays a dialog, which is a modal window containing a content table with a
 * button table underneath it. Methods are provided to add a label to the
 * content table and buttons to the button table, but any widgets can be added.
 * When a button is clicked, {@link #result(Object)} is called and the dialog is
 * removed from the stage.
 * 
 * @author Nathan Sweet
 */
public class MyDialog extends Window {
	/**
	 * The time in seconds that dialogs will fade in and out. Set to zero to
	 * disable fading.
	 */
	static public float fadeDuration = 0.4f;

	Table contentTable, buttonTable;
	private Skin skin;
	ObjectMap<Actor, Object> values = new ObjectMap<Actor, Object>();
	boolean cancelHide;
	Actor previousKeyboardFocus, previousScrollFocus;

	public Image back2;
	InputListener ignoreTouchDown = new InputListener() {
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			event.cancel();
			return false;
		}
	};

	public MyDialog(String title, Skin skin) {
		super(title, skin.get(WindowStyle.class));
		this.skin = skin;
		WindowStyle style = skin.get(WindowStyle.class);
		style.titleFont = Assets.instance.fontFactory.getFont(20);
		setStyle(skin.get(WindowStyle.class));
		initialize();
	}

	public MyDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin.get(windowStyleName, WindowStyle.class));
		setSkin(skin);
		this.skin = skin;
		initialize();
	}

	public MyDialog(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
		initialize();
	}

	@Override
	public void setSkin(Skin skin) {
		super.setSkin(skin);
		this.skin = skin;
	}

	private void initialize() {
		setModal(true);
		back2 = new Image(new NinePatchDrawable(new NinePatch(new NinePatch(
				Assets.instance.getRegion("ninepatch2"), 4, 4, 4, 4),
				new Color(240 / 255f, 240 / 255f, 240 / 255f, 1))));
		addActor(back2);

		defaults().space(6);
		add(contentTable = new Table(skin)).expand().fill();
		row();
		add(buttonTable = new Table(skin));

		contentTable.defaults().space(6);
		buttonTable.defaults().space(6);

		buttonTable.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if (!values.containsKey(actor))
					return;
				while (actor.getParent() != buttonTable)
					actor = actor.getParent();
				result(values.get(actor));
				if (!cancelHide)
					hide();
				cancelHide = false;
			}
		});

		addListener(new FocusListener() {
			public void keyboardFocusChanged(FocusEvent event, Actor actor,
					boolean focused) {
				if (!focused)
					focusChanged(event);
			}

			public void scrollFocusChanged(FocusEvent event, Actor actor,
					boolean focused) {
				if (!focused)
					focusChanged(event);
			}

			private void focusChanged(FocusEvent event) {
				Stage stage = getStage();
				if (isModal()
						&& stage != null
						&& stage.getRoot().getChildren().size > 0
						&& stage.getRoot().getChildren().peek() == MyDialog.this) { // Dialog
																					// is
																					// top
																					// most
																					// actor.
					Actor newFocusedActor = event.getRelatedActor();
					if (newFocusedActor != null
							&& !newFocusedActor.isDescendantOf(MyDialog.this))
						event.cancel();
				}
			}
		});
	}

	public Table getContentTable() {
		return contentTable;
	}

	public Table getButtonTable() {
		return buttonTable;
	}

	/**
	 * Adds a label to the content table. The dialog must have been constructed
	 * with a skin to use this method.
	 */
	public MyDialog text(String text) {
		return text(text,
				new LabelStyle(Assets.instance.fontFactory.getFont(20),
						Color.BLACK));
	}

	/** Adds a label to the content table. */
	public MyDialog text(String text, LabelStyle labelStyle) {
		Label la = new Label(text, labelStyle);
		la.setWrap(true);
		la.setWidth(Constants.WIDTH_SCREEN - 100);
		return text(la);
	}

	/** Adds the given Label to the content table */
	public MyDialog text(Label label) {
		contentTable.add(label).width(label.getTextBounds().width).pad(20);
		return this;
	}

	@Override
	public void validate() {
		super.validate();
		back2.setSize(getWidth() - 10, getHeight() - buttonTable.getHeight()
				/ 2);
		back2.setPosition(getWidth() / 2 - back2.getWidth() / 2, getHeight()
				- back2.getHeight() - 5);
	}

	/**
	 * Adds a text button to the button table. Null will be passed to
	 * {@link #result(Object)} if this button is clicked. The dialog must have
	 * been constructed with a skin to use this method.
	 */
	public MyDialog button(String text) {
		return button(text, Style.ins.textButtonStyle);
	}

	/**
	 * Adds a text button to the button table. The dialog must have been
	 * constructed with a skin to use this method.
	 * 
	 * @param object
	 *            The object that will be passed to {@link #result(Object)} if
	 *            this button is clicked. May be null.
	 */
	public MyDialog button(String text, Object object) {
		if (skin == null)
			throw new IllegalStateException(
					"This method may only be used if the dialog was constructed with a Skin.");
		TextButtonStyle style = skin.get(TextButtonStyle.class);
		style.font = Assets.instance.fontFactory.getFont(20);
		style.fontColor = Color.WHITE;
		return button(text, object, style);
	}

	/**
	 * Adds a text button to the button table.
	 * 
	 * @param object
	 *            The object that will be passed to {@link #result(Object)} if
	 *            this button is clicked. May be null.
	 */
	public MyDialog button(String text, Object object,
			TextButtonStyle buttonStyle) {

		return button(new TextButton(text, buttonStyle), object);
	}

	public MyDialog button(String text, TextButtonStyle buttonStyle) {
		return button(new TextButton(text, buttonStyle));
	}

	/** Adds the given button to the button table. */
	public MyDialog button(Button button) {
		return button(button, null);
	}

	/**
	 * Adds the given button to the button table.
	 * 
	 * @param object
	 *            The object that will be passed to {@link #result(Object)} if
	 *            this button is clicked. May be null.
	 */
	public MyDialog button(Button button, Object object) {
		// if (button.getWidth() < 100)
		buttonTable.add(button).width(100).height(60);
		// else
		// buttonTable.add(button);
		setObject(button, object);
		// debug();
		return this;
	}

	/** {@link #pack() Packs} the dialog and adds it to the stage, centered. */
	public MyDialog show(Stage stage) {
		clearActions();
		removeCaptureListener(ignoreTouchDown);

		previousKeyboardFocus = null;
		Actor actor = stage.getKeyboardFocus();
		if (actor != null && !actor.isDescendantOf(this))
			previousKeyboardFocus = actor;

		previousScrollFocus = null;
		actor = stage.getScrollFocus();
		if (actor != null && !actor.isDescendantOf(this))
			previousScrollFocus = actor;

		pack();
		setPosition(Math.round((stage.getWidth() - getWidth()) / 2),
				Math.round((stage.getHeight() - getHeight()) / 2));
		stage.addActor(this);
		stage.setKeyboardFocus(this);
		stage.setScrollFocus(this);
		if (fadeDuration > 0) {
			getColor().a = 0;
			addAction(Actions.fadeIn(fadeDuration, Interpolation.fade));
		}
		return this;
	}

	/**
	 * Hides the dialog. Called automatically when a button is clicked. The
	 * default implementation fades out the dialog over {@link #fadeDuration}
	 * seconds and then removes it from the stage.
	 */
	public void hide() {
		Stage stage = getStage();
		if (stage != null) {
			if (previousKeyboardFocus != null
					&& previousKeyboardFocus.getStage() == null)
				previousKeyboardFocus = null;
			Actor actor = stage.getKeyboardFocus();
			if (actor == null || actor.isDescendantOf(this))
				stage.setKeyboardFocus(previousKeyboardFocus);

			if (previousScrollFocus != null
					&& previousScrollFocus.getStage() == null)
				previousScrollFocus = null;
			actor = stage.getScrollFocus();
			if (actor == null || actor.isDescendantOf(this))
				stage.setScrollFocus(previousScrollFocus);
		}
		if (fadeDuration > 0) {
			addCaptureListener(ignoreTouchDown);
			addAction(sequence(fadeOut(fadeDuration, Interpolation.fade),
					Actions.removeListener(ignoreTouchDown, true),
					Actions.removeActor()));
		} else
			remove();
	}

	public void setObject(Actor actor, Object object) {
		values.put(actor, object);
	}

	/**
	 * If this key is pressed, {@link #result(Object)} is called with the
	 * specified object.
	 * 
	 * @see Keys
	 */
	public MyDialog key(final int keycode, final Object object) {
		addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode2) {
				if (keycode == keycode2) {
					result(object);
					if (!cancelHide)
						hide();
					cancelHide = false;
				}
				return false;
			}
		});
		return this;
	}

	/**
	 * Called when a button is clicked. The dialog will be hidden after this
	 * method returns unless {@link #cancel()} is called.
	 * 
	 * @param object
	 *            The object specified when the button was added.
	 */
	protected void result(Object object) {
	}

	public void cancel() {
		cancelHide = true;
	}
}
