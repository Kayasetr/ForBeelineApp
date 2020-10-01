package com.firebase.forbeeline;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView newsList;
    private NewsAdapter newsAdapter;
    private RequestQueue mQueue;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private int chosenUrl;
    private TextView textOutput;
    String url1, url2, url3;
    final ArrayList<NewsItem> newsArrayList = new ArrayList<>();
    void bind(String url) {
        newsArrayList.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("articles");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject articles = jsonArray.getJSONObject(i);
                                String title = articles.getString("title");
                                String description = articles.getString("description");
                                String url = articles.getString("urlToImage");
                                newsArrayList.add(new NewsItem(url, title, description));
                            }
                            swipeRefreshLayout.setRefreshing(false);
                            newsAdapter.notifyDataSetChanged();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        textOutput = toolbar.findViewById(R.id.toolbarTxt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        newsList = findViewById(R.id.newsRecyclerViewList);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        newsList.setHasFixedSize(true);
        drawer = findViewById(R.id.drawer_layout);
        mQueue = Volley.newRequestQueue(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        newsAdapter = new NewsAdapter(newsArrayList, MainActivity.this);
        newsList.setLayoutManager(layoutManager);
        chosenUrl = 1;
        url1 = "https://newsapi.org/v2/everything?q=bitcoin&apiKey=a54a489891fb492da0aae1696f671770";
        url2 = "https://newsapi.org/v2/everything?q=apple&from=2020-09-24&to=2020-09-24&sortBy=popularity&apiKey=a54a489891fb492da0aae1696f671770";
        url3 = "https://newsapi.org/v2/everything?domains=techcrunch.com,thenextweb.com&apiKey=a54a489891fb492da0aae1696f671770";
        bind(url1);
        textOutput.setText(R.string.news_Example_Bitcoin);
        newsList.setAdapter(newsAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (chosenUrl) {
                    case 1:bind(url1);break;
                    case 2:bind(url2);break;
                    case 3:bind(url3);break;
                }


            }
        });
        newsAdapter.setOnClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getBaseContext(), NewsPage.class);
                intent.putExtra("title", newsArrayList.get(position));
                switch (chosenUrl) {
                    case 1:intent.putExtra("url", url1);break;
                    case 2:intent.putExtra("url", url2);break;
                    case 3:intent.putExtra("url", url3);break;
                }


                startActivity(intent);
            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemExit:
                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Выход")
                        .setMessage("Вы уверены, что хотите выйти?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
                break;
            case R.id.itemUrl1:
                bind(url1);
                chosenUrl = 1;
                textOutput.setText(R.string.news_Example_Bitcoin);
                break;
            case R.id.itemUrl2:
                bind(url2);
                chosenUrl = 2;
                textOutput.setText(R.string.news_Example_Apple);
                break;
            case R.id.itemUrl3:
                bind(url3);
                chosenUrl = 3;
                textOutput.setText(R.string.news_Example_by_TechCrunch);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
