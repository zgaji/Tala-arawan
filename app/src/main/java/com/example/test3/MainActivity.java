package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView add_noteBTN = findViewById(R.id.add_noteBTN);

        add_noteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.activity_create_task);

                TextView addBtn = bottomSheetDialog.findViewById(R.id.addBTN);
                TextView cancelBtn = bottomSheetDialog.findViewById(R.id.cancelBTN);
                EditText titleEditText = bottomSheetDialog.findViewById(R.id.titleEditText);
                ImageView datebtn_createtask = bottomSheetDialog.findViewById(R.id.datebtn_createtask);
                TextView datetxt_createtask = bottomSheetDialog.findViewById(R.id.datetxt_createtask);
                ImageView alarmbtn_createtask = bottomSheetDialog.findViewById(R.id.alarmbtn_createtask);
                TextView alarmtxt_createtask = bottomSheetDialog.findViewById(R.id.alarmtxt_createtask);


                if (titleEditText != null) {
                    titleEditText.requestFocus();
                }

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss(); // Close the dialog
                    }
                });

                // CALENDAR VIEW
//                datebtn_createtask.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(MainActivity.this, ShowCalendar.class);
//                        startActivity(intent);
//                    }
//                });

                // DATE PICKER
                datebtn_createtask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                                        Calendar c = Calendar.getInstance();
                                        c.set(Calendar.YEAR, year);
                                        c.set(Calendar.MONTH, month);
                                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                        String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

                                        datetxt_createtask.setText(currentdate);

                                    }
                                }, year, month, dayOfMonth);

                        datePickerDialog.show();
                    }
                });

                // ALARM PICKER
                alarmbtn_createtask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar currentTime = Calendar.getInstance();
                        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = currentTime.get(Calendar.MINUTE);

                        // Create a TimePickerDialog and show it
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        int hour = hourOfDay % 12;
                                        if (hour == 0) {
                                            hour = 12; // 12 AM
                                        }

                                        String amPm = (hourOfDay < 12) ? "AM" : "PM";
                                        
                                        String selectedTime = String.format("%02d:%02d %s", hour, minute, amPm);
                                        alarmtxt_createtask.setText(selectedTime);
                                    }
                                }, hour, minute, false);

                        // Show the TimePickerDialog
                        timePickerDialog.show();
                    }
                });


                // Set TextChangedListener to title EditText to enable/disable add button
                if (titleEditText != null && addBtn != null) {
                    titleEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // Enable/disable add button based on text input
                            if (s.toString().trim().isEmpty()) {
                                addBtn.setEnabled(false);
                                addBtn.setTextColor(Color.parseColor("#abaab0")); // Change text color to gray
                            } else {
                                addBtn.setEnabled(true);
                                addBtn.setTextColor(Color.parseColor("#C57cff")); // Change text color back to black
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }


                    });
                }
                bottomSheetDialog.show();
            }
        });

    }
}