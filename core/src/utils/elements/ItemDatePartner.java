package utils.elements;

public class ItemDatePartner extends ItemPartnerSelectBox {
	public int daybefor;
	public String day;

	public ItemDatePartner(int daybefor) {
		this.daybefor = daybefor;
		if (daybefor == 0) {
			day = "Hôm nay";
			return;
		} else if (daybefor == 1) {
			day = "1 ngày trước";
		} else if (daybefor == 2) {
			day = "2 ngày trước";
		} else if (daybefor == 3) {
			day = "3 ngày trước";
		} else if (daybefor % 7 == 0) {
			day = (daybefor / 7) + " tuần trước";
		} else {
			day = daybefor + "ngày trước";
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return day;
	}
}
