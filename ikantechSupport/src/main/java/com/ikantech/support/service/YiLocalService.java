package com.ikantech.support.service;

import java.lang.ref.WeakReference;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;

import com.ikantech.support.listener.YiHttpResponseListener;
import com.ikantech.support.net.InHttpHelper;
import com.ikantech.support.net.YiHttpRequest;
import com.ikantech.support.utils.YiLog;

/**
 * 用于处理耗时操作
 * 
 */
public class YiLocalService extends Service
{
	private InWakeLock mWakeLock;
	private HandlerThread mExecutorThread;
	private ServiceExecutor mExecutor;

	@Override
	public IBinder onBind(Intent intent)
	{
		return new YiLocalServiceBinder(this);
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		mWakeLock = new InWakeLock(
				(PowerManager) getSystemService(Context.POWER_SERVICE));

		mExecutorThread = new HandlerThread("YiLocalServiceExecutorThread");
		mExecutorThread.start();
	}

	@Override
	public void onDestroy()
	{
		// 停止WorkThread
		if (mExecutorThread != null)
		{
			mExecutorThread.quit();
			mExecutor = null;
			mExecutorThread = null;
		}
		mWakeLock.reset();

		super.onDestroy();
	}

	public ServiceExecutor getExecutor()
	{
		// create mExecutor lazily
		if (mExecutor == null)
		{
			mExecutor = new ServiceExecutor(this, mExecutorThread.getLooper());
		}
		return mExecutor;
	}

	public static class YiLocalServiceBinder extends Binder
	{
		protected WeakReference<YiLocalService> mServiceHandler;

		public YiLocalServiceBinder(YiLocalService s)
		{
			mServiceHandler = new WeakReference<YiLocalService>(s);
		}

		public void execute(Runnable runnable)
		{
			YiLocalService s = mServiceHandler.get();
			if (s != null)
			{
				s.getExecutor().execute(runnable);
			}
		}

		public void executeDelayed(Runnable runnable, long delayMillis)
		{
			YiLocalService s = mServiceHandler.get();
			if (s != null)
			{
				s.getExecutor().execute(runnable, delayMillis);
			}
		}

		public void execute(YiHttpRequest request,
				YiHttpResponseListener listener)
		{
			execute(new HttpRunnable(request, listener));
		}

		public void executeDelayed(YiHttpRequest request,
				YiHttpResponseListener listener, long delayMillis)
		{
			executeDelayed(new HttpRunnable(request, listener), delayMillis);
		}
	}

	protected static class HttpRunnable implements Runnable
	{
		private YiHttpRequest mRequest;
		private YiHttpResponseListener mListener;

		public HttpRunnable(YiHttpRequest request,
				YiHttpResponseListener listener)
		{
			mRequest = request;
			mListener = listener;
		}

		@Override
		public void run()
		{
			if (mRequest != null && mListener != null)
			{
				mListener.onHttpResponse(InHttpHelper.execute(mRequest));
			}
		}
	}

	protected static class ServiceExecutor extends Handler
	{
		WeakReference<YiLocalService> mHandlerService;

		ServiceExecutor(YiLocalService s, Looper looper)
		{
			super(looper);
			mHandlerService = new WeakReference<YiLocalService>(s);
		}

		public void execute(Runnable task)
		{
			Message.obtain(this, 0/* don't care */, task).sendToTarget();
		}

		public void execute(Runnable task, long delayMillis)
		{
			Message msg = Message.obtain(this, 0/* don't care */, task);
			sendMessageDelayed(msg, delayMillis);
		}

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.obj instanceof Runnable)
			{
				executeInternal((Runnable) msg.obj);
			}
			else
			{
				YiLog.getInstance().w("can't handle msg: " + msg);
			}
		}

		private void executeInternal(Runnable task)
		{
			YiLocalService s = mHandlerService.get();
			try
			{
				if (s != null)
				{
					s.mWakeLock.acquire(task);
				}
				task.run();
			}
			catch (Throwable t)
			{
				YiLog.getInstance().e(t, "run task: " + task);
			}
			finally
			{
				if (s != null)
				{
					s.mWakeLock.release(task);
				}
			}
		}
	}
}
