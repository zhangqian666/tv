package com.iptv.common.data;

import java.io.Serializable;

/**
 * 回看频道节目单
 */
public class RecBill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6043928587537677072L;
	
	/*
	 	title 节目单名称节目单的名称
	 	programId	节目ID 节目编号
	 	beginDate 	节目单开始日期格式为“YYYY-MM-DD”
		beginTime 	节目单开始时间格式为“hh：mm：ss”
		endTime 	节目单结束时间格式为“hh：mm：ss”
		recStatus	录播节目单状态	0：未录制；1：录制成功；2：录制失败
	 */
	public String title;
	public int programId;
	public String programIdZte;
	public String beginTime;
	public String endTime;
	public String beginDate;
	public int recStatus;
	
}
