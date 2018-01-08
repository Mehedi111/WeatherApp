package com.minatech.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by dustu on 1/5/2018.
 */

public class WeatherPagerAdapter extends FragmentPagerAdapter {

    int tabNum;

    public WeatherPagerAdapter(FragmentManager fm, int tabNum) {
        super(fm);

        this.tabNum = tabNum;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new CurrentWeather();
            case 1:
                return new ForecastWeather();
        }
       return null;
    }

    @Override
    public int getCount() {
        return tabNum;
    }
}
