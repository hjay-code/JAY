package com.hjay.easychat.utils;

import com.hjay.easychat.entity.constants.Constants;
import com.hjay.easychat.entity.enums.UserContactTypeEnum;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

public class StringTools {

    public static boolean isEmpty(String string){
        if(null == string || "".equals(string) || "\u0000".equals(string) || "null".equals(string)){
            return true;
        }else if ("".equals(string.trim())){
            return true;
        }

        return false;
    }

    public static String getUserId(){
        return UserContactTypeEnum.USER.getPrefix() + getRandomNumber(Constants.LENGTH);
    }


    public static String getRandomNumber(Integer count){
        return RandomStringUtils.random(count, false, true);
    }

    public static String getRandomString(Integer count){
        return RandomStringUtils.random(count, true, true);
    }

    public static String encodeMd5(String originString){
        return isEmpty(originString)?null: DigestUtils.md5Hex(originString);
    }
}
