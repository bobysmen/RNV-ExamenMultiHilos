package com.example.examenmultihilos.data.remote;

import com.example.examenmultihilos.data.models.WeatherDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("weather")
    Call<WeatherDto> getWeather(
            @Query("appid") String key,
            @Query("q") String city,
            @Query("units") String units,
            @Query("lang") String lang
    );

}
