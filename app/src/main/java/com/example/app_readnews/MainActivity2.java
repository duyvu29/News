package com.example.app_readnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // ánh xạ
        webView= (WebView) findViewById(R.id.webview_main2);
        // code
        Intent intent= getIntent();

        String link= intent.getStringExtra("linknews");
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
    }
}