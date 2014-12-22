package utils.networks;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;

public class UserInfo {

	private ArrayList<Partner> listPartners;
	private Permission permission;
	private String username;
	private int role_id;
	private static UserInfo INSTANCE;

	public static String fullName;
	public static String address;
	public static String level;
	public static String phone;
	public static String phoneNGT;
	public static int money;
	public static String currency;
	public static String email;
	public static String imeiDevice;
	public static String nameDevice;
	public static int state;
	
	public UserInfo() {
		this.listPartners = new ArrayList<UserInfo.Partner>();
		this.permission = new Permission();
	}

	public static UserInfo getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserInfo();
		}
		return INSTANCE;
	}

	public void setRoleId(int id) {
		this.role_id = id;
		setListPartner(id);
	}

	public int getRoleId() {
		return role_id;
	}

	private void setListPartner(int id) {
		listPartners.clear();
		if (id == 0) {
			listPartners.add(new Partner(-1, "-1", "Tất cả"));
			listPartners.add(new Partner(1, "1", "Admin"));
			listPartners.add(new Partner(2, "2", "Đại lý cấp 1"));
			listPartners.add(new Partner(3, "3", "Đại lý cấp 2"));
		} else if (id == 1) {
			listPartners.add(new Partner(-1, "-1", "Tất cả"));
			listPartners.add(new Partner(2, "2", "Đại lý cấp 1"));
			listPartners.add(new Partner(3, "3", "Đại lý cấp 2"));
		} else if (id == 2) {
			listPartners.add(new Partner(3, "3", "Đại lý cấp 2"));
		} else {
		}
	}

	// Chuoi json nay lay tu params PARTNER_LIST
	public void setPartnerFromJson(JsonValue json) {
		if (json.size > 1) {
			listPartners.add(partnerAll());
		}
		for (int i = 0; i < json.size; i++) {
			try {
				JsonValue aPartner = json.get(i);
				int id = aPartner.getInt(ExtParamsKey.ID);
				// String code = aPartner.getString(ExtParamsKey.PARTNER_CODE);
				// String title = aPartner.getString(ExtParamsKey.TITLE);
				// listPartners.add(new Partner(id, code, title));
			} catch (Exception e) {

			}
		}
	}

	private Partner partnerAll() {
		return new Partner(0, "-1", "Tất cả");
	}

	public void setPermissionFromJson(JsonValue json) {
		this.permission.parseFromJsonArray(json);
	}

	public ArrayList<Partner> getListPartners() {
		return listPartners;
	}

	public Permission getPermission() {
		return this.permission;
	}

	public void setName(String name) {
		this.username = name;
	}

	public String getName() {
		return this.username;
	}

	public class Partner {

		public int id;
		public String code;
		public String title;

		public Partner(int id, String code, String title) {
			this.id = id;
			this.code = code;
			this.title = title;
		}

		@Override
		public String toString() {
			return title;
		}
	}

	public class Permission {
		//
		public final static int MAX_PERMISSION = 10;
		public final static int PERMISSION_THONGKECHISOGAME = 0;
		public final static int PERMISSION_MINIGAME = PERMISSION_THONGKECHISOGAME + 1;
		public final static int PERMISSION_TIENTRONGGAME = PERMISSION_MINIGAME + 1;
		public final static int PERMISSION_QUANLYDOANHTHU = PERMISSION_TIENTRONGGAME + 1;
		public final static int PERMISSION_NAPTIENKHUYENMAI = PERMISSION_QUANLYDOANHTHU + 1;
		public final static int PERMISSION_QUANLYUSER = PERMISSION_NAPTIENKHUYENMAI + 1;
		public final static int PERMISSION_NOTIFICATION = PERMISSION_QUANLYUSER + 1;
		public final static int PERMISSION_QUANLYPARTNER = PERMISSION_NOTIFICATION + 1;
		public final static int PERMISSION_PHANQUYENUSER = PERMISSION_QUANLYPARTNER + 1;
		//
		public int[] permission;

		public Permission() {
			permission = new int[MAX_PERMISSION];
			for (int i = 0; i < permission.length; i++) {
				permission[i] = 1;
			}
		}

		// Kiem tra xem co quyen nao do hay khong. PermissionId dung cac bien
		// khai bao o tren
		public boolean isHasPermission(int permissionId) {
			return this.permission[permissionId] > 0;
		}

		public void setPermission(int[] newper) {
			this.permission = newper;
		}

		public void parseFromJsonArray(JsonValue json) {
			permission = new int[json.size];
			for (int i = 0; i < permission.length; i++) {
				permission[i] = json.getInt(i);
			}
		}

	}

}
