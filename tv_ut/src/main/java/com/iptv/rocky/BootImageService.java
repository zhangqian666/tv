package com.iptv.rocky;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iptv.common.utils.LogUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

/**
 * 处理
 * 
 *
 */
public class BootImageService extends Service implements ImageLoadingListener{
	
	private String bootImageFileName;
	
	private final IBinder binder = new MyBinder();  
	
	public BootImageService(){
		
	}
	
	public BootImageService(String fileName){
		this.bootImageFileName = fileName;
	}

	public class MyBinder extends Binder{  
        /*show只是测试方法，可以不要*/  
       /* public void show() {  
            Toast.makeText(Service_01.this, "MyName is Service_01",  
                    Toast.LENGTH_LONG).show();  
        }  */
        /*返回service服务，方便activity中得到*/  
		BootImageService getService() {  
            return BootImageService.this;  
        }  
    }  
	
	
	private void searchFile(String keyword,File filepath){   
	  
	   //判断SD卡是否存在  
	   if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
		   File[] files = filepath.listFiles();
		   
		  
	       if (files.length > 0){  
	    	  int index = 0;
	    	  List<HashMap<String,Object>> bookList = new ArrayList<HashMap<String,Object>>();
	          for (File file : files){  
	             if (file.isDirectory()){  
	                  //如果目录可读就执行（一定要加，不然会挂掉）  
	                  if(file.canRead()){  
	                   searchFile(keyword,file);  //如果是目录，递归查找  
	                  }  
	             }  else {    
	                   //判断是文件，则进行文件名判断  
	                   try {     
	                       if (file.getName().indexOf(keyword) > -1||file.getName().indexOf(keyword.toUpperCase()) > -1){     
	                            	 HashMap<String,Object> rowItem = new HashMap<String, Object>();  
	                                        rowItem.put("number", index);    // 加入序列号  
	                                        rowItem.put("bookName", file.getName());// 加入名称  
	                                        rowItem.put("path", file.getPath());  // 加入路径  
	                                        rowItem.put("size", file.length());   // 加入文件大小  
	                                        bookList.add(rowItem);  
	                                        index++;  
	                                 }     
	                           } catch(Exception e) {     
	                                    //Toast.makeText(this,"查找发生错误", Toast.LENGTH_SHORT).show();     
	                            }     
	                   }  
	                }  
	     }  
	    }  
	}  
	
	
	public boolean existFile(String fileName, String filePathString) {

		// 判断SD卡是否存在
		boolean result = false;
		//if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			File filepath = new File(filePathString);
			String[] files = filepath.list();
			if(files != null){
				if (files.length > 0) {
					for (String file : files) {
						if (file.equals(fileName)) {
							result = true;
							break;
						}
					}
				}
			}
		//}
		return result;
	}
		
	
	
	public void saveToSD(Bitmap bmp, String dirName,String fileName) throws IOException {
        // 判断sd卡是否存在
		LogUtils.error("是否已经加载:"+Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
        //if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
           
			LogUtils.error("dirName:"+dirName);
			
			File dir = new File(dirName);
            // 判断文件夹是否存在，不存在则创建
            if(!dir.exists()){
                dir.mkdir();
            }
             
            LogUtils.error("dirName+fileName:"+dirName + "  "+ fileName);
            File file = new File(dirName + fileName);
            // 判断文件是否存在，不存在则创建
            if (!file.exists()) {
                file.createNewFile();
            }
      
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                if (fos != null) {
                    // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    // 用完关闭
                    fos.flush();
                    fos.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
    }

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap bmp) {
		
		String filePath ="/mnt/sdcard/bootimage/";
		
		if(!existFile(bootImageFileName,filePath)){
			try {
				saveToSD(bmp, filePath, bootImageFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Intent intent = new Intent("SK_SYSTEM_LOGO_UPGRADE");
			intent.putExtra("filepath", filePath);
			sendBroadcast(intent);
		}
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	  * 创建
	  */
	 @Override
	 public void onCreate() {
	  // TODO Auto-generated method stub
	  super.onCreate();
	  Log.d("info", "Service Create");
	 }
	 /*
	  * 开始
	  * (non-Javadoc)
	  * @see android.app.Service#onStart(android.content.Intent, int)
	  */
	 @Override
	 public void onStart(Intent intent, int startId) {
	  // TODO Auto-generated method stub
	  super.onStart(intent, startId);
	  Log.d("info", "Service Start");
	 }
	 /**
	  * 销毁 
	  */
	 @Override
	 public void onDestroy() {
	  // TODO Auto-generated method stub
	  super.onDestroy();
	  Log.d("info", "Service Destroy");
	 }

	@Override
	public IBinder onBind(Intent arg0) {
		LogUtils.error("Service Bind Success");
		return binder;
	}
	
	
	/**
	  * 重新绑定
	  */
	 @Override
	 public void onRebind(Intent intent) { 
	  super.onRebind(intent);
	  Log.d("info","Service ReBind Success");
	 }
	 /**
	  * 取消绑定
	  */
	 @Override
	 public boolean onUnbind(Intent intent) {
	  
	  Log.d("info","Service Unbind Success");
	  return super.onUnbind(intent);
	  
	 }
}
