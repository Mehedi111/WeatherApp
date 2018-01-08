package com.minatech.weatherapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minatech.weatherapp.interface_package.LocationHelper;
import com.minatech.weatherapp.interface_package.WeatherService;
import com.minatech.weatherapp.pojo_class.ForecastWeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastWeather extends Fragment {


    Retrofit retrofit;
    WeatherService service;
    LocationHelper helper;

    TextView dayOneTV,dayTwoTV,dayThreeTV,dayFourTV,dayFiveTV;
    double longitude=0.0;
    double latitude =0.0;
    String units = "metric";


    public ForecastWeather() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_forecast_weather, container, false);
        dayOneTV = v.findViewById(R.id.dayOne);
        dayTwoTV = v.findViewById(R.id.dayTwo);
        dayThreeTV = v.findViewById(R.id.dayThree);
        dayFourTV = v.findViewById(R.id.dayFour);
        dayFiveTV = v.findViewById(R.id.dayFive);

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(WeatherService.class);

        helper = (LocationHelper) getActivity();

        latitude = helper.getLatitude();
        longitude = helper.getLongitude();
        units = helper.getUnit();

        retrofitService();


        return v;
    }

    private void retrofitService() {

        String url = String.format("forecast?lat=%f&lon=%f&units=%s&appid=ac27c3ff05135d5e437f974d0e818186",latitude,longitude,units);

        Call<ForecastWeatherResponse> responseCall = service.getForeCastWeather(url);

        responseCall.enqueue(new Callback<ForecastWeatherResponse>() {
            @Override
            public void onResponse(Call<ForecastWeatherResponse> call, Response<ForecastWeatherResponse> response) {
                if (response.code()==200){
                    ForecastWeatherResponse forecastWeatherResponse = response.body();

                    List<ForecastWeatherResponse.ListData>list=forecastWeatherResponse.getList();


                    dayOneTV.setText(

                            "Date: "+
                                    list.get(0).getDtTxt()+
                           "\nTemperature:"+
                                    list.get(0).getMain().getTemp()
                                    +"\n\nStatus:"+
                                    list.get(0).getWeather().get(0).getMain()+
                                    "\n\nMaximum Temperature:"+
                                    list.get(0).getMain().getTempMax()+
                                    "\n\nMinimum Temperature:"+
                                    list.get(0).getMain().getTempMin()+
                                    "\n\nPressure:"+
                                    list.get(0).getMain().getPressure()+
                                    "\n\nHumidity:"+
                                    list.get(0).getMain().getHumidity()

                    );
                    dayTwoTV.setText(

                            "Date: "+
                                    list.get(1).getDtTxt()+
                                    "\n\nTemperature:"+
                                    list.get(1).getMain().getTemp()
                                    +"\n\nStatus:"+
                                    list.get(1).getWeather().get(0).getMain()+
                                    "\n\nMaximum Temperature:"+
                                    list.get(1).getMain().getTempMax()+
                                    "\n\nMinimum Temperature:"+
                                    list.get(1).getMain().getTempMin()+
                                    "\n\nPressure:"+
                                    list.get(1).getMain().getPressure()+
                                    "\n\nHumidity:"+
                                    list.get(1).getMain().getHumidity()

                    );
                    dayThreeTV.setText(

                            "Date: "+
                                    list.get(9).getDtTxt()+
                                    "\n\nTemperature:"+
                                    list.get(9).getMain().getTemp()
                                    +"\n\nStatus:"+
                                    list.get(9).getWeather().get(0).getMain()+
                                    "\n\nMaximum Temperature:"+
                                    list.get(9).getMain().getTempMax()+
                                    "\n\nMinimum Temperature:"+
                                    list.get(9).getMain().getTempMin()+
                                    "\n\nPressure:"+
                                    list.get(9).getMain().getPressure()+
                                    "\n\nHumidity:"+
                                    list.get(9).getMain().getHumidity()

                    );
                    dayFourTV.setText(

                            "Date: "+
                                    list.get(17).getDtTxt()+
                                    "\n\nTemperature:"+
                                    list.get(17).getMain().getTemp()
                                    +"\n\nStatus:"+
                                    list.get(17).getWeather().get(0).getMain()+
                                    "\n\nMaximum Temperature:"+
                                    list.get(17).getMain().getTempMax()+
                                    "\n\nMinimum Temperature:"+
                                    list.get(17).getMain().getTempMin()+
                                    "\n\nPressure:"+
                                    list.get(17).getMain().getPressure()+
                                    "\n\nHumidity:"+
                                    list.get(17).getMain().getHumidity()

                    );
                    dayFiveTV.setText(

                            "Date: "+
                                    list.get(25).getDtTxt()+
                                    "\n\nTemperature:"+
                                    list.get(25).getMain().getTemp()
                                    +"\n\nStatus:"+
                                    list.get(25).getWeather().get(0).getMain()+
                                    "\n\nMaximum Temperature:"+
                                    list.get(25).getMain().getTempMax()+
                                    "\n\nMinimum Temperature:"+
                                    list.get(25).getMain().getTempMin()+
                                    "\n\nPressure:"+
                                    list.get(25).getMain().getPressure()+
                                    "\n\nHumidity:"+
                                    list.get(25).getMain().getHumidity()

                    );
                }
            }

            @Override
            public void onFailure(Call<ForecastWeatherResponse> call, Throwable t) {

                Log.e("Error", t.getMessage());
            }
        });



    }

}
