package com.iptv.common.utils;

import java.lang.Thread.UncaughtExceptionHandler;


public class CrashHandler implements UncaughtExceptionHandler
{
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler()
    {
    }

    public static synchronized CrashHandler getInstance()
    {
        // if (instance == null)
        // {
        // instance = new CrashHandler();
        // }
        // return instance;
        return new CrashHandler();
    }

    public void init()
    {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (!handleException(ex) && mDefaultHandler != null)
        {
            mDefaultHandler.uncaughtException(thread, ex);
        }
        else
        {
            if (mDefaultHandler != null)
            {
                mDefaultHandler.uncaughtException(thread, ex);
                return;
            }

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private boolean handleException(Throwable ex)
    {
        if (ex == null)
        {
            return true;
        }

        LogUtils.error(ex.toString(), ex);

        return true;
    }

}
