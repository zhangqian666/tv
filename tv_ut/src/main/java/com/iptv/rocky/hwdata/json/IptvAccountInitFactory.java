package com.iptv.rocky.hwdata.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.util.JsonReader;

import com.iptv.common.HttpJsonFactoryBase;
import com.iptv.common.data.BillingTypeOfHotelGuest;
import com.iptv.common.data.EnumType.StbOrientation;
import com.iptv.common.data.GuestInfoToClient;
import com.iptv.common.data.IptvAccountInitResult;
import com.iptv.common.data.StbType;
import com.iptv.rocky.hwdata.IPTVUriUtils;

public class IptvAccountInitFactory extends HttpJsonFactoryBase<IptvAccountInitResult> {

	@Override
	protected IptvAccountInitResult AnalysisData(JsonReader reader)
			throws IOException {
		IptvAccountInitResult info = new IptvAccountInitResult();
		List<GuestInfoToClient> list = new ArrayList<GuestInfoToClient>();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			
			if(name.equals("result")) {
				info.setResult( nextInt(reader));
			}else if (name.equals("userid")) {
				info.setUserId( nextString(reader));
			}else if (name.equals("hotelId")) {
				info.setHotelId( nextString(reader));	
			}else if(name.equals("password")){
				info.setPassword(nextString(reader));
			}else if(name.equals("stbId")){
				info.setStbId(nextString(reader));
			}else if(name.equals("platform")){	
				info.setIptvPlatform(nextString(reader));
			}else if(name.equals("roomId")){
				info.setRoomId(nextString(reader));
			}else if(name.equals("floorId")){
				info.setFloorId(nextString(reader));
			}else if(name.equals("billingTypeOfHotelGuest")){
				info.setBillingTypeOfHotelGuest(BillingTypeOfHotelGuest.createType(nextString(reader)));
			}else if(name.equals("perDayPrice")){
				info.setPerDayPrice(nextDouble(reader));
			}else if(name.equals("perMonthPrice")){
				info.setPerMonthPrice(nextDouble(reader));
			}else if(name.equals("daylyPayVodPrice")){
				info.setDaylyPayVodPrice(nextDouble(reader));
			}else if(name.equals("perVodPrice")){
				info.setPerVodPrice(nextDouble(reader));
			}else if(name.equals("payForDaylyPayVod")){
				info.setPayForDaylyPayVod(nextBoolean(reader));
			}else if(name.equals("payForPayPerVod")){
				info.setPayForPayPerVod(nextBoolean(reader));
			}else if(name.equals("errorinfo")){
				info.setErrorInfo(nextString(reader));
			}else if(name.equals("loginIptv")){
				info.setLoginIptv(nextBoolean(reader));
			}else if(name.equals("hasSpecialWelcome")){
				info.setHasSpecialWelcome(nextBoolean(reader));		
			}else if(name.equals("welcomeId")){
				info.setWelcomeId(nextString(reader));	
			}else if(name.equals("screenProtectId")){
				info.setScreenProtectId(nextString(reader));
			}else if(name.equals("hasBackground")){
				info.setHasBackground(nextBoolean(reader));
			}else if(name.equals("hasScreenProtect")){
				info.setHasScreenProtect(nextBoolean(reader));	
			}else if(name.equals("backGround")){
				info.setBackGround(IPTVUriUtils.HotelHost +nextString(reader));	
			}else if(name.equals("screenProtectType")){
				info.setScreenProtectType(nextString(reader));
			}else if(name.equals("groupId")){
				info.setGroupId(nextString(reader));
			}else if(name.equals("stbType")){
				info.setStbType(StbType.createType(nextString(reader)));
			}else if(name.equals("hasHomePrompt")){
				info.setShowHomeNoticePrompt(nextBoolean(reader));	
			}else if(name.equals("bootImage")){
				info.setBootImage(nextString(reader));
			}else if(name.equals("bootVideo")){
				info.setBootVideo(nextString(reader));
			}else if(name.equals("stbOrientation")){
				info.setStbOrientation(StbOrientation.create(nextString(reader))); //机顶盒的旋转方向
			}else if(name.equals("showEnglish")){
				info.setShowEnglish(nextBoolean(reader)); //是否显示英文页
			}else if(name.equals("guests")){
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginObject();
					GuestInfoToClient guest = new GuestInfoToClient();
					while (reader.hasNext()) {
						String nameGuests = reader.nextName();
						if(nameGuests.equals("name")){
							guest.setName(nextString(reader));
						}else if(nameGuests.equals("sex")){
							guest.setSex(nextInt(reader));
						}else{
							reader.skipValue();
						}
					}
					list.add(guest);
					reader.endObject();
				}
				reader.endArray();
				info.setGuests(list);	
			}else{
				reader.skipValue();
			}
		}
		reader.endObject();
		return info;
	}

	@Override
	protected String CreateUri(Object... args) {
		return String.format("%s?stbid=%s&versioncode=%s&versionname=%s", IPTVUriUtils.IptvAccountInitHost, args[0],args[1],args[2]);
	}

	@Override
	protected ArrayList<NameValuePair> getPostArgs() {
		return null;
	}


}
