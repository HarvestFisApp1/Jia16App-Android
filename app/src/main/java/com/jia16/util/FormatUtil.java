package com.jia16.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字格式工具（检查、转换等）
 *
 * @author jiaohongyun
 * @date 2015年6月10日
 */
public class FormatUtil {
    /**
     * 检查用户名
     */
    public static boolean validateUserName(String userName) {
        return matches("^[A-Za-z][A-Za-z0-9_]{5,17}$", userName);
    }

//    /**
//     * 检查登录密码（8--位任意字符）
//     */
//    public static boolean validateLoginPwd(String pwd) {
//        return matches("^[\\w\\W]{8,}$", pwd);
//    }


    /**
     * 检查登录密码（8--位任意字符）
     */
    public static boolean validateLoginPwd(String pwd) {
        return matches("^[A-Za-z0-9]{8,}$", pwd);
    }


    /**
     * 检查登录密码（8--位不全是数字）
     */
    public static boolean jiaShivalidateLoginPwd(String pwd) {
        return matches("^[0-9]*$", pwd);//表示都是数字
    }

    /**
     * 检查交易密码(请输入6-16个数字和字母(字符a-zA-Z0-9)字符串)
     */
    public static boolean validatePaymenPwd(String PaymentPwd) {
        return matches("[a-zA-Z0-9]+", PaymentPwd);
    }


    /**
     * 检查嘉石榴交易密码(请输入6-18个数字和字母(字符a-zA-Z0-9)或符号字符串)
     */
    public static boolean validatejiashiPaymenPwd(String PaymentPwd) {
        //return matches("^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{6,18}$", PaymentPwd);

        return matches("^([a-zA-Z0-9_-`~!@#$%^&*()+\\|\\\\=,./?><\\{\\}\\[\\]]{6,18})+$", PaymentPwd);
    }


    /**
     * 检查用户名(请输入6-16个数字和字母(字符a-zA-Z0-9)字符串)
     */
    public static boolean validateuserName(String userName) {
        return matches("^[A-Za-z][A-Za-z0-9]{6,}$", userName);
    }

    /**
     * 检验数字输入框大于零且不能为"-"或"+"
     */
    public static boolean validateEditParams(String editStr) {
        return matches("^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$", editStr);
    }

    /**
     * 检测真实姓名格式
     */
    public static boolean validateRealName(String name) {
        return matches("^[\\u4e00-\\u9fa5]*$", name);
    }


    /**
     * 检测邮箱验证格式   验证嘉实邮箱
     */

    public static boolean checkEmail(String email){
        return matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$",email);

    }


    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        return matches("[1][3578]\\d{9}", mobiles);
    }

    public static boolean validateMoney(String money) {
        return matches("^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$", money);
    }

    public static boolean validatePhone(String phone) {
        return matches("[0-9]{11}", phone);
    }

    public static boolean checkIdCard(String idCard) {

        /*
		 * 身份证15位编码规则：dddddd yymmdd xx p
		 * dddddd：6位地区编码
		 * yymmdd: 出生年(两位年)月日，如：910215
		 * xx: 顺序编码，系统产生，无法确定
		 * p: 性别，奇数为男，偶数为女
		 *
		 * 身份证18位编码规则：dddddd yyyymmdd xxx y
		 * dddddd：6位地区编码
		 * yyyymmdd: 出生年(四位年)月日，如：19910215
		 * xxx：顺序编码，系统产生，无法确定，奇数为男，偶数为女
		 * y: 校验码，该位数值可通过前17位计算获得
		 *
		 * 前17位号码加权因子为 Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
		 * 验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]
		 * 如果验证码恰好是10，为了保证身份证是十八位，那么第十八位将用X来代替
		 * 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )
		 * i为身份证号码1...17 位; Y_P为校验码Y所在校验码数组位置
		 */

        //将前17位加权因子保存在数组里
        int[] idCardWi = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        //这是除以11后，可能产生的11位余数、验证码，也保存成数组
        int[] idCardY = new int[]{1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};
        int idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
        for (int i = 0; i < 17; i++) {
            idCardWiSum += Integer.parseInt(idCard.substring(i, i + 1)) * idCardWi[i];
        }
        int idCardMode = idCardWiSum % 11; //计算出校验码所在数组的位置
        String idCardLast = idCard.substring(17);//得到最后一位身份证号码
        //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
        if (idCardMode == 2) {
            if (idCardLast == "X" || idCardLast == "x") {
                return true;
            } else {
                return false;
            }
        } else {
            //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
            if (idCardLast.equals(idCardY[idCardMode] + "")) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 正则表达式检查
     *
     * @param regex
     * @param src
     * @return
     */
    public static boolean matches(String regex, String src) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(src);
        return matcher.matches();
    }

    /**
     * 格式化手机号
     * @return
     */
    public static String formatPhoneNum(String phoneNum) {

        return phoneNum.replace("+86", "").replace("-", "").replace(" ","");
    }
}
