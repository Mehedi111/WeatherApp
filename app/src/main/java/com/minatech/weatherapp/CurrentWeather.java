package com.minatech.weatherapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.minatech.weatherapp.interface_package.LocationHelper;
import com.minatech.weatherapp.interface_package.WeatherService;
import com.minatech.weatherapp.pojo_class.CurrentWeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeather extends Fragment {

    LocationHelper helper;
    ProgressDialog dialog;

    double latitude = 0.0;
    double longitude = 0.0;
    

    public static final String TAG = MainActivity.class.getSimpleName();
    TextView currentTempTV;
    WeatherService service;
    String unit = "metric";
    public CurrentWeather() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_current_weather, container, false);
        currentTempTV = v.findViewById(R.id.currentTempTV);


        dialog = new ProgressDialog(getActivity());
        helper = (LocationHelper) getActivity();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WeatherService.class);



        longitude = helper.getLongitude();
        latitude = helper.getLatitude();
        unit = helper.getUnit();


            //progressDialogStatus();
           retrofitService();

       // Log.e("lat", latitude+" "+longitude);

        return v;
    }

    private void retrofitService() {

      String urlString = String.format("weather?lat=%f&lon=%f&units=%s&appid=ac27c3ff05135d5e437f974d0e818186", latitude, longitude, unit);

            Call<CurrentWeatherResponse> call = service.getCurrentWeather(urlString);
            call.enqueue(new Callback<CurrentWeatherResponse>() {

                @Override
                public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {

                    if (response.code() == 200) {
                        CurrentWeatherResponse currentWeatherResponse = response.body();
                        String name = currentWeatherResponse.getName();
                        double temp = currentWeatherResponse.getMain().getTemp();
                        String country = currentWeatherResponse.getSys().getCountry();

                        currentTempTV.setText("CityName: " + name + "\nTemperature: " + temp + "\n" + country);


                       // progressDialogStatus();
                    }
                }

                @Override
                public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {

                    Log.e(TAG, t.getMessage());
                }
            });






        }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    void progressDialogStatus(){
        if (dialog.isShowing()){
            dialog.dismiss();
        }else {
            dialog.show();
        }
    }



}
