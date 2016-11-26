package com.iptv.rocky;

import android.os.Environment;

import com.iptv.common.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BootVideoService {

	private String SDPATH;
    /** 
     * 判断SD卡上的文件夹是否存在 
     */  
    public boolean isFileExist(String fileName, String filePathStringString){  
        File file = new File(filePathStringString + fileName);  
        return file.exists();  
    }  
	
    /** 
     * 在SD卡上创建文件 
     *  
     * @throws IOException 
     */  
    public File creatSDFile(String path,String fileName) throws IOException {  
    	LogUtils.error("视频文件路径和名称:"+path+fileName);
    	
    	if(isFileExist(fileName,path)){
    		LogUtils.error("开机视频已经存在");
    		return null;
    	}else{
    		LogUtils.error("开机视频不存在");
    		
    		File file = new File(path + fileName);  
            file.createNewFile();
            return file; 
    	}  
    }  
      
    /** 
     * 在SD卡上创建目录 
     *  
     * @param dirName 
     */  
    public File creatSDDir(String dirName) {  
        File dir = new File(dirName);  
        dir.mkdir();  
        return dir;  
    }  
  
	
	private URL url=null;  
    
    public int downfile(String urlStr,String path,String fileName)  
    {  
    	LogUtils.error("开始下载视频文件"+path+" "+fileName);
        if(isFileExist(fileName,path)){
        	LogUtils.debug("视频文件存在");
        	return 1;
        }  
        else{
        	LogUtils.debug("视频文件不存在。");
	        try {  
	            InputStream input=null;  
	            input = getInputStream(urlStr);  
	            File resultFile=write2SDFromInput(path, fileName, input);  
	            if(resultFile==null)  
	            {  
	                return -1;  
	            }  
	        } catch (IOException e) {  
	            
	            e.printStackTrace();  
	        }  
          
        }  
        return 0;  
    }  
  //由于得到一个InputStream对象是所有文件处理前必须的操作，所以将这个操作封装成了一个方法  
       public InputStream getInputStream(String urlStr) throws IOException  
       {       
    	   LogUtils.debug("getInputStream:"+urlStr);
           InputStream is=null;  
            try {  
                url=new URL(urlStr);  
                HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();  
                is=urlConn.getInputStream();  
                LogUtils.debug("下载开机视频完毕");
                  
            } catch (MalformedURLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
              
            return is;  
       }  
	
	
	public boolean existFile(String fileName, String filePathString) {

		// 判断SD卡是否存在
		boolean result = false;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

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
		}
		return result;
	}
	
	
	public File write2SDFromInput(String path,String fileName,InputStream input) {
       
		File fileOriginal = null; 
        //if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        	 
            OutputStream output = null;  
            try{  
            	
            	File dir = new File(path);
            	if(!dir.exists()){
            		creatSDDir(path);  
            	}
               
                fileOriginal = creatSDFile(path,fileName);  
                output = new FileOutputStream(fileOriginal);  
                byte buffer [] = new byte[4 * 1024];  
                while((input.read(buffer)) != -1){  
                    output.write(buffer);  
                }  
                output.flush(); 
                
                File bootFile = new File(path+"bootrocky.mp4");
                if(bootFile.exists()){
                	bootFile.delete();
                }
                
                LogUtils.debug("开始复制文件");
                copyFile(path+fileName,path+"bootrocky.mp4");
                
            }  
            catch(Exception e){  
                e.printStackTrace();  
            }  
            finally{  
                try{  
                    output.close();  
                }  
                catch(Exception e){  
                    e.printStackTrace();  
                }  
            }  
        //}
        return fileOriginal;  
    }
	
	
	/** 
     * 复制单个文件 
     * 
     */
   public boolean copyFile(String oldPath, String newPath) { 
       boolean isok = true;
       try { 
           int bytesum = 0; 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { //文件存在时 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1024]; 
               int length; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   bytesum += byteread; //字节数 文件大小 
                   LogUtils.debug("字节数:"+bytesum); 
                   fs.write(buffer, 0, byteread); 
               } 
               fs.flush(); 
               fs.close(); 
               inStream.close(); 
           }
           else
           {
            isok = false;
           }
       } 
       catch (Exception e) { 
          // System.out.println("复制单个文件操作出错"); 
          // e.printStackTrace(); 
           isok = false;
       } 
       return isok;
 
   } 
}
