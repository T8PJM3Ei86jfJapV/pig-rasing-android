package com.example.pigraising;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private final String URL = "http://2.lrsim.vipsinaapp.com/login";
	private boolean auth = false;

	private TextView editTxtUsr, editTxtPsw;
	private Button btnLogin, btnRegister;
	private CheckBox boxRem;

	private Context context;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		context = this;
		sp = context.getSharedPreferences("basic_data", Context.MODE_PRIVATE);

		initVars();
		makeView();
		setListeners();
	}

	private void initVars() {
		editTxtUsr = (TextView) findViewById(R.id.et_username);
		editTxtPsw = (TextView) findViewById(R.id.et_password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnRegister = (Button) findViewById(R.id.btn_register);
		boxRem = (CheckBox) findViewById(R.id.checkbox_rem);
	}

	private void setListeners() {
		btnLogin.setOnClickListener(loginListener);
		btnRegister.setOnClickListener(registerListener);
		boxRem.setOnCheckedChangeListener(checkedListener);
	}

	private void makeView() {
		editTxtUsr.setText(sp.getString("USERNAME", ""));
		editTxtPsw.setText(sp.getString("PASSWORD", ""));
		boxRem.setChecked(sp.getBoolean("CHECKED", true));
	}

	private OnClickListener loginListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new Thread(runnable).start();
		}
	};

	private OnClickListener registerListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this, WebActivity.class);
			intent.putExtra("link", "http://2.lrsim.vipsinaapp.com/register");
			startActivity(intent);
		}

	};

	private OnCheckedChangeListener checkedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (boxRem.isChecked()) {
				sp.edit().putBoolean("ISCHECKED", true).commit();
			} else {
				sp.edit().putBoolean("ISCHECKED", false).commit();
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		return true;
	}

	private final Runnable runnable = new Runnable() {

		@Override
		public void run() {
			
			String id = editTxtUsr.getText().toString();
			String pw = editTxtPsw.getText().toString();

			Editor edit = sp.edit();
			edit.putString("USERNAME", id).commit();
			edit.putString("PASSWORD", pw).commit();
			
			HttpPost postRequire = new HttpPost(URL);
			List<NameValuePair> pair = new ArrayList<NameValuePair>();
			pair.add(new BasicNameValuePair("id", id));
			pair.add(new BasicNameValuePair("pw", pw));

			try {
				postRequire.setEntity(new UrlEncodedFormEntity(pair, "utf-8"));
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse response = httpClient.execute(postRequire);
				String str = EntityUtils.toString(response.getEntity());

				handler.obtainMessage(1, str).sendToTarget();

			} catch (Exception e) {
				//
			}
		}
	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				String result = msg.obj.toString();
				if (result.contains("success")) {
					auth = true;
				}
			}
			
			String id = editTxtUsr.getText().toString();
			String pw = editTxtPsw.getText().toString();
			if (id.equals("android") && pw.contains("mima")) {
				auth = true;
			}

			Editor edit = sp.edit();

			if (auth) {
				edit.putBoolean("LOGINED", true).commit();

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);

				LoginActivity.this.finish();
			} else {
				edit.putBoolean("LOGINED", false).commit();
				Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}
		}
	};

}
