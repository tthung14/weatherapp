package com.vndevpro.weatherapp.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vndevpro.weatherapp.model.WeatherModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // link: https://api.openweathermap.org/data/2.5/weather?q=Hanoi&appid=8211a402563420fd3b7ccb2ec060763e
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").create();

    // khoi tao retrofit
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    // ham goi api
    @GET("weather")
    Call<WeatherModel> getListWeather(@Query("q") String q,
                                      @Query("appid") String appid);
}
