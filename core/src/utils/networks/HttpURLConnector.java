package utils.networks;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import utils.factory.Log;

import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

public class HttpURLConnector {

	private final String USER_AGENT = "Mozilla/5.0";

	// String host = "http://125.212.192.94:9091/";
	// String host = "http://115.146.121.82:9094/";
	private static String key = "8c24516c23b611420defccf253598412";

	String host = "http://192.168.1.252:9095/";

	// String host = "http://203.113.159.204:9091/";

	// String host = "http://192.168.1.101:9092/";

	// String host = "http://server.bai69.com/";

	// String host = "http://115.146.121.82:9092/";

	// String host = "http://api-analytics.bai69.com/";

	public static void main(String[] args) throws Exception {
		HttpURLConnector http = new HttpURLConnector();
		String name = "84125752333";
		// http.sendAgencyLogin();
		Request.getInstance().post(
				CommandRequest.LOGIN,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, "84125752333")
						.add(ExtParamsKey.PASSWORD, "123456")
						.add(ExtParamsKey.SIGNATURE,
								MD5Good.hash(name + "123456" + key)).build(),
				new HttpResponseListener() {

					@Override
					public void handleHttpResponse(HttpResponse httpResponse) {

					}

					@Override
					public void failed(Throwable t) {

					}

					@Override
					public void cancelled() {

					}
				});
	}

	private void sendAgencyLogin() throws Exception {
		String url = host + "8b8-agency/login";
		String name = "84125752333";
		String urlParameters = "agency_name=" + name
				+ "&password=123456&signature=" + MD5(name + "123456" + key);
		sendPostRequest(url, urlParameters);
	}

	private void sendGetMailListFromReport() throws Exception {
		String url = host + "8b8-agency/register";
		String urlParameters = "command=get_param&param_name=mail_recipient&param_value=bai.69@gmx.com,haichaule1990@gmail.com";
		sendPostRequest(url, urlParameters);
	}

	private void sendUpdateMailListFromReport() throws Exception {
		String url = host + "mail_report";
		String urlParameters = "command=update_param&param_name=mail_recipient&param_value=bai.69@gmx.com,haichaule1990@gmail.com";
		sendPostRequest(url, urlParameters);
	}

	private void sendKickUser() throws Exception {
		String url = host + "server";
		String user_name = "Hatuanyb";
		String urlParameters = "command=kich_user&user=" + user_name;
		sendPostRequest(url, urlParameters);
	}

	private void sendGetChangePassInfo() throws Exception {
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String url = host + "get_change_pass_info";
		String user_name = "thanbai";
		String signature = MD5(user_name + key);
		String urlParameters = "&user_name=" + user_name + "&signature="
				+ signature;
		sendPostRequest(url, urlParameters);
	}

	private void sendExchangeGift() throws Exception {
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String url = host + "exchange_gift";
		String gift_id = "1";
		String user_name = "thanbai";
		String signature = MD5(user_name + key);
		String urlParameters = "gift_id=" + gift_id + "&user_name=" + user_name
				+ "&signature=" + signature;
		sendPostRequest(url, urlParameters);
	}

	private void sendRechargeByWeb() throws Exception {
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String url = host + "recharge_card";
		String card_code = "111";
		String card_serial = "111-10-09";
		String card_type = "1";
		String user_name = "thanbai";
		String signature = MD5(user_name + key);
		// String urlParameters = "card_code=" + card_code + "&card_serial="
		// + card_serial + "&card_type=" + card_type + "&user_name="
		// + user_name + "&signature=" + signature;
		String urlParameters = "user_name=longnh&card_code=183183448013745&card_serial=APM375889&card_type=2&signature=fdc2dac35a96fbfe2cc9bd28251aec19";
		sendPostRequest(url, urlParameters);
	}

	private void sendAppotaRegister() throws Exception {
		String key = "ssg-8b5e0c232644e9e3b3313ec42a462f5f";
		String url = host + "responseAPI.php";
		String APIName = "register";
		String time = "2014-10-09 09:31:40";
		String userId = "1";
		String username = "thanbai";
		String sign = MD5(key + MD5(userId + username + time));
		String urlParameters = "APIName=" + APIName + "&time=" + time
				+ "&userId=" + userId + "&username=" + username + "&sign="
				+ sign;
		sendPostRequest(url, urlParameters);
	}

	private void sendAppotaActive() throws Exception {
		String key = "ssg-8b5e0c232644e9e3b3313ec42a462f5f";
		String url = host + "responseAPI.php";
		String APIName = "active";
		String time = "2014-10-09 09:31:40";
		String userId = "1";
		String username = "thanbai";
		String sign = MD5(key + MD5(userId + username + time));
		String urlParameters = "APIName=" + APIName + "&time=" + time
				+ "&userId=" + userId + "&username=" + username + "&sign="
				+ sign;
		sendPostRequest(url, urlParameters);
	}

	private void sendAppotaLogin() throws Exception {
		String key = "ssg-8b5e0c232644e9e3b3313ec42a462f5f";
		String url = host + "responseAPI.php";
		String APIName = "login";
		String time = "2014-10-09 09:31:40";
		String userId = "1";
		String username = "thanbai";
		String sign = MD5(key + MD5(userId + username + time));
		String urlParameters = "APIName=" + APIName + "&time=" + time
				+ "&userId=" + userId + "&username=" + username + "&sign="
				+ sign;
		sendPostRequest(url, urlParameters);
	}

	private void sendAppotaPayment() throws Exception {
		String key = "ssg-8b5e0c232644e9e3b3313ec42a462f5f";
		String url = host + "responseAPI.php";
		String APIName = "payment";
		String time = "2014-10-09 09:31:40";
		String userId = "1";
		String username = "thanbai";
		String transactionId = "1";
		String money = "1";
		String moneyIngame = "1";
		String sign = MD5(key + MD5(userId + username + time));
		String urlParameters = "APIName=" + APIName + "&time=" + time
				+ "&userId=" + userId + "&username=" + username + "&sign="
				+ sign + "&transactionId=" + transactionId + "&money=" + money
				+ "&moneyIngame=" + moneyIngame;
		sendPostRequest(url, urlParameters);
	}

	private void sendUserMessage() throws Exception {
		String url = host + "send_user_message";
		String userName = "chaulh";
		String title = "2014-10-09";
		String content = "bai69 táº·ng báº¡n Ä‘iá»‡n thoáº¡i iphone";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user=" + userName + "&title=" + title
				+ "&content=" + content + "&signature=" + MD5(userName + key);
		sendPostRequest(url, urlParameters);
	}

	private void sendAppotaCardRecharge() throws Exception {
		String url = host;
		String urlParameters = "status=1" + "&transaction_id=C91544721658527B"
				+ "&transaction_type=CARD" + "&card_code=1226298781059"
				+ "&card_serial=21817713385" + "&card_vendor=VIETTEL"
				+ "&target=username:samsunghd|userid:7520295" + "&amount=10000";
		sendPostRequest(url, urlParameters);
	}

	private void sendReadFileLog() throws Exception {
		String url = host + "read_file_log";
		String userName = "trantuyenyy";
		String date = "2014-11-06";
		String hour = "9";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user=" + userName + "&date=" + date + "&hour="
				+ hour + "&signature=" + MD5(userName + key);
		sendPostRequest(url, urlParameters);
	}

	private void sendChangeParam() throws Exception {
		String url = host + "system";
		String urlParameters = "command=change_system&param_name=admin_message&param_value=Giá»� vÃ ng online!!! Khuyá»ƒn máº¡i 200 khi náº¡p tháº» hoáº·c náº¡p  SMS tá»« 10h-14h vÃ  tá»« 20h-24h thá»© 5 ngÃ y 23/10/2014";
		sendPostRequest(url, urlParameters);
	}

	private void sendGetParam() throws Exception {
		String url = host + "system";
		String command = "get_param_value";
		String urlParameters = "command=" + command
				+ "&param_name=admin_message";
		sendPostRequest(url, urlParameters);
	}

	private void sendShutDown() throws Exception {
		String url = host + "server";
		String command = "shut_down";
		String urlParameters = "command=" + command;
		sendPostRequest(url, urlParameters);
	}

	private void sendSms() throws Exception {
		String url = host + "sms";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String message = "GAME5+chau80";
		String serviceNumber = "8118";
		String phone = "8412726508";
		String userName = "kurokd";
		String commanCode = "B69";
		// String urlParameters = "smsMessage=" + message + "&serviceName="
		// + serviceNumber + "&phoneNumber=" + phone + "&code=0&userName="
		// + userName + "&commandCode=" + commanCode + "&transactionId=1"
		// + "&signature=" + MD5(key + phone);
		String urlParameters = "smsMessage=B69+tuyetmuadongtuyetmuadong+KH"
				+ "&serviceName=8338" + "&phoneNumber=01287842128"
				+ "&userName=chau83" + "&commandCode=B69"
				+ "&transactionId=SMSCHARGE437154508D1F3FA762786"
				+ "&signature=d4cdd148958d625c04997be96ad7b1a8";
		sendPostRequest(url, urlParameters);
	}

	private void sendChangeUserMoneyInGameByName() throws Exception {
		String url = host + "change_money_in_game";
		String userName = "chaulh1";
		String moneyChange = "10000";
		String notifyContent = "";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_name=" + userName + "&money_change="
				+ moneyChange + "&notify_content=" + notifyContent
				+ "&signature=" + MD5(key + userName);
		sendPostRequest(url, urlParameters);
	}

	private void sendGetUserMoneyInGameByName() throws Exception {
		String url = host + "get_money_in_game";
		String userName = "chau83";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		// String urlParameters = "user_name=" + userName + "&signature="
		// + MD5(key + userName);
		String urlParameters = "user_name=trongkhanh&signature=235332610ddae7533e2c4f17e9c00c45";
		sendPostRequest(url, urlParameters);
	}

	private void sendGetUserInfoByName() throws Exception {
		String url = host + "get_user_info_by_name";
		String userId = "chau83";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "displayName=" + userId + "&signature="
				+ MD5(key);
		sendPostRequest(url, urlParameters);
	}

	private void sendGetUserInfoById() throws Exception {
		String url = host + "get_user_info_by_id";
		String userId = "57";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_id=" + userId + "&signature=" + MD5(key);
		sendPostRequest(url, urlParameters);

	}

	private void sendGetGitTransaction() throws Exception {
		String url = host + "get_gift_exchange_log";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "date=2014-07-18" + "&signature=" + MD5(key);
		sendPostRequest(url, urlParameters);
	}

	private void sendReloadParamElla() throws Exception {
		String url = host + "system";
		String urlParameters = "command=set_param";
		sendPostRequest(url, urlParameters);

	}

	private void sendGenerateLuckeyNumber() throws Exception {
		String url = host + "event";
		String urlParameters = "command=generate_lucky_number";
		sendPostRequest(url, urlParameters);

	}

	private void sendGetSmsTransaction() throws Exception {
		String url = host + "get_sms_transaction_log";
		String userName = "thanbai";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_name=" + userName + "&signature="
				+ MD5("thanbai" + key);
		sendPostRequest(url, urlParameters);

	}

	private void sendGetCardTransaction() throws Exception {
		String url = host + "get_card_transaction_log";
		String userName = "thanbai";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_name=" + userName + "&signature="
				+ MD5("thanbai" + key);
		sendPostRequest(url, urlParameters);

	}

	public static String MD5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	private void sendGetNumberPlayGame() throws Exception {
		String url = host + "get_number_play_game";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "signature=" + MD5(key) + "&date=2014-10-30";
		sendPostRequest(url, urlParameters);

	}

	private void senGetNumberActiveRoleByPartner() throws Exception {
		String url = host + "get_number_active";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "signature=" + MD5(key) + "&date=2014-10-28";
		sendPostRequest(url, urlParameters);

	}

	private void sendGetNumberNewRole() throws Exception {
		String url = host + "get_number_new_role";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "signature=" + MD5(key) + "&date=2014-09-28";
		sendPostRequest(url, urlParameters);

	}

	private void sendGetTotalRole() throws Exception {
		String url = host + "get_total_role";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "signature=" + MD5(key) + "&date=2014-09-30";
		sendPostRequest(url, urlParameters);

	}

	private void sendGetCcu() throws Exception {
		String url = host + "get_ccu";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "signature=" + MD5(key);
		sendPostRequest(url, urlParameters);

	}

	private void sendGetTotalMatch() throws Exception {
		String url = "http://192.168.1.252:9092/get_total_match";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "signature=" + MD5(key);
		sendPostRequest(url, urlParameters);

	}

	private void sendGetTopMoney() throws Exception {
		String url = "http://192.168.1.101:9092/get_top_money";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "limit=5" + "&signature=" + MD5(key);
		sendPostRequest(url, urlParameters);

	}

	private void sendFindUser() throws Exception {
		String url = host + "find_user";
		String userName = "saruno20";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_name=" + userName + "&signature="
				+ MD5(userName + key);
		sendPostRequest(url, urlParameters);
	}

	private void sendChangeAvatar() throws Exception {
		String url = host + "change_avatar";
		String userName = "chau79";
		String password = "123456";
		String avatar = "1";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_name=" + userName + "&password="
				+ password + "&avatar=" + avatar + "&signature="
				+ MD5(userName + MD5(password) + key);
		sendPostRequest(url, urlParameters);
	}

	private void sendChangePass() throws Exception {
		String url = host + "change_pass";
		String userName = "chau79";
		String oldPassword = "123456";
		String newPassword = "123456";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_name=" + userName + "&old_password="
				+ oldPassword + "&new_password=" + newPassword + "&signature="
				+ MD5(userName + MD5(oldPassword) + key);
		sendPostRequest(url, urlParameters);
	}

	private void sendLogin() throws Exception {
		String url = host + "login";
		String userName = "kurokd32";
		String password = "123w3456";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_name=" + userName + "&password="
				+ password + "&signature=" + MD5(userName + password + key);
		sendPostRequest(url, urlParameters);

	}

	private void sendRegister() throws Exception {
		String url = host + "register";
		String userName = "kurokq8";
		String password = "123456";
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		String urlParameters = "user_name=" + userName + "&type=1"
				+ "&password=" + password + "&partner_id=1&platform=Java"
				+ "&signature=" + MD5(userName + MD5(password) + key)
				+ "&email=1@" + "&phone=12312";
		sendPostRequest(url, urlParameters);

	}

	private void sendPostRequest(String link, String params) throws Exception {
		Log.d("params:" + params);
		URL obj = new URL(link);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// add reuqest header
		con.setRequestMethod("POST");
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(params.getBytes("UTF-8"));
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + link);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		Log.d("response:" + response.toString());
	}

	private void sendPost() throws Exception {
		String url = "http://192.168.1.252:9091/sms";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// add reuqest header
		String key = "2621fafbb8b403cd1633de2e6a9cb844";
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		// String urlParameters =
		// "command=reload_room_config&param_name=recharge_card_bonus";
		// Send post request
		String urlParameters = "smsMessage=GI43 VG chau11"
				+ "&serviceName=8533" + "&phoneNumber=8412726508"
				+ "&code=0&userName=chau11" + "&commandCode=GI43 VG"
				+ "&transactionId=1" + "&signature="
				+ MD5("2621fafbb8b403cd1633de2e6a9cb8448412726508");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(urlParameters.getBytes("UTF-8"));
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		Log.d("response:" + response.toString());
	}
}