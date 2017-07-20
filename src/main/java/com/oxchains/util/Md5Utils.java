package com.oxchains.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Md5Utils {
	public static String getMD5(String str) throws NoSuchAlgorithmException{
	    try {
	        // 生成一个MD5加密计算摘要
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 计算md5函数
	        md.update(str.getBytes());
	        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
	        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
	        String md5 = new BigInteger(1, md.digest()).toString(16);
	        log.debug("===md5==="+md5);
	        return md5;
	    } catch (NoSuchAlgorithmException e) {
	    	log.error("MD5 algorithm error", e.getMessage());
	        throw new NoSuchAlgorithmException("MD5加密出现错误:"+e.getMessage());
	    }
	}

	public static void main(String[] args) {
		String passwd = "123abc";
		try {
			String md5 = getMD5(passwd);
			log.debug(md5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
