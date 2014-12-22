package com.coder5560.game.enums;

public class Constants {

	public static final int HEIGHT_SCREEN = 800;
	public static final int WIDTH_SCREEN = 480;
	public static final int HEIGHT_ACTIONBAR = 60;
	public static final int WIDTH_MAINMENU = 300;
	public static final String APP_NAME = "8b8gifcode";
	public static String DEVICE_ID = "id";
	public static String DEVICE_NAME = "name";

	public static final String TEXTURE_ATLAS_UI = "packs/ui.pack";
	public static final String TEXTURE_ATLAS_UIP = "Img/phu/ui/uip.pack";
	public static final String DEFAULT_SKIN = "skins/uiskin.json";
	public static Density density = Density.mdpi;

	public static String[] stateAccount = { "Chưa kích hoạt",
			"Hoạt động bình thường", "Bị khóa" };
	public static int agency_type_all = -1;
	public static int agency_type_non_active = 0;
	public static int agency_type_active = 1;
	public static int agency_type_lock = 2;
	public static int agency_type_lock_active = 3;
	public static int agency_type_wait_active = 4;// Đợi người dùng nhập tin
													// nhắn active
	

}
