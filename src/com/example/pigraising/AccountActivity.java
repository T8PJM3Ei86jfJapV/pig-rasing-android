package com.example.pigraising;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
/**
 * @author Linhai
 * @since 2013-8-1
 * @version 1.0
 */
public class AccountActivity extends Activity implements MyScrollView.OnScrollListener{
	private MyScrollView sv;
	private ImageView iv_bg;
	
	private TextView tvProfileName , tvUsername, tvRate, tvPignum;

	private String username;
	private String rate, pignum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		
		Bundle bundle = this.getIntent().getExtras();
		username = bundle.getString("username");
		rate = bundle.getString("rate");
		pignum = bundle.getString("pignum");
		
		tvProfileName = (TextView) findViewById(R.id.tv_profile_uname);
		tvUsername = (TextView) findViewById(R.id.tv_username);
		tvRate = (TextView) findViewById(R.id.tv_rate);
		tvPignum = (TextView) findViewById(R.id.tv_pignum);
		
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
}
