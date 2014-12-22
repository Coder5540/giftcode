package utils.factory;

public class StringUtil {

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

	public static boolean isValidEmail(String email) {
		return email
				.matches("[a-zA-Z0-9\\.]+@[a-zA-Z0-9\\-\\_\\.]+\\.[a-zA-Z0-9]{3}");
	}

	public static int countChar(String content, String contentCount) {
		int number = content.split(contentCount, -1).length - 1;
		return number;
	}

}
