package com.example.examenmultihilos.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenmultihilos.R;
import com.example.examenmultihilos.base.RequestState;
import com.example.examenmultihilos.data.models.WeatherDto;
import com.example.examenmultihilos.data.remote.ApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class MainFragment extends Fragment {


    private MainFragmentViewModel viewModel;
    private TextView lblCity;
    private EditText txtCity;
    private TextView nameCity;
    private FloatingActionButton fabSearch;
    private TextView description;
    private TextView tempMedia;
    private TextView tempMax;
    private TextView tempMin;
    private TextView humidity;
    private TextView windSpeed;
    private TextView windDegrees;
    private TextView clouds;
    private TextView dawn;
    private TextView sunset;
    private ProgressBar pbWeather;
    private ImageView imgWeather;

    static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, new MainFragmentViewModelFactory(
                ApiService.getInstance(requireContext()).getApi()
        )).get(MainFragmentViewModel.class);

        setupViews(getView());
        observeWeather();
    }

    private void setupViews(View view) {
        lblCity = ViewCompat.requireViewById(view, R.id.lblCity);
        txtCity = ViewCompat.requireViewById(view, R.id.txtCity);
        nameCity = ViewCompat.requireViewById(view, R.id.nameCity);
        description = ViewCompat.requireViewById(view, R.id.description);
        tempMedia = ViewCompat.requireViewById(view, R.id.tempMedia);
        tempMax = ViewCompat.requireViewById(view, R.id.tempMax);
        tempMin = ViewCompat.requireViewById(view, R.id.tempMin);
        humidity = ViewCompat.requireViewById(view, R.id.humidity);
        windSpeed = ViewCompat.requireViewById(view, R.id.windSpeed);
        windDegrees = ViewCompat.requireViewById(view, R.id.windDegrees);
        clouds = ViewCompat.requireViewById(view, R.id.clouds);
        dawn = ViewCompat.requireViewById(view, R.id.dawn);
        sunset = ViewCompat.requireViewById(view, R.id.sunset);
        pbWeather = ViewCompat.requireViewById(view, R.id.pbWeather);
        imgWeather = ViewCompat.requireViewById(view, R.id.imgWeather);
        fabSearch = ViewCompat.requireViewById(view, R.id.fabSearch);

        fabSearch.setOnClickListener(v -> searchCity(txtCity.getText().toString()));
    }

    private void observeWeather() {
        viewModel.getWeatherLiveData().observe(this, request -> {
            if(request !=null){
                if(request instanceof RequestState.Loading){
                    pbWeather.setVisibility(
                            ((RequestState.Loading) request).isLoading() ? View.VISIBLE : View.INVISIBLE);
                }else if(request instanceof RequestState.Error){
                    Toast.makeText(getContext(), ((RequestState.Error) request).getException().peekContent().getMessage(), Toast.LENGTH_LONG).show();
                    pbWeather.setVisibility(View.INVISIBLE);
                }else if(request instanceof RequestState.Result){
                    showWeather(((RequestState.Result< WeatherDto >)request).getData());
                }
            }
        });
    }

    private void showWeather(WeatherDto data) {
        pbWeather.setVisibility(View.INVISIBLE);
        Calendar horaDawn = Calendar.getInstance();
        Calendar horaSunset = Calendar.getInstance();
        horaDawn.setTimeInMillis(data.getSys().getSunrise());
        horaSunset.setTimeInMillis(data.getSys().getSunset());

        nameCity.setText(data.getName());
        description.setText(String.format("Descripcion: %s", (data.getWeather().get(0).getDescription() != null) ? data.getWeather().get(0).getDescription() : ""));
        tempMedia.setText(String.format("Temperatura Media: %sºC", data.getMain().getTemp().toString()));
        tempMax.setText(String.format("Temperatura Maxima: %sºC", data.getMain().getTempMax().toString()));
        tempMin.setText(String.format("Temperatura Minima: %sºC", data.getMain().getTempMin().toString()));
        humidity.setText(String.format("Humedad %s%%", data.getMain().getHumidity().toString()));
        windSpeed.setText(String.format("Velocidad del viento: %s mps", data.getWind().getSpeed().toString()));
        windDegrees.setText(String.format("Direccion del viento: %sº", data.getWind().getDeg().toString()));
        clouds.setText(String.format("Porcentaje nubosidad: %s%%", data.getClouds().getAll().toString()));
        dawn.setText(String.format("Amanecer: %s:%s", horaDawn.get(Calendar.HOUR_OF_DAY), horaDawn.get(Calendar.MINUTE)));
        sunset.setText(String.format("Atardecer: %s:%s", horaSunset.get(Calendar.HOUR_OF_DAY), horaSunset.get(Calendar.MINUTE)));
        Picasso.with(getContext()).load("https://openweathermap.org/img/w/" + data.getWeather().get(0).getIcon() + ".png").into(imgWeather);

    }

    private void searchCity(String city) {
        if(!TextUtils.isEmpty(txtCity.getText().toString())){
            viewModel.weatherFromApi(city);
        }
    }
}
