package com.example.pigraising;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FriendActivity extends Activity {

	private ListView listView;

	private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		initData();
		getViews();
		setViews();
	}

	private void getViews() {
		listView = (ListView) findViewById(R.id.listView1);
	}

	private void setViews() {
		ActionBar actionBar = getActionBar();
		actionBar.setTitle("我的关注");

		listView.setAdapter(adapter);
	}

	private void initData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", "王尔德");
		map.put("pignum", "1");
		map.put("rate", "4");
		dataList.add(map);

		map = new HashMap<String, String>();
		map.put("username", "王叔叔");
		map.put("pignum", "3");
		map.put("rate", "3");
		dataList.add(map);
		
		map = new HashMap<String, String>();
		map.put("username", "王阿姨");
		map.put("pignum", "2");
		map.put("rate", "1");
		dataList.add(map);
		
		map = new HashMap<String, String>();
		map.put("username", "王奶奶");
		map.put("pignum", "2");
		map.put("rate", "5");
		dataList.add(map);

		map = new HashMap<String, String>();
		map.put("username", "王尼玛");
		map.put("pignum", "4");
		map.put("rate", "1");
		dataList.add(map);
		
		map = new HashMap<String, String>();
		map.put("username", "王阿玛");
		map.put("pignum", "5");
		map.put("rate", "4");
		dataList.add(map);

		map = new HashMap<String, String>();
		map.put("username", "隔壁老王");
		map.put("pignum", "0");
		map.put("rate", "2");
		dataList.add(map);

		map = new HashMap<String, String>();
		map.put("username", "老王妹子");
		map.put("pignum", "1");
		map.put("rate", "4");
		dataList.add(map);
		
		map = new HashMap<String, String>();
		map.put("username", "老王儿子");
		map.put("pignum", "2");
		map.put("rate", "6");
		dataList.add(map);

		map = new HashMap<String, String>();
		map.put("username", "老王孙子");
		map.put("pignum", "3");
		map.put("rate", "1");
		dataList.add(map);
		
		adapter = new SimpleAdapter(this, dataList, R.layout.list_item,
				new String[] { "username", "pignum", "rate" }, new int[] {
						R.id.list_item_username, R.id.list_item_pignum, R.id.list_item_rate }) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);

				LinearLayout item = (LinearLayout) view.findViewById(R.id.linear_layout_item);

				item.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String username = ((TextView) v.findViewById(R.id.list_item_username)).getText().toString();
						String pignum = ((TextView) v.findViewById(R.id.list_item_pignum)).getText().toString();
						String rate = ((TextView) v.findViewById(R.id.list_item_rate)).getText().toString();
						
						Bundle bundle = new Bundle();
						bundle.putString("username", username);
						bundle.putString("pignum", pignum);
						bundle.putString("rate", rate);
						bundle.putBoolean("self", false);

						Intent in = new Intent();
						in.setClass(FriendActivity.this, AccountActivity.class);
						in.putExtras(bundle);
						startActivity(in);
					}
				});

				return view;
			}

		};
	}
}
