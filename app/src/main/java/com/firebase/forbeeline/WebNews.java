package com.firebase.forbeeline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.Toast;

public class WebNews extends AppCompatActivity {

    WebView webView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_news);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        webView = findViewById(R.id.webView);
        String url = intent.getStringExtra("url");
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        webView.loadUrl(url);
    }
}
