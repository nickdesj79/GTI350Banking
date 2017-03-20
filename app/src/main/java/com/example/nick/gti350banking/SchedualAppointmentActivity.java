package com.example.nick.gti350banking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

public class SchedualAppointmentActivity extends AppCompatActivity {

    private OnlineAccount account;

    String[] differentMonthsOfYear = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    ArrayList<String> day = new ArrayList<>();
    ArrayList<String> month = new ArrayList<>();
    ArrayList<String> year = new ArrayList<>();
    ArrayList<String> hour = new ArrayList<>();
    ArrayList<String> min = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedual_appointment);

        account = (OnlineAccount) getIntent().getSerializableExtra("account");

        initialiseList();
        updateUI();
    }

    private void initialiseList() {
        for(int i = 1; i < 32;i++) {
            if(i<10){
                day.add("0" + i);
            } else {
                day.add("" + i);
            }
        }
        for(int i = 2016; i < 2050;i++) {
            year.add(""+i);
        }
        for(int i = 0; i < 12; i++) {
            month.add(differentMonthsOfYear[i]);
        }
        for(int i = 8; i <21;i++) {
            if(i<10){
                hour.add("0" + i);
            } else {
                hour.add("" + i);
            }
        }
        min.add("00");
        min.add("15");
        min.add("30");
        min.add("45");
    }

    private void updateUI() {

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);

        //--------------------------------------------------------------------------------
        Spinner daySpinner = (Spinner)findViewById(R.id.day);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SchedualAppointmentActivity.this,
                android.R.layout.simple_spinner_item,day);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(Integer.toString(currentDay));
        daySpinner.setSelection(spinnerPosition);

        //----------------------------------------------------------------------------------
        Spinner monthSpinner = (Spinner)findViewById(R.id.month);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SchedualAppointmentActivity.this,
                android.R.layout.simple_spinner_item,month);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter2);

        spinnerPosition = adapter2.getPosition(differentMonthsOfYear[currentMonth]);
        monthSpinner.setSelection(spinnerPosition);

        //----------------------------------------------------------------------------------
        Spinner yearSpinner = (Spinner)findViewById(R.id.year);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(SchedualAppointmentActivity.this,
                android.R.layout.simple_spinner_item,year);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter3);

        spinnerPosition = adapter3.getPosition(Integer.toString(currentYear));
        yearSpinner.setSelection(spinnerPosition);

        //-----------------------------------------------------------------------------------
        Spinner hourSpinner = (Spinner)findViewById(R.id.hour);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(SchedualAppointmentActivity.this,
                android.R.layout.simple_spinner_item,hour);

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(adapter4);

        Spinner minSpinner = (Spinner)findViewById(R.id.min);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(SchedualAppointmentActivity.this,
                android.R.layout.simple_spinner_item,min);

        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minSpinner.setAdapter(adapter5);
    }

    public void openMenu(View v) {


        Intent i = new Intent(getApplicationContext(), RightMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    public void goHome(View v) {
        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }
}
