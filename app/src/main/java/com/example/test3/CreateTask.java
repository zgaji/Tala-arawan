package com.example.test3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CreateTask extends BottomSheetDialogFragment {

    private MainModel mainModel;
    private MainAdapter mainAdapter;
    private DatabaseReference databaseReference;
    private boolean dataChanged = false;
    private MaterialTimePicker timePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private EditText titleEditText;
    private EditText notesEditText;
    private String password;


    // Method to set data from MainAdapter
    public void setData(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    // Method to set the adapter
    public void setAdapter(MainAdapter adapter) {
        this.mainAdapter = adapter;
    }

    // Method to set the password
    public void setPassword(String password) {
        this.password = password;
    }


    // Method to add data to Firebase
    private void addDataToFirebase(String title, String notes, String date, String alarm, String password) {
        String taskId = databaseReference.push().getKey();
        String creationDate = DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
        Calendar calendar = Calendar.getInstance();
        String lastOpenedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        MainModel newTask  = new MainModel(title, taskId, alarm, notes, date, "false", lastOpenedDate, creationDate, password, "");
        databaseReference.child("tasks").child(taskId).setValue(newTask);

        //mainAdapter.insertItem(newTask);

        mainModel = newTask;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_task, container, false);

        TextView addBtn = view.findViewById(R.id.addBTN);
        TextView cancelBtn = view.findViewById(R.id.cancelBTN);
        titleEditText = view.findViewById(R.id.titleEditText);
        notesEditText = view.findViewById(R.id.notesEditText);
        ImageView datebtn_createtask = view.findViewById(R.id.datebtn_createtask);
        TextView datetxt_createtask = view.findViewById(R.id.datetxt_createtask);
        ImageView alarmbtn_createtask = view.findViewById(R.id.alarmbtn_createtask);
        TextView alarmtxt_createtask = view.findViewById(R.id.alarmtxt_createtask);
        TextView setalarmbtn = view.findViewById(R.id.setalarmbtn);
        TextView cancelalarmbtn = view.findViewById(R.id.cancelalarmbtn);
        ImageView lockbtn = view.findViewById(R.id.lockbtn);
        EditText passwordEditText = view.findViewById(R.id.passwordEditText);

        addBtn.setEnabled(false); // Disable add button initially
        databaseReference = FirebaseDatabase.getInstance().getReference();


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataChanged) {
                    String title = titleEditText.getText().toString();
                    String notes = notesEditText.getText().toString();
                    String date = datetxt_createtask.getText().toString();
                    String alarm = alarmtxt_createtask.getText().toString();
                    String password = passwordEditText.getText().toString();  // Get the password from the UI

                    if (mainModel != null) {
                        // Update existing data in Firebase
                        String storedPassword = mainModel.getTaskPassword();


                        if (!TextUtils.isEmpty(password) && !password.equals(storedPassword)) {
                            Toast.makeText(requireContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                            mainModel.setTaskTitle(title);
                            mainModel.setTaskDesc(notes);
                            mainModel.setDate(date);
                            mainModel.setAlarm(alarm);
                            mainModel.setTaskPassword(password); // Update the password

                            databaseReference.child("tasks").child(mainModel.getTaskId()).setValue(mainModel);
                            Toast.makeText(requireContext(), "Task Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        // Add new data to Firebase
                        addDataToFirebase(title, notes, date, alarm, password);
                        //mainAdapter.insertItem(mainModel);
                    }

                    // Notify MainAdapter to update the data set
                    if (mainAdapter != null) {
                        mainAdapter.notifyDataSetChanged();

                    }

                    dismiss();
                } else {

                }
            }
        });

        // Check if the fragment received existing data to edit
        if (mainModel != null) {
            addBtn.setText("Update");
            titleEditText.setText(mainModel.getTaskTitle());
            notesEditText.setText(mainModel.getTaskDesc());
            datetxt_createtask.setText(mainModel.getDate());
            alarmtxt_createtask.setText(mainModel.getAlarm());
        }

        if (titleEditText != null) {
            titleEditText.requestFocus();
        }


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // DATE PICKER
        datebtn_createtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(datetxt_createtask);
            }
        });

        // ALARM PICKER
        alarmbtn_createtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(alarmtxt_createtask);
            }
        });

        lockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setLock();
            }
        });

        setalarmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        cancelalarmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        // Add a TextChangedListener to the titleEditText
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEnableAddButton(addBtn, titleEditText, notesEditText, datetxt_createtask, alarmtxt_createtask);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add a TextChangedListener to the notesEditText
        notesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEnableAddButton(addBtn, titleEditText, notesEditText, datetxt_createtask, alarmtxt_createtask);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add a TextChangedListener to the datetxt_createtask
        datetxt_createtask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEnableAddButton(addBtn, titleEditText, notesEditText, datetxt_createtask, alarmtxt_createtask);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Add a TextChangedListener to the alarmtxt_createtask
        alarmtxt_createtask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEnableAddButton(addBtn, titleEditText, notesEditText, datetxt_createtask, alarmtxt_createtask);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }


    private void checkEnableAddButton(TextView addBtn, EditText titleEditText, EditText notesEditText, TextView datetxt_createtask, TextView alarmtxt_createtask) {
        boolean anyFieldNotEmpty = !TextUtils.isEmpty(titleEditText.getText()) || !TextUtils.isEmpty(notesEditText.getText()) || !TextUtils.isEmpty(datetxt_createtask.getText()) || !TextUtils.isEmpty(alarmtxt_createtask.getText());
        addBtn.setEnabled(anyFieldNotEmpty);
        dataChanged = true;
        addBtn.setTextColor(ContextCompat.getColor(requireContext(), anyFieldNotEmpty ? android.R.color.holo_purple : android.R.color.darker_gray));
    }

    private void showDatePickerDialog(TextView datetxt_createtask) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(R.style.DatePickerTheme)
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Handle the selection of date
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            datetxt_createtask.setText(currentDate);
        });

        datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");
    }


    private void showTimePickerDialog(TextView alarmtxt_createtask) {
        // Implement time picker dialog logic
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTheme(R.style.TimePickerTheme)
                .setTitleText("Select Alarm Time")
                .build();

        timePicker.show(requireActivity().getSupportFragmentManager(), "timePicker");
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s",
                        hour > 12 ? hour - 12 : hour,
                        minute, hour >= 12 ? "PM" : "AM");

                alarmtxt_createtask.setText(formattedTime);

                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });

    }

    private void setAlarm() {
        String title = titleEditText.getText().toString();
        String text = notesEditText.getText().toString();

        // Set alarm logic
        if (calendar != null) {
            alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(requireContext(), AlarmReceiver.class);

            // Pass the title as an extra in the intent
            intent.putExtra("title", title);
            intent.putExtra("text", text);

            // Use a unique request code for each alarm (here, taskId is used as the request code)
            int requestCode = mainModel != null ? mainModel.getTaskId().hashCode() : title.hashCode();
            pendingIntent = PendingIntent.getBroadcast(requireContext(), requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(requireContext(), "Alarm Set", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Please select alarm time", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelAlarm() {
        // Cancel alarm logic
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(requireContext(), "Alarm Cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "No alarm to cancel", Toast.LENGTH_SHORT).show();
        }
    }
}