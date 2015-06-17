package com.example.pigraising;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Context context;
	private SharedPreferences sp;

	private int rate;
	private int pignum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = this;
		sp = context.getSharedPreferences("basic_data", Context.MODE_PRIVATE);
		
		Editor edit = sp.edit();
		edit.putInt("TOTAL", sp.getInt("TOTAL", 2) + 1).commit();
		
		boolean logined = sp.getBoolean("LOGINED", false);
		if (logined) {
			makeView();
		} else {
		    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		    startActivity(intent);
		    MainActivity.this.finish();
		}
		
	}
	
	private void makeView() {
		// 处理工具栏
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("一起来养猪"); 
		
		int total = sp.getInt("TOTAL", 2);
		rate = total / 5;
		pignum = (total % 5) + 1;
		
		// 显示用户等级
		TextView tvRate = (TextView) findViewById(R.id.tvRate);
		tvRate.setText(String.valueOf(rate));
		
		// 获取待机时间
		Long uptime = android.os.SystemClock.uptimeMillis() / 1000; //转为秒
		String hour = String.valueOf(uptime / 3600).concat("小时");
		String min = String.valueOf((uptime % 3600) / 60).concat("分钟");
		
		// 显示待机时间
		TextView tvUptime = (TextView) findViewById(R.id.tvUptime);
		String upinfo = hour + min;
		tvUptime.setText(upinfo);
		
		// 显示通知
		Notify("一起来养猪", "最近狂玩手机，您已获得“养猪专业户”称号");
	}
	
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.main, menu);  
        return super.onCreateOptionsMenu(menu);  
    }  

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_friend) {
		    Intent intent = new Intent(MainActivity.this, FriendActivity.class);
		    startActivity(intent);
			return true;
		}

		if (id == R.id.action_account) {
			
		    Bundle bundle = new Bundle();
		    bundle.putString("username", sp.getString("USERNAME", ""));
		    bundle.putString("rate", String.valueOf(rate));
		    bundle.putString("pignum", String.valueOf(pignum));
		    bundle.putBoolean("self", true);

		    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
		    intent.putExtras(bundle);
		    startActivity(intent);

		    return true;
		}
		if (id == R.id.action_logout) {
			sp.edit().putBoolean("LOGINED", false).commit();
		    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		    startActivity(intent);
		    MainActivity.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void Notify(String notificationTitle, String notificationMessage) {
	      NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	      @SuppressWarnings("deprecation")
	      
	      Notification notification = new Notification(R.drawable.ic_launcher,"New Message", System.currentTimeMillis());
	      Intent notificationIntent = new Intent(this,NotificationView.class);
	      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);
	      
	      notification.setLatestEventInfo(MainActivity.this, notificationTitle,notificationMessage, pendingIntent);
	      notificationManager.notify(9999, notification);
	   }
	
}
