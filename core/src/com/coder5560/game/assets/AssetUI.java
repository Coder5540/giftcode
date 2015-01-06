package com.coder5560.game.assets;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetUI {

	public TextureRegion	reg_submenu;
	public TextureRegion	reg_user;
	public TextureRegion	reg_ninepatch;
	public TextureRegion	reg_ninepatch1;
	public TextureRegion	reg_ninepatch2;
	public TextureRegion	reg_ninepatch3;
	public TextureRegion	reg_ninepatch4;
	public TextureRegion	reg_ninepatch5;
	public TextureRegion	reg_down;
	public TextureRegion	reg_avatar;

	private TextureRegion	reg_circle;

	private TextureRegion	reg_ninepathKnob;
	private TextureRegion	reg_logout;
	private TextureRegion	reg_logo;

	public TextureRegion	reg_userManagements;
	private TextureRegion	reg_icon_transition;
	private TextureRegion	reg_icon_giftcode;
	private TextureRegion	reg_mail;

	private TextureRegion	reg_icon_user;
	private TextureRegion	reg_icon_phone;
	private TextureRegion	reg_icon_money;
	private TextureRegion	reg_icon_role;
	private TextureRegion	reg_icon_refresh;
	private TextureRegion	reg_icon_menu;
	private TextureRegion	reg_icon_home;

	private TextureAtlas	textureAtlas;

	public AssetUI(TextureAtlas textureAtlas) {
		this.textureAtlas = textureAtlas;
		reg_down = textureAtlas.findRegion("down");
		reg_submenu = textureAtlas.findRegion("icon_submenu");
		reg_user = textureAtlas.findRegion("icon_user");
		reg_ninepatch = textureAtlas.findRegion("ninepatch_none");
		reg_ninepatch1 = textureAtlas.findRegion("ninepatch_rounded");
		reg_ninepatch2 = textureAtlas.findRegion("ninepatch_outline");
		reg_ninepatch3 = textureAtlas.findRegion("ninepatch_stock");
		reg_ninepatch4 = textureAtlas.findRegion("ninepatch_shadow_bottom");
		reg_ninepatch5 = textureAtlas.findRegion("ninepatch6");
	}

	public TextureRegion getAvatar() {
		if (reg_avatar == null)
			reg_avatar = textureAtlas.findRegion("user");
		return reg_avatar;
	}

	public TextureRegion getRegUsermanagement() {
		if (reg_userManagements == null)
			reg_userManagements = textureAtlas.findRegion("agency");
		return reg_userManagements;
	}

	public TextureRegion getRegionMail() {
		if (reg_mail == null)
			reg_mail = textureAtlas.findRegion("mail");
		return reg_mail;
	}

	public TextureRegion getCircle() {
		if (reg_circle == null)
			reg_circle = textureAtlas.findRegion("circle");
		return reg_circle;
	}

	public TextureRegion getNinePathKnob() {
		if (reg_ninepathKnob == null)
			reg_ninepathKnob = textureAtlas.findRegion("bg_white");
		return reg_ninepathKnob;
	}

	public TextureRegion getIconTransition() {
		if (reg_icon_transition == null) {
			reg_icon_transition = textureAtlas.findRegion("history");
		}
		return reg_icon_transition;
	}

	public TextureRegion getIconGiftcode() {
		if (reg_icon_giftcode == null) {
			reg_icon_giftcode = textureAtlas.findRegion("giftcode");
		}
		return reg_icon_giftcode;
	}

	public TextureRegion getRegionLogout() {
		if (reg_logout == null)
			reg_logout = textureAtlas.findRegion("logout");
		return reg_logout;
	}

	public TextureRegion getIconUser() {
		if (reg_icon_user == null)
			reg_icon_user = textureAtlas.findRegion("user");
		return reg_icon_user;
	}

	public TextureRegion getIconPhone() {
		if (reg_icon_phone == null)
			reg_icon_phone = textureAtlas.findRegion("phone");
		return reg_icon_phone;
	}

	public TextureRegion getIconMoney() {
		if (reg_icon_money == null)
			reg_icon_money = textureAtlas.findRegion("money");
		return reg_icon_money;
	}

	public TextureRegion getIconRole() {
		if (reg_icon_role == null)
			reg_icon_role = textureAtlas.findRegion("admin");
		return reg_icon_role;
	}

	public TextureRegion getIconRefresh() {
		if (reg_icon_refresh == null)
			reg_icon_refresh = textureAtlas.findRegion("refresh");
		return reg_icon_refresh;
	}

	public TextureRegion getIconMenu() {
		if (reg_icon_menu == null)
			reg_icon_menu = textureAtlas.findRegion("home-list");
		return reg_icon_menu;
	}
	public TextureRegion getIconHome() {
		if (reg_icon_home == null)
			reg_icon_home = textureAtlas.findRegion("home");
		return reg_icon_home;
	}

	public TextureRegion getLogo() {
		if (reg_logo == null)
			reg_logo = textureAtlas.findRegion("logo");
		return reg_logo;
	}

}