package com.task.digitalcurrency;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import Models.Coinadaptor;
import Models.Coinsignature;
import Util.Mysingleton;
import Util.Utils;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class Landingpage extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener {

    private RecyclerView currencyrecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<Coinsignature> arrayList;
    private WaveSwipeRefreshLayout refresh;
    private ProgressBar progressBar;
    private ImageView nointernetimage;
    private android.support.v7.widget.Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landingpage);
        getviews();
        setrefreshlayout();
        retreivedata();
        setrecycleview();
        recyclerviewanimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getdata();
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void getviews(){
        currencyrecyclerView = findViewById(R.id.currencyrecyclerview);
        refresh = findViewById(R.id.refresh);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        progressBar = findViewById(R.id.progressbar);
        nointernetimage = findViewById(R.id.nointernetimage);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout.setTitle("Cryptocurrency");
        controlprogressandnoconnection(4);
    }

    private void setrecycleview(){
        currencyrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new Coinadaptor(arrayList,Landingpage.this);
        currencyrecyclerView.setLayoutManager(layoutManager);
        currencyrecyclerView.setAdapter(adapter);

    }

    private void recyclerviewanimation(){
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(this, R.anim.layoutanimation);
        currencyrecyclerView.setLayoutAnimation(animationController);
        scrollerror();
    }

    private void setrefreshlayout(){
        refresh.setColorSchemeColors(Color.WHITE, Color.YELLOW);
        refresh.setOnRefreshListener(this);
        refresh.setWaveColor(Color.parseColor("#000000"));
        refresh.setMaxDropHeight(400);
    }

    private void savedata() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("data", json);
        editor.apply();
    }

    private void retreivedata() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("data", null);
        Type type = new TypeToken<ArrayList<Coinsignature>>() {}.getType();
        arrayList = gson.fromJson(json, type);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            }
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layoutanimation);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void scrollerror() {
        currencyrecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                boolean enabled = manager.findFirstCompletelyVisibleItemPosition() == 0;
                refresh.setEnabled(enabled);
            }
        });
    }

    public void getdata() {
        controlprogressandnoconnection(1);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Utils.cryptocurrencyapi, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while (count < response.length()) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                Coinsignature coinsignature = new Coinsignature(
                                        Utils.getString("name", jsonObject),
                                        Utils.getString("symbol", jsonObject),
                                        Utils.getString("rank", jsonObject),
                                        Utils.getString("price_usd", jsonObject),
                                        Utils.getString("percent_change_1h", jsonObject));

                                if (arrayList.size() > count) {
                                    arrayList.remove(count);
                                }
                                arrayList.add(count, coinsignature);
                                adapter.notifyDataSetChanged();
                                count++;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        savedata();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Landingpage.this, "Poor Connection", Toast.LENGTH_SHORT).show();
                if (arrayList.size() == 0){
                    controlprogressandnoconnection(3);
                }
            }
        });

        Mysingleton.getInstance(this.getApplicationContext()).addtorequestque(jsonArrayRequest);
        //refresh.setRefreshing(false);

    }


    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                runLayoutAnimation(currencyrecyclerView);

            }
        }, 2000);
        getdata();
    }

    private void controlprogressandnoconnection(Integer code){
        switch (code){
            case 1:
                progressBar.setVisibility(View.GONE);
                nointernetimage.setVisibility(View.GONE);
                break;
            case 2:
                progressBar.setVisibility(View.VISIBLE);
                nointernetimage.setVisibility(View.VISIBLE);
                break;
            case 3:
                progressBar.setVisibility(View.GONE);
                nointernetimage.setVisibility(View.VISIBLE);
                break;
            case 4:
                progressBar.setVisibility(View.VISIBLE);
                nointernetimage.setVisibility(View.GONE);
                break;
        }
    }
}
