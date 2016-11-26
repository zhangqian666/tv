package com.ikantech.support.utils;

import android.view.MotionEvent;
import android.view.View;

public class YiUtils
{
	private YiUtils()
	{
	}

	public static boolean isStringInvalid(String str)
	{
		if (str == null || str.length() < 1)
		{
			return true;
		}
		return false;
	}

	public static boolean isInRangeOfView(View view, MotionEvent ev)
	{
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth())
				|| ev.getRawY() < y || ev.getRawY() > (y + view.getHeight()))
		{
			return false;
		}
		return true;
	}
}
