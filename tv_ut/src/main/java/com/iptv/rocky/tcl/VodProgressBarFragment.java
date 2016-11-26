package com.iptv.rocky.tcl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iptv.common.utils.LogUtils;
import com.iptv.rocky.R;
import com.iptv.rocky.mediaplayer.MediaPlayer;
import com.iptv.rocky.mediaplayer.MediaPlayer.PlayStatus;
//import com.iptv.rocky.tcl.VodChannelPlayActivity.OnKeyDownEventListener;

public class VodProgressBarFragment extends Fragment {

	private Float speed;// = (float) 0;
	
	private Handler handler;

	private TextView vodPlayVodName;
	private TextView txtSpeedDisplay;
	private TextView vodPlayCurrentTime;
	private TextView vodPlayTotalTime;
	private ProgressBar progressBar;
	private ImageView playStatusIcon;
	
	private StartOrderPlayedVodListener mStartOrderPlayedVodListener;
	//public OnKeyDownEventListener mOnKeyDownEventListener;
	
	// 控制播放购买的信息
	private boolean orderStatus;
	private int playLength= 300; //可以播放5分钟的试看，300秒
	private boolean finishedPrePlay; // 是否已经完成预览
	
	
	private VodChannelPlayActivity activity;
	private MediaPlayer tclPlayer;
	private String vodName;

	// 是否启用选择互动和纯直播模式
	private boolean displayChooseInteractive = false;

	/** Fragment第一次附属于Activity时调用,在onCreate之前调用 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.debug("welcomeFragment--->onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		handler = new Handler();
		speed = (float)0;
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_vodplay_progress_bar,container, false);
		
		activity = (VodChannelPlayActivity)getActivity();
		//tclPlayer =  activity.tclPlayer;
		
		vodPlayVodName = (TextView) view.findViewById(R.id.vod_play_vod_name);
		vodPlayCurrentTime = (TextView) view.findViewById(R.id.vod_play_current_time);
		vodPlayTotalTime = (TextView) view.findViewById(R.id.vod_play_total_time);
		txtSpeedDisplay = (TextView) view.findViewById(R.id.vod_play_speed);
		progressBar = (ProgressBar) view.findViewById(R.id.vod_play_progressBar);
		
	
		playStatusIcon = (ImageView) view.findViewById(R.id.vod_play_status_icon);
		
		LogUtils.error("VodProgress创建," +(playStatusIcon == null) +"  visible:"+(playStatusIcon.getVisibility()));
		
        //progressBar = (ProgressBar) view.findViewById(R.id.vod_play_progressBar);
		LogUtils.error("progressbar是否为空" + (progressBar ==null) );
		//progressBar.setMax(activity.vodDetailInfo.ELAPSETIME * 60);
		progressBar.setMax(100);
		progressBar.setProgress(0);
		
		/*btnStartOrder = (Button) view.findViewById(R.id.btn_vodplay_start_order);
		btnStartOrder.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				LogUtils.debug("点击购买键。下一步动作：停止播放、弹出购买的fragment.");
				mStartOrderPlayedVodListener.startOrder(1);
			}
		});*/
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/** Acitivity要实现这个接口，这样Fragment和Activity就可以共享事件触发的资源了 */
	public interface StartOrderPlayedVodListener {
		public void startOrder(int index);
	}
	
/*	public void registerMyOnKeyDownListener(OnKeyDownEventListener listener)
	{
		mOnKeyDownEventListener =listener;
	}
	
	public void unRegisterMyOnKeyDownListener(OnKeyDownEventListener listener)
	{
		mOnKeyDownEventListener = null;
	}*/
	
	
/*	private OnKeyDownEventListener mTouchListener = new OnKeyDownEventListener() {
	        @Override
	        public void onKeyDown(int keyCode, KeyEvent event){
	            
	            //逻辑处理
	        	switch (keyCode) {
	    		case KeyEvent.KEYCODE_BACK:
	    			if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
	    				tclPlayer.resume();
	    				playStatusIcon.setImageResource(R.drawable.play_icon);
	    				//controlbar.setVisibility(View.INVISIBLE);
	    				handler.removeCallbacks(runnable);
	    				//return false;
	    			}
	    	    	else if(tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
	    	    			|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND))
	    	    	{
	    				tclPlayer.normalrate();
	    				playStatusIcon.setImageResource(R.drawable.play_icon);
	    				//controlbar.setVisibility(View.INVISIBLE);
	    				handler.removeCallbacks(runnable);
	    				//return false;
	    			}	
	    			else if (tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY))
	    			{
	    				handler.removeCallbacks(runnable);

	    				
	    				
	    				tclPlayer.stop();
	    				Log.d("VodChannelPlayActivity", "停止播放，添加上报数据到服务器信息");
	    				
	    				//return super.onKeyDown(keyCode, event);
	    			}
	    			break;
	    		case KeyEvent.KEYCODE_HOME:
//	    			if (status == 1 || status == 3) {
//	    				status = 0;
//	    				testPlayer(3);
//	    			}
	    			break;
	    		case 17:
	    			break;
	    			
	    		case KeyEvent.KEYCODE_DPAD_UP:
	    			break;
	    		
	    		case KeyEvent.KEYCODE_DPAD_DOWN:
	    			break;
	    		
	    		case KeyEvent.KEYCODE_DPAD_LEFT:
	    			
	    				
	    					if (!tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
	    						speed = (float) 2.0;
	    					} else {
	    						if (speed == 32) {
	    							speed = (float) 2.0;
	    						} else {
	    							speed = speed*2;
	    						}
	    					}
	    					//tclPlayer.fastRewind(speed);
	    					playStatusIcon.setImageResource(R.drawable.rewind_icon);
	    					//controlbar.setVisibility(View.VISIBLE);
	    					
	    					//进度条的fragment处理
	    					if(handler != null){
	    						handler.removeCallbacks(runnable);
	    						handler.postDelayed(runnable, 500);
	    					}
	    					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
	    				
	    			
	    			break;
	    		case KeyEvent.KEYCODE_DPAD_RIGHT:
	    			LogUtils.debug( "快进键");
					if (!tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD)) {
						speed = (float) 2.0;
						
					} else {
						if (speed == 32) {
							speed = (float) 2.0;
						} else {
							speed = speed*2;
						}
					}
					//tclPlayer.fastForward(speed);
					playStatusIcon.setImageResource(R.drawable.speed_icon);
					//controlbar.setVisibility(View.VISIBLE);
					
					if (handler != null) {
						handler.removeCallbacks(runnable);
						handler.postDelayed(runnable, 500);
					}
					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
	    			break;
	    		case KeyEvent.KEYCODE_DPAD_CENTER:
	    			
	    			//System.out.println("当前状态:"+controlbar.getVisibility());
	    		
	    			if(finishedPrePlay){
	    				
	    			}else{
	    				if(VodProgressBarFragment.this.isVisible()) {
	    					if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
	    						//System.out.println("当前状态:"+tclPlayer.getPlayStatus()+";  resume;");
	    						tclPlayer.resume();
	    						//controlbar.setVisibility(View.INVISIBLE);
	    						handler.removeCallbacks(runnable);
	    					} else if( tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
	    							|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
	    						//System.out.println("当前状态:"+tclPlayer.getPlayStatus()+";  normal;");
	    						tclPlayer.normalrate();
	    						//controlbar.setVisibility(View.INVISIBLE);
	    						handler.removeCallbacks(runnable);
	    					}
	    				
	    				} else {	
	    					LogUtils.info("播放状态:"+tclPlayer.getPlayStatus());
	    					if (tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)) {
	    						tclPlayer.pause();
	    						playStatusIcon.setImageResource(R.drawable.pause_iocn);
	    						txtSpeedDisplay.setText("X 0");
	    						//controlbar.setVisibility(View.VISIBLE);
	    					} else if (tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
	    							|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
	    						tclPlayer.normalrate();
	    						//controlbar.setVisibility(View.INVISIBLE);
	    						txtSpeedDisplay.setText("X 1");
	    					} else if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
	    						tclPlayer.resume();
	    						//exitConfirmDialog.setVisibility(View.INVISIBLE);
	    					}
	    				}
	    			//}
	    			break;
	    		case 82:
//	    			Intent destIntent = new Intent();
//	    			destIntent.setAction("com.tcl.matrix.action.submenu.show");
//	    			destIntent.putExtra("callback", "com.tcl.simpleplay.TestSubMenuService");
//	    			destIntent.putExtra("fucos_item", "");
//	    			Log.d(TAG, "352 key down, send submenu broadcast..." );
//	        		sendOrderedBroadcast(destIntent, null);
	    			break;
	    		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
	    			
	    			if(finishedPrePlay && orderStatus == false){
	    				
	    			}else if(!finishedPrePlay && orderStatus == false){
	    				LogUtils.debug("暂停播放键 播放状态:"+tclPlayer.getPlayStatus());
	    				if(tclPlayer.getPlayStatus() == PlayStatus.NORMAL_PLAY){
	    					tclPlayer.pause();
	    					playStatusIcon.setImageResource(R.drawable.pause_iocn);
	    					txtSpeedDisplay.setText("X 0");
	    					//controlbar.setVisibility(View.VISIBLE);
	    				}else if(tclPlayer.getPlayStatus() == PlayStatus.PAUSE){
	    					tclPlayer.resume();
	    					//controlbar.setVisibility(View.INVISIBLE);
	    				}else if(tclPlayer.getPlayStatus() == PlayStatus.FASTFORWORD || tclPlayer.getPlayStatus() == PlayStatus.FASTREWIND){
	    					tclPlayer.normalrate();
	    					//controlbar.setVisibility(View.INVISIBLE);
	    				}
	    			}else{
	    				LogUtils.debug("暂停播放键 播放状态:"+tclPlayer.getPlayStatus());
	    				if(tclPlayer.getPlayStatus() == PlayStatus.NORMAL_PLAY){
	    					//tclPlayer.pause();
	    					playStatusIcon.setImageResource(R.drawable.pause_iocn);
	    					txtSpeedDisplay.setText("X 0");
	    					//controlbar.setVisibility(View.VISIBLE);
	    				}else if(tclPlayer.getPlayStatus() == PlayStatus.PAUSE){
	    					//tclPlayer.resume();
	    					//controlbar.setVisibility(View.INVISIBLE);
	    				}else if(tclPlayer.getPlayStatus() == PlayStatus.FASTFORWORD || tclPlayer.getPlayStatus() == PlayStatus.FASTREWIND){
	    					//tclPlayer.normalrate();
	    					//controlbar.setVisibility(View.INVISIBLE);
	    				}
	    			}
	    			break;
	    		case 253:
//	    			Intent destIntent2 = new Intent();
//	    			destIntent2.setAction("com.tcl.matrix.action.submenu.show");
//	    			destIntent2.putExtra("callback", "com.tcl.simpleplay.TestSubMenuService");
//	    			destIntent2.putExtra("fucos_item", "vedio:1");
//	    			Log.d(TAG, "352 key down, send submenu broadcast..." );
//	        		sendOrderedBroadcast(destIntent2, null);
	    			break;
	    		case 303:
//	    			Intent destIntent1 = new Intent();
//	    			destIntent1.setAction("com.tcl.matrix.action.tvmenu.inputsrc.show");
//	    			destIntent1.putExtra("callback", "");
//	    			destIntent1.putExtra("currentInputSource", 1);
//	    			destIntent1.putIntegerArrayListExtra("inputSourceList", null);
//	        		sendOrderedBroadcast(destIntent1, null);
	        		break;
	    		default:
	    			break;
	    		}
	        }
	    };*/
	    

        public void onKeyDown(int keyCode, KeyEvent event){
            
            //逻辑处理
        	switch (keyCode) {
    		case KeyEvent.KEYCODE_BACK:
    			if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
    				tclPlayer.resume();
    				playStatusIcon.setImageResource(R.drawable.play_icon);
    				//controlbar.setVisibility(View.INVISIBLE);
    				handler.removeCallbacks(runnable);
    				//return false;
    			}
    	    	else if(tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
    	    			|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND))
    	    	{
    				tclPlayer.normalrate();
    				playStatusIcon.setImageResource(R.drawable.play_icon);
    				//controlbar.setVisibility(View.INVISIBLE);
    				handler.removeCallbacks(runnable);
    				//return false;
    			}	
    			else if (tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY))
    			{
    				handler.removeCallbacks(runnable);

    				
    				
    				tclPlayer.stop();
    				Log.d("VodChannelPlayActivity", "停止播放，添加上报数据到服务器信息");
    				
    				//return super.onKeyDown(keyCode, event);
    			}
    			break;
    		case KeyEvent.KEYCODE_HOME:
//    			if (status == 1 || status == 3) {
//    				status = 0;
//    				testPlayer(3);
//    			}
    			break;
    		case 17:
    			break;
    			
    		case KeyEvent.KEYCODE_DPAD_UP:
    			break;
    		
    		case KeyEvent.KEYCODE_DPAD_DOWN:
    			break;
    		
    		case KeyEvent.KEYCODE_DPAD_LEFT:
    			
    				
    					/*if (!tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
    						speed = (float) 2.0;
    					} else {
    						if (speed == 32) {
    							speed = (float) 2.0;
    						} else {
    							speed = speed*2;
    						}
    					}*/
    					//tclPlayer.fastRewind(speed);
    					LogUtils.error("playStatusIcon is null? "+(playStatusIcon == null));
    					if(playStatusIcon == null){
    						LogUtils.error("playStatusIcon 为空");
    					}else{
    						playStatusIcon.setImageResource(R.drawable.rewind_icon);
    					}
    					
    					
    					
    					//进度条的fragment处理
    					if(handler != null){
    						handler.removeCallbacks(runnable);
    						handler.postDelayed(runnable, 500);
    					}
    					txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
    					
    					
    			
    			break;
    		case KeyEvent.KEYCODE_DPAD_RIGHT:
    			LogUtils.debug( "快进键");
				/*if (!tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD)) {
					speed = (float) 2.0;
					
				} else {
					if (speed == 32) {
						speed = (float) 2.0;
					} else {
						speed = speed*2;
					}
				}*/
				//tclPlayer.fastForward(speed);
				playStatusIcon.setImageResource(R.drawable.speed_icon);
				//controlbar.setVisibility(View.VISIBLE);
				
				if (handler != null) {
					handler.removeCallbacks(runnable);
					handler.postDelayed(runnable, 500);
				}
				txtSpeedDisplay.setText("X "+(new DecimalFormat("0").format(speed)));
    			break;
    		case KeyEvent.KEYCODE_DPAD_CENTER:
    			/*if(finishedPrePlay){
    				
    			}else{*/
    				if(VodProgressBarFragment.this.isVisible()) {
    					if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
    						//System.out.println("当前状态:"+tclPlayer.getPlayStatus()+";  resume;");
    						tclPlayer.resume();
    						//controlbar.setVisibility(View.INVISIBLE);
    						handler.removeCallbacks(runnable);
    					} else if( tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
    							|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
    						//System.out.println("当前状态:"+tclPlayer.getPlayStatus()+";  normal;");
    						tclPlayer.normalrate();
    						//controlbar.setVisibility(View.INVISIBLE);
    						handler.removeCallbacks(runnable);
    					}
    				
    				} else {	
    					LogUtils.info("播放状态:"+tclPlayer.getPlayStatus());
    					if (tclPlayer.getPlayStatus().equals(PlayStatus.NORMAL_PLAY)) {
    						tclPlayer.pause();
    						playStatusIcon.setImageResource(R.drawable.pause_iocn);
    						txtSpeedDisplay.setText("X 0");
    						//controlbar.setVisibility(View.VISIBLE);
    					} else if (tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
    							|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
    						tclPlayer.normalrate();
    						//controlbar.setVisibility(View.INVISIBLE);
    						txtSpeedDisplay.setText("X 1");
    					} else if (tclPlayer.getPlayStatus().equals(PlayStatus.PAUSE)) {
    						tclPlayer.resume();
    						//exitConfirmDialog.setVisibility(View.INVISIBLE);
    					}
    				}
    			//}
    			break;
    		case 82:
//    			Intent destIntent = new Intent();
//    			destIntent.setAction("com.tcl.matrix.action.submenu.show");
//    			destIntent.putExtra("callback", "com.tcl.simpleplay.TestSubMenuService");
//    			destIntent.putExtra("fucos_item", "");
//    			Log.d(TAG, "352 key down, send submenu broadcast..." );
//        		sendOrderedBroadcast(destIntent, null);
    			break;
    		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
    			
    			if(finishedPrePlay && orderStatus == false){
    				
    			}else if(!finishedPrePlay && orderStatus == false){
    				LogUtils.debug("暂停播放键 播放状态:"+tclPlayer.getPlayStatus());
    				if(tclPlayer.getPlayStatus() == PlayStatus.NORMAL_PLAY){
    					tclPlayer.pause();
    					playStatusIcon.setImageResource(R.drawable.pause_iocn);
    					txtSpeedDisplay.setText("X 0");
    					//controlbar.setVisibility(View.VISIBLE);
    				}else if(tclPlayer.getPlayStatus() == PlayStatus.PAUSE){
    					tclPlayer.resume();
    					//controlbar.setVisibility(View.INVISIBLE);
    				}else if(tclPlayer.getPlayStatus() == PlayStatus.FASTFORWORD || tclPlayer.getPlayStatus() == PlayStatus.FASTREWIND){
    					tclPlayer.normalrate();
    					//controlbar.setVisibility(View.INVISIBLE);
    				}
    			}else{
    				LogUtils.debug("暂停播放键 播放状态:"+tclPlayer.getPlayStatus());
    				if(tclPlayer.getPlayStatus() == PlayStatus.NORMAL_PLAY){
    					//tclPlayer.pause();
    					playStatusIcon.setImageResource(R.drawable.pause_iocn);
    					txtSpeedDisplay.setText("X 0");
    					//controlbar.setVisibility(View.VISIBLE);
    				}else if(tclPlayer.getPlayStatus() == PlayStatus.PAUSE){
    					//tclPlayer.resume();
    					//controlbar.setVisibility(View.INVISIBLE);
    				}else if(tclPlayer.getPlayStatus() == PlayStatus.FASTFORWORD || tclPlayer.getPlayStatus() == PlayStatus.FASTREWIND){
    					//tclPlayer.normalrate();
    					//controlbar.setVisibility(View.INVISIBLE);
    				}
    			}
    			break;
    		case 253:
//    			Intent destIntent2 = new Intent();
//    			destIntent2.setAction("com.tcl.matrix.action.submenu.show");
//    			destIntent2.putExtra("callback", "com.tcl.simpleplay.TestSubMenuService");
//    			destIntent2.putExtra("fucos_item", "vedio:1");
//    			Log.d(TAG, "352 key down, send submenu broadcast..." );
//        		sendOrderedBroadcast(destIntent2, null);
    			break;
    		case 303:
//    			Intent destIntent1 = new Intent();
//    			destIntent1.setAction("com.tcl.matrix.action.tvmenu.inputsrc.show");
//    			destIntent1.putExtra("callback", "");
//    			destIntent1.putExtra("currentInputSource", 1);
//    			destIntent1.putIntegerArrayListExtra("inputSourceList", null);
//        		sendOrderedBroadcast(destIntent1, null);
        		break;
    		default:
    			break;
    		}
        }    
	    
	    
	// 定时刷新进度条;
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			//要做的事情
			setProgressInfo();
			handler.postDelayed(runnable, 1000);
		}
	};
	
	// 绘画当前点的信息
	private void setProgressInfo() {
		if (tclPlayer.getPlayStatus().equals(PlayStatus.FASTFORWORD) 
				|| tclPlayer.getPlayStatus().equals(PlayStatus.FASTREWIND)) {
			
			vodPlayTotalTime.setText(createTimeString(tclPlayer.getMediaDuration()));
			int currentTime = tclPlayer.getCurrentPlayTime();
			
			SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
			String now = formatter.format(new Date());
			
			//Log.d("设置进度条","提取的当前时间:"+currentTime+""+ "; 现在时间"+ now+ "; 当前倍速："+txtSpeedDisplay.getText());
			progressBar.setMax(tclPlayer.getMediaDuration());
			progressBar.setProgress(currentTime);
			vodPlayCurrentTime.setText(createTimeString(currentTime));
		}
	}
	
	// 创建要显示的时间
	private String createTimeString(int second) {
		// 秒
        int sec = (second) % 60;
        // 分钟
        int min = (second / 60) % 60;
        // 小时
        int hour = (second / 60 / 60) % 24;
        // 天
        int day = second / 60 / 60 / 24;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        date.setHours(hour);
        date.setMinutes(min);
        date.setSeconds(sec);
		return format.format(date);
	}

	public Float getSpeed() {
		return speed;
	}

	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	
	public void onRtspStatusStartOfStream(){
		progressBar.setProgress(0);
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}
	}

	public String getVodName() {
		return vodName;
	}

	public void setVodName(String vodName) {
		this.vodName = vodName;
	}
	

}
