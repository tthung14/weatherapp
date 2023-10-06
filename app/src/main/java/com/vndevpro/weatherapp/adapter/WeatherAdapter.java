package com.vndevpro.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vndevpro.weatherapp.MainActivity;
import com.vndevpro.weatherapp.R;
import com.vndevpro.weatherapp.model.ListItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>{
    private Context mContext;
    private List<ListItem> mList;
    public WeatherAdapter(List<ListItem> mList) {
        this.mList = mList;
    }
    @NonNull
    @Override
    public WeatherAdapter.WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_future, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.WeatherViewHolder holder, int position) {
        ListItem listItem = mList.get(position);

        MainActivity activity = new MainActivity();
        activity.changeIcon(listItem.getWeather().get(0).getIcon(), holder.imgWeatherItem);
        holder.tvHour.setText(convertHour(listItem.getDtTxt()));
        holder.tvMax.setText(activity.splitTemperatureCelsius(listItem.getMain().getTempMax()));
        holder.tvMin.setText(activity.splitTemperatureCelsius(listItem.getMain().getTempMin()));
        holder.tvDescriptionItem.setText(listItem.getWeather().get(0).getDescription());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        ImageView imgWeatherItem;
        TextView tvHour, tvMax, tvMin, tvDescriptionItem;
        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            imgWeatherItem = itemView.findViewById(R.id.imgWeatherItem);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvMax = itemView.findViewById(R.id.tvMax);
            tvMin = itemView.findViewById(R.id.tvMin);
            tvDescriptionItem = itemView.findViewById(R.id.tvDescriptionItem);
        }
    }

    private String convertHour(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

        try {
            Date d = inputFormat.parse(date);
            return outputFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "00:00";
        }
    }
}
