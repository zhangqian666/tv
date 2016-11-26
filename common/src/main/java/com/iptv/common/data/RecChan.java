package com.iptv.common.data;

import java.io.Serializable;
import java.util.ArrayList;

public class RecChan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5486928075121408448L;
	
	public int CHANNELID;
	public String CHANNELIDZTE;
	public String CHANNELINDEX;
	public String CHANNELNAME;
	public int RECORDLENGTH;
	public int DEFINITION;
	
	public ArrayList<RecBill> lstRecBill;
	
	public RecChan()
	{
		lstRecBill = new ArrayList<RecBill>();
	}
	
}
