package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView smartListRecyclerView;
    private SmartListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView add_noteBTN = findViewById(R.id.add_noteBTN);

        smartListRecyclerView = findViewById(R.id.smartlist_recycler_view);
        smartListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Sample data
        List<SmartListData> dataList = new ArrayList<>();
        dataList.add(new SmartListData("All", "1", R.drawable.ic_calendar, Color.parseColor("#a6d3f2")));
        dataList.add(new SmartListData("Scheduled", "2", R.drawable.ic_calendar, Color.parseColor("#fcc7e1")));
        dataList.add(new SmartListData("Favorites", "3", R.drawable.ic_calendar, Color.parseColor("#afffca")));

        adapter = new SmartListAdapter(dataList);
        smartListRecyclerView.setAdapter(adapter);


        add_noteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.activity_create_task);

                TextView addBtn = bottomSheetDialog.findViewById(R.id.addBTN);
                EditText titleEditText = bottomSheetDialog.findViewById(R.id.titleEditText);

                if (titleEditText != null) {
                    titleEditText.requestFocus();
                }

                TextView cancelBtn = bottomSheetDialog.findViewById(R.id.cancelBTN);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss(); // Close the dialog
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