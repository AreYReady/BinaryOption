package com.xkj.binaryoption.bean;

/**
 * Created by estel on 2015/10/12.
 */
public interface IUserLogin {
	int getLogin();
	String getPassword_hash();
	void onLoginHttpfaild(String faildString);
}