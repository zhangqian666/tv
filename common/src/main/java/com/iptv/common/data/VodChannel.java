package com.iptv.common.data;

import java.io.Serializable;

/**
 * 点播分类
 */
public class VodChannel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -499504994600293479L;

	/*
	 	VODID ;//String 节目编号
		VODNAME String 节目名称
		PICPATH String 影片海报路径
		DEFINITION Integer 高清标识		1：高清；2：标清；3：超高清
	 */

	public String VODID;
	public EnumType.ContentType content_type;
	public String VODNAME;
	public String PICPATH;
	public int DEFINITION;
	public String CONTENTCODE;//内容code
	public int nNumber;
	//节目名称长度
	public int programnamelen;
	//节目类型， 1：普通VOD节目，8：卡拉OK，9：广告，10: 连续剧的单集节目，14：连续剧剧头，15：专辑剧头，  16：专题单集节目，19：TVOD归档节目
	public String programtype;
	//	内容code,连续剧剧头时为剧头code,专辑剧头时为空
	public String seriesprogramcode;
	//是否推荐，0：否，1：是//备注：
	//由metadata元数据下发，非推荐数据
	public int isrecommend;
	//是否作为热点，0：否，1：是
	//备注：
	//由metadata元数据下发，非热度数据
	public int ishot;
	//是否作为首页海报，0：否，1：是
	public int isfirstpage;
	//文广的栏目code
	public String catagorycode;
	//业务运营商code
	public String bocode;
	//搜索关键字(名称首字母组合)
	public String programsearchkey;
	//内容的媒体服务类型集合，见通用注释说明
	public int mediaservices;
	//内容分级唯一标识，见通用注释说明 
	public int ratingid;
	//推荐级别唯一标识
	public int recommendid;
	//排序序列值
	public int sortnum;
	//节目价格，单位：分
	public int price;
	//即将上线时间，时间格式：YYYY.MM.DD HH:MM:SS
	public String enabledtime;
	//即将下线时间，时间格式：YYYY.MM.DD HH:MM:SS
	public String disabledtime;
	//上线时间，时间格式：YYYY.MM.DD HH:MM:SS
	public String onlinetime;
	//下线时间，时间格式：YYYY.MM.DD HH:MM:SS
	public String offlinetime;
	//节目更新时间，时间格式：YYYY.MM.DD HH:MM:SS
	public String updatetime;
	//节目创建时间，时间格式：YYYY.MM.DD HH:MM:SS
	public String createtime;
	//地区描述
	public String countryname;
	//简单片花，0：否 1：是
	public String issimpletrailer;
	//节目外部Code，如电信code
	public String telecomcode;
	//媒体提供商Code
	public String mediacode;
	//简单片花开始时间，时间格式HH:MM:SS
	public String trailerbegintime;
	//简单片花结束时间，时间格式HH:MM:SS
	public String trailerendtime;
	//连续剧的集数，非连续剧缺省为1
	public int seriesnum;
	//节目海报名称列表，此字段包含12张海报，用‘;‘分割。海报排列顺序为IPTV、MVS-Mobile、PC、MVS-Tablets四种终端，每屏3张海报，分别为普通海报、缩略图和剧照，poster1-poster12字段为分割后并加上相对路径的海报列表
	public String posterfilelist;
	//文广节目的默认类别
	public String wggenre;
	//文广的关键字
	public String wgkeywords;
	//文广的关联标签
	public String wgtags;
	//描述信息
	public String description;
	//导演姓名列表，以‘;‘分割
	public String director;
	//导演姓名首字母列表，以‘;‘分割
	public String directorsearchkey;
	//主要演员姓名列表，以‘;‘分割
	public String actor	;
	//主要演员姓名首字母列表，以‘;‘分割
	public String actorsearchkey;
	//内容提供商code
	public String cpcode;
	//内容提供商名称
	public String cpname;
	//所属栏目Code
	public String columncode;
	//分类名称
	public String catalogname;
	//内容描述关键字
	public String descriptionkey;
	//内容是否支持广告，0：否，1：是
	public String advertisecontent;
	//发布日期描述
	public String releasedate;
	//作者列表，以‘;‘分割
	public String writer;
	//String 音频语言信息列表，以‘;‘分割
	public String audiolang;
	//字幕语言信息列表，以‘;‘分割
	public String subtitlelang;
	//出品公司
	public String pubcompany;
	//详细描述信息，可选字段
	public String detaildescribed;
	//电视剧季数
	public String seriesseason;
	//内容风格类型，如科幻片、动作片等
	public String genre; 
	//牌照有效期，时间格式：YYYY.MM.DD HH:MM:SS
	public String licenseperiod;
	//归档节目所属频道code
	public String channelcode;
	//子风格类型，如科幻片、动作片等
	public String subgenre;
	//看点（简短的剧情描述）
	public String shortdesc;
	//副标题
	public String shorttitle;
	//星级，取值范围为0-10，一星值是2，半星值是1
	public String starlevel;
	//节目片长,格式HH:MI:SS
	public String elapsedtime;
	//海报1相对路径
	public String poster1;
	//海报1相对路径
	public String poster2;
	//海报1相对路径
	public String poster3;
	//海报1相对路径
	public String poster4;
	//海报1相对路径
	public String poster5;
	//海报1相对路径
	public String poster6;
	//海报1相对路径
	public String poster7;
	//海报1相对路径
	public String poster8;
	//海报1相对路径
	public String poster9;
	//海报1相对路径
	public String poster10;
	//海报1相对路径
	public String poster11;
	//海报1相对路径
	public String poster12;
	//分类信息类型 
	public int catalogtype;
	//是否支持时移(startover) ，0：否，1：是
	public int istimeshift;
	//是否支持归档，0：否，1：是
	public int isarchivemode;
	//是否支持拷贝保护，0：否，1：是
	public int isprotection;
	//startover支持的操作模式采用按位与的方式
	public int timeshiftmode;
	//归档模式
	public int archivemode;
	//拷贝保护标识
	public String copyprotection;
	//默认音频语言 取值：eng、zho、chi
	public String language;
	//视频文件的格式AV_ClearTS
	public String format;
	//视频高宽比
	public String aspect;
	//Dolby杜比特性
	public String dolby;
	//连续剧单集的主题词
	public String episodetitle;
	//内容警告信息
	public String parentaladvisory;
	//内容码率(展示字段，具体值在实体中)，由局方内容metadata下发，用于非三屏局点，默认值为标清码率2000，单位kbps
	public int bitrate;
	//排序名称（顺序为特殊字符、数字、字母不关心大小写）
	public String sortname;//sort	
	
	public EnumType.Platform platform;

	public VodChannel()
	{

	}

	public VodChannel(String VODID)
	{
		this.VODID = VODID;
	}

}
