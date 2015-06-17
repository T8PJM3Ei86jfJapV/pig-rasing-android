package com.example.pigraising;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class AccountActivity extends Activity implements MyScrollView.OnScrollListener{
	private MyScrollView sv;
	private ImageView iv_bg;
	
	private TextView tvProfileName , tvUsername, tvRate, tvPignum;
	private Button btn;
	
	private String username;
	private String rate, pignum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		tvProfileName = (TextView) findViewById(R.id.tv_profile_uname);
		tvUsername = (TextView) findViewById(R.id.tv_username);
		tvRate = (TextView) findViewById(R.id.tv_rate);
		tvPignum = (TextView) findViewById(R.id.tv_pignum);
		btn = (Button) findViewById(R.id.button1);
		
		Bundle bundle = this.getIntent().getExtras();
		username = bundle.getString("username");
		rate = bundle.getString("rate");
		pignum = bundle.getString("pignum");
		boolean self = bundle.getBoolean("self", false);
		
		if (self) {
			btn.setVisibility(View.GONE);
		} else {
			btn.setOnClickListener(btnListener);
		}
		
		tvProfileName.setText(username);
		tvUsername.setText(username);
		tvRate.setText(rate);
		tvPignum.setText(pignum);
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("用户资料"); 

		createView();
	}
	private void createView(){
		sv = (MyScrollView)findViewById(R.id.sv_profile);
		iv_bg = (ImageView)findViewById(R.id.iv_profile_bg);
		sv.setOnScrollListener(this);
		sv.getView();
	}

	@Override
	public void onBottom() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTop() {
		setHeadBgMargin(0);
		
	}

	@Override
	public void onScroll(int height) {
		setHeadBgMargin(0-(int)(height*speed));
		
	}

	@Override
	public void onScrollStop() {
		// TODO Auto-generated method stub
		
	}
	int headViewHeight = 0;
	RelativeLayout.LayoutParams lp;
	float speed = 0.5f;
	private void setHeadBgMargin(int top){
		
		if(headViewHeight == 0)
			headViewHeight = getResources().getDimensionPixelSize(R.dimen.image_profile_headbg_height);
		
		if(lp == null)
			lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,headViewHeight);
		lp.setMargins(0, top, 0, 0);
		iv_bg.setLayoutParams(lp);
	}
	
	private OnClickListener btnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int total = Integer.valueOf(rate) * 5 + Integer.valueOf(pignum) - 1;
			total += 1;
			rate = String.valueOf(total / 5);
			pignum = String.valueOf((total % 5) + 1);
			tvRate.setText(rate);
			tvPignum.setText(pignum);
			String info = "已往" + username + "的猪圈塞了1头猪！";
			Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
		}
		
	};
	
}
