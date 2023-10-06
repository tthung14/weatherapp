package com.vndevpro.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vndevpro.weatherapp.api.ApiService;
import com.vndevpro.weatherapp.model.WeatherModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtSearchCity;
    ImageButton btnSearch;
    TextView tvCityName, tvDay, tvDate, tvTemperature, tvTemperatureMin, tvTemperatureMax, tvCondition, tvWind, tvHumidity, tvSunrise, tvSunset, tvSea;
    ImageView imgWeather, imgCondition, imgWind, imgHumidity, imgSunrise, imgSunset, imgSea;
    String description, icon, lon, lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        clickCallApi("hanoi");
    }

    private void initView() {
        edtSearchCity = findViewById(R.id.edtSearchCity);
        btnSearch = findViewById(R.id.btnSearch);
        tvCityName = findViewById(R.id.tvCityName);
        tvDay = findViewById(R.id.tvDay);
        tvDate = findViewById(R.id.tvDate);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvTemperatureMin = findViewById(R.id.tvTemperatureMin);
        tvTemperatureMax = findViewById(R.id.tvTemperatureMax);
        tvCondition = findViewById(R.id.tvCondition);
        tvWind = findViewById(R.id.tvWind);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvSunrise = findViewById(R.id.tvSunrise);
        tvSunset = findViewById(R.id.tvSunset);
        tvSea = findViewById(R.id.tvSea);
        imgWeather = findViewById(R.id.imgWeather);
        imgCondition = findViewById(R.id.imgCondition);
        imgWind = findViewById(R.id.imgWind);
        imgHumidity = findViewById(R.id.imgHumidity);
        imgSunrise = findViewById(R.id.imgSunrise);
        imgSunset = findViewById(R.id.imgSunset);
        imgSea = findViewById(R.id.imgSea);

        btnSearch.setOnClickListener(this);
        tvCityName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                String city = String.valueOf(edtSearchCity.getText()).trim();
                // ẩn bàn phím ảo khi click
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(edtSearchCity.getWindowToken(), 0);

                clickCallApi(city);
                break;
            case R.id.tvCityName:
                Intent intent = new Intent(this, DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("icon", icon);
                bundle.putString("cityName", String.valueOf(tvCityName.getText()));
                bundle.putString("temperature", String.valueOf(tvTemperature.getText()));
                bundle.putString("description", description);

                bundle.putString("lon", lon);
                bundle.putString("lat", lat);

                intent.putExtra("bun", bundle);
                startActivity(intent);
                break;
        }
    }

    private void clickCallApi(String city) {
        // link: https://api.openweathermap.org/data/2.5/weather?q=Hanoi&appid=8211a402563420fd3b7ccb2ec060763e
        ApiService.apiService.getListWeather(city, "8211a402563420fd3b7ccb2ec060763e")
                .enqueue(new Callback<WeatherModel>() {
                    @Override
                    public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                        WeatherModel weatherModel = response.body();
                        if (weatherModel != null) {
                            tvCityName.setText(weatherModel.getName());
                            changeIcon(weatherModel.getWeather().get(0).getIcon(), imgWeather);
                            tvDay.setText(dayName(Long.valueOf(weatherModel.getDt())) +", ");
                            tvDate.setText(date(Long.valueOf(weatherModel.getDt())));

                            tvTemperature.setText(splitTemperatureCelsius(weatherModel.getMain().getTemp()) + "°");
                            tvTemperatureMin.setText(convertTemperatureCelsius(weatherModel.getMain().getTempMin()) + "°");
                            tvTemperatureMax.setText(convertTemperatureCelsius(weatherModel.getMain().getTempMax()) + "°");

                            tvCondition.setText(weatherModel.getWeather().get(0).getMain());
                            tvWind.setText(String.valueOf(weatherModel.getWind().getSpeed()) + " m/s");
                            tvHumidity.setText(String.valueOf(weatherModel.getMain().getHumidity()) + "%");
                            tvSunrise.setText(String.valueOf(weatherModel.getSys().getSunrise()));
                            tvSunset.setText(String.valueOf(weatherModel.getSys().getSunset()));
                            tvSea.setText(String.valueOf(weatherModel.getMain().getSeaLevel()));

                            description = weatherModel.getWeather().get(0).getDescription();
                            icon = weatherModel.getWeather().get(0).getIcon();
                            lon = String.valueOf(weatherModel.getCoord().getLon());
                            lat = String.valueOf(weatherModel.getCoord().getLat());
                            Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherModel> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String dayName(Long times) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(times * 1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        String dayOfWeek = dateFormat.format(calendar.getTime());

        return dayOfWeek;
    }
    private String date(Long times) {
        Date date = new Date(times * 1000); // Chuyển đổi từ giây thành mili giây
        // Định dạng ngày và tháng thành chuỗi
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
    private static String convertTemperatureCelsius(double temp) {
        double tempInCelsius = temp - 273;
        Locale locale = Locale.ENGLISH;
        return String.format(locale, "%.2f", tempInCelsius); // để trả về 30.34 chứ không phải 30,34
    }

    public static String splitTemperatureCelsius(double temp) {
        int index = convertTemperatureCelsius(temp).indexOf(".");
        String split = convertTemperatureCelsius(temp).substring(0, index);
        return split;
    }
    public static void changeIcon(String icon, ImageView imgWeather) {
        if (icon.equals("01d")) {
            imgWeather.setImageResource(R.drawable.oned);
        }
        if (icon.equals("01n")) {
            imgWeather.setImageResource(R.drawable.onen);
        }
        if (icon.equals("02d")) {
            imgWeather.setImageResource(R.drawable.twod);
        }
        if (icon.equals("02n")) {
            imgWeather.setImageResource(R.drawable.twon);
        }
        if (icon.equals("03d") || icon.equals("03n")) {
            imgWeather.setImageResource(R.drawable.threedn);
        }
        if (icon.equals("04d") || icon.equals("04n")) {
            imgWeather.setImageResource(R.drawable.fourdn);
        }
        if (icon.equals("09d") || icon.equals("09n")) {
            imgWeather.setImageResource(R.drawable.ninedn);
        }
        if (icon.equals("10d")) {
            imgWeather.setImageResource(R.drawable.tend);
        }
        if (icon.equals("10n")) {
            imgWeather.setImageResource(R.drawable.tenn);
        }
        if (icon.equals("11d") || icon.equals("11n")) {
            imgWeather.setImageResource(R.drawable.elevend);
        }
        if (icon.equals("13d") || icon.equals("13n")) {
            imgWeather.setImageResource(R.drawable.thirteend);
        }
        if (icon.equals("50d") || icon.equals("50n")) {
            imgWeather.setImageResource(R.drawable.fiftydn);
        }
    }
}