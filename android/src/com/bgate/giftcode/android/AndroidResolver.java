package com.bgate.giftcode.android;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import utils.factory.PlatformResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.badlogic.gdx.graphics.Pixmap;
import com.coder5560.game.enums.PlatformType;

public class AndroidResolver implements PlatformResolver {
	AndroidLauncher	main;
	MediaPlayer		mediaPlayer;
	PlatformType	type;

	public AndroidResolver(AndroidLauncher main) {
		this.main = main;
		mediaPlayer = new MediaPlayer();
	}

	@Override
	public Pixmap formatBitmap(InputStream in) {
		Pixmap pixmap = null;

		try {
			Bitmap myBitmap = BitmapFactory.decodeStream(in);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			myBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			pixmap = new Pixmap(outStream.toByteArray(), 0,
					outStream.toByteArray().length);
		} catch (Exception e) {
		}
		return pixmap;
	}

	@Override
	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		String data = "";

		if (model.startsWith(manufacturer)) {
			data = capitalize(model);
		} else {
			data = capitalize(manufacturer) + " " + model;
		}
		return data.replaceAll(",", ".");
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	@Override
	public String getDeviceID() {
		TelephonyManager mngr = (TelephonyManager) main
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mngr.getDeviceId();
		return imei;
	}

	@Override
	public void setPlatform(PlatformType platformType) {
		this.type = platformType;
	}

	@Override
	public PlatformType getPlatform() {
		return type;
	}

	@Override
	public void sendSMS(String number, String message, OnResultListener listener) {
		main.sendSMS(number, message, listener);
	}

	@Override
	public String getCountryCode() {
		String pair = "93,AF, 355,AL, 213,DZ, 376,AD, 244,AO, 672,AQ, 54,AR, 374,AM, 297,AW, 61,AU, 43,AT, 994,AZ, 973,BH, 880,BD, 375,BY, 32,BE, 501,BZ, 229,BJ, 975,BT, 591,BO, 387,BA, 267,BW, 55,BR, 673,BN, 359,BG, 226,BF, 95,MM, 257,BI, 855,KH, 237,CM, 1,CA, 238,CV, 236,CF, 235,TD, 56,CL, 86,CN, 61,CX, 61,CC, 57,CO, 269,KM, 242,CG, 243,CD, 682,CK, 506,CR, 385,HR, 53,CU, 357,CY, 420,CZ, 45,DK, 253,DJ, 670,TL, 593,EC, 20,EG, 503,SV, 240,GQ, 291,ER, 372,EE, 251,ET, 500,FK, 298,FO, 679,FJ, 358,FI, 33,FR, 689,PF, 241,GA, 220,GM, 995,GE, 49,DE, 233,GH, 350,GI, 30,GR, 299,GL, 502,GT, 224,GN, 245,GW, 592,GY, 509,HT, 504,HN, 852,HK, 36,HU, 91,IN, 62,ID, 98,IR, 964,IQ, 353,IE, 44,IM, 972,IL, 39,IT, 225,CI, 81,JP, 962,JO, 7,KZ, 254,KE, 686,KI, 965,KW, 996,KG, 856,LA, 371,LV, 961,LB, 266,LS, 231,LR, 218,LY, 423,LI, 370,LT, 352,LU, 853,MO, 389,MK, 261,MG, 265,MW, 60,MY, 960,MV, 223,ML, 356,MT, 692,MH, 222,MR, 230,MU, 262,YT, 52,MX, 691,FM, 373,MD, 377,MC, 976,MN, 382,ME, 212,MA, 258,MZ, 264,NA, 674,NR, 977,NP, 31,NL, 599,AN, 687,NC, 64,NZ, 505,NI, 227,NE, 234,NG, 683,NU, 850,KP, 47,NO, 968,OM, 92,PK, 680,PW, 507,PA, 675,PG, 595,PY, 51,PE, 63,PH, 870,PN, 48,PL, 351,PT, 1,PR, 974,QA, 40,RO, 7,RU, 250,RW, 590,BL, 685,WS, 378,SM, 239,ST, 966,SA, 221,SN, 381,RS, 248,SC, 232,SL, 65,SG, 421,SK, 386,SI, 677,SB, 252,SO, 27,ZA, 82,KR, 34,ES, 94,LK, 290,SH, 508,PM, 249,SD, 597,SR, 268,SZ, 46,SE, 41,CH, 963,SY, 886,TW, 992,TJ, 255,TZ, 66,TH, 228,TG, 690,TK, 676,TO, 216,TN, 90,TR, 993,TM, 688,TV, 971,AE, 256,UG, 44,GB, 380,UA, 598,UY, 1,US, 998,UZ, 678,VU, 39,VA, 58,VE, 84,VN, 681,WF, 967,YE, 260,ZM, 263,ZW";
		String CountryID = "";
		String CountryZipCode = "";

		TelephonyManager manager = (TelephonyManager) main
				.getSystemService(main.TELEPHONY_SERVICE);
		CountryID = manager.getSimCountryIso().toUpperCase();
		String[] rl = pair.split(", ");
		for (int i = 0; i < rl.length; i++) {
			String[] g = rl[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				CountryZipCode = g[0];
				break;
			}
		}
		return CountryZipCode;
	}
}