package com.example.eyezo.usersurvey;

import android.app.Application;

import com.backendless.Backendless;

public class SurveyCloud extends Application {

    private static final String APPLICATION_ID = "4D2F36BA-0E35-DD2C-FF15-EEAB2D41D500";
    private static final String API_KEY = "3ED7575F-17E1-49AF-A951-B8311544C730";
    private static final String SERVER_URL = "https://api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(),APPLICATION_ID,API_KEY);
    }
}
