package com.zr.encrypt.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CheckUtil  {

	public static boolean isMobile(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		String s2="^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";// 验证手机号
		Pattern p = Pattern.compile(s2);
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
