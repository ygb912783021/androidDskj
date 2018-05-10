package dingshi.com.hibook.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/14.
 */

public class PhoneUtils {
    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    public static boolean isPhoneNumber(String phoneNums) {
        //
        if (isMatchLength(phoneNums, 11) && checkMobileNumber(phoneNums)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     */
    private static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    private static boolean isMobileNO(String mobileNums) {
                /* * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
                 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
				 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9*/

        String telRegex = "[1][0123456789]\\d{9}";
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums)) {
            return false;
        } else {
            return mobileNums.matches(telRegex);
        }
    }

    /**
     * 验证手机号是否符合
     * @param mobileNumber
     * @return boolean true 符合 false 不符合
     */

    public static boolean checkMobileNumber(String mobileNumber) {

        boolean flag = false;
        try {
            Pattern regex = Pattern
                    .compile("^(((13[0-9])|(14([0-9]))|(15([0-9]))|(17([0-9]))|(18[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
