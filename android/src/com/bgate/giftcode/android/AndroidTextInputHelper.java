package com.bgate.giftcode.android;

import uitls.input.TextInputHelper;
import uitls.input.TextInputTarget;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.backends.android.AndroidInput;

public class AndroidTextInputHelper extends TextInputHelper {

	private AndroidApplication	activity;
	private EditText			textView;

	public AndroidTextInputHelper(AndroidApplication context) {
		super();
		activity = context;
		textView = new EditText(context);
		textView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(final Editable s) {
				if (target != null) {
					activity.postRunnable(new Runnable() {

						@Override
						public void run() {
							target.setText(s.toString());
							target.setCursorPosition(textView
									.getSelectionStart());
						}
					});
				}
			}
		});
		textView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				AndroidInput input = activity.getInput();
				input.onKey(
						((AndroidGraphics) activity.getGraphics()).getView(),
						keyCode, event);
				if (keyCode == Keys.BACK) {
					onHideKeyBoard();
					requestGame();
					
				} else if (keyCode == Keys.ENTER) {
					onHideKeyBoard();
					requestGame();
				}
				return false;
			}
		});
		textView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	}

	@Override
	public void setPointer(final int position) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public synchronized void run() {
				textView.setSelection(position);
			}
		});
	}

	@Override
	public void setTarget(TextInputTarget target) {
		prepare(target);
		super.setTarget(target);
	}

	@Override
	public void requestFocus(FilterStyle style) {
		requestKeyboard();
		switch (style) {
			case ALPHANUMERIC:
				textView.setFilters(new InputFilter[] { new InputFilter() {

					@Override
					public CharSequence filter(CharSequence source, int start,
							int end, Spanned dest, int dstart, int dend) {
						for (int i = start; i < end; i++) {
							char c = source.charAt(i);
							if (!Character.isLetterOrDigit(c)
									&& !Character.isSpaceChar(c)) {
								return "";
							}
						}
						return null;
					}
				} });
				break;
			case NORMAL:
				textView.setFilters(null);
				break;
			default:
				break;
		}
	}

	public View getView() {
		return textView;
	}

	private void prepare(TextInputTarget target) {
		if (target != null
				&& !target.getCurrentText().equalsIgnoreCase(
						textView.getText().toString())) {
			final String content = target.getCurrentText();
			activity.runOnUiThread(new Runnable() {

				@Override
				public synchronized void run() {
					textView.setText(content);
				}
			});
		}
	}

	private void requestGame() {
		System.out.println("request game");
		activity.handler.post(new Runnable() {
			public void run() {
				{
					View view = textView;
					view.setFocusable(false);
					view.setFocusableInTouchMode(false);
				}
				{
					View view = ((AndroidGraphics) activity.getGraphics())
							.getView();
					view.setFocusable(true);
					view.setFocusableInTouchMode(true);
					view.requestFocus();
					InputMethodManager manager = (InputMethodManager) activity
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
			}
		});
	}

	private void requestKeyboard() {
		activity.handler.post(new Runnable() {
			public void run() {
				{
					View view = ((AndroidGraphics) activity.getGraphics())
							.getView();
					view.setFocusable(false);
					view.setFocusableInTouchMode(false);
				}
				{
					View view = textView;
					view.setFocusable(true);
					view.setFocusableInTouchMode(true);
					view.requestFocus();
					InputMethodManager manager = (InputMethodManager) activity
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					manager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
				}
			}
		});
	}

}
