package com.iptv.rocky.auth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import com.ikantech.support.net.YiHttpRequest;
import com.ikantech.support.net.YiHttpRequest.YiHttpRequestMode;
import com.ikantech.support.net.YiHttpResponse;
import com.ikantech.support.utils.YiLog;
import com.iptv.common.data.LiveChannel;

// UT
public class CTCAuthService extends AuthService {
	private static final int FIRST_HEART_BEAT_TIME = 2;
	private static final int HEART_BEAT_TIME = 15;

	private enum HttpReq {
		HTTP_AUTH(0x01), HTTP_UPLOADAUTHINFO(0x02), HTTP_GET_CHANNEL_LIST(0x03), 
		HTTP_GET_SERVICE_LIST(0x04), HTTP_GO_EPG_AUTH(0x05), HTTP_HEART_BEAT(0x06);
		
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

		YiLog.getInstance().i("CTCAuthService onCreate");
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
		// TODO Auto-generated method stub
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

		YiLog.getInstance().i("response : %s", html);

		boolean errFlag = true;

		HttpReq req = HttpReq.eval(response.getUid());
		if (req != null) {
			switch (req) {
			case HTTP_AUTH: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					if (html != null 
							&& html.contains("DoAuth()")
							&& html.contains("CTCGetAuthInfo")
							&& html.contains("AuthenticateForm")) {
						Pattern pattern = Pattern.compile("Authentication\\.CTCGetAuthInfo\\(\'(\\w+)\'\\);");
						Matcher m = pattern.matcher(html);
						if (m.find()) {
							Pattern pattern1 = Pattern.compile("<form name=\"AuthenticateForm\" method=\"post\" action=\"([^\"]+)\">");
							Matcher m1 = pattern1.matcher(html);
							if (m1.find()) {
								YiHttpRequest request = new YiHttpRequest(
										mAuthBaseUrl + m1.group(1),
										YiHttpRequestMode.MODE_POST);
								request.addParam("UserID", mUserId);
								request.addParam("authenticator", 
										AuthUtils.CTCGetAuthInfo(m.group(1), mUserId, mPasswd));
								request.addParam("SupportHD", "1");

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
													+ "; commondPrefix="
													+ mParams.get("commondPrefix"));
									YiLog.getInstance().i("set-cookie %s", basicHeader.getValue());
									Header[] headers1 = new Header[] { basicHeader };
									request.setHeaders(headers1);
								}

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
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					if (html != null 
							&& html.contains("CTCSetConfig")
							&& html.contains("UserToken")
							&& html.contains("EPGDomain")) {
						Pattern pattern = Pattern.compile("Authentication\\.CTCSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
						Matcher m = pattern.matcher(html);
						while (m.find()) {
							String key = m.group(1);
							String value = m.group(2);
							if (key != null && value != null) {
								mParams.put(key, value);
							}
						}

						Pattern pattern1 = Pattern.compile("document.location=\\s*'([^']+)';");
						Matcher m1 = pattern1.matcher(html);
						if (m1.find()) {
							YiHttpRequest request = new YiHttpRequest(
									mAuthBaseUrl + m1.group(1),
									YiHttpRequestMode.MODE_GET);
							request.setUid(HttpReq.HTTP_GET_CHANNEL_LIST.getCode());
							request.setTimeOut(HTTP_TIME_OUT);
							Header[] headers = new Header[] { new BasicHeader(
									"Cookie", 
									"JSESSIONID="
											+ mParams.get("JSESSIONID")
											+ "; commondPrefix="
											+ mParams.get("commondPrefix")
											+ "; UserToken="
											+ mParams.get("UserToken")) };
							request.setHeaders(headers);
							mServiceBinder.execute(request, this);
							errFlag = false;
						}
					}
				}
				break;
			}
			case HTTP_GET_CHANNEL_LIST: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					if (html != null 
							&& html.contains("CTCSetConfig")
							&& html.contains("ConfigChannel")) {
						Pattern pattern = Pattern.compile("Authentication\\.CTCSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
						Matcher m = pattern.matcher(html);
						while (m.find()) {
							String key = m.group(1);
							String value = m.group(2);
							if (key != null && value != null) {
								if ("Channel".equals(key)) {
									try {
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

						Pattern pattern1 = Pattern.compile("document.location=\\s*'([^']+)';");
						Matcher m1 = pattern1.matcher(html);
						if (m1.find()) {
							YiHttpRequest request = new YiHttpRequest(
									mAuthBaseUrl + m1.group(1),
									YiHttpRequestMode.MODE_GET);
							request.setUid(HttpReq.HTTP_GET_SERVICE_LIST.getCode());
							request.setTimeOut(HTTP_TIME_OUT);
							Header[] headers = new Header[] { new BasicHeader(
									"Cookie", 
									"JSESSIONID="
											+ mParams.get("JSESSIONID")
											+ "; commondPrefix="
											+ mParams.get("commondPrefix")
											+ "; UserToken="
											+ mParams.get("UserToken")) };
							request.setHeaders(headers);
							mServiceBinder.execute(request, this);
							errFlag = false;
						}
					}
				}
				break;
			}
			case HTTP_GET_SERVICE_LIST: {
				if (response.getErrorCode() == YiHttpResponse.SUCCESS) {
					if (html != null 
							&& html.contains("SetService")
							&& html.contains("ServiceEntry")) {
						Pattern pattern = Pattern.compile("Authentication\\.CTCSetConfig\\s?\\(\'([^']+)\',\'([^']+)\'\\);");
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

						Pattern pattern1 = Pattern.compile("var\\s*url\\s*=\\s*'([^']+)';");
						Matcher m1 = pattern1.matcher(html);
						if (m1.find()) {
							YiHttpRequest request = new YiHttpRequest(
									m1.group(1), YiHttpRequestMode.MODE_GET);
							request.setUid(HttpReq.HTTP_GO_EPG_AUTH.getCode());
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
					Header[] headers = response.getHeaders();
					for (Header header : headers) {
						if ("Set-Cookie".equals(header.getName())) {
							Pattern cookie = Pattern.compile("([^=]+)=([^;]+);");
							Matcher m2 = cookie.matcher(header.getValue());
							while (m2.find()) {
								String vString = m2.group(2);
								mParams.put(m2.group(1), vString);
								YiLog.getInstance().i("get %s : %s", header.getName(), vString);
							}
						}
					}

					broadcast(ACTION_AUTH_SUCCEED, null);
					errFlag = false;
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
				YiHttpRequest request = new YiHttpRequest(mAuthUrl,
						YiHttpRequestMode.MODE_GET);
				request.addParam("UserID", mUserId);
				request.addParam("Action", "Login");
				request.setUid(HttpReq.HTTP_AUTH.getCode());
				request.setTimeOut(HTTP_TIME_OUT);
				mServiceBinder.execute(request, CTCAuthService.this);
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
			mServiceBinder.execute(request, CTCAuthService.this);
		}
	}


}
