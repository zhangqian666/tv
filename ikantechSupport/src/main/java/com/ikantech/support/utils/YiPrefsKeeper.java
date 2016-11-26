package com.ikantech.support.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class YiPrefsKeeper
{
	// instance no need.
	private YiPrefsKeeper()
	{

	}

	public static void write(Context context, YiPrefsKeepable v)
	{
		SharedPreferences preferences = context.getSharedPreferences(
				v.getPrefsName(), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		v.save(editor);
		editor.commit();
	}

	public static void read(Context context, YiPrefsKeepable v)
	{
		try
		{
			SharedPreferences preferences = context.getSharedPreferences(
					v.getPrefsName(), Context.MODE_PRIVATE);
			v.restore(preferences);
		}
		catch (Exception e)
		{
			YiLog.getInstance().e(e, "read %s failed.",
					v.getClass().getSimpleName());
		}
	}

	public static void clear(Context context, YiPrefsKeepable v)
	{
		try
		{
			SharedPreferences preferences = context.getSharedPreferences(
					v.getPrefsName(), Context.MODE_PRIVATE);
			Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
		}
		catch (Exception e)
		{
			YiLog.getInstance().e(e, "clear %s failed.",
					v.getClass().getSimpleName());
		}
	}

	public interface YiPrefsKeepable
	{
		void save(Editor editor);

		void restore(SharedPreferences preferences);

		String getPrefsName();
	}
}
