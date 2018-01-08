package com.minatech.weatherapp.interface_package;

import com.minatech.weatherapp.pojo_class.CurrentWeatherResponse;
import com.minatech.weatherapp.pojo_class.ForecastWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by dustu on 1/6/2018.
 */

public interface WeatherService {

    @GET()
    Call<CurrentWeatherResponse> getCurrentWeather(@Url String urlString);

    @GET()
    Call<ForecastWeatherResponse> getForeCastWeather(@Url String forecastURL);
}
