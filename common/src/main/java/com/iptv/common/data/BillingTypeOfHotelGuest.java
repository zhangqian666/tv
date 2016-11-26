package com.iptv.common.data;

/**
 * 酒店客房的计费类型
 * 计费是酒店针对客人收费的，不是联通和唯真针对酒店的
 * 付费周期_
 * 2015-08-21
 */
public enum BillingTypeOfHotelGuest {
	
	Day_UsedDay_FreeAllVod("包天计费,使用扣费,不用不扣费,免费播放所有收费VOD"),
	Day_UsedDay_FreeDaylyPayVod_PayPPV("按天计费,使用扣费,不用不扣费,免费看包天点播包，可购PPV"),
	Day_UsedDay_PayDaylyPayVod_PayPPV("按天计费,使用扣费,不用不扣费,免费看免费VOD,可购包天点播包，可购PPV"), 
	Month_FreeAllVod("包月计费,免费播放所有收费VOD"),
	Month_FreeMonthlyPayVod_PayPPV("包月计费,免费播放收费包月点播，可购PPV"),
	Month_PayMonthlyPayVod_payPPV("包月计费,可选收费包月点播包，可购PPV"),
	Free_PayAllVod("免费,所有VOD收费播放"),
	Free_All("免费,免费所有点播"),
	UNKNOW("未设置");

	private final String type;

	private BillingTypeOfHotelGuest(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
	
	public static BillingTypeOfHotelGuest createType(String type) {
		switch (type) {
		case "Day_UsedDay_FreeAllVod":
			return BillingTypeOfHotelGuest.Day_UsedDay_FreeAllVod;
		case "Day_UsedDay_FreeDaylyPayVod_PayPPV":
			return BillingTypeOfHotelGuest.Day_UsedDay_FreeDaylyPayVod_PayPPV;
		case "Day_UsedDay_PayDaylyPayVod_PayPPV":
			return BillingTypeOfHotelGuest.Day_UsedDay_PayDaylyPayVod_PayPPV;
		case "Month_FreeAllVod":
			return BillingTypeOfHotelGuest.Month_FreeAllVod;
		case "Month_FreeMonthlyPayVod_PayPPV":
			return BillingTypeOfHotelGuest.Month_FreeMonthlyPayVod_PayPPV;
		case "Month_PayMonthlyPayVod_payPPV":
			return BillingTypeOfHotelGuest.Month_PayMonthlyPayVod_payPPV;
		case "Free_PayAllVod":
			return BillingTypeOfHotelGuest.Free_PayAllVod;	
		case "Free_All":
			return BillingTypeOfHotelGuest.Free_All;		
		default:
			return BillingTypeOfHotelGuest.UNKNOW;
		}
	}
	
	public static BillingTypeOfHotelGuest convertStr(String str) {
		if ("Day_UsedDay_FreeAllVod".equalsIgnoreCase(str)) {
			return BillingTypeOfHotelGuest.Day_UsedDay_FreeAllVod;
		} else if ("Day_UsedDay_FreeDaylyPayVod_PayPPV".equalsIgnoreCase(str)) {
			return BillingTypeOfHotelGuest.Day_UsedDay_FreeDaylyPayVod_PayPPV;
		} else if ("Day_UsedDay_PayDaylyPayVod_PayPPV".equalsIgnoreCase("str")) {
			return BillingTypeOfHotelGuest.Day_UsedDay_PayDaylyPayVod_PayPPV;
		} else if ("Month_FreeAllVod".equalsIgnoreCase("str")) {
			return BillingTypeOfHotelGuest.Month_FreeAllVod;
		} else if ("Month_FreeMonthlyPayVod_PayPPV".equalsIgnoreCase("str")) {
			return BillingTypeOfHotelGuest.Month_FreeMonthlyPayVod_PayPPV;	
		} else if ("Month_PayMonthlyPayVod_payPPV".equalsIgnoreCase("str")) {
			return BillingTypeOfHotelGuest.Month_PayMonthlyPayVod_payPPV;		
		} else if ("Free_PayAllVod".equalsIgnoreCase("str")) {
			return BillingTypeOfHotelGuest.Free_PayAllVod;	
		} else if ("Free_All".equalsIgnoreCase("str")) {
			return BillingTypeOfHotelGuest.Free_All;	
		} else {
			return BillingTypeOfHotelGuest.UNKNOW;
		}
	}
	
}
