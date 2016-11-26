package com.iptv.rocky.utils;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;
import com.iptv.rocky.view.TextViewDip;
import com.iptv.rocky.R;

public class AppCommonUtils {

	public static void showToast(Context context, String message) {
		Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		View toastView = LayoutInflater.from(context).inflate(
				R.layout.tv_layout_toast, null);
		TextViewDip textView = (TextViewDip) toastView.findViewById(R.id.tv_layout_toast);
		textView.setText(message);
		int padding = (int)(TvApplication.sTvHomeCommViewTextSize);
		textView.setPadding(padding, padding / 3, padding, padding / 3);
		textView.setTextSize(TvApplication.sTvHomeCommViewTextSize);
		toast.setView(toastView);
		toast.show();
	}

	public static boolean isChild(ViewGroup parent, View child) {
		if (parent == null || child == null) {
			return false;
		}
		if (parent == child) {
			return true;
		}
		for (ViewParent viewParent = child.getParent(); viewParent != null; viewParent = viewParent
				.getParent()) {
			if (viewParent == parent) {
				return true;
			}
		}
		return false;
	}

	public static int convertDipToPixels(Context context, float dips) {
		return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
	}

	public static BitmapDrawable scaleDrawable(BitmapDrawable drawable,
			float scaleRate) {
		Bitmap b = Bitmap.createScaledBitmap(drawable.getBitmap(),
				(int) (drawable.getIntrinsicWidth() * scaleRate),
				(int) (drawable.getIntrinsicHeight() * scaleRate), false);
		return new BitmapDrawable(b);
	}

	public static Bitmap scaleBitmap(Bitmap bitmap, float scaleRate) {
		return Bitmap.createScaledBitmap(bitmap,
				(int) (bitmap.getWidth() * scaleRate),
				(int) (bitmap.getHeight() * scaleRate), false);
	}

	public static int getResourceIdByName(Context context, String name) {
		if (!TextUtils.isEmpty(name)) {
			return context.getResources().getIdentifier(name, null,
					context.getPackageName());
		}
		return -1;
	}

	public static int minutesOf(long time) {
		return (int) ((time ) / 60);
	}

	public static int secondsOf(long time) {
		return (int) ((time ) % 60);
	}
	
	public static Bitmap createBitmap(String path)
    {
        BitmapFactory.Options opts = new BitmapFactory.Options();

        opts.inSampleSize = 1;

        try
        {
            Bitmap bitmap = BitmapFactory.decodeFile(path, opts);

            if (bitmap == null)
            {
                try
                {
                    LogUtils.debug("path:" + path);
                    new File(path).delete();
                }
                catch (Exception e)
                {
                    LogUtils.error(e.toString(), e);
                }
            }

            return bitmap;
        }
        catch (OutOfMemoryError e)
        {
            LogUtils.error(path, e);

            System.gc();
            return null;
        }
    }
}
