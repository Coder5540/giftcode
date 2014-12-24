package com.coder5560.game.ui;

import utils.keyboard.VirtualKeyboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class CustomTextField extends TextField {

	public CustomTextField(String text, Skin skin, String styleName) {
		super(text, skin, styleName);
	}

	public CustomTextField(String text, Skin skin) {
		super(text, skin);
	}

	public CustomTextField(String text, TextFieldStyle style) {
		super(text, style);
	}

	@Override
	protected InputListener createInputListener() {
		// TODO Auto-generated method stub
		return new CustomTextFieldClickListener();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		Stage stage = getStage();
		boolean focused = stage != null && stage.getKeyboardFocus() == this;

		final BitmapFont font = getStyle().font;
		final Color fontColor = (isDisabled() && getStyle().disabledFontColor != null) ? getStyle().disabledFontColor
				: ((focused && getStyle().focusedFontColor != null) ? getStyle().focusedFontColor
						: getStyle().fontColor);
		final Drawable selection = getStyle().selection;
		final Drawable cursorPatch = getStyle().cursor;
		final Drawable background = (isDisabled() && getStyle().disabledBackground != null) ? getStyle().disabledBackground
				: ((focused && getStyle().focusedBackground != null) ? getStyle().focusedBackground
						: getStyle().background);

		Color color = getColor();
		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		float bgLeftWidth = 0;
		if (background != null) {
			background.draw(batch, x, y, width, height);
			bgLeftWidth = background.getLeftWidth();
		}

		float textY = getTextY(font, background);
		calculateOffsets();

		if (focused && hasSelection && selection != null) {
			drawSelection(selection, batch, font, x + bgLeftWidth, y + textY);
		}

		float yOffset = font.isFlipped() ? -textHeight : 0;
		super.draw(batch, parentAlpha);
		if (displayText.length() == 0) {
			if (getMessageText() != null) {
				if (getStyle().messageFontColor != null) {
					font.setColor(getStyle().messageFontColor.r,
							getStyle().messageFontColor.g,
							getStyle().messageFontColor.b,
							getStyle().messageFontColor.a * parentAlpha);
				} else
					font.setColor(0.7f, 0.7f, 0.7f, parentAlpha);
				BitmapFont messageFont = getStyle().messageFont != null ? getStyle().messageFont
						: font;
				messageFont.draw(batch, getMessageText(), x + bgLeftWidth, y
						+ textY + yOffset);
			}
		}
	}

	public class CustomTextFieldClickListener extends TextFieldClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			int count = getTapCount() % 4;
			if (count == 0) clearSelection();
		}
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			System.out.println(" x = " + x + ":" + "y = " + y);
			super.touchDown(event, x - getStyle().background.getLeftWidth(), y,
					pointer, button);
			System.out.println("cursor tf : " + getCursorPosition());
			((VirtualKeyboard) getOnscreenKeyboard())
					.setCursorPosition(getCursorPosition() - 1);
			System.out.println(((VirtualKeyboard) getOnscreenKeyboard()).getCursorPosition());
			return true;
		}	
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer,
				int button) {
			// TODO Auto-generated method stub
			super.touchUp(event, x - getStyle().background.getLeftWidth(), y,
					pointer, button);
		}
		
		@Override
		public void touchDragged(InputEvent event, float x, float y, int pointer) {
			// TODO Auto-generated method stub
			super.touchDragged(event, x - getStyle().background.getLeftWidth(), y,
					pointer);
		}
	}
}
