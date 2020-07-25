package com.yzit.util;

import java.security.MessageDigest;

public class MD5Util2 {
    //password表示要加密的字符串，algorithm 表示 使用神魔方式 MD5
    public static String encode(String password, String algorithm)
    {
        byte[] unencodedPassword = password.getBytes();
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance(algorithm);
        }
        catch (Exception e)
        {
            return password;
        }
        md.reset();
        md.update(unencodedPassword);
        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < encodedPassword.length; i++)
        {
            if ((encodedPassword[i] & 0xFF) < 16)
            {
                buf.append("0");
            }
            buf.append(Long.toString(encodedPassword[i] & 0xFF, 16));
        }
        return buf.toString();
    }
}
