package com.example.examenmultihilos.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.examenmultihilos.R;
import com.example.examenmultihilos.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.flContent,
                    MainFragment.newInstance(), MainFragment.class.getSimpleName());
        }
    }
}
