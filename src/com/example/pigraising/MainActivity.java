package com.example.pigraising;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private Context context;
	private SharedPreferences sp;
	
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
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("天天养猪"); 
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
			
			int total = sp.getInt("TOTAL", 2);
			int rate = total / 5;
			int pignum = (total % 5) + 1;
			
			
		    Bundle bundle = new Bundle();
		    bundle.putString("username", sp.getString("USERNAME", ""));
		    bundle.putString("rate", String.valueOf(rate));
		    bundle.putString("pignum", String.valueOf(pignum));

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
}
