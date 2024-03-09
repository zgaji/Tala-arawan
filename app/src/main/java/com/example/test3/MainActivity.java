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
import android.content.SharedPreferences;
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
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.Switch;
import android.widget.CompoundButton;


public class MainActivity extends AppCompatActivity {

    private RecyclerView smartListRecyclerView;
    private SmartListAdapter adapter;
    private MaterialTimePicker timePicker;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    RecyclerView task_recycler_view;
    MainAdapter mainAdapter;
    private SwitchCompat switchMode;
    private boolean nightMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

        setContentView(R.layout.activity_main);

        ImageView add_noteBTN = findViewById(R.id.add_noteBTN);
        ImageView calendarviewbtn = findViewById(R.id.calendarviewbtn);

        task_recycler_view = (RecyclerView) findViewById(R.id.task_recycler_view);
        task_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        //SMART LIST
        smartListRecyclerView = findViewById(R.id.smartlist_recycler_view);
        smartListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("tasks"), MainModel.class)
                        .build();

        mainAdapter = new MainAdapter(options, this);
        task_recycler_view.setAdapter(mainAdapter);

        // Sample data of SmartList
        List<SmartListData> dataList = new ArrayList<>();
        dataList.add(new SmartListData("Today", "1", R.drawable.ic_calendar, Color.parseColor("#a6d3f2")));
        dataList.add(new SmartListData("Completed", "2", R.drawable.ic_calendar, Color.parseColor("#fcc7e1")));
        dataList.add(new SmartListData("Favorites", "3", R.drawable.ic_calendar, Color.parseColor("#afffca")));

        adapter = new SmartListAdapter(dataList);
        smartListRecyclerView.setAdapter(adapter);

        //SWITCH MODE
        switchMode = findViewById(R.id.switchMode);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (nightMode) {
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                }
                editor.apply();
            }
        });


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
                ShowCalendar showCalendar = new ShowCalendar();
                //showCalendar.setTaskDates(mainAdapter.getTaskDates());
                showCalendar.show(getSupportFragmentManager(), "ShowCalendar");
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "tala_arawan";
            String desc = "Channel for Alarm Manager";
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("tala", name, imp);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }

    @Override
    protected void onStart() {
        mainAdapter.startListening();
        super.onStart();

    }

    @Override
    protected void onStop() {
        mainAdapter.stopListening();
        super.onStop();
    }
}