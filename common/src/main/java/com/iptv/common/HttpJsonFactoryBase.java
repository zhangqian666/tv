package com.iptv.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.protocol.HTTP;

import com.iptv.common.utils.LogUtils;

import android.util.JsonReader;
import android.util.JsonToken;

/**
 * Json格式处理
 *
 * @param <T>
 */
public abstract class HttpJsonFactoryBase<T> extends HttpFactoryBase<T> {
	
	@Override
	protected T AnalysisContent(InputStream stream) throws IOException {
		InputStreamReader reader = new InputStreamReader(stream, HTTP.UTF_8);
		
		BufferedReader in = new BufferedReader(reader);
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = in.readLine()) != null){
		buffer.append(line);
		}
		LogUtils.error("读取到的内容:"+buffer.toString());
	      
		InputStream   inputStream   =   new   ByteArrayInputStream(buffer.toString().getBytes());
		InputStreamReader streams = new InputStreamReader(inputStream, HTTP.UTF_8);
		
		JsonReader json = new JsonReader(streams);
		//JsonReader json = new JsonReader(reader);
//		BufferedReader bf=new BufferedReader(reader);  
//	     //最好在将字节流转换为字符流的时候 进行转码  
//	     StringBuffer buffer=new StringBuffer();  
//	     String line=""; 
//	     try {
//	    	 while((line=bf.readLine())!=null){  
//	    		 buffer.append(line);  
////		    	 LogUtils.info("line=======>"+line.toString());
//
//	    	 }
//	    	 String str=buffer.toString().toString();
//	    	 int length=(str.length())/10;
//	    	 for(int i=0;i<10;i++){
//	    		 String a =(String) str.subSequence(i*length, (i+1)*length);
////	    		 LogUtils.i("节目",a);
//	    	 }
//	    	 LogUtils.info("buffer:"+buffer.toString().trim());
//		} catch (Exception e) {
//			LogUtils.error("---------------->", e);
//		}
		try {
			return AnalysisData(json);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			reader.close();
			json.close();
		}
	}
	
	protected String nextString(JsonReader reader) throws IOException
	{
		try {
			if (reader.peek() != JsonToken.NULL)
			{
				return reader.nextString();
			}
			else
			{
				reader.skipValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	protected int nextInt(JsonReader reader) throws IOException
	{
		try {
			if (reader.peek() != JsonToken.NULL)
			{
				return reader.nextInt();
			}
			else
			{
				reader.skipValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	protected long nextLong(JsonReader reader) throws IOException
	{
		try {
			if (reader.peek() != JsonToken.NULL)
			{
				return reader.nextLong();
			}
			else
			{
				reader.skipValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	protected double nextDouble(JsonReader reader) throws IOException
	{
		try {
			if (reader.peek() != JsonToken.NULL)
			{
				return reader.nextDouble();
			}
			else
			{
				reader.skipValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	protected boolean nextBoolean(JsonReader reader) throws IOException
	{
		try {
			if (reader.peek() != JsonToken.NULL)
			{
				return reader.nextBoolean();
			}
			else
			{
				reader.skipValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	protected abstract T AnalysisData(JsonReader reader) throws IOException;
}
