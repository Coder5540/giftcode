package utils.factory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uitls.input.TextInputHelper;
import utils.networks.ExtParamsKey;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.enums.Direct;

public class Factory {
	public static TextInputHelper	helper;

	public static int getNext(int curentValue, int min, int max) {
		curentValue = clamp(curentValue, min, max);
		if (curentValue == max)
			return min;

		return (curentValue + 1);
	}

	public static int getPrevious(int curentValue, int min, int max) {
		curentValue = clamp(curentValue, min, max);
		if (curentValue == min)
			return max;

		return (curentValue - 1);
	}

	public static int clamp(int curentValue, int min, int max) {
		if (curentValue <= min)
			curentValue = min;
		if (curentValue >= max)
			curentValue = max;
		return curentValue;
	}

	public static Vector2 getPosition(Rectangle bounds, Direct direct) {
		switch (direct) {
			case TOP_LEFT:
				return new Vector2(bounds.x, bounds.y + bounds.height);
			case TOP_RIGHT:
				return new Vector2(bounds.x + bounds.width, bounds.y
						+ bounds.height);
			case TOP:
				return new Vector2(bounds.x + bounds.width / 2, bounds.y
						+ bounds.height);
			case BOTTOM:
				return new Vector2(bounds.x + bounds.width / 2, bounds.y);
			case BOTTOM_LEFT:
				return new Vector2(bounds.x, bounds.y);
			case BOTTOM_RIGHT:
				return new Vector2(bounds.x + bounds.width, bounds.y);
			case MIDDLE:
				return new Vector2(bounds.x + bounds.width / 2, bounds.y
						+ bounds.height / 2);
			case MIDDLE_LEFT:
				return new Vector2(bounds.x, bounds.y + bounds.height / 2);
			case MIDDLE_RIGHT:
				return new Vector2(bounds.x + bounds.width, bounds.y
						+ bounds.height / 2);
			default:
				return new Vector2();
		}
	}

	public static TextureRegion[] getArrayTextureRegion(
			TextureRegion textureRegion, int FRAME_COLS, int FRAME_ROWS) {
		float width = textureRegion.getRegionWidth() / FRAME_COLS;
		float height = textureRegion.getRegionHeight() / FRAME_ROWS;

		TextureRegion[] textureRegions = new TextureRegion[FRAME_COLS
				* FRAME_ROWS];
		TextureRegion[][] temp = textureRegion.split((int) width, (int) height);
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				textureRegions[index++] = temp[i][j];
			}
		}
		return textureRegions;
	}

	// ====================Common Checker=====================
	public static boolean validEmail(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean validDate(String dateToValidate, String dateFromat) {
		if (dateToValidate == null) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		try {
			// if not valid, it will throw ParseException
			@SuppressWarnings("unused")
			Date date = sdf.parse(dateToValidate);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean isNumeric(String str) {
		return str.matches("\\d+");
	}

	public static boolean validPhone(String phoneNumber) {
		if (!(phoneNumber.startsWith("0") || phoneNumber.startsWith("84") || phoneNumber
				.startsWith("+84")))
			return false;
		String phone = "";
		if (phoneNumber.startsWith("+84"))
			phone = phoneNumber.substring(3);
		if (phoneNumber.startsWith("0"))
			phone = phoneNumber.substring(1);
		if (phoneNumber.startsWith("84"))
			phone = phoneNumber.substring(2);

		System.out.println("Phone : " + phone);
		if (phone.length() == 9 || phone.length() == 10) {
			return isNumeric(phone);
		}
		return false;
	}

	public static String getStrMoney(int money) {
		String str = money + "";
		if (money < 10000) {
			str = money + "";
		} else if (money < 1000000) {
			int firstNumber = money / 1000;
			int secondNumber = (money - firstNumber * 1000) / 100;
			str = firstNumber + "." + secondNumber + "K";
		} else if (money < 1000000000) {
			int firstNumber = money / 1000000;
			int secondNumber = (money - firstNumber * 1000000) / 100000;
			str = firstNumber + "." + secondNumber + "M";
		} else {
			int firstNumber = money / 1000000000;
			int secondNumber = (money - firstNumber * 1000000000) / 100000000;
			str = firstNumber + "." + secondNumber + "B";
		}
		return str;
	}

	public static String getDotMoney(int money) {
		String str = "";
		while (money > 1000) {
			int temp = money % 1000;
			if (temp < 10)
				str = ".00" + money % 1000 + str;

			else if (temp < 100)
				str = ".0" + money % 1000 + str;

			else
				str = "." + money % 1000 + str;

			money = money / 1000;
		}
		return money + str;
	}

	public static int round(float a) {
		int result = Math.round(a);
		if (result < a) {
			result++;
		}
		return result;
	}

	public static int countChar(String content, String contentCount) {
		int number = content.split(contentCount, -1).length - 1;
		return number;
	}

	public static String getSubString(String str, float width, BitmapFont font) {
		String substr = str;
		for (int i = 0; i < str.length(); i++) {
			if (font.getBounds(str.substring(0, i)).width > width
					- font.getBounds("...").width) {
				substr = str.substring(0, i) + "...";
				break;
			}
		}
		return substr;
	}

	static String	monthNames[]	= { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	@SuppressWarnings("deprecation")
	public static String getTime(long time) {
		long minute = (time / (1000 * 60)) % 60;
		long hour = (time / (1000 * 60 * 60)) % 24;

		Date d = new Date(time * 1000);
		int date = d.getDate();
		int month = d.getMonth();

		return String.format("%02d:%02d", hour, minute) + "  "
				+ monthNames[month] + " " + date;
	}

	@SuppressWarnings("deprecation")
	public static String getTimeContainSecond(long time) {
		long second = (time / 1000) % 60;
		long minute = (time / (1000 * 60)) % 60;
		long hour = (time / (1000 * 60 * 60)) % 24;

		Date d = new Date(time * 1000);
		int date = d.getDate();
		int month = d.getMonth();

		return String.format("%02d:%02d:%02d", hour, minute, second) + "  "
				+ monthNames[month] + " " + date;
	}

	public static String getDeviceID(JsonValue jsonValue) {
		JsonValue responeDevices = jsonValue.get(ExtParamsKey.DEVICE_ID);
		String imeiDevice = "";
		for (int i = 0; i < responeDevices.size; i++) {
			if (!responeDevices.getString(i).equalsIgnoreCase("")
					&& !responeDevices.getString(i).equalsIgnoreCase("null")) {
				imeiDevice += responeDevices.getString(i);
				if (i < responeDevices.size - 1) {
					imeiDevice += ",";
				}
			}
		}
		if (!imeiDevice.equalsIgnoreCase("")&&String.valueOf(imeiDevice.charAt(imeiDevice.length() - 1))
				.equalsIgnoreCase(",")) {
			imeiDevice = imeiDevice.substring(0, imeiDevice.length() - 2);
		}
		return imeiDevice;
	}

	public static String getDeviceName(JsonValue jsonValue) {
		JsonValue responseNames = jsonValue.get(ExtParamsKey.DEVICE_NAME);
		String nameDevice = "";
		for (int i = 0; i < responseNames.size; i++) {
			if (!responseNames.getString(i).equalsIgnoreCase("")
					&& !responseNames.getString(i).equalsIgnoreCase("null")) {
				nameDevice += responseNames.getString(i);
				if (i < responseNames.size - 1) {
					nameDevice += ",";
				}
			}
		}
		if (!nameDevice.equalsIgnoreCase("")&& String.valueOf(nameDevice.charAt(nameDevice.length() - 1))
				.equalsIgnoreCase(",")) {
			nameDevice = nameDevice.substring(0, nameDevice.length() - 2);
		}
		return nameDevice;
	}
	public static String getListByKey(String parameterKey,JsonValue jsonValue) {
		JsonValue responseNames = jsonValue.get(parameterKey);
		String nameDevice = "";
		for (int i = 0; i < responseNames.size; i++) {
			if (!responseNames.getString(i).equalsIgnoreCase("")
					&& !responseNames.getString(i).equalsIgnoreCase("null")) {
				nameDevice += responseNames.getString(i);
				if (i < responseNames.size - 1) {
					nameDevice += ",";
				}
			}
		}
		if (!nameDevice.equalsIgnoreCase("")&& String.valueOf(nameDevice.charAt(nameDevice.length() - 1))
				.equalsIgnoreCase(",")) {
			nameDevice = nameDevice.substring(0, nameDevice.length() - 2);
		}
		return nameDevice;
	}

}
