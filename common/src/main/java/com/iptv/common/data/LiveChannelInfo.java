package com.iptv.common.data;

import java.util.ArrayList;
import java.util.HashMap;

public class LiveChannelInfo extends LiveChannel {
	/**
	*/
	public int TVODSTATUS;
	public int PAUSELENGTH;
	public int ISSUBSCRIBED;
	public int ISTVOD;
	public int ISPLTV;
	public int LIVESTATUS;
	public int isSubscribe;
	
	public String VIDEOTYPE;		// H.264
	public int STORELENGTH;
	public String INTRODUCTION;
	public int PLTVSTATUS;
	public int CHANNELINDEX;
	
	public ArrayList<String> AREAID;
	public String CODE;
	public int SERVICEPORT;
	public String PICTURE;
	public ArrayList<String> SERVICEID;
	public String SERVICEIP;
	public HashMap<Integer, ArrayList<String>> POSTERPATHS;
	public String INTRODUCE;
	public int ISCPVR;
	public int NPVRSTATUS;
	public int ISNPVR;
	public int DEFINITION;
	public String MEDIAID;
	public int RECORDLENGTH;
	public int RATINGID;
	public int ISNVOD;
	public int NVODSTATUS;
	
	public LiveChannelInfo()
	{
		AREAID = new ArrayList<String>();
		SERVICEID = new ArrayList<String>();
		POSTERPATHS = new HashMap<Integer, ArrayList<String>>();
	}
	
}
