package com.coder5560.game.ui;

import utils.factory.DateTime;
import utils.factory.FontFactory.fontType;
import utils.factory.Style;
import utils.keyboard.KeyboardConfig;
import utils.keyboard.VirtualKeyboard.OnBackSpaceComma;
import utils.keyboard.VirtualKeyboard.OnDoneListener;
import utils.keyboard.VirtualKeyboard.OnHideListener;
import utils.screen.AbstractGameScreen;
import utils.screen.Toast;
import utils.screen.Toast.ALIGN;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.coder5560.game.assets.Assets;

public class DatePicker extends Table {
	public int thisYear;
	private TextField dayTF, monthTF, yearTF;
	Label dayL, monthL, yearL;
	int day, month, year;
	protected LabelStyle style = new LabelStyle(
			Assets.instance.fontFactory.getFont(20, fontType.Regular),
			Color.BLACK);
	String startString = "Ngày";

	public DatePicker(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
		thisYear = Integer.parseInt(DateTime.getCurrentDate("yyyy"));
		init();
	}

	// date format yyyy-MM-dd
	public void setDate(String date) {
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8);
		setDate(day, month, year);
	}

	public void setDate(String day, String month, String year) {
		dayTF.setText(day);
		monthTF.setText(month);
		yearTF.setText(year);
	}

	public DatePicker() {
		day = Integer.parseInt(DateTime.getCurrentDate("dd"));
		month = Integer.parseInt(DateTime.getCurrentDate("MM"));
		year = Integer.parseInt(DateTime.getCurrentDate("yyyy"));
		thisYear = Integer.parseInt(DateTime.getCurrentDate("yyyy"));
		init();
	}

	public void setDate(long time) {
		day = Integer.parseInt(DateTime.getStringDate(time, "dd"));
		month = Integer.parseInt(DateTime.getStringDate(time, "MM"));
		year = Integer.parseInt(DateTime.getStringDate(time, "yyyy"));
		thisYear = Integer.parseInt(DateTime.getStringDate(time, "yyyy"));
		refreshText();
	}

	public void init() {

		TextFieldStyle tfStyle = new TextFieldStyle();
		tfStyle.background = new NinePatchDrawable(new NinePatch(
				Assets.instance.getRegion("textfieldshadow"), 5, 5, 5, 5));
		tfStyle.cursor = new NinePatchDrawable(new NinePatch(
				Assets.instance.getRegion("bg_white"), new Color(0, 191 / 255f,
						1, 1)));
		tfStyle.font = Assets.instance.fontFactory.getFont(20, fontType.Light);
		tfStyle.fontColor = Color.BLACK;

		tfStyle.background.setLeftWidth(5);
		// tfStyle = Style.ins.getTextFieldStyle(5,
		// Assets.instance.fontFactory.getFont(20, fontType.Regular));
		dayL = new Label(startString, style);
		monthL = new Label("-", style);
		yearL = new Label("-", style);
		dayTF = new TextField(day + "", tfStyle);
		monthTF = new TextField(month + "", tfStyle);
		yearTF = new TextField(year + "", tfStyle);
		dayTF.setSize(30, 45);
		monthTF.setSize(30, 45);
		yearTF.setSize(30, 45);

		dayTF.setOnscreenKeyboard(AbstractGameScreen.keyboard);

		dayTF.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				registerKeyBoard(dayTF);
				return true;
			}

			@Override
			public boolean keyTyped(InputEvent event, char character) {
				return true;
			}
		});
		monthTF.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		monthTF.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				registerKeyBoard(monthTF);
				return true;
			}

			@Override
			public boolean keyTyped(InputEvent event, char character) {
				return true;
			}
		});
		yearTF.setOnscreenKeyboard(AbstractGameScreen.keyboard);
		yearTF.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				registerKeyBoard(yearTF);
				return true;
			}

			@Override
			public boolean keyTyped(InputEvent event, char character) {
				return true;
			}
		});
		dayTF.setMaxLength(2);
		monthTF.setMaxLength(2);
		yearTF.setMaxLength(4);
		add(dayL).center();
		add(dayTF).width(38).height(36).pad(5).padLeft(10);
		add(monthL);
		add(monthTF).width(38).height(36).pad(5);
		add(yearL);
		add(yearTF).width(60).height(36).pad(5);
		left();
	}

	public void setStartString(String startString) {
		this.startString = startString;
		dayL.setText(startString);
	}

	boolean error = false;

	private void registerKeyBoard(final TextField tf) {
		error = false;
		AbstractGameScreen.keyboard.registerTextField(tf,
				KeyboardConfig.NUMBER, KeyboardConfig.SINGLE_LINE);
		AbstractGameScreen.keyboard.show(true);
		AbstractGameScreen.keyboard.setBackspaceComma(new OnBackSpaceComma() {
			@Override
			public void backComma() {
			}
		});
		AbstractGameScreen.keyboard.setDoneListener(new OnDoneListener() {

			@Override
			public void done() {
				checkValid();
			}
		});

		AbstractGameScreen.keyboard.setHideListener(new OnHideListener() {

			@Override
			public void hide() {
				if (error) {
					error = false;
					registerKeyBoard(tf);
				}
			}
		});
	}

	public void refreshText() {
		dayTF.setText(day + "");
		monthTF.setText(month + "");
		yearTF.setText(year + "");
	}

	public void checkValid() {

		int temp = Integer.parseInt(dayTF.getText());
		if (temp < 0 || temp > 31) {
			Toast.makeText(getStage(), "Nhập sai dữ liệu", Toast.LENGTH_SHORT,
					ALIGN.MID);
			dayTF.setText(day + "");
			error = true;
		} else {
			day = Integer.parseInt(dayTF.getText());
		}
		temp = Integer.parseInt(monthTF.getText());
		if (temp < 0 || temp > 12) {
			Toast.makeText(getStage(), "Nhập sai dữ liệu", Toast.LENGTH_SHORT,
					ALIGN.MID);
			monthTF.setText(month + "");
			error = true;
		} else {
			month = Integer.parseInt(monthTF.getText());
		}
		temp = Integer.parseInt(yearTF.getText());
		if (temp < 2000 || temp > thisYear) {
			Toast.makeText(getStage(), "Nhập sai dữ liệu", Toast.LENGTH_SHORT,
					ALIGN.MID);
			yearTF.setText(year + "");

			error = true;
		} else {
			year = Integer.parseInt(yearTF.getText());
		}
	}

	public String getDate() {
		String date = Integer.parseInt(yearTF.getText()) + "-"
				+ Integer.parseInt(monthTF.getText()) + "-"
				+ Integer.parseInt(dayTF.getText());
		if (DateTime.isDateValid(date)) {
			day = Integer.parseInt(dayTF.getText());
			month = Integer.parseInt(monthTF.getText());
			year = Integer.parseInt(yearTF.getText());
			String dayT = (String) (day >= 10 ? (day + "") : ("0" + day) + "");
			String monthT = (String) (month >= 10 ? (month + "")
					: ("0" + month) + "");
			return year + "-" + monthT + "-" + dayT;
		}
		checkValid();
		return null;
	}

}
