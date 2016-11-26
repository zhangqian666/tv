package com.iptv.common.data;


/**
 * 点播二级分类
 */
public class VodSec {
	/*
	 	type_id String vod二级分类的ID 类似：10000100000000090000000000002306
		title String 名称
	 */
	public int id;
	public EnumType.LayoutType layout_type;
	public EnumType.ContentType content_type;
	public EnumType.SubContentType sub_content_type;
	public String title;
	public String type_id;
	public String bg;
	//李东东补充  SUBJECT_TYPE 子栏目类型：  0：视频 VOD  1：视频频道  2：音频频道  3：频道  4：音频 AOD  10：VOD  100：增值业务  300：节目  9999：混合栏目 
	public int subjectType;
	@Override
	public String toString() {
		return "VodSec [id=" + id + ", layout_type=" + layout_type
				+ ", content_type=" + content_type + ", sub_content_type="
				+ sub_content_type + ", title=" + title + ", type_id="
				+ type_id + ", bg=" + bg + ", subjectType=" + subjectType + "]";
	}
}
