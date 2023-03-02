package com.wu.commonapi.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具
 */
public class SignUtils {
    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return
     */
    private static final String SALT = "Gun";
    public static String genSign(String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = SALT + "." + secretKey;
        return md5.digestHex(content);
    }
}
