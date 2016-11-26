package com.iptv.common.data;

/**
 * 酒店点播和直播的播放上传反馈结果
 * 
 *
 */
public class VodOrLivePlayRecordReportResult {

	private int result;
	private String reason;
	
	public int getResult() {
		return result;
	}
	public String getReason() {
		return reason;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
