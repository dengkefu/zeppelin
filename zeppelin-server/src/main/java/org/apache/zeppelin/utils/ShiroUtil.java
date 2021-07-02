package org.apache.zeppelin.utils;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

import java.util.Collection;
import java.util.Random;

public class ShiroUtil {
	
	public static Collection<Realm> getRealmsList() {
	    String key = ThreadContext.SECURITY_MANAGER_KEY;
	    DefaultWebSecurityManager defaultWebSecurityManager = (DefaultWebSecurityManager) ThreadContext.get(key);
	    return defaultWebSecurityManager.getRealms();
  }
	
	
	//length用户要求产生字符串的长度
	 public static String getRandomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	 
	 public static void main(String[] args) {
		System.out.println(ShiroUtil.getRandomString(10));
	}

}
