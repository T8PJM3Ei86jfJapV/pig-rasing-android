package com.example.pigraising;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;


public class WebActivity extends Activity {
	private Button btn;
	private WebView webview;
	private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        
        btn = (Button) findViewById(R.id.back);
        webview = (WebView) findViewById(R.id.webView1);
        progressBar = (ProgressBar) findViewById(R.id.webprogressBar);
        
        String url = getIntent().getStringExtra("link");
        webview.loadUrl(url);
        webview.setWebViewClient(new WebViewClient() {
        	 @Override
             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                 // TODO Auto-generated method stub
                 super.onPageStarted(view, url, favicon);
                 progressBar.setVisibility(android.view.View.VISIBLE);
             }

             //网页加载完成时调用，隐藏加载提示旋转进度条
             @Override
             public void onPageFinished(WebView view, String url) {
                 // TODO Auto-generated method stub
                 super.onPageFinished(view, url);
                 progressBar.setVisibility(android.view.View.GONE);
             }
             //网页加载失败时调用，隐藏加载提示旋转进度条
             @Override
             public void onReceivedError(WebView view, int errorCode,
                     String description, String failingUrl) {
                 // TODO Auto-generated method stub
                 super.onReceivedError(view, errorCode, description, failingUrl);
                 progressBar.setVisibility(android.view.View.GONE);
             }
             
         });	
        
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭事件
				WebActivity.this.finish();
			}
        	
        });
    }
}
