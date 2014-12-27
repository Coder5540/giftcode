package utils.networks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import utils.factory.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.coder5560.game.enums.Constants;

public class Request {

	private static Request	INSTANCE;

	public HttpRequest		lastestHttpRequest;

	float					timeout	= 0;

	private Request() {
	}

	public String hash(String key) {
		try {
			return MD5Good.hash(key);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Request getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Request();
		}
		return INSTANCE;
	}

	/**
	 * Sử dụng HttpRequest và HttpResponse của Libgdx Method GET
	 */
	void get(String cmd, String params,
			HttpResponseListener httpResponseListener) {
		// if (UserInfo.getInstance().userLogged()) {
		// params = ParamsBuilder.builder().parseParams(params)
		// .add(ExtParamsKey.USER, UserInfo.getInstance().getUserId())
		// .build();
		// }
		params = params.replace(" ", "%20");
		String serviceUrl = ConnectionConfig.MAIN_URL + "/"
				+ ConnectionConfig.serviceName + "/" + cmd + "?" + params;
		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.GET);
		httpRequest.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		httpRequest.setUrl(serviceUrl);
		lastestHttpRequest = httpRequest;
		Gdx.net.sendHttpRequest(httpRequest, httpResponseListener);
	}

	/**
	 * Sử dụng HttpRequest và HttpResponse của Libgdx Method POST
	 * 
	 * @throws IOException
	 */
	public void post(String cmd, String params,
			HttpResponseListener httpResponseListener) {
		String serviceUrl = ConnectionConfig.MAIN_URL + "/"
				+ ConnectionConfig.serviceName + "/" + cmd;
		HttpRequest httpRequest = new HttpRequest(Net.HttpMethods.POST);
		httpRequest.setUrl(serviceUrl);
		httpRequest.setContent(params);
		httpRequest.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");
		Log.d("url=" + serviceUrl);
		Log.d("parrams=" + params);
		lastestHttpRequest = httpRequest;
		Gdx.net.sendHttpRequest(httpRequest, httpResponseListener);

	}

	public void loadConfig() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				String response = DefaultHttpConnection.get(
						ConnectionConfig.CONFIG, "");
				JsonValue params = (new JsonReader()).parse(response);
				if (params.has("server_primary_host")) {
					String host = params.getString("server_primary_host");
					if (!host.startsWith("http")) {
						host = "http://" + host;
					}
					ConnectionConfig.MAIN_URL = host;
				}
				if (params.has("zone")) {
					ConnectionConfig.serviceName = params.getString("zone");
				}
			}
		});
		thread.start();
	}

	public void login(String userName, String pass, String deviceID,
			String deviceName, HttpResponseListener listener) {
		post(CommandRequest.LOGIN,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, userName)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.DEVICE_ID, deviceID)
						.add(ExtParamsKey.DEVICE_NAME, deviceName)
						.add(ExtParamsKey.SIGNATURE,
								hash(userName + pass
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void active(String phone, String activeCode,
			HttpResponseListener listener) {
		post(CommandRequest.ACTIVE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, phone)
						.add(ExtParamsKey.ACTIVE_CODE, activeCode)
						.add(ExtParamsKey.SIGNATURE,
								hash(phone + activeCode
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void rejectActive(String phoneReject, String phoneAdmin,
			String passAdmin, HttpResponseListener listener) {
		post(CommandRequest.REJECT_AGENCY_ACTIVE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, phoneReject)
						.add(ExtParamsKey.ADMIN_NAME, phoneAdmin)
						.add(ExtParamsKey.PASSWORD, passAdmin)
						.add(ExtParamsKey.SIGNATURE,
								hash(phoneReject + phoneAdmin + passAdmin
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void doneSendingActiveCode(String phoneReject, String phoneAdmin,
			String passAdmin, HttpResponseListener listener) {
		post(CommandRequest.ADMIN_SEND_ACTIVE_CODE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, phoneReject)
						.add(ExtParamsKey.ADMIN_NAME, phoneAdmin)
						.add(ExtParamsKey.PASSWORD, passAdmin)
						.add(ExtParamsKey.SIGNATURE,
								hash(phoneReject + phoneAdmin + passAdmin
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getListAgency(String phone, String pass, int state,
			HttpResponseListener listener) {
		post(CommandRequest.GET_AGENCY_LIST,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, phone)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.STATE, state)
						.add(ExtParamsKey.SIGNATURE,
								hash(phone + pass + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void getActiveCode(String phoneAdmin, String pass, String phone,
			HttpResponseListener listener) {
		post(CommandRequest.GET_ACTIVE_CODE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.ADMIN_NAME, phoneAdmin)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.AGENCY_NAME, phone)
						.add(ExtParamsKey.SIGNATURE,
								hash(phone + phoneAdmin + pass
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void register(String phoneNumber, String pass,
			String phoneIntroduce, String fullName, String address,
			String email, HttpResponseListener listener) {
		post(CommandRequest.REGISTER,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, phoneNumber)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.REF_CODE, phoneIntroduce)
						.add(ExtParamsKey.FULL_NAME, fullName)
						.add(ExtParamsKey.ADDRESS, address)
						.add(ExtParamsKey.EMAIL, email)
						.add(ExtParamsKey.DEVICE_ID, Constants.DEVICE_ID)
						.add(ExtParamsKey.DEVICE_NAME, Constants.DEVICE_NAME)
						.add(ExtParamsKey.SIGNATURE,
								hash(phoneNumber + pass + Constants.DEVICE_ID
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getInfoDaily(String agencyName, String pass,
			HttpResponseListener listener) {
		post(CommandRequest.GET_INFO,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agencyName)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.SIGNATURE,
								hash(agencyName + pass
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void chaneStateAdmin(String agencyName, String adminName,
			String pass, int state, HttpResponseListener listener) {
		post(CommandRequest.CHANGE_AGENCY_STATE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agencyName)
						.add(ExtParamsKey.ADMIN_NAME, adminName)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.STATE, state)
						.add(ExtParamsKey.SIGNATURE,
								hash(agencyName + adminName + pass
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getMessageUnseen(String agency_name,
			HttpResponseListener responseListener) {
		post(CommandRequest.MESSAGE_UNSEEN,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agency_name)
						.add(ExtParamsKey.SIGNATURE,
								hash(agency_name + ConnectionConfig.CLIENT_KEY))
						.build(), responseListener);
	}

	public void getMessage(String agency_name,
			HttpResponseListener responseListener) {
		post(CommandRequest.GET_MESSAGE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agency_name)
						.add(ExtParamsKey.SIGNATURE,
								hash(agency_name + ConnectionConfig.CLIENT_KEY))
						.build(), responseListener);
	}

	public void requestSeen(String agency_name, int idMessage,
			HttpResponseListener responseListener) {
		post(CommandRequest.READ_MESSAGE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agency_name)
						.add(ExtParamsKey.ID, idMessage)
						.add(ExtParamsKey.SIGNATURE,
								hash(agency_name + idMessage
										+ ConnectionConfig.CLIENT_KEY)).build(),
				responseListener);

	}

	public void killAllProcess() {
		lastestHttpRequest = null;
	}

	public void checkDaily(String name, String receive,
			HttpResponseListener listener) {
		post(CommandRequest.CHECK_AGENCY_RECEIVE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_TRANSFER, name)
						.add(ExtParamsKey.AGENCY_RECEIVE, receive)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + receive
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getInfoDaily(String agencyName, HttpResponseListener listener) {
		post(CommandRequest.GET_INFO,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agencyName)
						.add(ExtParamsKey.SIGNATURE,
								hash(agencyName + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void getLowerDaily(String phone, String pass, int type,
			HttpResponseListener listener) {
		post(CommandRequest.GET_AGENCY_LIST,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, phone)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.STATE, type)
						.add(ExtParamsKey.SIGNATURE,
								hash(phone + pass + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void getExchangeInGame(String amount, String currency,
			HttpResponseListener listener) {
		post(CommandRequest.GET_EXCHANGE_IN_GAME,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AMOUNT, amount)
						.add(ExtParamsKey.CURRENCY, currency)
						.add(ExtParamsKey.SIGNATURE,
								hash(amount + currency
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void generateGiftCode(String agencyName, String amount,
			String currency, int timeExpire, HttpResponseListener listener) {
		post(CommandRequest.GET_EXCHANGE_IN_GAME,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agencyName)
						.add(ExtParamsKey.AMOUNT, amount)
						.add(ExtParamsKey.CURRENCY, currency)
						.add(ExtParamsKey.TIME_EXPIRE, timeExpire)
						.add(ExtParamsKey.SIGNATURE,
								hash(amount + currency
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void sendMoney(String name, String pass, String receiveName,
			String amount, String currency, String note,
			HttpResponseListener listener) {
		post(CommandRequest.TRANSFER_MONEY,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_TRANSFER, name)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.AGENCY_RECEIVE, receiveName)
						.add(ExtParamsKey.AMOUNT, amount)
						.add(ExtParamsKey.CURRENCY, currency)
						.add(ExtParamsKey.NOTE, note)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + pass + receiveName
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getLogMoneyByName(int type, String sdt, String timestart,
			String timeend, HttpResponseListener listener) {
		post(CommandRequest.GET_TRANSFER_LOG,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.TRANSFER_TYPE, type)
						.add(ExtParamsKey.AGENCY_NAME, sdt)
						.add(ExtParamsKey.SIGNATURE,
								hash(sdt + timestart + timeend
										+ ConnectionConfig.CLIENT_KEY))
						.add(ExtParamsKey.DATE_FROM, timestart)
						.add(ExtParamsKey.DATE_TO, timeend).build(), listener);
	}

	public void getLogMoneyByRole(int type, String sdt, String timestart,
			String timeend, int role_id, HttpResponseListener listener) {
		post(CommandRequest.GET_TRANSFER_LOG_SUB,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.TRANSFER_TYPE, type)
						.add(ExtParamsKey.AGENCY_NAME, sdt)
						.add(ExtParamsKey.DATE_FROM, timestart)
						.add(ExtParamsKey.DATE_TO, timeend)
						.add(ExtParamsKey.ROLE_ID, role_id)
						.add(ExtParamsKey.SIGNATURE,
								hash(sdt + timestart + timeend
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getNormalGiftCode(String name, int type,
			HttpResponseListener listener) {
		post(CommandRequest.GET_GIFTCODE_LIST,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, name)
						.add(ExtParamsKey.STATE, type)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void getUsedGiftCode(String name, HttpResponseListener listener) {
		post(CommandRequest.GET_GIFTCODE_LOG,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, name)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void returnGiftCode(String name, String idGiftCode,
			HttpResponseListener listener) {
		post(CommandRequest.RETURN_GIFT_CODE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, name)
						.add(ExtParamsKey.GIFT_CODE_ID, idGiftCode)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void setStateGiftCode(String name, String giftCode, int state,
			HttpResponseListener listener) {
		post(CommandRequest.CHANGE_GIFTCODE_STATE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, name)
						.add(ExtParamsKey.GIFT_CODE, giftCode)
						.add(ExtParamsKey.IS_SOLD, state)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void getExchangeInGame(int amount, String currency,
			HttpResponseListener listener) {
		post(CommandRequest.GET_EXCHANGE_IN_GAME,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AMOUNT, amount)
						.add(ExtParamsKey.CURRENCY, currency)
						.add(ExtParamsKey.SIGNATURE,
								hash(amount + currency
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void generateGiftCode(String agencyName, int amount,
			String currency, int timeExpire, HttpResponseListener listener) {
		post(CommandRequest.GENERATE_GIFT_CODE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agencyName)
						.add(ExtParamsKey.AMOUNT, amount)
						.add(ExtParamsKey.CURRENCY, currency)
						.add(ExtParamsKey.TIME_EXPIRE, timeExpire)
						.add(ExtParamsKey.QUANTITY, 1)
						.add(ExtParamsKey.SIGNATURE,
								hash(agencyName + amount
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void registerDevice(String agencyName, String pass, String deviceID,
			String deviceName, HttpResponseListener listener) {
		post(CommandRequest.REGISTER_DEVICE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agencyName)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.DEVICE_ID, deviceID)
						.add(ExtParamsKey.DEVICE_NAME, deviceName)
						.add(ExtParamsKey.SIGNATURE,
								hash(agencyName + deviceID + deviceName
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);

	}

	public void lockLoginDevice(String agencyAdmin, String agencyBlock,
			String deviceID, String deviceName, HttpResponseListener listener) {
		Log.d(agencyAdmin + " " + agencyBlock + " " + deviceID + " "
				+ deviceName);
		post(CommandRequest.LOCK_LOGIN_DEVICE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_ADMIN, agencyAdmin)
						.add(ExtParamsKey.AGENCY_BLOCK, agencyBlock)
						.add(ExtParamsKey.DEVICE_ID, deviceID)
						.add(ExtParamsKey.DEVICE_NAME, deviceName)
						.add(ExtParamsKey.SIGNATURE,
								hash(agencyAdmin + deviceID + deviceName
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void unLockLoginDevice(String agencyAdmin, String agencyBlock,
			String deviceID, String deviceName, HttpResponseListener listener) {
		Log.d(agencyAdmin + " " + agencyBlock + " " + deviceID + " "
				+ deviceName);
		post(CommandRequest.UNLOCK_LOGIN_DEVICE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_ADMIN, agencyAdmin)
						.add(ExtParamsKey.AGENCY_BLOCK, agencyBlock)
						.add(ExtParamsKey.DEVICE_ID, deviceID)
						.add(ExtParamsKey.DEVICE_NAME, deviceName)
						.add(ExtParamsKey.SIGNATURE,
								hash(agencyAdmin + deviceID + deviceName
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getListMoney(String agencyName, HttpResponseListener listener) {
		post(CommandRequest.GET_LIST_MONEY_LEVEL,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agencyName)
						.add(ExtParamsKey.SIGNATURE,
								hash(agencyName + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void getListTotalMoney(String name, String datefrom, String dateto,
			HttpResponseListener listener) {
		post(CommandRequest.MONEY_OVERVIEW,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, name)
						.add(ExtParamsKey.DATE_FROM, datefrom)
						.add(ExtParamsKey.DATE_TO, dateto)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + datefrom + dateto
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);

	}

	public void getLogMoneyGiftCodeByName(String sdt, String timestart,
			String timeend, HttpResponseListener listener) {
		post(CommandRequest.GET_MONEY_GIFT_CODE_LOG,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, sdt)
						.add(ExtParamsKey.SIGNATURE,
								hash(sdt + timestart + timeend
										+ ConnectionConfig.CLIENT_KEY))
						.add(ExtParamsKey.DATE_FROM, timestart)
						.add(ExtParamsKey.DATE_TO, timeend).build(), listener);
	}

	public void getLogMoneyGiftCodeByRole(String sdt, String timestart,
			String timeend, int role_id, HttpResponseListener listener) {
		post(CommandRequest.GET_MONEY_GIFT_CODE_LOG_SUB,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, sdt)
						.add(ExtParamsKey.DATE_FROM, timestart)
						.add(ExtParamsKey.DATE_TO, timeend)
						.add(ExtParamsKey.ROLE_ID, role_id)
						.add(ExtParamsKey.SIGNATURE,
								hash(sdt + timestart + timeend
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getAllAgency(String agency_name, int role_id,
			HttpResponseListener listener) {
		post(CommandRequest.GET_ALL_AGENCY,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agency_name)
						.add(ExtParamsKey.ROLE_ID, role_id)
						.add(ExtParamsKey.SIGNATURE,
								hash(agency_name + role_id
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void changeAgencyRoleName(String agency_name, int new_role_id,
			HttpResponseListener listener) {
		post(CommandRequest.CHANGE_AGENCY_ROLE,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, agency_name)
						.add(ExtParamsKey.ROLE_ID, new_role_id)
						.add(ExtParamsKey.SIGNATURE,
								hash(agency_name + new_role_id
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}

	public void getTotalMoneyInfo(String name, HttpResponseListener listener) {
		post(CommandRequest.TOTAL_MONEY_OVERVIEW,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_NAME, name)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + ConnectionConfig.CLIENT_KEY))
						.build(), listener);
	}

	public void sendMoneyFromAdmin(String name, String pass,
			String receiveName, String amount, String currency, String note,
			HttpResponseListener listener) {
		post(CommandRequest.TRANSFER_MONEY_FROM_ADMIN,
				ParamsBuilder
						.builder()
						.add(ExtParamsKey.AGENCY_TRANSFER, name)
						.add(ExtParamsKey.PASSWORD, pass)
						.add(ExtParamsKey.AGENCY_RECEIVE, receiveName)
						.add(ExtParamsKey.AMOUNT, amount)
						.add(ExtParamsKey.CURRENCY, currency)
						.add(ExtParamsKey.NOTE, note)
						.add(ExtParamsKey.SIGNATURE,
								hash(name + pass + receiveName
										+ ConnectionConfig.CLIENT_KEY)).build(),
				listener);
	}
}
