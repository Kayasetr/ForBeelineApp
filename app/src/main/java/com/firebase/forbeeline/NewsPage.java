package com.firebase.forbeeline;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsPage extends AppCompatActivity {
    private ImageView imgNews;
    private Toolbar toolbar;
    private TextView contentTxt, authorTxt, publDataTxt, sourceTxt, titleTxt;
    public String title, urlImg, urlSource;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        contentTxt = findViewById(R.id.NewsContent);
        authorTxt = findViewById(R.id.NewsAuthor);
        publDataTxt = findViewById(R.id.NewsPublishedAt);
        sourceTxt = findViewById(R.id.NewsSource);
        titleTxt = findViewById(R.id.title);
        imgNews = findViewById(R.id.imageNews);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        NewsItem newsItem = intent.getParcelableExtra("title");
        title = newsItem.getTitle();
        urlImg = newsItem.getNewsImageResource();
        Picasso.with(this).load(urlImg).into(imgNews);
        mQueue = Volley.newRequestQueue(this);
        String urlFromMain = intent.getStringExtra("url");
        bind(urlFromMain);
        sourceTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), WebNews.class);
                intent.putExtra("url", urlSource);
                startActivity(intent);
            }
        });

    }
    void bind(String url) {
        //String url = "https://newsapi.org/v2/everything?q=bitcoin&apiKey=a54a489891fb492da0aae1696f671770";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("articles");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject articles = jsonArray.getJSONObject(i);
                                String titleOfNews = articles.getString("title");
                                if (title.equals(titleOfNews)){
                                    titleTxt.setText(titleOfNews);
                                    urlSource = articles.getString("url");
                                    SpannableString content = new SpannableString("source: "+urlSource);
                                    content.setSpan(new UnderlineSpan(), 0, content.toString().length(), 0);
                                    sourceTxt.setText(content);
                                    contentTxt.setText(articles.getString("content"));
                                    authorTxt.setText("author: "+articles.getString("author"));
                                    publDataTxt.setText("published At: "+articles.getString("publishedAt"));
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}
