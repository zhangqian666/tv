package com.iptv.rocky.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.ikantech.support.net.YiHttpRequest;
import com.ikantech.support.net.YiHttpRequest.YiHttpRequestMode;
import com.ikantech.support.net.YiHttpResponse;
import com.ikantech.support.utils.YiLog;
import com.ikantech.support.utils.YiUtils;
import com.iptv.common.data.LiveChannel;
import com.iptv.common.utils.LogUtils;

import android.app.DownloadManager.Request;

// 中兴
public class CUZTEAuthService extends AuthService {
	private static final int FIRST_HEART_BEAT_TIME = 2;
	private static final int HEART_BEAT_TIME = 15;
	public static String SESSIONID="";
	public static  String FRAMECODE="";
	public static  String IP_PORT="";
	 String session = null;
	private enum HttpReq {
		HTTP_AUTH(0x01), HTTP_UPLOADAUTHINFO(0x02), HTTP_GET_CHANNEL_LIST(0x03), 
		HTTP_GET_SERVICE_LIST(0x04), HTTP_LOAD_BALANCE(0x05), HTTP_GO_BALANCED_EPG(0x06), 
		HTTP_PORTAL_AUTH(0x07), HTTP_HEART_BEAT(0x08),HTTP_GET_ENCRYPT_TOKEN(0x09);
		private int code;

		private HttpReq(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public static HttpReq eval(int code) {
			HttpReq[] reqs = HttpReq.values();
			for (HttpReq httpReq : reqs) {
				if (httpReq.getCode() == code) {
					return httpReq;
				}
			}
			return null;
		}
	}

	private ReHeartBeatManager mReHeartBeatManager;
	private ReAuthManager mReAuthManager;

	@Override
	public void onCreate() {
		super.onCreate();

		mReAuthManager = new ReAuthManager(this);
		mReHeartBeatManager = new ReHeartBeatManager(this);

		YiLog.getInstance().i("CUZTEAuthService onCreate");
	}

	private void onError(int uid, boolean errFlag) {
		HttpReq req = HttpReq.eval(uid);
		if (req != null) {
			if (errFlag) {
				YiLog.getInstance().e("%s : failed", req.name());
				mReAuthManager.start();
				broadcast(ACTION_AUTH_ERROR, null);
			} else {
				YiLog.getInstance().e("%s : succeed", req.name());
			}
		} else {
			YiLog.getInstance().e("unknown error, request uid : %d", uid);
		}
	}

	public void reAuth() {
		mReAuthManager.start();
	}

	public void startAuth() {
		mServiceBinder.execute(new AuthRunnable());
	}
	
	@Override
	public void startNormalPlatformAuth() {
		mServiceBinder.execute(new AuthRunnable());
	}

	public void startAuth(long delay) {
		mServiceBinder.executeDelayed(new AuthRunnable(), delay);
	}

	public void reHeartBeat() {
		mReHeartBeatManager.start();
	}

	public void startHeartBeat(boolean immediately) {
		if (immediately) {
			mServiceBinder.execute(new HeartBeatRunnable());
		} else {
			mServiceBinder.executeDelayed(new HeartBeatRunnable(),
					FIRST_HEART_BEAT_TIME * ONE_MINUTE);
		}
	}

	@Override
	public void onHttpResponse(YiHttpResponse response) {
		String html = response.getResponse();

		//YiLog.getInstance().i("response 1111111111: %s", html);

		boolean errFlag = true;

		HttpReq req = HttpReq.eval(response.getUid());
		if (req != null) {
			switch (req) {
			case HTTP_AUTH: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					if(html != null 
							&& html.contains("getencry()")
							&& html.contains("getencrypttoken")){
						
						//Pattern pattern = Pattern.compile("Authentication\\.CTCGetAuthInfo\\(\'([^\']+)\'\\);");
						//Matcher m = pattern.matcher(html);
						//if (m.find()) {
							Pattern pattern1 = Pattern.compile("document.location=\"([^\"]+)\";");
							Matcher m1 = pattern1.matcher(html);
							if (m1.find()) {
								LogUtils.debug("HTTP_AUTH：m1 count:"+m1.groupCount());
								LogUtils.debug("HTTP_AUTH：m1:"+m1.group(1));
								
								
								LogUtils.debug("response.getUrl():"+response.getUrl());
								
								YiHttpRequest request = new YiHttpRequest(
										"http://10.0.5.72:4337/iptvepg/platform/"+m1.group(1), YiHttpRequestMode.MODE_GET);
								
								request.setUid(HttpReq.HTTP_GET_ENCRYPT_TOKEN.getCode());
								request.setTimeOut(HTTP_TIME_OUT);
//								Header[] headers = new Header[] { new BasicHeader(
//										"Cookie", "UserToken="
//												+ mParams.get("UserToken")) };
//								request.setHeaders(headers);
								mServiceBinder.execute(request, this);
								errFlag = false;
							}else{
								LogUtils.debug("HTTP_AUTH：m1,未发现");
							}
						//}
					}
				}
				break;
			}
			case HTTP_GET_ENCRYPT_TOKEN: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					LogUtils.debug("HTTP_GET_ENCRYPT_TOKEN返回数据："+html);
					if (html != null 
							&& html.contains("DoAuth()")) {
						LogUtils.debug("HTTP_GET_ENCRYPT_TOKEN");
						Pattern pattern = Pattern.compile("document.authform.Authenticator.value\\s*=\\s*GetAuthInfo\\(\'([^\']+)\'\\);");
						Matcher m = pattern.matcher(html);
						if (m.find()) {
							LogUtils.debug("HTTP_GET_ENCRYPT_TOKEN m1.group(1):"+m.group(1));
							
							String serial=m.group(1);
							
							// <form action="http://10.0.5.220:8080/iptvepg/platform/auth.jsp?easip=10.0.5.72&ipVersion=4&networkid=1" name="authform" method="post">
							
							Pattern pattern1 = Pattern.compile("<form\\s*action=\"([^\"]+)\"\\s*name=\"authform\"\\s*method=\"post\">");
							Matcher m1 = pattern1.matcher(html);
							if (m1.find()) {
								LogUtils.debug("m1.find():"+m1.group(1));
								
								// 提取IP地址
								
								Pattern pattern2 = Pattern.compile("<input\\s*type=\"hidden\"\\s*name=\"StbIP\"\\s*value=\"([^\"]+)\">");
								Matcher m2 = pattern2.matcher(html);
								if(m2.find()){
									LogUtils.debug("m2:"+m2.group(1));
									String stbIp= m2.group(1);
									YiHttpRequest request = new YiHttpRequest(
											m1.group(1),
											YiHttpRequestMode.MODE_POST);
									request.addParam("UserID", mUserId);
									request.addParam("Authenticator", AuthUtils.CTCGetAuthInfo(serial, mUserId, mPasswd));
									request.addParam("StbIP", stbIp);
								
									request.setUid(HttpReq.HTTP_UPLOADAUTHINFO.getCode());
									request.setTimeOut(HTTP_TIME_OUT);
									mServiceBinder.execute(request, this);

									errFlag = false;
								}
								
								
							}
						}else{
							LogUtils.debug("HTTP_GET_ENCRYPT_TOKEN: 不包含2");
						}
					}else{
						LogUtils.debug("HTTP_GET_ENCRYPT_TOKEN: 不包含1");
					}
				}
				break;
			}
			case HTTP_UPLOADAUTHINFO: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					LogUtils.debug("HTTP_UPLOADAUTHINFO");
					if (html != null 
							&& html.contains("AuthFinish()")
							) {
						Pattern pattern = Pattern.compile("jsSetConfig\\(\'([^']+)\',\'([^']+)\'\\);");
						Matcher m = pattern.matcher(html);
						while (m.find()) {
							String key = m.group(1);
							String value = m.group(2);
							LogUtils.debug("key："+key+"  value:"+value);
							if (key != null && value != null) {
								mParams.put(key, value);
							}
						}

						// 处理cookie信息
//						String session = null;
						Header[] headers = response.getHeaders();
						for (Header header : headers) {
							if ("Set-Cookie".equals(header.getName())) {
								Pattern cookie = Pattern.compile("([^=]+)=([^;]+);");
								Matcher m1 = cookie.matcher(header.getValue());
								while (m1.find()) {
									session = m1.group(2);
									mParams.put(m1.group(1), session);
									YiLog.getInstance().i("get session : %s", session);
								}
							}
						}
						
						
						
						
						Pattern pattern1 = Pattern.compile("window\\.location\\s*=\\s*\"([^']+)\";");
						Matcher m1 = pattern1.matcher(html);
						if (m1.find()) {
							
							LogUtils.debug("HTTP_UPLOADAUTHINFO:"+m1.group(1));
							
							String url = response.getUrl().substring(0, response.getUrl().lastIndexOf("/")+1);
							LogUtils.debug("url:"+url);
							
							YiHttpRequest request = new YiHttpRequest(
									url+ m1.group(1), YiHttpRequestMode.MODE_GET);
							request.setUid(HttpReq.HTTP_GET_CHANNEL_LIST.getCode());
							request.setTimeOut(HTTP_TIME_OUT);
							
							if (session != null) {
								Header[] headers1 = new Header[] { new BasicHeader(
										"Cookie", "JSESSIONID="
												+ mParams.get("JSESSIONID")) };
								request.setHeaders(headers1);
							}
							
							
//							Header[] headers = new Header[] { new BasicHeader(
//									"Cookie", "UserToken="
//											+ mParams.get("UserToken")) };
//							request.setHeaders(headers);
							mServiceBinder.execute(request, this);
							errFlag = false;
						}else{
							LogUtils.debug("url 未能找到");
						}
					}
				}
				break;
			}
			case HTTP_GET_CHANNEL_LIST: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					LogUtils.debug("HTTP_GET_CHANNEL_LIST");
					if (html != null&& html.contains("ConfigChannel")) {
						Pattern pattern = Pattern.compile("Authentication\\.CTCSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
						Matcher m = pattern.matcher(html);
						while (m.find()) {
							String key = m.group(1);
							String value = m.group(2);
							if (key != null && value != null) {
								if ("Channel".equals(key)) {
									try {
										//LogUtils.info("value-->"+value);
										value = value+",Platform=\"ZTE\"";
										LiveChannel channel = new LiveChannel(value);
										
										if (channel != null) {
											mChannels.add(channel);
										}
									} catch (Exception e) {
										YiLog.getInstance().i("parse live channel failed %s", e.getMessage());
									}
								} else {
									mParams.put(key, value);
								}
							}
						}

						Pattern pattern1 = Pattern.compile("window\\.location\\s*=\\s*\"([^']+)\";");
						Matcher m1 = pattern1.matcher(html);
						if (m1.find()) {
							String url = response.getUrl().substring(0, response.getUrl().lastIndexOf("/")+1);
							LogUtils.debug("url:"+url);
							YiHttpRequest request = new YiHttpRequest(
									url+m1.group(1), YiHttpRequestMode.MODE_GET);
							request.setUid(HttpReq.HTTP_GET_SERVICE_LIST.getCode());
							request.setTimeOut(HTTP_TIME_OUT);
							if (session != null) {
								Header[] headers1 = new Header[] { new BasicHeader(
										"Cookie", "JSESSIONID="
												+ mParams.get("JSESSIONID")) };
								request.setHeaders(headers1);
							}
							mServiceBinder.execute(request, this);
							errFlag = false;
						}else{
							LogUtils.debug("未找到！");
						}
					}
				}
				break;
			}
			case HTTP_GET_SERVICE_LIST: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					LogUtils.debug("HTTP_GET_SERVICE_LIST");
					if (html != null) {
						
						LogUtils.debug("HTTP_GET_SERVICE_LIST 2");
//						Pattern pattern = Pattern.compile("Authentication\\.CTCSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
//						Matcher m = pattern.matcher(html);
//						while (m.find()) {
//							String key = m.group(1);
//							String value = m.group(2);
//							if (key != null && value != null) {
//								if ("ServiceEntry".equals(key)) {
//									try {
//										ServiceEntry serviceEntry = new ServiceEntry(value);
//										if (serviceEntry != null) {
//											mServiceEntries.add(serviceEntry);
//										}
//									} catch (Exception e) {
//										YiLog.getInstance().i("parse serviceEntry failed %s", e.getMessage());
//									}
//								} else {
//									mParams.put(key, value);
//								}
//							}
//						}

						Pattern pattern1 = Pattern.compile("window\\.location\\s*=\\s*\'([^\']+)\'");
						Matcher m1 = pattern1.matcher(html);
						m1.find();
						if (m1.find()) {
							String url =null;
							LogUtils.debug("m1.groupCount()"+m1.groupCount());
							if(m1.groupCount()>1){
								 url=m1.group(2);
							}else{
								url=m1.group(1);
							}
							LogUtils.debug("url2："+url);
							YiHttpRequest request = new YiHttpRequest(
									url, YiHttpRequestMode.MODE_GET);
							request.setUid(HttpReq.HTTP_LOAD_BALANCE.getCode());
							request.setTimeOut(HTTP_TIME_OUT);
							if (session != null) {
								Header[] headers1 = new Header[] { new BasicHeader(
										"Cookie", "JSESSIONID="
												+ mParams.get("JSESSIONID")) };
								request.setHeaders(headers1);
							}
							mServiceBinder.execute(request, this);
							errFlag = false;
						}
					}
				}
				break;
			}
			case HTTP_LOAD_BALANCE: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
//					LogUtils.debug("HTTP_LOAD_BALANCE");
//					if (html != null 
//							&& html.contains("gotoBalancedEPG")
//							&& html.contains("CTCSetConfig")) {
//						Pattern pattern = Pattern.compile("document\\.location\\s*=\\s*\'(http://(\\d+\\.\\d+\\.\\d+.\\d+):([\\d]+)[^\']+)';");
//						Matcher m = pattern.matcher(html);
//						while (m.find()) {
//							String url = m.group(1);
//							String sIP = m.group(2);
//							String sPort = m.group(3);
//							if (url != null && !YiUtils.isStringInvalid(url)) {
//								YiHttpRequest request = new YiHttpRequest(url, YiHttpRequestMode.MODE_GET);
//								request.setUid(HttpReq.HTTP_GO_BALANCED_EPG.getCode());
//								request.setTimeOut(HTTP_TIME_OUT);
//								mServiceBinder.execute(request, this);
//								errFlag = false;
//							}
//
//							if (sPort != null && !YiUtils.isStringInvalid(sPort)) {
//								mParams.put("EPGPort", sPort);
//							}
//
//							if (sIP != null && !YiUtils.isStringInvalid(sIP)) {
//								mParams.put("EPGServerIP", sIP);
//							}
//						}
//					}

					if (html != null 
							&& html.contains("getSelfServiceSTBInfo")
							&& html.contains("funcportalauth")) {
						Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"([^\"]+)\" value=\"([^\"]+)\"\\s*>");
						Matcher m = pattern.matcher(html);
						
						LogUtils.debug("HTTP_LOAD_BALANCE");
						while (m.find()) {
							String key = m.group(1);
							String value = m.group(2);
							LogUtils.debug("key："+key+"  value:"+value);
							if (key != null && value != null) {
								mParams.put(key, value);
							}
						}
						
						//String url = response.getUrl();
						String url =response.getUrl().substring(0, response.getUrl().lastIndexOf("/"));
						
						YiHttpRequest request = new YiHttpRequest(
								String.format("%s%s",
										url,
										"/funcportalauth.jsp"),
								YiHttpRequestMode.MODE_POST);
						request.setUid(HttpReq.HTTP_PORTAL_AUTH.getCode());
						request.setTimeOut(HTTP_TIME_OUT);

						//String session = null;
//						Header[] headers = response.getHeaders();
//						for (Header header : headers) {
//							if ("Set-Cookie".equals(header.getName())) {
//								Pattern cookie = Pattern.compile("([^=]+)=([^;]+);");
//								Matcher m1 = cookie.matcher(header.getValue());
//								while (m1.find()) {
//									session = m1.group(2);
//									mParams.put(m1.group(1), session);
//									YiLog.getInstance().i("get session : %s", session);
//								}
//							}
//						}
						if (session != null) {
							Header[] headers1 = new Header[] { new BasicHeader(
									"Cookie", "JSESSIONID="
											+ mParams.get("JSESSIONID")) };
							request.setHeaders(headers1);
						}

						while (m.find()) {
							request.addParam(m.group(1), m.group(2));
						}

						mServiceBinder.execute(request, this);

						errFlag = false;
					}
				
				}
				break;
			}
			case HTTP_GO_BALANCED_EPG: {
//				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
//					if (html != null 
//							&& html.contains("getSelfServiceSTBInfo")
//							&& html.contains("funcportalauth")) {
//						Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"([^\"]+)\" value=\"([^\"]+)\"\\s*>");
//						Matcher m = pattern.matcher(html);
//						
//						YiHttpRequest request = new YiHttpRequest(
//								String.format("http://%s:%s%s",
//										mParams.get("EPGServerIP"),
//										mParams.get("EPGPort"),
//										"/iptvepg/function/funcportalauth.jsp"),
//								YiHttpRequestMode.MODE_POST);
//						request.setUid(HttpReq.HTTP_PORTAL_AUTH.getCode());
//						request.setTimeOut(HTTP_TIME_OUT);
//
//						String session = null;
//						Header[] headers = response.getHeaders();
//						for (Header header : headers) {
//							if ("Set-Cookie".equals(header.getName())) {
//								Pattern cookie = Pattern.compile("([^=]+)=([^;]+);");
//								Matcher m1 = cookie.matcher(header.getValue());
//								while (m1.find()) {
//									session = m1.group(2);
//									mParams.put(m1.group(1), session);
//									YiLog.getInstance().i("get session : %s", session);
//								}
//							}
//						}
//						if (session != null) {
//							Header[] headers1 = new Header[] { new BasicHeader(
//									"Cookie", "JSESSIONID="
//											+ mParams.get("JSESSIONID")) };
//							request.setHeaders(headers1);
//						}
//
//						while (m.find()) {
//							request.addParam(m.group(1), m.group(2));
//						}
//
//						mServiceBinder.execute(request, this);
//
//						errFlag = false;
//					}
//				}
				break;
			}
			case HTTP_PORTAL_AUTH: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					LogUtils.debug("HTTP_PORTAL_AUTH");
					if (html != null 
							&& html.contains("setInfoForFatClient")
							&& html.contains("jsSetConfig")) {
						
					   LogUtils.debug("HTTP_PORTAL_AUTH 2");

						
						String str=html.substring(html.indexOf("SessionID")+"SessionID".length());
						String SessionID=str.substring(0,str.indexOf(")"));
						SessionID=SessionID.replaceAll("'","").trim();
						SessionID=SessionID.replaceAll(",", "").trim();
						SessionID=SessionID.replaceAll("\"", "").trim();
						SESSIONID=SessionID;
						LogUtils.error("SessionID>>>"+SessionID);
						str=str.substring(str.indexOf("framecode")+"framecode".length());
						String framecode=str.substring(0,str.indexOf(")"));
						framecode=framecode.replaceAll("'","").trim();
						framecode=framecode.replaceAll(",", "").trim();
						framecode=framecode.replaceAll("\"", "").trim();
						LogUtils.error("framecode>>>"+framecode);
						FRAMECODE=framecode;
						str=str.substring(str.indexOf("IpPort")+"IpPort".length());
						String IpPort=str.substring(0,str.indexOf(")"));
						IpPort=IpPort.replaceAll("'","").trim();
						IpPort=IpPort.replaceAll(",", "").trim();
						IpPort=IpPort.replaceAll("\"", "").trim();
						IP_PORT=IpPort;
						LogUtils.error("IpPort>>>"+IpPort);
						
//						Pattern pattern = Pattern.compile("Authentication\\.CTCSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
//						Matcher m = pattern.matcher(html);
//						while (m.find()) {
//							String key = m.group(1);
//							String value = m.group(2);
//							
//							LogUtils.error("key------->>>>"+key);
//								LogUtils.error("value------->>>>"+value);
//							if (key != null && value != null) {
//								mParams.put(key, value);
//							}
//						}

						broadcast(ACTION_AUTH_SUCCEED, null);
						errFlag = false;
					}
				}
				break;
			}
			case HTTP_HEART_BEAT: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					mServiceBinder.executeDelayed(new HeartBeatRunnable(),
							HEART_BEAT_TIME * ONE_MINUTE);
					errFlag = false;
				} else {
					mReHeartBeatManager.start();
				}
				break;
			}
			default:
				break;
			}
		}
		onError(response.getUid(), errFlag);
	}

	protected class AuthRunnable implements Runnable {
		@Override
		public void run() {
			mIsAuthed = false;
			if (isNetConnected()) {
				YiHttpRequest request = new YiHttpRequest(mAuthUrl, YiHttpRequestMode.MODE_GET);
				request.addParam("UserID", mUserId);
//				request.addParam("STBAdminStatus", "1");
				request.addParam("Mode", "MENU");
				request.addParam("Action", "Login");
				request.setUid(HttpReq.HTTP_AUTH.getCode());
				request.setTimeOut(HTTP_TIME_OUT);
				mServiceBinder.execute(request, CUZTEAuthService.this);
				broadcast(ACTION_AUTH_START, null);
			} else {
				mReAuthManager.start();
			}
		}
	}

	protected class HeartBeatRunnable implements Runnable {
		@Override
		public void run() {
			YiHttpRequest request = new YiHttpRequest(String.format(
					"http://%s:%s%s", mParams.get("EPGServerIP"),
					mParams.get("EPGPort"), "/iptvepg/heartbeat.jsp"),
					YiHttpRequestMode.MODE_GET);

			String session = mParams.get("JSESSIONID");
			if (session != null) {
				Header[] headers1 = new Header[] { new BasicHeader("Cookie",
						"JSESSIONID=" + session) };
				request.setHeaders(headers1);
			}

			request.setUid(HttpReq.HTTP_HEART_BEAT.getCode());
			request.setTimeOut(HTTP_TIME_OUT);
			mServiceBinder.execute(request, CUZTEAuthService.this);
		}
	}


}
