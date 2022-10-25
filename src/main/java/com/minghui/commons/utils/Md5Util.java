package com.minghui.commons.utils;

import com.minghui.commons.constant.FanbookConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

@Slf4j
public class Md5Util {

    public static String getMD5(String str) {
        if (str != null) {
            String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
            return md5;
        }
        return null;
    }

    public static boolean checkParameterMd5(String parameterStr,String requestMd5Key) {
        if (StringUtils.isAnyBlank(parameterStr,requestMd5Key)) {
            return Boolean.FALSE;
        }
        String checkMd5 = getMD5(parameterStr+","+ FanbookConstant.MD5_KEY);

        System.out.println("checkMd5:"+checkMd5);
        //校验通过
        if(requestMd5Key.equals(checkMd5)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
