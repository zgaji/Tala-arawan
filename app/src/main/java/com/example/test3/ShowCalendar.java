package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;

import com.applandeo.materialcalendarview.EventDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calendar);

//        List<EventDay> events = new ArrayList<>();
//
//        Calendar calendar = Calendar.getInstance();
//        events.add(new EventDay(calendar, R.drawable.ic_calendar, Color.parseColor("#228B22")));
//
//        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
//        calendarView.setEvents(events);

//        public List<EventDay> getHighlitedDays() {
//            List<EventDay> events = new ArrayList<>();
//
//            for(int i = 0; i < tasks.size(); i++) {
//                Calendar calendar = Calendar.getInstance();
//                String[] items1 = tasks.get(i).getDate().split("-");
//                String dd = items1[0];
//                String month = items1[1];
//                String year = items1[2];
//
//                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
//                calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
//                calendar.set(Calendar.YEAR, Integer.parseInt(year));
//                events.add(new EventDay(calendar, R.drawable.dot));
//            }
//            return events;
//        }

    }
}