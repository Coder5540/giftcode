package uitls.input;

import uitls.input.TextInputHelper.FilterStyle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.coder5560.game.listener.OnRunListener;

public class MyTextField extends TextField implements TextInputTarget {

	private FilterStyle		filterStyle;
	private TextInputHelper	helper;
	private OnRunListener	onShowListener, onHideListener;

	public MyTextField(String text, TextFieldStyle style,
			TextInputHelper phelper, FilterStyle filterStyle) {
		super(text, style);
		this.helper = phelper;
		this.filterStyle = filterStyle;
		setOnscreenKeyboard(new OnscreenKeyboard() {

			@Override
			public void show(boolean visible) {
				if (visible) {
					onShowKeyBoard();
				} else {
					onHideKeyBoard();
				}
				System.out.println(" Show key board "+ visible);
				showKeyboard(visible);
			}
		});
	}

	@Override
	protected InputListener createInputListener() {
		return new MyTextFieldClickListener();
	}

	@Override
	public String getCurrentText() {
		return getText();
	}

	private void showKeyboard(boolean visible) {
		if (helper != null) {
			if (visible) {
				System.out.println("Show .........");
				helper.requestFocus(filterStyle);
				helper.setTarget(this);
			} else {
				System.out.println("Hide .............");
				helper.setTarget(null);
			}
		} else {
			Gdx.input.setOnscreenKeyboardVisible(visible);
			switch (filterStyle) {
				case ALPHANUMERIC:
					setTextFieldFilter(new TextFieldFilter() {

						@Override
						public boolean acceptChar(TextField textField, char c) {
							if (Character.isLetterOrDigit(c)
									|| Character.isSpaceChar(c))
								return true;
							return false;
						}
					});
					break;
				case NORMAL:
					setTextFieldFilter(null);
					break;
			}
		}
	}

	private class MyTextFieldClickListener extends TextFieldClickListener {

		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			boolean flag = super.touchDown(event, x, y, pointer, button);
			updateCursor();
			return flag;
		}

		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			super.touchDragged(event, x, y, pointer);
			updateCursor();
		}

		@Override
		protected void goHome(boolean jump) {
			super.goHome(jump);
			updateCursor();
		}

		@Override
		protected void goEnd(boolean jump) {
			super.goEnd(jump);
			updateCursor();
		}

	}

	private void updateCursor() {
		if (helper != null) {
			helper.setPointer(cursor);
		}
	}

	public void onHideKeyBoard() {
		if (onHideListener != null) {
			onHideListener.run();
		}
	}

	public void onShowKeyBoard() {
		if (onShowListener != null)
			onShowListener.run();
	}

	public void setOnShowListener(OnRunListener listener) {
		this.onShowListener = listener;
	}

	public void setOnHideListener(OnRunListener listener) {
		this.onHideListener = listener;
	}
}
