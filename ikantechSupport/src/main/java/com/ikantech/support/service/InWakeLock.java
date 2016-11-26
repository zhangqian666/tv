package com.ikantech.support.service;

import java.util.HashSet;

import android.os.PowerManager;

import com.ikantech.support.utils.YiLog;

public class InWakeLock
{
	private PowerManager mPowerManager;
	private PowerManager.WakeLock mWakeLock;
	private PowerManager.WakeLock mTimerWakeLock;
	private HashSet<Object> mHolders = new HashSet<Object>();

	public InWakeLock(PowerManager powerManager)
	{
		mPowerManager = powerManager;
	}

	/**
	 * Release this lock and reset all holders
	 */
	public synchronized void reset()
	{
		mHolders.clear();
		release(null);
		if (mWakeLock != null)
		{
			while (mWakeLock.isHeld())
			{
				mWakeLock.release();
			}
			YiLog.getInstance().v(
					"~~~ hard reset wakelock :: still held : "
							+ mWakeLock.isHeld());
		}
	}

	public synchronized void acquire(long timeout)
	{
		if (mTimerWakeLock == null)
		{
			mTimerWakeLock = mPowerManager.newWakeLock(
					PowerManager.PARTIAL_WAKE_LOCK, "SipWakeLock.timer");
			mTimerWakeLock.setReferenceCounted(true);
		}
		mTimerWakeLock.acquire(timeout);
	}

	public synchronized void acquire(Object holder)
	{
		mHolders.add(holder);
		if (mWakeLock == null)
		{
			mWakeLock = mPowerManager.newWakeLock(
					PowerManager.PARTIAL_WAKE_LOCK, "SipWakeLock");
		}
		if (!mWakeLock.isHeld())
			mWakeLock.acquire();
		YiLog.getInstance().v(
				"acquire wakelock: holder count=" + mHolders.size());
	}

	public synchronized void release(Object holder)
	{
		mHolders.remove(holder);
		if ((mWakeLock != null) && mHolders.isEmpty() && mWakeLock.isHeld())
		{
			mWakeLock.release();
		}

		YiLog.getInstance().v(
				"release wakelock: holder count=" + mHolders.size());
	}
}