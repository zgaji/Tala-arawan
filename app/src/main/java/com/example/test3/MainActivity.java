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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
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
                TextView setalarmbtn = bottomSheetDialog.findViewById(R.id.setalarmbtn);
                TextView cancelalarmbtn = bottomSheetDialog.findViewById(R.id.cancelalarmbtn);

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

                                        // Set the date components of the calendar object
                                        calendar.set(Calendar.YEAR, year);
                                        calendar.set(Calendar.MONTH, month);
                                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    }
                                }, year, month, dayOfMonth);

                        datePickerDialog.show();
                    }
                });

                // ALARM PICKER
                alarmbtn_createtask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker = new MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_12H)
                                .setHour(12)
                                .setMinute(0)
                                .setTitleText("Select Alarm Time")
                                .build();

                        timePicker.show(getSupportFragmentManager(),"tala");
                        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                if (timePicker.getHour() > 12){
                                    alarmtxt_createtask.setText(
                                            String.format("%02d", (timePicker.getHour()-12))+":"+ String.format("%02d", timePicker.getMinute())+ "PM");
                                } else {
                                    alarmtxt_createtask.setText(timePicker.getHour()+":"+ timePicker.getMinute()+ "AM");
                                }

                                calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND,0);
                            }
                        });
                    }
                });

                setalarmbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (calendar != null) {
                            // Set the alarm using the calendar object
                            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                            // Use setExact() for precise timing
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            } else {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            }
                            Toast.makeText(MainActivity.this, "Alarm Set", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Please set both date and time", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancelalarmbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent, PendingIntent.FLAG_IMMUTABLE);

                        if (alarmManager == null){
                            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        }
                        alarmManager.cancel(pendingIntent);
                        Toast.makeText(MainActivity.this,"Alarm Cancelled", Toast.LENGTH_SHORT).show();
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