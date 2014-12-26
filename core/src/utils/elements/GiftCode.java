package utils.elements;

public class GiftCode {

	public String code, id, amount, currency, moneyInGame, dateExpire,
			genarateDate;
	public boolean isSold, isUse;

	public GiftCode(String code, String id) {
		this.code = code;
		this.id = id;
	}
}
