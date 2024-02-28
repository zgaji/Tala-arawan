package com.example.test3;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CreateTask extends AppCompatActivity {

    //    int myear, mmonth, mday;
//    DatePickerDialog datePickerDialog;
    //EditText setcalendar = bottomSheetDialog.findViewById(R.id.setcalendar);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);


//        setcalendar.setOnTouchListener((view, motionEvent) ->{
//            if(motionEvent.getAction() == MotionEvent.ACTION_UP){
//                final Calendar c = Calendar.getInstance();
//                myear = c.get(Calendar.YEAR);
//                mmonth = c.get(Calendar.MONTH);
//                mday = c.get(Calendar.DAY_OF_MONTH);
//                datePickerDialog = new DatePickerDialog(getActivity(),
//                        (view1, year, monthOfYear, dayOfMonth) -> {
//                            setcalendar.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                            datePickerDialog.dismiss();
//                        }, myear, mmonth, mday);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                datePickerDialog.show();
//            }
//            return true;
//        });
//
//        setalarm.setOnTouchListener((view, motionEvent) -> {
//            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                // Get Current Time
//                final Calendar c = Calendar.getInstance();
//                mhour = c.get(Calendar.HOUR_OF_DAY);
//                mminute = c.get(Calendar.MINUTE);
//
//                // Launch Time Picker Dialog
//                timePickerDialog = new TimePickerDialog(getActivity(),
//                        (view12, hourOfDay, minute) -> {
//                            setalarm.setText(hourOfDay + ":" + minute);
//                            timePickerDialog.dismiss();
//                        }, mhour, mminute, false);
//                timePickerDialog.show();
//            }
//            return true;
//        });

    }
}