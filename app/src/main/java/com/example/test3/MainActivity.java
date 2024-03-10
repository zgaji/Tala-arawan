package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class MainActivity extends AppCompatActivity {

    private RecyclerView smartListRecyclerView;
    private SmartListAdapter adapter;
    RecyclerView task_recycler_view;
    MainAdapter mainAdapter;
    private SwitchCompat switchMode;
    private boolean nightMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DatabaseReference tasksRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

        setContentView(R.layout.activity_main);

        ImageView add_noteBTN = findViewById(R.id.add_noteBTN);
        ImageView calendarviewbtn = findViewById(R.id.calendarviewbtn);

        task_recycler_view = (RecyclerView) findViewById(R.id.task_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        task_recycler_view.setLayoutManager(layoutManager);

        EditText searchEditText = findViewById(R.id.search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mainAdapter.filter(s.toString()); // Call filter method with search query
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //SMART LIST
        smartListRecyclerView = findViewById(R.id.smartlist_recycler_view);
        smartListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        tasksRef = FirebaseDatabase.getInstance().getReference().child("tasks");
        Query taskQuery = tasksRef.orderByChild("isComplete").equalTo("false");

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(taskQuery, MainModel.class)
                        .build();


        mainAdapter = new MainAdapter(options, this);
        task_recycler_view.setAdapter(mainAdapter);


        // Sample data of SmartList
        List<SmartListData> dataList = new ArrayList<>();
        dataList.add(new SmartListData("Today",  R.drawable.ic_calendar, Color.parseColor("#a6d3f2")));
        dataList.add(new SmartListData("Completed", R.drawable.ic_calendar, Color.parseColor("#fcc7e1")));
        dataList.add(new SmartListData("Favorites", R.drawable.ic_calendar, Color.parseColor("#afffca")));

        adapter = new SmartListAdapter(dataList, this);
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

    public void addNewTask(MainModel newTask) {
        mainAdapter.addToTop(newTask);
    }
}