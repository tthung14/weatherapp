package com.vndevpro.weatherapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vndevpro.weatherapp.model.WeatherFutureModel;
import com.vndevpro.weatherapp.model.WeatherModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FutureApiService {
    // link: https://api.openweathermap.org/data/2.5/forecast?lat=21.0245&lon=105.8412&appid=8211a402563420fd3b7ccb2ec060763e
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();

    // khoi tao retrofit
    FutureApiService apiService = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FutureApiService.class);

    // ham goi api
    @GET("forecast")
    Call<WeatherFutureModel> getListWeatherItem(@Query("lat") String lat,
                                                @Query("lon") String lon,
                                                @Query("appid") String appid);
}
