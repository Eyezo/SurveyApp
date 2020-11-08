package com.example.eyezo.usersurvey;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class TakeSurvey extends AppCompatActivity {

    EditText etFirstName, etSurname, etContact, etDate, etAge;
    String name, surname, contact, mydate, food, holder;
    int age;
    int testage;

    DatePickerDialog picker;

    Person person;
    Habit habit;
    Dialog dialog;


    int eat, mov, tv, rad;
    int eat1, mov1, tv1, rad1;
    int eat2, mov2, tv2, rad2;
    int eat3, mov3, tv3, rad3;
    String dat;
    RadioButton StrongAgree, Agree, Neutral, Disagree, StrongDisagree;
    RadioButton StrongAgree1, Agree1, Neutral1, Disagree1, StrongDisagree1;
    RadioButton StrongAgree2, Agree2, Neutral2, Disagree2, StrongDisagree2;
    RadioButton StrongAgree3, Agree3, Neutral3, Disagree3, StrongDisagree3;
    Button btnSaveSurvay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_survey);

        etFirstName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etLastname);
        etContact = findViewById(R.id.etContact);
        etDate = findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etAge = findViewById(R.id.etAge);


        StrongAgree = (RadioButton)findViewById(R.id.rbStrongAgree);
        Agree = (RadioButton)findViewById(R.id.rbAgree);
        Neutral = (RadioButton)findViewById(R.id.rbNeutral);
        Disagree = (RadioButton)findViewById(R.id.rbDisagree);
        StrongDisagree = (RadioButton)findViewById(R.id.rbStrongDisagree);

        StrongAgree1 = (RadioButton)findViewById(R.id.rbStrongAgree1);
        Agree1 = (RadioButton)findViewById(R.id.rbAgree1);
        Neutral1 = (RadioButton)findViewById(R.id.rbNeutral1);
        Disagree1 = (RadioButton)findViewById(R.id.rbDisagree1);
        StrongDisagree1 = (RadioButton)findViewById(R.id.rbStrongDisagree1);

        StrongAgree2 = (RadioButton)findViewById(R.id.rbStrongAgree2);
        Agree2 = (RadioButton)findViewById(R.id.rbAgree2);
        Neutral2 = (RadioButton)findViewById(R.id.rbNeutral2);
        Disagree2 = (RadioButton)findViewById(R.id.rbDisagree2);
        StrongDisagree2 = (RadioButton)findViewById(R.id.rbStrongDisagree2);

        StrongAgree3 = (RadioButton)findViewById(R.id.rbStrongAgree3);
        Agree3 = (RadioButton)findViewById(R.id.rbAgree3);
        Neutral3 = (RadioButton)findViewById(R.id.rbNeutral3);
        Disagree3 = (RadioButton)findViewById(R.id.rbDisagree3);
        StrongDisagree3 = (RadioButton)findViewById(R.id.rbStrongDisagree3);



        person = new Person();
        habit = new Habit();

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(TakeSurvey.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            dat = etDate.getText().toString();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        dialog = new SpotsDialog.Builder().setContext(this).setTheme(R.style.Custom).build();

        btnSaveSurvay = findViewById(R.id.btnSubmit);


        btnSaveSurvay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){



                food = holder.toString().trim();

                if(etFirstName.getText().toString().isEmpty() || etSurname.getText().toString().isEmpty() || etContact.getText().toString().isEmpty() ||
                        etDate.getText().toString().isEmpty() || etAge.getText().toString().isEmpty() || food.isEmpty())
                {
                    Toast.makeText(TakeSurvey.this,"Please enter all values", Toast.LENGTH_SHORT).show();
                }

                else {
                    checkAge();

                }

            }

        });
    }


    public  void checkAge(){

        int t1 = 4;
        int t2 = 121;
        testage = Integer.parseInt(etAge.getText().toString());

        if(testage <= t1){
            Toast.makeText(TakeSurvey.this, "Age must not be lesser than 5 ", Toast.LENGTH_LONG).show();
        }
        else if(testage >= t2){
            Toast.makeText(TakeSurvey.this, "Age must not be greater than 120", Toast.LENGTH_LONG).show();
        }else {
            if (connectionAvailable()) {

                name = etFirstName.getText().toString().trim();
                surname = etSurname.getText().toString().trim();
                contact = etContact.getText().toString().trim();
                mydate = etDate.getText().toString().trim();
                String n = etAge.getText().toString().trim();
                age = Integer.parseInt(n);


                dialog.show();

                person.setAge(age);
                person.setContact(contact);
                person.setFavFood(food);
                person.setHabit("5");
                person.setPersDate(dat);
                person.setName(name);
                person.setSurname(surname);

                habit.setEatOut(eat);
                habit.setWatchMovies(mov);
                habit.setWatchTv(tv);
                habit.setLisRadio(rad);


                Backendless.Persistence.save(habit, new AsyncCallback<Habit>() {
                    @Override
                    public void handleResponse(Habit response) {

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                        Toast.makeText(TakeSurvey.this, "error " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                    }
                });

                Backendless.Persistence.save(person, new AsyncCallback<Person>() {
                    @Override
                    public void handleResponse(Person response) {

                        dialog.dismiss();
                        Toast.makeText(TakeSurvey.this, "saved", Toast.LENGTH_SHORT).show();
                        TakeSurvey.this.finish();
                        startActivity(new Intent(TakeSurvey.this, MainActivity.class));

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(TakeSurvey.this, "error " + fault.getDetail(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            } else {
                Toast.makeText(TakeSurvey.this, "No internet connection", Toast.LENGTH_LONG).show();
            }

        }

        }



    public void onCheckboxClicked(View view){

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.cboxPizza:
                if(checked)
                    holder = "Pizza";
                break;
            case R.id.cboxPsta:
                if(checked)
                    holder = "Pasta";
                break;
            case R.id.cboxPap:
                if(checked)
                    holder = "Pap and wors";
                break;
            case R.id.cboxChicken:
                if(checked)
                    holder = "Chicken stir fry";
                break;
            case R.id.cboxBeef:
                if(checked)
                    holder = "Beef stir fry";
                break;
            case R.id.cboxOther:
                if(checked)
                    holder = "Other";
                break;
        }

    }
    public void onRadioButtonClicked(View view){

        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.rbStrongAgree:
                if(checked)
                StrongAgree.setChecked(true);
                Agree.setChecked(false);
                Neutral.setChecked(false);
                Disagree.setChecked(false);
                StrongDisagree.setChecked(false);

                eat = 1;
                    break;
            case R.id.rbAgree:
                if(checked)
                StrongAgree.setChecked(false);
                Agree.setChecked(true);
                Neutral.setChecked(false);
                Disagree.setChecked(false);
                StrongDisagree.setChecked(false);
                eat = 2;
                    break;
            case R.id.rbNeutral:
                if(checked)
                StrongAgree.setChecked(false);
                Agree.setChecked(false);
                Neutral.setChecked(true);
                Disagree.setChecked(false);
                StrongDisagree.setChecked(false);
                eat = 3;
                    break;
            case R.id.rbDisagree:
                if(checked)
                StrongAgree.setChecked(false);
                Agree.setChecked(false);
                Neutral.setChecked(false);
                Disagree.setChecked(true);
                StrongDisagree.setChecked(false);
                eat = 4;
                    break;
            case R.id.rbStrongDisagree:
                if (checked)
                StrongAgree.setChecked(false);
                Agree.setChecked(false);
                Neutral.setChecked(false);
                Disagree.setChecked(false);
                StrongDisagree.setChecked(true);
                eat = 5;
                    break;

            case R.id.rbStrongAgree1:
                if (checked)
                StrongAgree1.setChecked(true);
                Agree1.setChecked(false);
                Neutral1.setChecked(false);
                Disagree1.setChecked(false);
                StrongDisagree1.setChecked(false);
                mov = 1;
                    break;
            case R.id.rbAgree1:
                if (checked)
                StrongAgree1.setChecked(false);
                Agree1.setChecked(true);
                Neutral1.setChecked(false);
                Disagree1.setChecked(false);
                StrongDisagree1.setChecked(false);
                mov = 2;
                    break;
            case R.id.rbNeutral1:
                if (checked)
                StrongAgree1.setChecked(false);
                Agree1.setChecked(false);
                Neutral1.setChecked(true);
                Disagree1.setChecked(false);
                StrongDisagree1.setChecked(false);
                mov = 3;
                    break;
            case R.id.rbDisagree1:
                if (checked)
                StrongAgree1.setChecked(false);
                Agree1.setChecked(false);
                Neutral1.setChecked(false);
                Disagree1.setChecked(true);
                StrongDisagree1.setChecked(false);
                mov = 4;
                    break;
            case R.id.rbStrongDisagree1:
                if (checked)
                StrongAgree1.setChecked(false);
                Agree1.setChecked(false);
                Neutral1.setChecked(false);
                Disagree1.setChecked(false);
                StrongDisagree1.setChecked(true);
                mov = 5;
                    break;

            case R.id.rbStrongAgree2:
                if (checked)
                StrongAgree2.setChecked(true);
                Agree2.setChecked(false);
                Neutral2.setChecked(false);
                Disagree2.setChecked(false);
                StrongDisagree2.setChecked(false);
                tv = 1;
                    break;
            case R.id.rbAgree2:
                if (checked)
                StrongAgree2.setChecked(false);
                Agree2.setChecked(true);
                Neutral2.setChecked(false);
                Disagree2.setChecked(false);
                StrongDisagree2.setChecked(false);
                tv = 2;
                    break;
            case R.id.rbNeutral2:
                if (checked)
                StrongAgree2.setChecked(false);
                Agree2.setChecked(false);
                Neutral2.setChecked(true);
                Disagree2.setChecked(false);
                StrongDisagree2.setChecked(false);
                tv = 3;
                    break;
            case R.id.rbDisagree2:
                if (checked)
                StrongAgree2.setChecked(false);
                Agree2.setChecked(false);
                Neutral2.setChecked(false);
                Disagree2.setChecked(true);
                StrongDisagree2.setChecked(false);
                tv = 4;
                    break;
            case R.id.rbStrongDisagree2:
                if (checked)
                StrongAgree2.setChecked(false);
                Agree2.setChecked(false);
                Neutral2.setChecked(false);
                Disagree2.setChecked(false);
                StrongDisagree2.setChecked(true);
                tv = 5;
                    break;

            case R.id.rbStrongAgree3:
                if (checked)
                StrongAgree3.setChecked(true);
                Agree3.setChecked(false);
                Neutral3.setChecked(false);
                Disagree3.setChecked(false);
                StrongDisagree3.setChecked(false);
                rad = 1;
                    break;
            case R.id.rbAgree3:
                if (checked)
                StrongAgree3.setChecked(false);
                Agree3.setChecked(true);
                Neutral3.setChecked(false);
                Disagree3.setChecked(false);
                StrongDisagree3.setChecked(false);
                rad = 2;
                    break;
            case R.id.rbNeutral3:
                if (checked)
                StrongAgree3.setChecked(false);
                Agree3.setChecked(false);
                Neutral3.setChecked(true);
                Disagree3.setChecked(false);
                StrongDisagree3.setChecked(false);
                rad = 3;
                    break;
            case R.id.rbDisagree3:
                if (checked)
                StrongAgree3.setChecked(false);
                Agree3.setChecked(false);
                Neutral3.setChecked(false);
                Disagree3.setChecked(true);
                StrongDisagree3.setChecked(false);
                rad = 4;
                    break;
            case R.id.rbStrongDisagree3:
                if (checked)
                StrongAgree3.setChecked(false);
                Agree3.setChecked(false);
                Neutral3.setChecked(false);
                Disagree3.setChecked(false);
                StrongDisagree3.setChecked(true);
                rad = 5;
                    break;
        }
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

    @Override
    public void onBackPressed() {
        this.finish();
        AlertDialog.Builder builder = new AlertDialog.Builder(TakeSurvey.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit app?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
