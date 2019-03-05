package com.example.examenmultihilos.ui;

import com.example.examenmultihilos.base.RequestState;
import com.example.examenmultihilos.data.remote.Api;
import com.example.examenmultihilos.data.remote.WeatherLiveData;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class MainFragmentViewModel extends ViewModel {

    private final WeatherLiveData weatherLiveData;

    public MainFragmentViewModel(Api api) {
        weatherLiveData = new WeatherLiveData(api);
    }

    public LiveData<RequestState> getWeatherLiveData() {
        return weatherLiveData;
    }

    public void weatherFromApi(String city){
        weatherLiveData.weather(city);
    }
}
