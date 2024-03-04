package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContentInfo;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test3.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView smartListRecyclerView;
    private SmartListAdapter adapter;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

        setContentView(R.layout.activity_main);

        ImageView add_noteBTN = findViewById(R.id.add_noteBTN);
        ImageView calendarviewbtn = findViewById(R.id.calendarviewbtn);


        smartListRecyclerView = findViewById(R.id.smartlist_recycler_view);
        smartListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Sample data
        List<SmartListData> dataList = new ArrayList<>();
        dataList.add(new SmartListData("Today", "1", R.drawable.ic_calendar, Color.parseColor("#a6d3f2")));
        dataList.add(new SmartListData("Scheduled", "2", R.drawable.ic_calendar, Color.parseColor("#fcc7e1")));
        dataList.add(new SmartListData("Favorites", "3", R.drawable.ic_calendar, Color.parseColor("#afffca")));

        adapter = new SmartListAdapter(dataList);
        smartListRecyclerView.setAdapter(adapter);

        //Adding note/reminder
        add_noteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTask createTask = new CreateTask();
                createTask.show(getSupportFragmentManager(), "CreateTask");
            }
        });

        //CALENDAR VIEW
        calendarviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the bottom sheet dialog
                BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog1.setContentView(R.layout.activity_show_calendar);

//                // Initialize views after the dialog is shown
//                ImageView backbtn_calendar = bottomSheetDialog1.findViewById(R.id.backbtn_calendar);
//                CalendarView calendarView = bottomSheetDialog1.findViewById(R.id.calendarView);
//
//                backbtn_calendar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Dismiss the bottom sheet dialog
//                        bottomSheetDialog1.dismiss();
//                    }
//                });

                // Show the bottom sheet dialog
                bottomSheetDialog1.show();
            }
        });
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "tala_arawan";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("tala", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}