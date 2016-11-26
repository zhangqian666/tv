package com.iptv.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class VodDetailInfo extends VodChannel implements Serializable {
	
	private static final long serialVersionUID = -3634193700798649766L;
	/* DIRECTOR String 导演名字*/
	public String DIRECTOR;
	/* ASSESSID Integer 片花对应的VOD节目编号*/
	public int ASSESSID;
	/* CASTMAP  HashMap 演职人员信息，其中key值为角色值，String类型；value值为演职人员名称数组，String[]类型。
			角色值：	0：演员	1：导演	2：词曲作者	3：演唱者    6: 主演     100：其他*/
	public HashMap<Integer, ArrayList<String>> CASTMAP;
	/* SERVICEID String[] 服务编号*/
	public ArrayList<String> SERVICEID;
	/* ELAPSETIME Integer 节目片长*/
	public int ELAPSETIME;
	/* SUPVODIDSET HashSet 该点播内容绑定的父集编号的集合*/
	public HashSet<Integer> SUPVODIDSET;
	/* POSTERPATHS HashMap 多海报信息，其中key值为海报位置，为String型，value值为该位置的多张海报，value值类型为String数组类型。数组下标即为该位置海报的顺序。*/
	public HashMap<Integer, ArrayList<String>> POSTERPATHS;
	/* INTRODUCE String 节目简介*/
	public String INTRODUCE;
	/* VODPRICE String 影片价格*/
	public String VODPRICE;
	/* */
	public double price;
	/* */
	public double priceDisp;
	/* SUBVODIDLIST ArrayList 该点播内容包含的子集列表*/
	public ArrayList<VodChannel> SUBVODIDLIST;
	/* SITCOMNUM Integer 如果ISSITCOM==1是剧集时，代表总集数*/
	public int SITCOMNUM;
	/* SUBVODNUMLIST ArrayList 该点播内容包含的子集列表对应的集号列表和上面的子集是一一对应的，表明对应的子集的集号。*/
	public ArrayList<Integer> SUBVODNUMLIST;
	/* ISASSESS Integer 是否包含片花：	1：包含；0：不包含*/
	public int ISASSESS;
	/* ISSITCOM Integer 连续剧类型：	0：非连续剧父集；1：连续剧父集*/
	public int ISSITCOM;
	/* allTypeId String[] VOD所属的栏目ID列表*/
	public ArrayList<String> allTypeId;
	/* CODE String VOD的MediaCode*/
	public String CODE;
	/* FATHERVODID Integer 如果该点播内容有和父集绑定，显示该点播内容绑定的父集中的一个，如果没有绑定父集，否则显示为-1*/
	public int FATHERVODID;
	/* */
	public boolean ordered;
	
	public String authid;
	public VodDetailInfo()
	{
		CASTMAP = new HashMap<Integer, ArrayList<String>>();
		SERVICEID = new ArrayList<String>();
		SUPVODIDSET = new HashSet<Integer>();
		POSTERPATHS = new HashMap<Integer, ArrayList<String>>();
		SUBVODIDLIST = new ArrayList<VodChannel>();
		SUBVODNUMLIST = new ArrayList<Integer>();
		allTypeId = new ArrayList<String>();
	}
	
}
