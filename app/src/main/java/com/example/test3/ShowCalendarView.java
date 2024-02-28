package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class ShowCalendarView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calendar_view);

        //CALENDAR VIEW
        //CalendarView calendarView = findViewById(R.id.calendarView);


        ImageView backbtn_calendar = findViewById(R.id.backbtn_calendar);

        backbtn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}

