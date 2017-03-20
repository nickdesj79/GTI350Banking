package com.example.nick.gti350banking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    //TODO
    //Analyse si la date sélectionner par l'utilisateur est plus vieille que le temps actuel.
    //il n'est pas possible de prendre un rendez-vous à une date déja passé.
    public void confirmSchedule(View v) {

        EditText subjectET = (EditText) findViewById(R.id.subject);
        String subject = subjectET.getText().toString();

        Spinner daySpinner = (Spinner) findViewById(R.id.day);
        String day = daySpinner.getSelectedItem().toString();

        Spinner monthSpinner = (Spinner) findViewById(R.id.month);
        String month = monthSpinner.getSelectedItem().toString();

        Spinner yearSpinner = (Spinner) findViewById(R.id.year);
        String year = yearSpinner.getSelectedItem().toString();

        Spinner hourSpinner = (Spinner) findViewById(R.id.hour);
        String hour = hourSpinner.getSelectedItem().toString();

        Spinner minSpinner = (Spinner) findViewById(R.id.min);
        String min = minSpinner.getSelectedItem().toString();

        if(subject.isEmpty()) {
            displayError("NO_SUBJECT_SPECIFIED");
        } else {
            proceed(subject,day+"-"+month+"-"+year ,hour+":"+min);
        }

    }

    private void proceed(String subject, String date, String time) {
        LayoutInflater layoutInflater = LayoutInflater.from(SchedualAppointmentActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_schedule_confirmation, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchedualAppointmentActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView dateTF = (TextView) promptView.findViewById(R.id.date);
        TextView timeTF = (TextView) promptView.findViewById(R.id.time);


        dateTF.setText(date);
        timeTF.setText(time);



        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateAccountInformation();

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //Ici on devrait ajouter le message dans les informations du compte, mais je ne sais pas si c'est néccessaire pour le cadre du cours.
    //To do pour y réfléchir(pour l'instant on redirect vers le home page c'est tout.
    //TODO
    private void updateAccountInformation() {
        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    private void displayError(String reason) {


            LayoutInflater layoutInflater = LayoutInflater.from(SchedualAppointmentActivity.this);
            final View promptView = layoutInflater.inflate(R.layout.prompt_error, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchedualAppointmentActivity.this);
            alertDialogBuilder.setView(promptView);

            TextView errorMessageTF = (TextView) promptView.findViewById(R.id.errorMessage);

            if(reason.equals("NO_SUBJECT_SPECIFIED")) {
                errorMessageTF.setText("You have to specify a reason for your visit.");
            }

            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

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
