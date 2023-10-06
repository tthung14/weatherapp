package com.vndevpro.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vndevpro.weatherapp.adapter.WeatherAdapter;
import com.vndevpro.weatherapp.api.ApiService;
import com.vndevpro.weatherapp.api.FutureApiService;
import com.vndevpro.weatherapp.model.ListItem;
import com.vndevpro.weatherapp.model.Main;
import com.vndevpro.weatherapp.model.WeatherFutureModel;
import com.vndevpro.weatherapp.model.WeatherModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imgBack, imgWeather;
    TextView tvCityName, tvTemperature, tvDescription;
    private RecyclerView rvWeather;
    private WeatherAdapter mWeatherAdapter;
    private List<ListItem> mList;
    String lon, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initView();
        getData();
        callApiItem();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        imgWeather = findViewById(R.id.imgWeather);
        tvCityName = findViewById(R.id.tvCityName);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvDescription = findViewById(R.id.tvDescription);

        imgBack.setOnClickListener(this);

        rvWeather = findViewById(R.id.rvWeather);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvWeather.setLayoutManager(linearLayoutManager);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bun");
        MainActivity activity = new MainActivity();
        activity.changeIcon(bundle.getString("icon"), imgWeather);
        tvCityName.setText(bundle.getString("cityName"));
        tvTemperature.setText(bundle.getString("temperature"));
        tvDescription.setText(bundle.getString("description"));

        lon = bundle.getString("lon");
        lat = bundle.getString("lat");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void callApiItem() {
        FutureApiService.apiService.getListWeatherItem(lat, lon, "8211a402563420fd3b7ccb2ec060763e")
                .enqueue(new Callback<WeatherFutureModel>() {
                    @Override
                    public void onResponse(Call<WeatherFutureModel> call, Response<WeatherFutureModel> response) {
                        WeatherFutureModel weatherFutureModel = response.body();
                        if (weatherFutureModel != null) {
                            mList = new ArrayList<>();
                            mList = weatherFutureModel.getList();
                            mWeatherAdapter = new WeatherAdapter(mList);
                            rvWeather.setAdapter(mWeatherAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherFutureModel> call, Throwable t) {
                        Toast.makeText(DetailsActivity.this,"Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}