package com.iptv.rocky.auth;

public class ReAuthManager {
	private static final int ONE_SECOND = 1000;

	private AuthService mAuthService;

	private int mAuthTimes;

	public ReAuthManager(AuthService authService) {
		mAuthService = authService;
		mAuthTimes = 0;
	}

	public void start() {
		long delay = 1000;
		switch (mAuthTimes) {
		case 0:
			delay = 30 * ONE_SECOND;
			break;
		case 1:
			delay = 60 * ONE_SECOND;
			break;
		case 2:
			delay = 120 * ONE_SECOND;
			break;
		case 3:
			delay = 180 * ONE_SECOND;
			break;
		case 4:
			delay = 240 * ONE_SECOND;
			break;
		case 5:
			delay = 180 * ONE_SECOND;
			break;
		case 6:
			delay = 120 * ONE_SECOND;
			break;
		case 7:
			delay = 60 * ONE_SECOND;
		default:
			break;
		}
		mAuthTimes++;
		if (mAuthTimes > 7) {
			mAuthTimes = 0;
		}
		mAuthService.startAuth(delay);
	}
}
