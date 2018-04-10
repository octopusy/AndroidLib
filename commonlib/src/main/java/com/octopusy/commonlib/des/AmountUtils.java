package com.octopusy.commonlib.des;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户名校验工具类
 *
 * Created by Richard on 15/9/8.
 */
public class AmountUtils {

    public static boolean isNumber(String str) {
        String regex = "^([0-9]+(.[0-9]{1,2})?)|(-[0-9]+(.[0-9]{1,2})?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(str);
        return match.matches();
    }

}
