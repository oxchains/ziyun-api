package com.oxchains.common;

public class ConstantsData {
		/**
		 * 返回码定义值
		 */
		public static final int RTN_SUCCESS = 0;//成功
		public static final int RTN_SERVER_INTERNAL_ERROR = 1;//服务器内部错误
		public static final int RTN_INVALID_ARGS = 2;//参数错误
		public static final int RTN_UNLOGIN = 3;//未登录
		public static final int RTN_UNAUTH = 4;//未授权
		public static final int RTN_UNREGISTER = 5;//未注册
		public static final int RTN_LOGIN_EXPIRED = 6;//登录超时
		public static final int RTN_DATA_ALREADY_EXISTS = 1001;//此数据已经存在
		public static final int RTN_USERNAMEORPWD_ERROR = 1002;//用户名或密码错误
		
		/**
		 * 用户状态
		 */
		public static final int USER_NORMAL = 1;//正常	
		public static final int USER_AUDITING = 2;//审核中
		public static final int USER_FROZEN = 3;//冻结
		
		/**
		 * token info
		 */
		public static final int TOKEN_EXPIRES = 7200;//TOKEN过期时间，单位秒
		public static final String TOKEN_SECRET = "tokensecret";//token secret , to be replaced later
		public static final String TOKEN_SUBJECT = "token";
		public static final String TOKEN_ISSUER = "oxchains";
}
