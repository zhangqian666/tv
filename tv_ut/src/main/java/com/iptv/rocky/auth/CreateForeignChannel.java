package com.iptv.rocky.auth;

import java.util.ArrayList;
import java.util.List;

import com.iptv.common.data.LiveChannel;

/**
 * 在AAA阶段手工产生境外频道信息
 * @author Xiaoqiang
 *
 */
public class CreateForeignChannel {
	
	public List<LiveChannel> create(){
		List<LiveChannel> channels = new ArrayList<LiveChannel>();
		String channel_701 ="ChannelID=\"2701\",ChannelName=\"凤凰卫视资讯台\",UserChannelID=\"701\",ChannelURL=\"igmp://239.253.92.1:8101\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.1:8101\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel701 = new LiveChannel(channel_701);
		channels.add(channel701);
		
		String channel_702 ="ChannelID=\"2702\",ChannelName=\"凤凰卫视电影台\",UserChannelID=\"702\",ChannelURL=\"igmp://239.253.92.2:8001\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.2:8001\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel702 = new LiveChannel(channel_702);
		channels.add(channel702);
		
		String channel_703 ="ChannelID=\"2703\",ChannelName=\"凤凰卫视中文台\",UserChannelID=\"703\",ChannelURL=\"igmp://239.253.92.55:8002\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.55:8002\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel703 = new LiveChannel(channel_703);
		channels.add(channel703);
		
		String channel_704 ="ChannelID=\"2704\",ChannelName=\"TVB8\",UserChannelID=\"704\",ChannelURL=\"igmp://239.253.92.1:8101\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.1:8101\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel704 = new LiveChannel(channel_704);
		channels.add(channel704);
		
		String channel_705 ="ChannelID=\"2705\",ChannelName=\"MTV音乐电视\",UserChannelID=\"705\",ChannelURL=\"igmp://239.253.92.55:8002\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.55:8002\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel705 = new LiveChannel(channel_705);
		channels.add(channel705);
		
		String channel_706 ="ChannelID=\"2706\",ChannelName=\"[V]音乐台\",UserChannelID=\"706\",ChannelURL=\"igmp://239.253.92.1:8101\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.1:8101\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel706 = new LiveChannel(channel_706);
		channels.add(channel706);
		
		String channel_707 ="ChannelID=\"2707\",ChannelName=\"卫视体育台1\",UserChannelID=\"707\",ChannelURL=\"igmp://239.253.92.55:8002\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.55:8002\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel707 = new LiveChannel(channel_707);
		channels.add(channel707);
		
		String channel_708 ="ChannelID=\"2708\",ChannelName=\"卫视体育台2\",UserChannelID=\"708\",ChannelURL=\"igmp://239.253.92.1:8101\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.1:8101\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel708 = new LiveChannel(channel_708);
		channels.add(channel708);
		
		String channel_709 ="ChannelID=\"2709\",ChannelName=\"探索频道\",UserChannelID=\"709\",ChannelURL=\"igmp://239.253.92.55:8002\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.55:8002\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel709 = new LiveChannel(channel_709);
		channels.add(channel709);
		
		String channel_710 ="ChannelID=\"2710\",ChannelName=\"索尼动作频道\",UserChannelID=\"710\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel710 = new LiveChannel(channel_710);
		channels.add(channel710);
		
		String channel_711 ="ChannelID=\"2711\",ChannelName=\"CINEMAX电影频道\",UserChannelID=\"711\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel711 = new LiveChannel(channel_711);
		channels.add(channel711);
		
		String channel_712 ="ChannelID=\"2712\",ChannelName=\"CNN国际新闻网络\",UserChannelID=\"712\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel712 = new LiveChannel(channel_712);
		channels.add(channel712);
		
		String channel_713 ="ChannelID=\"2713\",ChannelName=\"HBO电影频道\",UserChannelID=\"713\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel713 = new LiveChannel(channel_713);
		channels.add(channel713);
		
		String channel_714 ="ChannelID=\"2714\",ChannelName=\"NHK世界收费电视频道\",UserChannelID=\"714\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel714 = new LiveChannel(channel_714);
		channels.add(channel714);
		
		String channel_715 ="ChannelID=\"2715\",ChannelName=\"韩国KBS世界频道\",UserChannelID=\"715\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel715 = new LiveChannel(channel_715);
		channels.add(channel715);
		
		String channel_716 ="ChannelID=\"2716\",ChannelName=\"法国综合娱乐频道\",UserChannelID=\"716\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel716 = new LiveChannel(channel_716);
		channels.add(channel716);
		
		String channel_717 ="ChannelID=\"2717\",ChannelName=\"财经新闻信息频道\",UserChannelID=\"717\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel717 = new LiveChannel(channel_717);
		channels.add(channel717);
		
		String channel_718 ="ChannelID=\"2718\",ChannelName=\"BBC世界台\",UserChannelID=\"718\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel718 = new LiveChannel(channel_718);
		channels.add(channel718);
		
		String channel_719 ="ChannelID=\"2719\",ChannelName=\"DIVA\",UserChannelID=\"719\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel719 = new LiveChannel(channel_719);
		channels.add(channel719);
		
		String channel_720 ="ChannelID=\"2720\",ChannelName=\"国家地理频道\",UserChannelID=\"720\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel720 = new LiveChannel(channel_720);
		channels.add(channel720);
		
		String channel_721 ="ChannelID=\"2721\",ChannelName=\"卫视国际电影台\",UserChannelID=\"721\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel721 = new LiveChannel(channel_721);
		channels.add(channel721);
		
		String channel_722 ="ChannelID=\"2722\",ChannelName=\"TVB星河频道\",UserChannelID=\"722\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel722 = new LiveChannel(channel_722);
		channels.add(channel722);
		
		String channel_723 ="ChannelID=\"2723\",ChannelName=\"天映频道\",UserChannelID=\"723\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel723 = new LiveChannel(channel_723);
		channels.add(channel723);
		
		String channel_724 ="ChannelID=\"2724\",ChannelName=\"欧亚体育新闻台\",UserChannelID=\"724\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel724 = new LiveChannel(channel_724);
		channels.add(channel724);
		
		String channel_725 ="ChannelID=\"2725\",ChannelName=\"印度ZEE亚太频道\",UserChannelID=\"725\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel725 = new LiveChannel(channel_725);
		channels.add(channel725);
		
		
		String channel_726 ="ChannelID=\"2726\",ChannelName=\"NOW\",UserChannelID=\"726\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel726 = new LiveChannel(channel_726);
		channels.add(channel726);
		
		String channel_727 ="ChannelID=\"2727\",ChannelName=\"澳亚卫视中文台\",UserChannelID=\"727\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel727 = new LiveChannel(channel_727);
		channels.add(channel727);
		
		String channel_728 ="ChannelID=\"2728\",ChannelName=\"星空卫视\",UserChannelID=\"728\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel728 = new LiveChannel(channel_728);
		channels.add(channel728);
		
		String channel_729 ="ChannelID=\"2729\",ChannelName=\"华娱卫视\",UserChannelID=\"729\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel729 = new LiveChannel(channel_729);
		channels.add(channel729);
		
		String channel_730 ="ChannelID=\"2730\",ChannelName=\"亚洲新闻台国际\",UserChannelID=\"730\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel730 = new LiveChannel(channel_730);
		channels.add(channel730);
		
		String channel_731 ="ChannelID=\"2727\",ChannelName=\"古巴视野国际频道\",UserChannelID=\"731\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel731 = new LiveChannel(channel_731);
		channels.add(channel731);
		
		String channel_732 ="ChannelID=\"2732\",ChannelName=\"俄罗斯环球\",UserChannelID=\"732\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel732 = new LiveChannel(channel_732);
		channels.add(channel732);
		
		String channel_733 ="ChannelID=\"2733\",ChannelName=\"今日俄罗斯\",UserChannelID=\"733\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel733 = new LiveChannel(channel_733);
		channels.add(channel733);
		
		String channel_734 ="ChannelID=\"2734\",ChannelName=\"彭博财经亚太频道\",UserChannelID=\"734\",ChannelURL=\"igmp://239.253.92.67:6007\",TimeShift=\"0\",TimeShiftLength=\"0\",ChannelSDP=\"igmp://239.253.92.67:6007\",TimeShiftURL=\"\",ChannelType=\"1\",IsHDChannel=\"2\",PreviewEnable=\"1\",ChannelPurchased=\"1\",ChannelLocked=\"0\",ChannelLogURL=\"\",PositionX=\"null\",PositionY=\"null\",BeginTime=\"null\",Interval=\"null\",Lasting=\"null\",ActionType=\"1\",FCCEnable=\"0\"";
		LiveChannel channel734 = new LiveChannel(channel_734);
		channels.add(channel734);
		
		return channels;
	}
}
