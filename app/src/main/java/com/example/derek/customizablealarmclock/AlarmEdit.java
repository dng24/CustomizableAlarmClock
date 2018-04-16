package com.example.derek.customizablealarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmEdit extends AppCompatActivity {
    TimePicker timePicker; //creates TimePicker for user to choose
    ArrayList<String> fileNames; //creates ArrayList to store the sounds to be played in the alarm
    AllSounds s = new AllSounds(); //creates AllSound object to pull the sounds to be played

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        s.makeSounds(); //prepares the sound files to be imported into this Java class from the AllSounds page
        //makes sure there are actually sounds in the alarm
        try {
            fileNames = s.getFileNames(); //gets the sound files to be played
        }
        catch (NullPointerException e){
            Log.d("AlarmEdit","fileNames Null");
        }

        timePicker = findViewById(R.id.timePicker);
        findViewById(R.id.ButtonSetAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //gets the current date/time and gets the time the user set when the button Set Alarm is clicked
            Calendar calendar = Calendar.getInstance();
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.getHour(),
                    timePicker.getMinute(),
                    0
                );
            } else {
                calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.getCurrentHour(),
                    timePicker.getCurrentMinute(),
                    0
                );
            }

            setAlarm(calendar.getTimeInMillis()); //sets the alarm
            }
        });
    }

    //sets the alarm
    private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //goes to receiver when alarm goes off
        Intent intent = new Intent(this, AlarmReceiver.class); //creates intent to go to the AlarmReceiver
        intent.putExtra("files",fileNames); //moves sounds to be played to the AlarmReceiver
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show(); //shows message that alarm is set
    }

    //allows user to choose when to repeat alarm
    public void repeat(View v){
        //goes to AlarmRepeat page
        Intent intent = new Intent(this,AlarmRepeat.class);
        startActivity(intent);
    }

    //allows the user to edit each sound in the alarm
    public void editSounds(View v){
        //goes to SoundsEdit page
        Intent intent = new Intent(this,AllSounds.class);
        startActivity(intent);
    }
}