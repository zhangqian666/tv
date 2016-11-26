package com.iptv.rocky.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.iptv.common.data.EnumType;
import com.iptv.common.data.LiveChannelPlayRecord;
import com.iptv.common.data.VodPlayRecord;
import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.model.TvApplication;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/******************************************
 * RABBITMQ ANDROID
 * 
 * 
 ******************************************/
@SuppressLint("HandlerLeak")
public class ReceiverService extends Service {

	// 转发服务器来的通知信息
	private static final String ACTION_PUBLISHSERVERNOTICE = "com.virgintelecom.iptv.RABBITMQ.PUBLISH.SERVERNOTICE";
	// 发送AMQP信息到服务器
	private static final String ACTION_PUBLISH_AMQPINFO = "com.virgintelecom.iptv.PUBLISHAMQPINFO";
	private static final String ACTION_START_CONNECT_RABBITMQ = "com.virgintelecom.iptv.START_CONNECT_RABBIT";
	private static final String ACTION_REPORT_VOD_START_PLAY ="com.virgintelecom.iptv.VOD.START.PLAY";
	private static final String ACTION_REPORT_VOD_PLAY_RECORD ="com.virgintelecom.iptv.VOD.PLAYRECORD";
	private static final String ACTION_REPORT_LIVE_PLAY_RECORD ="com.virgintelecom.iptv.LIVE.PLAYRECORD";
	private static final String ACTION_REPORT_LIVE_START_PLAY ="com.virgintelecom.iptv.LIVE.START.PLAY";
	// 回看
	private static final String ACTION_REPORT_REVIEW_START_PLAY ="com.virgintelecom.iptv.LIVE.REVIEW.START.PLAY";
	/**
	 * 微信扫码支付完成
	 */
	private static final String ACTION_PUBLISHSERVER_WECHAT_PAY_SUCCESS = "com.virgintelecom.iptv.RABBITMQ.PUBLISH.SERVER.WECHAT.PAY.SUCCESS";
	// 转发服务器来的通知信息  公共区域
	private static final String ACTION_PUBLISHSERVERNOTICE_PUBLIC = "com.virgintelecom.iptv.RABBITMQ.PUBLISH.SERVERNOTICE.PUBLIC ";
	// 发送AMQP信息到服务器
	// IDS的信息发布信息
	private static final String ACTION_IDS_UPDATE="com.virgintelecom.iptv.ids.update"; //内容更新，先简单化重新去去一次数据

	//private static final String HOSTNAME = "10.144.0.2";
	private static final String HOSTNAME = "10.0.2.210";

	private static String hostName;

	private static final int PORT = 5672;
	private static final String USERNAME = "virgin";
	private static final String PASSWORD = "virgintelecom";
	private static final String EXCHANGE_NAME = "amq.topic";
	private static final String TAG = "ReceiverService";
	//	private static final String queue = "iptv.stb.queue";
	private static final String iptvQueue = "iptv.stb.";
	private static final String VIRTUALHOST ="hotel";
	private static final int HEARTBEAT  = 120000;

	private ArrayList<String> subscribedChannels;
	private Thread subscribeThread;
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;

	private boolean connectRabbitServer; //是否开始登陆服务器
	private Handler incomingMessageHandler;
	// 心跳控制
	private Handler heartbeatHandler = new Handler();

	public ReceiverService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "Rabbitmq Service onCreate called");
		connectRabbitServer = false;

		incomingMessageHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				String message = bundle.getString("JSON");
				switch (msg.what) {
				case 0:
					LogUtils.error("要发送的NOTICE消息:" + message);
					deliverMessage(message,ACTION_PUBLISHSERVERNOTICE_PUBLIC);
					break;

				case 1:

					LogUtils.error("要发送的NOTICE消息:" + message);
					deliverMessage(message,ACTION_PUBLISHSERVERNOTICE);
					break;

				case 2:
					LogUtils.error("要发送的WECHAT PAY消息:" + message);
					deliverMessage(message,ACTION_PUBLISHSERVER_WECHAT_PAY_SUCCESS);
					break;

				default:
					break;
				}
			}
		};

		// 绑定KEY
		subscribedChannels = new ArrayList<String>();
		//		subscribedChannels.add("HILTON_QUEQUE");
		//		LogUtils.info("TvApplication.stbId------->"+TvApplication.stbId);
		//		if (TvApplication.stbType == StbType.DASHBOARD_STB) 
		//			subscribedChannels.add("IDS." + TvApplication.stbId);
		//		else 
		//			subscribedChannels.add("ROOMNOTICE." + TvApplication.stbId);
		subscribedChannels.add(iptvQueue+ TvApplication.stbId);

		IntentFilter intent = new IntentFilter(ACTION_PUBLISH_AMQPINFO);
		intent.addAction(ACTION_START_CONNECT_RABBITMQ);
		intent.addAction(ACTION_REPORT_LIVE_PLAY_RECORD);
		intent.addAction(ACTION_REPORT_VOD_PLAY_RECORD);
		intent.addAction(ACTION_REPORT_VOD_START_PLAY);
		intent.addAction(ACTION_REPORT_LIVE_START_PLAY);
		intent.addAction(ACTION_REPORT_REVIEW_START_PLAY);
		this.registerReceiver(amqpSendbroadCast, intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtils.debug("启动Rabbitmq Service");
		//startConnectRabbitMQ();
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy called");

		unregisterReceiver(amqpSendbroadCast);
		subscribeThread.interrupt();
		super.onDestroy();
	}

	private void setupConnectionFactory() {
		if(factory == null){
			factory = new ConnectionFactory();
			factory.setHost(HOSTNAME);
			factory.setPort(PORT);
			factory.setUsername(USERNAME);
			factory.setPassword(PASSWORD);
			factory.setVirtualHost(VIRTUALHOST);
			factory.setAutomaticRecoveryEnabled(true);
			factory.setRequestedChannelMax(1);
		}else {
			LogUtils.error("factory 为空");
		}
	}

	private void setupSubscription(final Handler handler) {

		LogUtils.error("subscribeThread 是否为空?" + (subscribeThread == null));

		if (subscribeThread != null) {
			subscribeThread.interrupt();

			if(subscribeThread.isAlive()){
				LogUtils.error("subscribeThread 已经启动");
				return;
			}else{
				subscribeThread.start();
			}
		}
		LogUtils.error("开始创建 subscribeThread.");
		subscribeThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					LogUtils.debug("开始创建连接");
					try {
						LogUtils.error("factory 是否为空？"+(factory == null));
						connection = factory.newConnection();
						channel = connection.createChannel();
						channel.exchangeDeclare(EXCHANGE_NAME, "topic",true);
						DeclareOk ok = channel.queueDeclare(iptvQueue+TvApplication.stbId, true, false, false, null);
						String queueName = ok.getQueue();
						LogUtils.error("获取到的queName名称:" + queueName);
						// bind queue to channels
						for (int i = 0; i < subscribedChannels.size(); i++) {
							channel.queueBind(queueName, EXCHANGE_NAME,subscribedChannels.get(i));
						}
						QueueingConsumer consumer = new QueueingConsumer(channel);
						channel.basicConsume(queueName, true, consumer);

						heartbeatHandler.postDelayed(heartbeatRunnable, HEARTBEAT);
						//LogUtils.error("启动心跳，开始准备进入接收循环，"+TvApplication.stbId);
						while (true) {
							try {
								QueueingConsumer.Delivery delivery = consumer.nextDelivery();
								String routingKey = delivery.getEnvelope().getRoutingKey();
								String message = new String(delivery.getBody());
								LogUtils.error("RoutingKey： " + routingKey + "客户端收到服务器传送来的消息: " + message);
								JSONObject jsonObj=new JSONObject(message);
								String type=jsonObj.getString("type");
								String result=jsonObj.getString("result");
								String desc=jsonObj.getString("desc");
								switch(EnumType.NoticeType.createNoticeType(type)){
								case IDS:
									if ("200".equals(result)) {
										LogUtils.error("收到IDS的绑定和解绑定信息"+message.toString());
										Message msg = handler.obtainMessage();
										Bundle bundle = new Bundle();
										bundle.putString("JSON", desc);
										msg.setData(bundle);
										msg.what=0;
										handler.sendMessage(msg);
									}else{
										LogUtils.error("获取IDS消息失败，错误描述｛"+desc+"｝");
									}

									break;
								case ROOMNOTICE:
									if ("200".equals(result)) {
										LogUtils.error("收到ROOMNOTICE的绑定和解绑定信息"+message.toString());
										Message msg = handler.obtainMessage();
										Bundle bundle = new Bundle();
										bundle.putString("JSON", desc);
										msg.setData(bundle);
										msg.what=1;
										handler.sendMessage(msg);
									}else{
										LogUtils.error("获取ROOMNOTICE消息失败，错误描述｛"+desc+"｝");
									}
									break;
								case WECHATPAY:
									if ("200".equals(result)) {
										LogUtils.error("收到WECHATPAY的绑定和解绑定信息"+message.toString());
										Message msg = handler.obtainMessage();
										Bundle bundle = new Bundle();
										bundle.putString("JSON", desc);
										msg.setData(bundle);
										msg.what=2;
										handler.sendMessage(msg);
									}else{
										LogUtils.error("获取WECHATPAY消息失败，错误描述｛"+desc+"｝");
									}
									break;
								default:
									break;
								}
							} catch (InterruptedException ie) {
								ie.printStackTrace();
								return;
							}
						}
					} catch (Exception e) {
						if (e.getClass().equals(InterruptedException.class)) {
							Log.e(TAG, "thread interrupted");
							break;
						}

						Log.e(TAG, "connection broke");
						LogUtils.error("失败原因:" + e.getCause() + "  "+ e.getMessage() + "   "+ e.getLocalizedMessage());
						e.printStackTrace();

						try {
							Thread.sleep(60000);
						} catch (InterruptedException e1) {
							break;
						}
					}
				}
			}
		});

		subscribeThread.start();
	}

	// 发射信息测试
	private void setupPublisher(String routingKey, String message) {

		if(connectRabbitServer){
			try {
				if (connection == null) {
					connection = factory.newConnection();
					channel = connection.createChannel();
					channel.exchangeDeclare(EXCHANGE_NAME, "topic");
				}

				channel.basicPublish(EXCHANGE_NAME, routingKey, null,message.getBytes());
				LogUtils.error("信息从rabbitmq 发送完毕");
			} catch (Exception e) {
				if (e.getClass().equals(InterruptedException.class)) {
					Log.e(TAG, "thread interrupted");

				}
				Log.e(TAG, "connection broke");
				LogUtils.error("失败原因:" + e.getCause() + "  " + e.getMessage()
				+ "   " + e.getLocalizedMessage());
				e.printStackTrace();

				try {
					Thread.sleep(4000);
				} catch (InterruptedException e1) {

				}
			}
		}
	}

	/**
	 * 向页面发送信息
	 * @param message
	 */
	private void deliverMessage(String message,String action) {
		Intent intent = new Intent(action);
		Bundle bundle = new Bundle();
		bundle.putString("MESSAGE", message);
		intent.putExtras(bundle);
		LogUtils.error("将接收到的NOTICE发送到页面去："+message);
		sendBroadcast(intent);
	}

	public void onEvent(ServiceCommunicationEvent event) {
		// do nothing
	}

	private BroadcastReceiver amqpSendbroadCast = new BroadcastReceiver() {

		@SuppressWarnings("unused")
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtils.error("收到事件:"+intent.getAction());

			Bundle bundle =intent.getExtras();
			String action = intent.getAction();

			VodPlayRecord mVodPlayRecord;
			LiveChannelPlayRecord mLiveChannelPlayRecord;

			switch (action) {
			case ACTION_START_CONNECT_RABBITMQ:
				connectRabbitServer = true;
				LogUtils.error("收到ACTION_START_CONNECT_RABBITMQ，开始连接");
				startConnectRabbitMQ();
				break;
			case ACTION_PUBLISH_AMQPINFO:
				String routingKey = "HILTON_QUEQUE";
				String actions = intent.getStringExtra("ACTION");
				String message = "Android Send " + actions;
				sendMessageToRabbitMQ(message);
				break;
			case ACTION_REPORT_LIVE_PLAY_RECORD:
				mLiveChannelPlayRecord = (LiveChannelPlayRecord) bundle.get("record");
				sendLivePlayRecord(mLiveChannelPlayRecord);
				break;
			case ACTION_REPORT_VOD_PLAY_RECORD:

				mVodPlayRecord = (VodPlayRecord) bundle.get("record");
				sendVodPlayRecord(mVodPlayRecord);
				break;

			case ACTION_REPORT_VOD_START_PLAY:

				mVodPlayRecord = (VodPlayRecord) bundle.get("record");
				sendVodStartPlay(mVodPlayRecord);
				break;
			case ACTION_REPORT_LIVE_START_PLAY:

				LiveChannelPlayRecord record = (LiveChannelPlayRecord) bundle.get("record");
				sendLiveStartPlay(record);
				break;
			case ACTION_REPORT_REVIEW_START_PLAY:

				break;
			case ACTION_IDS_UPDATE:	
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 开始连接服务器
	 */
	private void startConnectRabbitMQ(){
		// 绑定KEY
		if(connectRabbitServer){
			if (factory == null) {
				setupConnectionFactory();
				setupSubscription(incomingMessageHandler);
			}
		}
	}

	/**
	 * 发送信息到Rabbitmq去
	 */
	private void sendMessageToRabbitMQ(String message){
		String routingKey = "HILTON_QUEQUE";
		setupPublisher(routingKey, message);
	}


	// 定义控制未购买的播放的
	Runnable heartbeatRunnable = new Runnable() {
		@Override
		public void run() {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("userId", TvApplication.account);
				jsonObject.put("stbId", TvApplication.stbId);
				jsonObject.put("date", Calendar.getInstance().getTimeInMillis());
				jsonObject.put("status", TvApplication.status);
				jsonObject.put("position", TvApplication.position);
				jsonObject.put("versionCode", TvApplication.mAppVersionCode);
				jsonObject.put("versionName", TvApplication.mAppVersionName);
				jsonObject.put("stbOrientation", TvApplication.rotation);
				if(TvApplication.status.equals("SIGNAGE_PICTURE") || TvApplication.status.equals("SIGNAGE_VIDEO")){
					jsonObject.put("idsContent", TvApplication.idsContent);
				}
			} catch (JSONException e){
				e.printStackTrace();
			}
			LogUtils.error("开始向Rabbitmq发送心跳信息。 状态:"+TvApplication.status);
			String sendRoutingKey = "iptv.log.heartbeat";

			AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put("TYPE", "HEARTBEAT");
			builder.headers(headers);
			AMQP.BasicProperties theProps = builder.build();
			if(connection != null){
				if(connection.isOpen()){
					if(channel.isOpen()){
						try {
							LogUtils.e("发送的心跳信息:", jsonObject.toString());
							channel.basicPublish(EXCHANGE_NAME, sendRoutingKey, theProps,jsonObject.toString().getBytes());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

			//重复心跳。
			heartbeatHandler.removeCallbacks(heartbeatRunnable);
			heartbeatHandler.postDelayed(heartbeatRunnable, HEARTBEAT);
		}
	};

	// 发送直播记录
	private void sendLiveStartPlay(LiveChannelPlayRecord record){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", record.getUserId());
			jsonObject.put("stbId", record.getStbId());
			jsonObject.put("hotelId", record.getHotelId());
			jsonObject.put("roomId",   record.getRoomId());
			jsonObject.put("language",  record.getLanguage());
			jsonObject.put("channelId", record.getChannelId());
			jsonObject.put("userChannelId",record.getUserChannelId());
			jsonObject.put("channelName",  record.getChannelName());
			jsonObject.put("startPlayTime",  record.getStartPlayTime().getTime());
			jsonObject.put("platform", record.getPlatform());
		} catch (JSONException e){
			e.printStackTrace();
		}
		LogUtils.error("开始向Rabbitmq发送开始直播信息。");
		String sendRoutingKey = "iptv.log.live.play.start";

		if(connection != null){
			if(connection.isOpen()){
				if(channel.isOpen()){
					try {
						AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
						Map<String, Object> headers = new HashMap<String, Object>();
						headers.put("TYPE", "LIVE");
						// Add the headers to the builder.
						builder.headers(headers);
						// Use the builder to create the BasicProperties object.
						AMQP.BasicProperties theProps = builder.build();
						channel.basicPublish(EXCHANGE_NAME, sendRoutingKey, theProps,jsonObject.toString().getBytes());

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// 发送直播记录
	private void sendLivePlayRecord(LiveChannelPlayRecord record){
		JSONObject jsonObject = new JSONObject();
		try {

			//jsonObject.put("action", "HEARTBEAT");
			jsonObject.put("userId", record.getUserId());
			jsonObject.put("stbId", record.getStbId());
			jsonObject.put("hotelId", record.getHotelId());
			jsonObject.put("roomId",   record.getRoomId());
			jsonObject.put("language",  record.getLanguage());
			jsonObject.put("channelId", record.getChannelId());
			jsonObject.put("userChannelId",record.getUserChannelId());
			jsonObject.put("channelName",  record.getChannelName());
			jsonObject.put("startPlayTime",  record.getStartPlayTime().getTime());
			jsonObject.put("endPlayTime",  record.getEndPlayTime().getTime());
			jsonObject.put("platform", record.getPlatform());
		} catch (JSONException e){
			e.printStackTrace();
		}
		LogUtils.error("开始向Rabbitmq发送直播停止播放信息。");
		String sendRoutingKey = "iptv.log.live.play.stop";

		if(connection != null){
			if(connection.isOpen()){
				if(channel.isOpen()){
					try {
						AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
						Map<String, Object> headers = new HashMap<String, Object>();
						headers.put("TYPE", "LIVE");
						// Add the headers to the builder.
						builder.headers(headers);

						// Use the builder to create the BasicProperties object.
						AMQP.BasicProperties theProps = builder.build();
						channel.basicPublish(EXCHANGE_NAME, sendRoutingKey, theProps,jsonObject.toString().getBytes());

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}


	}

	// 发送点播记录
	private void sendVodPlayRecord(VodPlayRecord record){

		if(record != null){
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("userId", TvApplication.account);
				jsonObject.put("stbId", TvApplication.stbId);
				jsonObject.put("hotelId", TvApplication.hotelId);
				jsonObject.put("roomId", TvApplication.roomId);
				if(record.getBeginPlayDateTime() != null){
					jsonObject.put("beginPlayDateTime",  record.getBeginPlayDateTime().getTime());
				}
				if(record.getEndPlayDateTime() != null){
					jsonObject.put("endPlayDateTime", record.getEndPlayDateTime().getTime());
				}
				jsonObject.put("beginPosition",record.getBeginPosition());
				jsonObject.put("endPosition",  record.getEndPosition());
				jsonObject.put("programId",  record.getProgramId());
				jsonObject.put("programName", record.getProgramName());
				jsonObject.put("orderType",  record.getOrderType());
				jsonObject.put("price",  record.getPrice());
				jsonObject.put("platform", record.getPlatform());
				jsonObject.put("ordered", record.isOrdered());
				jsonObject.put("billingType", record.getBillingType());
				jsonObject.put("length", record.getLength());
			} catch (JSONException e){
				e.printStackTrace();
			}
			LogUtils.error("开始向Rabbitmq发送结束点播信息。");
			String sendRoutingKey = "iptv.log.vod.play.stop";
			if(connection != null){
				if(connection.isOpen()){
					try {
						if(channel.isOpen()){
							channel.basicPublish(EXCHANGE_NAME, sendRoutingKey, null,jsonObject.toString().getBytes());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	//发送开始点播
	private void sendVodStartPlay(VodPlayRecord record){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", TvApplication.account);
			jsonObject.put("stbId", TvApplication.stbId);
			jsonObject.put("hotelId", TvApplication.hotelId);
			jsonObject.put("roomId", TvApplication.roomId);
			jsonObject.put("beginPlayDateTime",  record.getBeginPlayDateTime().getTime());
			jsonObject.put("beginPosition",record.getBeginPosition());
			jsonObject.put("programId",  record.getProgramId());
			jsonObject.put("programName", record.getProgramName());
			jsonObject.put("orderType",  record.getOrderType());
			jsonObject.put("price",  record.getPrice());
			jsonObject.put("platform", record.getPlatform());
			jsonObject.put("ordered", record.isOrdered());
			jsonObject.put("billingType", record.getBillingType());
			jsonObject.put("length", record.getLength());
		} catch (JSONException e){
			e.printStackTrace();
		}
		LogUtils.error("开始向Rabbitmq发送开始点播信息。");
		String sendRoutingKey = "iptv.log.vod.play.start";
		if(connection != null){
			if(connection.isOpen()){
				try {
					if(channel.isOpen()){
						channel.basicPublish(EXCHANGE_NAME, sendRoutingKey, null,jsonObject.toString().getBytes());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getHostName() {
		return hostName;
	}

	public static void setHostName(String hostName) {
		ReceiverService.hostName = hostName;
	}
}
