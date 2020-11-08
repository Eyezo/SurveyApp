package com.example.eyezo.usersurvey;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class SurveyResults extends AppCompatActivity {

    TextView surveyTotal, ageAverage, oldSurv, ySurv, pzza, past, papWors,
               eatRate, movRate, tvRate, radioRate;
    int s;
    int n;
    double av;
    int countPizza, countPasta, countPap;
    double outRate, movieRate, tvRat, radRate;
    int sumOfEat, sumofMov, sumofTv, sumRad;

    List<Person> lisPerson;
    List<Habit> lisHabit;

    int total;
    Dialog dialog;
    DecimalFormat df;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_results);

        surveyTotal = (TextView)findViewById(R.id.txtSurvey);
        ageAverage = (TextView)findViewById(R.id.txtAge);
        oldSurv = (TextView)findViewById(R.id.txtmaxAge);
        ySurv = (TextView)findViewById(R.id.txtminAge);
        pzza = (TextView)findViewById(R.id.txtpercPizza);
        past = (TextView)findViewById(R.id.txtpercPasta);
        papWors = (TextView)findViewById(R.id.txtpercPap);
        eatRate = (TextView)findViewById(R.id.txtlikeEat);
        movRate = (TextView)findViewById(R.id.txtlikeMove);
        tvRate = (TextView)findViewById(R.id.txtlikeTv);
        radioRate = (TextView)findViewById(R.id.txtlikeRad);
        btn = (Button)findViewById(R.id.btnOkay);


        lisPerson = new ArrayList<>();
        lisHabit = new ArrayList<>();

        dialog = new SpotsDialog.Builder().setContext(this).setTheme(R.style.Custom).build();
        dialog.show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SurveyResults.this, MainActivity.class));
            }
        });

        if(connectionAvailable()){

            Backendless.Persistence.of(Person.class).getObjectCount(new AsyncCallback<Integer>() {
                @Override
                public void handleResponse(Integer response) {
                     total = response;


                }

                @Override
                public void handleFault(BackendlessFault fault) {

                    Toast.makeText(SurveyResults.this, "error "+ fault.getDetail(), Toast.LENGTH_SHORT).show();
                }
            });

            Backendless.Persistence.of(Person.class).find(new AsyncCallback<List<Person>>() {
                @Override
                public void handleResponse(List<Person> response) {

                    lisPerson.clear();
                    lisPerson = response;
                    int count = lisPerson.size();
                    surveyTotal.setText(String.valueOf(total));


                    for (int i = 0; i <lisPerson.size();i++)
                    {
                         s += lisPerson.get(i).getAge();
                         av = s /total;
                         }

                    df = new DecimalFormat("#");
                    ageAverage.setText(String.valueOf(df.format(av)));

                    double max = 0;
                    for (int i = 0; i < lisPerson.size(); i++){
                        if(lisPerson.get(i).getAge() > max){
                            max = lisPerson.get(i).getAge();
                            df = new DecimalFormat("#");
                            oldSurv.setText(String.valueOf(df.format(max)));

                        }
                    }
                    int min = lisPerson.get(0).getAge();
                    for (int i = 1; i<lisPerson.size();i++){
                        if(lisPerson.get(i).getAge() < min){
                            min = lisPerson.get(i).getAge();
                            ySurv.setText(String.valueOf(min));
                        }

                    }

                    for (int i = 0; i < lisPerson.size();i++){
                        if (lisPerson.get(i).getFavFood() == "Pizza"){
                            countPizza += 1;
                        }
                        else  if(lisPerson.get(i).getFavFood() == "Pasta"){
                            countPasta += 1;
                        }
                        else  if(lisPerson.get(i).getFavFood() == "Pap and wors"){
                            countPap += 1;
                        }
                    }
                    int a = (countPizza / total) * 100;
                    pzza.setText(String.valueOf(a));
                    int b = (countPasta / total) * 100;
                    past.setText(String.valueOf(b));
                    int c = (countPap / total) * 100;
                    papWors.setText(String.valueOf(c));


                    Backendless.Persistence.of(Habit.class).find(new AsyncCallback<List<Habit>>() {
                        @Override
                        public void handleResponse(List<Habit> response) {

                            lisHabit.clear();
                            lisHabit = response;
                            int habTotal = lisHabit.size();

                            for (int i = 0; i < lisHabit.size(); i++){
                                sumOfEat += lisHabit.get(i).getEatOut();
                                sumofMov += lisHabit.get(i).getWatchMovies();
                                sumofTv += lisHabit.get(i).getWatchTv();
                                sumRad += lisHabit.get(i).getLisRadio();

                                outRate = sumOfEat / habTotal;
                                movieRate = sumofMov / habTotal;
                                tvRat = sumofTv / habTotal;
                                radRate = sumRad / habTotal;
                            }
                            df = new DecimalFormat("#");
                            eatRate.setText(String.valueOf(df.format(outRate)));
                            movRate.setText(String.valueOf(df.format(movieRate)));
                            tvRate.setText(String.valueOf(df.format(tvRat)));
                            radioRate.setText(String.valueOf(df.format(radRate)));
                            dialog.dismiss();


                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }
                    });

                }

                @Override
                public void handleFault(BackendlessFault fault) {

                }
            });

        }
        else {
            Toast.makeText(SurveyResults.this, "No internet connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed(){

    }
    private boolean connectionAvailable() {

        /*
         * checks for internet connection
         */
        boolean connected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                connected = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                connected = true;
            }
        } else {
            connected = false;
        }
        return connected;
    }
}
