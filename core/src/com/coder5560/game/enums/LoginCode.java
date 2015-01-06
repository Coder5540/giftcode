package com.coder5560.game.enums;
public enum LoginCode {
	
	SUCCESS, // thanh cong
	ACC_NOT_EXITS, // tk khong ton tai
	WRONG_PASS, // sai mat khau
	NOT_ACTIVE, // chua kich hoat
	REMAIN_ANOTHER_SESSION, // van con 1 thiet bi khac dang nhap
	DEVICE_BLOCK, // device bi khoa
	DEVICE_NOT_IN_LIST_CAN_NOT_REGISTER, // khong nam trong ds đang nhap, ko the đăng ký
	DEVICE_NOT_IN_LIST_CAN_REGISTER; // khong nam tron ds dang nhap, co the dang ky
}