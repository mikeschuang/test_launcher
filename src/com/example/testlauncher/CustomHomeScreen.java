package com.example.testlauncher;

import android.app.Activity;
import android.os.Bundle;

public class CustomHomeScreen extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_home);
    }

}
