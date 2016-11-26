package com.iptv.rocky.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import com.ikantech.support.net.YiHttpRequest;
import com.ikantech.support.net.YiHttpRequest.YiHttpRequestMode;
import com.ikantech.support.net.YiHttpResponse;
import com.ikantech.support.utils.YiLog;
import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannel;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;

// 华为
public class CUAuthService extends AuthService {
	
	//first heart beat delay time, minute
	private static final int FIRST_HEART_BEAT_TIME = 2;
	//heart beat delay time, minute
	private static final int HEART_BEAT_TIME = 10;
	private static String mUserToken;
	
    public static final String ACTION_START_AUTH = "com.rocky.android.auth.action.AUTH_START";

	private enum HttpReq {
		HTTP_AUTH(0x01), HTTP_UPLOADAUTHINFO(0x02), HTTP_GET_CHANNEL_LIST(0x03), 
		HTTP_GET_SERVICE_LIST(0x04), HTTP_GO_EPG_AUTH(0x05), HTTP_HEART_BEAT(0x06), 
		HTTP_AUTO_LOGIN(0x07);
		
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

	private String mLang = "1";

	private ReHeartBeatManager mReHeartBeatManager;
	private ReAuthManager mReAuthManager;

	@Override
	public void onCreate() {
		super.onCreate();

		mReAuthManager = new ReAuthManager(this);
		mReHeartBeatManager = new ReHeartBeatManager(this);

		YiLog.getInstance().i("CUAuthService onCreate");
	}

	private void onError(int uid, boolean errFlag, int errorCode) {
		Log.d("CUAuthService", "onError errflag:"+errFlag +"; uid:"+uid+"; errorCode:"+errorCode);
		HttpReq req = HttpReq.eval(uid);
		Log.d("CUAuthService","req:"+ req);
		if (req != null) {
			if (errFlag) {
				
				if(errorCode == YiHttpResponse.ERROR_TIME_OUT){
					Map<String,String> reasons= new HashMap<String,String>();
					reasons.put("errorCode", Integer.toString(errorCode));
					reasons.put("reason", "IPTV认证服务器连接失败");
					broadcast(ACTION_AUTH_ERROR, reasons);
				}else if(req.equals("HTTP_UPLOADAUTHINFO")){
					if(errorCode == 30022){
						Log.d("CUAuthService","req name:"+req.name());
						
						Map<String,String> reasons= new HashMap<String,String>();
						reasons.put("reason", "用户处于欠费停用状态，您的IPTV业务已经被停用！");
						reasons.put("errorCode", Integer.toString(errorCode));
						broadcast(ACTION_AUTH_ERROR, reasons);
					}
				}
				else{
					Log.d("CUAuthService","req name:"+req.name());
					//YiLog.getInstance().e("%s : failed", req.name());
					//mReAuthManager.start();
					Map<String,String> reasons= new HashMap<String,String>();
					reasons.put("reason", "用户名或密码错误");
					reasons.put("errorCode", Integer.toString(errorCode));
					broadcast(ACTION_AUTH_ERROR, reasons);
				}
				
				
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
	
	public void startNormalPlatformAuth() {
		mServiceBinder.execute(new NormalPlatformAuthRunnable());
	}

	public void startAuth(long delay) {
		mServiceBinder.executeDelayed(new AuthRunnable(), delay);
	}

	public void reHeartBeat() {
		mReHeartBeatManager.start();
	}

	public void startHeartBeat(boolean immediately) {
		if (immediately) {
			mServiceBinder.execute(new AaaHeartBeatRunnable());
		} else {
			mServiceBinder.executeDelayed(new AaaHeartBeatRunnable(),
					FIRST_HEART_BEAT_TIME * ONE_MINUTE);
		}
	}

	@Override
	public void onHttpResponse(YiHttpResponse response) {
		try {
			
			String html = response.getResponse();
			 LogUtils.error("返回结果html--》:"+html);
			boolean errFlag = true;
			int errorCode = 0;

			HttpReq req = HttpReq.eval(response.getUid());
			// LogUtils.error(req+"  "+"返回数据 : "+html);
			// LogUtils.error("要发起的请求:"+req );
			if (req != null) {
				switch (req) {
				case HTTP_AUTH: {
					LogUtils.debug("HTTP_AUTH");
					if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
						if (html != null 
								&& html.contains("doLogin()")
								&& html.contains("CUSetConfig")
								&& html.contains("authform")) {
							Pattern pattern = Pattern.compile("<form\\s*action=\"([^\"]+)\"\\s*name=\"authform\"\\s*method=\"post\">");
							Matcher m = pattern.matcher(html);
							if (m.find()) {
								Header[] headers2 = response.getHeaders2();
								if (headers2 != null) {
									for (Header header : headers2) {
										if ("Set-Cookie".equals(header.getName())) {
											Pattern cookie = Pattern.compile("([^=]+)=([^;]+);");
											Matcher m2 = cookie.matcher(header.getValue());
											while (m2.find()) {
												String name = m2.group(1);
												String value = m2.group(2);
												mParams.put(name, value);
												if ("EPGADDR".equals(name)) {
													LogUtils.debug("value :"+ value);
													mAuthBaseUrl = value.replaceAll("\"", "") + "/EPG/jsp/";
													LogUtils.info("提取到的服务器基本注册地址:"+mAuthBaseUrl);
												}
												LogUtils.debug("get cookie name:"+name+": value:"+ value);
											}
										}
									}
								}
								
								
								YiHttpRequest request = new YiHttpRequest(mAuthBaseUrl + m.group(1),YiHttpRequestMode.MODE_POST);
								//LogUtils.info("发起请求:" +mAuthBaseUrl + m.group(1));
								String session = null;
								Header[] headers = response.getHeaders();
								for (Header header : headers) {
									if ("Set-Cookie".equals(header.getName())) {
										Pattern cookie = Pattern.compile("([^=]+)=([^;]+);");
										Matcher m2 = cookie.matcher(header.getValue());
										while (m2.find()) {
											session = m2.group(2);
											mParams.put(m2.group(1), session);
											YiLog.getInstance().i("get session : %s", session);
										}
									}
								}
								if (session != null) {
									BasicHeader basicHeader = new BasicHeader(
											"Cookie", 
											"JSESSIONID="
													+ mParams.get("JSESSIONID")
													+ ";");
									YiLog.getInstance().i("set-cookie %s", basicHeader.getValue());
									Header[] headers1 = new Header[] { basicHeader };
									request.setHeaders(headers1);
								}

								request.setUid(HttpReq.HTTP_AUTO_LOGIN.getCode());
								request.setTimeOut(HTTP_TIME_OUT);
								mServiceBinder.execute(request, this);

								errFlag = false;
							}
						}
					}else{
						LogUtils.error("返回错误：" +response.getErrorCode());
						errorCode = response.getErrorCode();
					}
					break;
				}
				case HTTP_AUTO_LOGIN: {
					LogUtils.debug("HTTP_AUTO_LOGIN");
					if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
						if (html != null 
								&& html.contains("DoAuth()")
								&& html.contains("CUSetConfig")
								&& html.contains("authform")) {
							Pattern pattern = Pattern.compile("var\\s*EncryptToken\\s*=\\s*\"([^\"]+)\";");
							Matcher m = pattern.matcher(html);
							if (m.find()) {
								Pattern pattern1 = Pattern.compile("<form\\s*action=\"([^\"]+)\"\\s*name=\"authform\"\\s*method=\"post\">");
								Matcher m1 = pattern1.matcher(html);
								if (m1.find()) {
									LogUtils.debug("提取的参数"+m1.group(1));
									YiHttpRequest request = new YiHttpRequest(
											mAuthBaseUrl + m1.group(1),
											YiHttpRequestMode.MODE_POST);
									request.addParam("UserID", mUserId);
									request.addParam("UserPwd", "");
									request.addParam("Lang", mLang);
									request.addParam("NetUserID", "");

									String userToken = m.group(1);
									this.mUserToken=userToken;
									mParams.put("userToken", userToken);
									request.addParam("Authenticator", 
											AuthUtils.CTCGetAuthInfo(userToken, mUserId, mPasswd));
									request.addParam("STBType", "EC2108B_pub");
									request.addParam("STBVersion", "SBox8600");
									request.addParam("conntype", "3");
									request.addParam("STBID", getSTBID());
									request.addParam("SupportHD", "1");
									request.addParam("templateName", "defaultnew1");
									request.addParam("areaId", "1111");
									request.addParam("userToken", userToken);
									request.addParam("userGroupId", "1");
									request.addParam("productPackageId", "HDPackage");
									String mac = URLEncoder.encode(AuthUtils.getMacAddress(), HTTP.UTF_8);
									YiLog.getInstance().i("mac : %s", mac);
									request.addParam("mac", mac);
									request.addParam("isfirst", "0");

									Header[] headers = new Header[] { new BasicHeader(
											"Cookie", 
											"JSESSIONID="
													+ mParams.get("JSESSIONID")
													+ ";") };
									request.setHeaders(headers);

									request.setUid(HttpReq.HTTP_UPLOADAUTHINFO.getCode());
									request.setTimeOut(HTTP_TIME_OUT);
									mServiceBinder.execute(request, this);

									errFlag = false;
								}
							}
						}
					}
					break;
				}
				case HTTP_UPLOADAUTHINFO: {
					LogUtils.debug("HTTP_UPLOADAUTHINFO");
					if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
						if (html != null 
								&& html.contains("AuthFinish()")
								&& html.contains("UserToken")
								&& html.contains("EPGDomain")) {
							LogUtils.debug("都包含，需要进一步分析");
							Pattern pattern = Pattern.compile("Authentication\\.CUSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
							Matcher m = pattern.matcher(html);
							while (m.find()) {
								String key = m.group(1);
								String value = m.group(2);
								if (key != null && value != null) {
									mParams.put(key, value);
								}
							}

							Pattern pattern1 = Pattern.compile("var\\s*_nextURL\\s*=\\s*\"([^\"]+)\";");
							Matcher m1 = pattern1.matcher(html);
							if (m1.find()) {
								LogUtils.debug("分析出来的url："+m1.group(1));
								
								String url = m1.group(1);
								
								if(url.contains("ERROR_TYPE") || url.contains("ERROR_ID")){
									LogUtils.debug("包含");
									//在请求一次
//									LogUtils.error(">>>>>>在请求一次");
									YiHttpRequest errorRequest = new YiHttpRequest(mAuthBaseUrl + m1.group(1),YiHttpRequestMode.MODE_GET);
//									LogUtils.error("URL>>>>>>"+	mAuthBaseUrl + m1.group(1));
								
									errorRequest.setUid(HttpReq.HTTP_AUTH.getCode());
									errorRequest.setTimeOut(HTTP_TIME_OUT);

									YiHttpResponse errorResponse = new YiHttpResponse(errorRequest.getUrl());
									executeHttpGet(errorRequest, errorResponse);
									html = errorResponse.getResponse();
									
									String errorCode1 = "";
									String errorInfo = "";

									 m1 = Pattern.compile("<span.*[noticecontent1]*?>([\\s\\S]*)</span>").matcher(html);
									if (m1.find()) {
										errorCode1 = m1.group(1).replaceAll("&nbsp;", "");
									}
									errorInfo = html.substring(html.lastIndexOf("noticecontent2") + "noticecontent2".length() + 2);
									errorInfo = errorInfo.substring(0, errorInfo.indexOf("</div>"));
									Map<String,String> reasons= new HashMap<String,String>();
									reasons.put("reason",errorInfo);
									reasons.put("errorCode",errorCode1);
									broadcast(ACTION_AUTH_ERROR, reasons);
									return;
								}else{
									YiHttpRequest request = new YiHttpRequest(
											mAuthBaseUrl + m1.group(1),
											YiHttpRequestMode.MODE_GET);
									request.addParam("tempKey", "92DEB1ACDDE8DF9EADCB37155A2A1170");
									request.setUid(HttpReq.HTTP_GET_CHANNEL_LIST.getCode());
									request.setTimeOut(HTTP_TIME_OUT);
									Header[] headers = new Header[] { new BasicHeader(
											"Cookie", 
											"JSESSIONID="
													+ mParams.get("JSESSIONID")
													+ ";") };
									request.setHeaders(headers);
									mServiceBinder.execute(request, this);
									errFlag = false;
								}
							}
						}else{
							LogUtils.error("没有包括所有数据关键项目");
						}
					}else{
						Log.d("CUAuthService","错误 "+ response.getErrorCode()+"  "+response.getHeaders().toString());
					}
					break;
				}
				case HTTP_GET_CHANNEL_LIST: {
					if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
						if (html != null 
								&& html.contains("CUSetConfig")
								&& html.contains("ChannelCount")) {
							Pattern pattern = Pattern.compile("Authentication\\.CUSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
							
							Matcher m = pattern.matcher(html);
							while (m.find()) {
								String key = m.group(1);
								String value = m.group(2);
								if (key != null && value != null) {
									if ("Channel".equals(key)) {
										try {
											//Log.d("频道信息：", "value:"+value);
											value = value+",Platform=\""+EnumType.Platform.createPlatform(TvApplication.platform)+"\"";
											LiveChannel channel = new LiveChannel(value);
											if (channel != null) {
												//Log.d("channel ID:", channel.ChannelID+""+"; name:"+channel.ChannelName+"； userchannelID:"+channel.UserChannelID);
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
							
							Pattern pattern1 = Pattern.compile("document.location=\\s*'([^']+)';");
							Matcher m1 = pattern1.matcher(html);
							if (m1.find()) {
								YiHttpRequest request = new YiHttpRequest(mAuthBaseUrl + m1.group(1),YiHttpRequestMode.MODE_GET);
								request.setUid(HttpReq.HTTP_GET_SERVICE_LIST.getCode());
								request.setTimeOut(HTTP_TIME_OUT);
								Header[] headers = new Header[] { new BasicHeader("Cookie", "JSESSIONID="+ mParams.get("JSESSIONID")+ ";") };
								request.setHeaders(headers);
								mServiceBinder.execute(request, this);
								errFlag = false;
							}
						}
					}else{
						Log.d("CUAuthService","错误");
					}
					break;
				}
				case HTTP_GET_SERVICE_LIST: {
					if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
						if (html != null 
								&& html.contains("Authentication")
								&& html.contains("CUSetConfig")) {
							Pattern pattern = Pattern.compile("Authentication\\.CUSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
							Matcher m = pattern.matcher(html);
							while (m.find()) {
								String key = m.group(1);
								String value = m.group(2);
								if (key != null && value != null) {
									if ("ServiceEntry".equals(key)) {
										try {
											ServiceEntry serviceEntry = new ServiceEntry(value);
											if (serviceEntry != null) {
												mServiceEntries.add(serviceEntry);
											}
										} catch (Exception e) {
											YiLog.getInstance().i("parse serviceEntry failed %s", e.getMessage());
										}
									} else {
										mParams.put(key, value);
									}
								}
							}

							Pattern pattern1 = Pattern.compile("document.location\\s*=\\s*'([^']+)'\\+'&STBType='\\+STBType\\+'&STBVersion='\\+STBVersion;");
							Matcher m1 = pattern1.matcher(html);
							if (m1.find()) {
								YiHttpRequest request = new YiHttpRequest(
										mAuthBaseUrl.replaceAll("/EPG/jsp/", "") + m1.group(1),
										YiHttpRequestMode.MODE_GET);
								request.addParam("STBType", "EC2108B_pub");
								request.addParam("STBVersion", "SBox8600");
								request.setUid(HttpReq.HTTP_GO_EPG_AUTH.getCode());
								
								Header[] headers = new Header[] { new BasicHeader("Cookie", "JSESSIONID="+ mParams.get("JSESSIONID")+ ";") };
								request.setHeaders(headers);
								
								request.setTimeOut(HTTP_TIME_OUT);
								mServiceBinder.execute(request, this);

								errFlag = false;
							}
						}
					}
					break;
				}
				case HTTP_GO_EPG_AUTH: {
					if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
						if (html != null 
								&& html.contains("PreDealHWCU")
								&& html.contains("DirectPlay")) {
							broadcast(ACTION_AUTH_SUCCEED, null);
							
							//mServiceBinder.executeDelayed(new HeartBeatRunnable(),FIRST_HEART_BEAT_TIME * ONE_MINUTE);
							// mServiceBinder.executeDelayed(new HeartBeatRunnable(),ONE_MINUTE);
							
							mServiceBinder.executeDelayed(new AaaHeartBeatRunnable(),ONE_MINUTE);
							TvApplication.shouldLoginDate = Calendar.getInstance();
							TvApplication.shouldLoginDate.add(Calendar.MINUTE, 10);
							
							TvApplication.shouldLoginIptvDate = Calendar.getInstance();
							TvApplication.shouldLoginIptvDate.add(Calendar.MINUTE, 20);
							
							errFlag = false;
						}
					}
					break;
				}
				case HTTP_HEART_BEAT: {
					if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
						LogUtils.debug("成功，心跳返回数据"+response.getResponse());
						//mServiceBinder.executeDelayed(new HeartBeatRunnable(),HEART_BEAT_TIME * ONE_MINUTE);
						
						TvApplication.shouldLoginDate = Calendar.getInstance();
						TvApplication.shouldLoginDate.add(Calendar.MINUTE, 13);
						
						mServiceBinder.executeDelayed(new AaaHeartBeatRunnable(),HEART_BEAT_TIME *ONE_MINUTE);
						errFlag = false;
					} else {
						LogUtils.info("心跳返回失败");
						//mReHeartBeatManager.start();
					}
					break;
				}
				default:
					Log.d("CUAuthService", "无匹配信息到default");
					break;
				}
			}
			
			onError(response.getUid(), errFlag, errorCode);
		} catch (Exception e) {
			YiLog.getInstance().e(e, "error");
		}
	}

	protected class AuthRunnable implements Runnable {
		@Override
		public void run() {
			mIsAuthed = false;
			if (isNetConnected()) {
				LogUtils.info("mAuthUrl:" + mAuthUrl);
				YiHttpRequest request = new YiHttpRequest(mAuthUrl, YiHttpRequestMode.MODE_POST);
				request.addParam("UserID", mUserId);
				request.addParam("Action", "Login");
				request.setUid(HttpReq.HTTP_AUTH.getCode());
				request.setTimeOut(HTTP_TIME_OUT);

				YiHttpResponse response = new YiHttpResponse(request.getUrl());
				response.setResponseId(request.getRequestId());
				response.setUid(request.getUid());
				
				executeHttpGet(request, response);
				onHttpResponse(response);

				broadcast(ACTION_AUTH_START, null);
			} else {
				mReAuthManager.start();
			}
		}
	}
	
	//正式平台的开始注册
	protected class NormalPlatformAuthRunnable implements Runnable {
		@Override
		public void run() {
			mIsAuthed = false;
			if (isNetConnected()) {
				LogUtils.info("normal platform mPreAuthUrl:" + mPreAuthUrl);
				YiHttpRequest request = new YiHttpRequest(mPreAuthUrl, YiHttpRequestMode.MODE_GET);
				request.addParam("UserID", mUserId);
				request.addParam("Action", "Login");
				request.setUid(HttpReq.HTTP_AUTH.getCode());
				request.setTimeOut(HTTP_TIME_OUT);

				YiHttpResponse response = new YiHttpResponse(request.getUrl());
				response.setResponseId(request.getRequestId());
				response.setUid(request.getUid());
				LogUtils.info("开始执行正式平台的注册");
				executeHttpGet(request, response);
				LogUtils.info("正式平台执行完毕。");
				onHttpResponse(response);

				broadcast(ACTION_AUTH_START, null);
			} else {
				mReAuthManager.start();
			}
		}
	}
	
	
	// 正式平台的二次发起。
	public void resend(String mAuthUrl){
		
		YiHttpRequest request = new YiHttpRequest(mAuthUrl, YiHttpRequestMode.MODE_POST);
		request.addParam("UserID", mUserId);
		request.addParam("Action", "Login");
		request.setUid(HttpReq.HTTP_AUTH.getCode());
		request.setTimeOut(HTTP_TIME_OUT);

		YiHttpResponse response = new YiHttpResponse(request.getUrl());
		response.setResponseId(request.getRequestId());
		response.setUid(request.getUid());

		executeHttpGet(request, response);
		onHttpResponse(response);

		broadcast(ACTION_AUTH_START, null);
	}

	// use apache http client.
	protected static void executeHttpGet(YiHttpRequest request,
			final YiHttpResponse yiHttpResponse) {
		String result = null;
		BufferedReader reader = null;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			//纠正错误  Invalid cookie header: "Set-Cookie: EPGADDR="http://10.7.1.36:33200"; Version=1; Max-Age=86400; Expires=Sun, 21-Jun-2015 03:31:36 GMT". Unable to parse expires attribute: Sun
			HttpClientParams.setCookiePolicy(client.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);   
			
			int timeOut = 60000;
			if (request.getTimeOut() > 0) {
				timeOut = request.getTimeOut();
			}
			client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeOut);
			client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 60000);
			client.setRedirectHandler(new DefaultRedirectHandler() {
				@Override
				public boolean isRedirectRequested(HttpResponse response,
						HttpContext context) {
					LogUtils.debug("isRedirectRequested "+super.isRedirectRequested(response, context));
					return super.isRedirectRequested(response, context);
				}

				@Override
				public URI getLocationURI(HttpResponse response,
						HttpContext context) throws ProtocolException {
					LogUtils.debug("getLocationURI");
					yiHttpResponse.setHeaders2(response.getAllHeaders());
					Header[] location = response.getHeaders("Location");
					if (location != null && location.length > 0) {
						LogUtils.debug("get location : "+ location[0].getValue());
					}
					return super.getLocationURI(response, context);
				}
			});
			
			HttpGet httpGet = new HttpGet();
			String url = request.getUrl();
			if (request.getParams() != null) {
				List<NameValuePair> params = request.getParams();
				for (int i = 0; i < params.size(); i++) {
					NameValuePair pair = params.get(i);
					if (i == 0) {
						url += "?" + pair.getName() + "=" + pair.getValue();
					} else {
						url += "&" + pair.getName() + "=" + pair.getValue();
					}
				}
			}
			httpGet.setURI(new URI(url));
			
			Header[] headers = request.getHeaders();
			if (headers != null) {
				for (Header header : headers) {
					httpGet.addHeader(header);
				}
			}

			HttpResponse httpResponse = client.execute(httpGet);
			/*LogUtils.info("222222222");
			try {
				Thread.sleep(2000000000);
			}catch (Exception ex) {
				
			}*/
			
			
			yiHttpResponse.setHeaders(httpResponse.getAllHeaders());
			
			LogUtils.debug("Response charset:"+request.getResponseCharset());
			reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent(), request.getResponseCharset()));

			StringBuffer strBuffer = new StringBuffer("");
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
			yiHttpResponse.setResponse(result);
		} catch (ConnectTimeoutException timeoutException) {
			yiHttpResponse.setErrorCode(YiHttpResponse.ERROR_TIME_OUT);
			YiLog.getInstance().e(timeoutException, "executeHttpGet error:");
		} catch (Exception e) {
			yiHttpResponse.setErrorCode(YiHttpResponse.ERROR_UNKNOWN);
			YiLog.getInstance().e(e, "executeHttpGet error:");
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					YiLog.getInstance().e(e, "executeHttpGet close reader error:");
				}
			}
		}
	}

	/*protected class HeartBeatRunnable implements Runnable {
		@Override
		public void run() {
			LogUtils.info("心跳开始");
			YiHttpRequest request = new YiHttpRequest(String.format(
					"%s%s", mAuthBaseUrl,"gdgaoqing/en/ut/hw_heartbeat.jsp"),YiHttpRequestMode.MODE_GET);
			
			YiHttpRequest request = new YiHttpRequest(String.format(
					"%s%s", getEPGServerIP(),
					"/EPG/jsp/gdgaoqing/en/ut/hw_heartbeat.jsp"),
					YiHttpRequestMode.MODE_GET);
			LogUtils.info("心跳地址："+request.getUrl());

			String session = mParams.get("JSESSIONID");
			if (session != null) {
				Header[] headers1 = new Header[] { new BasicHeader("Cookie",
						"JSESSIONID=" + session) };
				request.setHeaders(headers1);
			}

			request.setUid(HttpReq.HTTP_HEART_BEAT.getCode());
			request.setTimeOut(HTTP_TIME_OUT);
			mServiceBinder.execute(request, CUAuthService.this);
		}
	}*/
	
	protected class AaaHeartBeatRunnable implements Runnable {
		@Override
		public void run() {
			LogUtils.info("3A心跳开始：应登陆时间" +TvApplication.shouldLoginDate.getTime() + "  当前时间："+Calendar.getInstance().getTime());
			
			if(TvApplication.shouldLoginDate.after(Calendar.getInstance())){
				YiHttpRequest request = new YiHttpRequest(String.format("%s%s", mAuthBaseUrl,"GetHeartBit"),YiHttpRequestMode.MODE_GET);
				
				request.addParam("UserStatus", 1);
				request.addParam("ChannelVer", "");
				request.addParam("STBID", TvApplication.stbId);
				request.addParam("STBType", "EC2106V1B_pub");
				request.addParam("Version", "SBox8000C");
				request.addParam("Usertoken", mUserToken);

				String session = mParams.get("JSESSIONID");
				if (session != null) {
					Header[] headers1 = new Header[] { new BasicHeader("Cookie","JSESSIONID=" + session) };
					request.setHeaders(headers1);
				}

				request.setUid(HttpReq.HTTP_HEART_BEAT.getCode());
				request.setTimeOut(HTTP_TIME_OUT);
				mServiceBinder.execute(request, CUAuthService.this);
			}else {
				LogUtils.error("心跳过时，应该发起注册，目前ntp消息有bug");
				broadcast(ACTION_START_AUTH, null); 
			}
			
			LogUtils.error("测试3A注册");
			if(TvApplication.shouldLoginIptvDate.after(Calendar.getInstance())){
				LogUtils.error("3A,未到期，不处理");
			}else{
				LogUtils.error("心跳过时，应该发起注册，目前ntp消息有bug");
				// broadcast(ACTION_START_AUTH, null); 
			}
			
		}
	}
}
