package com.example.examenmultihilos.data.remote;

import android.widget.Toast;

import com.example.examenmultihilos.base.Event;
import com.example.examenmultihilos.base.RequestState;
import com.example.examenmultihilos.data.models.WeatherDto;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherLiveData extends MutableLiveData<RequestState> {

    private final String KEY = "05812cf1b284ae27aed5160688e21bd9";
    private final String UNITS = "metric";
    private final String LANG = "es";

    private final Api api;
    private Call<WeatherDto> call;

    public WeatherLiveData(Api api) {
        this.api = api;
    }

    public void weather (String city){
        postValue(new RequestState.Loading(true));
        call = api.getWeather(KEY, city, UNITS, LANG);
        call.enqueue(new Callback<WeatherDto>() {
            @Override
            public void onResponse(Call<WeatherDto> call, Response<WeatherDto> response) {
                if(response.body() != null && response.isSuccessful()){
                    postValue(new RequestState.Result<>(response.body()));
                }else{
                    postValue(new RequestState.Error(new Event<>(new Exception(response.message()))));
                }
            }

            @Override
            public void onFailure(Call<WeatherDto> call, Throwable t) {
                postValue(new RequestState.Error(new Event<>(new Exception(t.getMessage()))));
            }
        });
    }
}
