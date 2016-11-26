package com.iptv.common.data;

import java.io.Serializable;

/**
 * 点播播放地址
 */
public class VodUrl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3755837100204680987L;
	
	/*
 	<URL>
		<PROGID>100901</PROGID>
		<PLAYTYPE>1</PLAYTYPE>
		<TIME>0</TIME>
		<RTSPURL>
		rtsp://10.0.3.74/88888889/16/20140327/268741433/268741433.ts?rrsip=10.0.3.72,rrsip=10.0.3.73&icpid=SSPID&accounttype=1&limitflux=-1&limitdur=-1&accountinfo=g/SM8avLqCefLSlE9DjLopMr+6YJCileh16SXvreIxu8u9ncc2SXnuRM5NNpu+AgwnZNCeT+TId8vzdDVySSdBqRtfvCahCf1kf7LTQL34Jm6lpye1tOz040/W+Lr00pNdsTyMIaitVfg6WgBqo6vQ==:20140423120353,gdtest,172.25.41.240,20140423120353,00000001000000010000000013787942,38A11D904BF19C11862D87C743C0948D,0,1,10,,,1,0,0,,1,END
		</RTSPURL>
	</URL>
	 */
	public String RTSPURL;
	
}
