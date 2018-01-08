package com.minatech.weatherapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.minatech.weatherapp.interface_package.LocationHelper;
import com.minatech.weatherapp.interface_package.WeatherService;
import com.minatech.weatherapp.pojo_class.SearchedWeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LocationHelper {

    TabLayout tabLayout;
    WeatherPagerAdapter weatherPagerAdapter;


    WeatherService service;

    String unit = "metric";
    ProgressDialog dialog;

    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;

    double latitude = 0.0;
    double longitude = 0.0;

    boolean isMetric = true;
    ViewPager viewPager;
    String searchResult = "";
    Intent intent;
    int checkedValue = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationFinder();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

        ////retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WeatherService.class);


        //////get searchable query
        intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchResult = intent.getStringExtra(SearchManager.QUERY);
            searchingService();

            //  if (weatherResponse!= null) {


            // }else {
            //      Toast.makeText(MainActivity.this, "Error",Toast.LENGTH_SHORT).show();
            //   }

        }

        /////getSelected unit

        checkedValue = intent.getIntExtra("check", 0);
        if (checkedValue == 1) {
            this.unit = intent.getStringExtra("unit");
            this.isMetric = intent.getBooleanExtra("status", false);

        }

        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Current Weather").setIcon(R.drawable.weather));
        tabLayout.addTab(tabLayout.newTab().setText("Forecast Weather").setIcon(R.drawable.forecast));

        weatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(weatherPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void locationFinder() {

        client = LocationServices.getFusedLocationProviderClient(this);

        request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(1000);

        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    if (longitude==0.0 && longitude==0.0){
                        locationFinder();
                    }else {
                        dialog.dismiss();
                    }

                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
            return;
        }
        client.requestLocationUpdates(request, callback, null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults[0] == RESULT_OK) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.requestLocationUpdates(request, callback, null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem cel = menu.findItem(R.id.celsious);
        MenuItem fah = menu.findItem(R.id.farenhite);
        if (isMetric) {
            cel.setVisible(false);
            fah.setVisible(true);

        } else {
            cel.setVisible(true);
            fah.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.celsious:
                isMetric = true;
                unit = "metric";
                startActivity(new Intent(this, MainActivity.class).putExtra("unit", unit).putExtra("status", isMetric).putExtra("check", 1));
                finish();

                Toast.makeText(MainActivity.this, "Convert To Celsious", Toast.LENGTH_SHORT).show();
                break;

            case R.id.farenhite:
                isMetric = false;
                unit = "imperial";
                startActivity(new Intent(this, MainActivity.class).putExtra("unit", unit).putExtra("status", isMetric).putExtra("check", 1));
                finish();
                Toast.makeText(MainActivity.this,"Convert To Fahrenhite",Toast.LENGTH_SHORT).show();
                break;



        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public String getUnit() {
        return this.unit;
    }


    public void searchingService() {

        String url = String.format("weather?q=%s&units=metric&appid=ac27c3ff05135d5e437f974d0e818186", searchResult);
        Call<SearchedWeatherResponse> call = service.getSearchedWeather(url);
        call.enqueue(new Callback<SearchedWeatherResponse>() {
            @Override
            public void onResponse(Call<SearchedWeatherResponse> call, Response<SearchedWeatherResponse> response) {

                if (response.code() == 200) {
                    SearchedWeatherResponse weatherResponse = response.body();

                    if (weatherResponse != null) {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("Search Result for " + searchResult.toUpperCase());
                        dialog.setMessage("Current Weather: " + weatherResponse.getWeather().get(0).getMain() +
                                "\nTemperature: " + weatherResponse.getMain().getTemp()
                                + "\nPressure: " + weatherResponse.getMain().getPressure()
                                + "\nHumidity: " + weatherResponse.getMain().getHumidity()
                        );
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        dialog.show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"City not found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchedWeatherResponse> call, Throwable t) {

                Log.e("Error",t.getMessage());

            }
        });


    }


}