package com.iptv.rocky.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import android.os.AsyncTask;

public class Send extends AsyncTask<String, Void, Void> {

	private String QUEUE_NAME = "hello.world.queue";
	private String EXCHANGE_NAME = "HILTON";

	@Override
	protected Void doInBackground(String... Message) {
		try {

			ConnectionFactory connFac = new ConnectionFactory();
			
			//RabbitMQ-Server安装在本机，所以直接用127.0.0.1
			connFac.setHost("211.149.195.231");
			
			//创建一个连接
			Connection conn = connFac.newConnection();
			
			//创建一个渠道
			Channel channel = conn.createChannel();
			
			String exchangeName = "HILTON";
			
			String messageType = "fs.type01";
			
			channel.exchangeDeclare(exchangeName, "topic");
			
			//定义Queue名
			String msg = "Hello World!";
			
			//发送消息
			channel.basicPublish( exchangeName , messageType , null , msg.getBytes());
			
			System.out.println("send message[" + msg + "] to "+ exchangeName +" success!");
			
			channel.close(); 
			conn.close(); 


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

}
