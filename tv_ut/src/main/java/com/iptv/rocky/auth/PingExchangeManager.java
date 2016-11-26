package com.iptv.rocky.auth;

public class PingExchangeManager {
	private static final int MAX_HEART_BEAT_TIME = 3;

	private AuthService mAuthService;

	private int mHeartBeatFailedTime;

	public PingExchangeManager(AuthService authService) {
		mAuthService = authService;
		mHeartBeatFailedTime = 1;
	}

	public synchronized void start() {
		mHeartBeatFailedTime++;
		if (mHeartBeatFailedTime > MAX_HEART_BEAT_TIME) {
			mHeartBeatFailedTime = 1;
			mAuthService.reAuth();
		} else {
			mAuthService.startHeartBeat(false);
		}
	}

	public synchronized void reset() {
		mHeartBeatFailedTime = 1;
	}
}
