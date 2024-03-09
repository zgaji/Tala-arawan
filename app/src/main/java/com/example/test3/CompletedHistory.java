package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CompletedHistory extends AppCompatActivity {

    private RecyclerView completedTasksRecyclerView;
    private CompletedTasksAdapter adapter;
    private DatabaseReference completedTasksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_history);

        completedTasksRecyclerView = findViewById(R.id.HistoryRecyclerView);
        completedTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        completedTasksRef = FirebaseDatabase.getInstance().getReference().child("tasks");
        Query completedTasksQuery = completedTasksRef.orderByChild("isComplete").equalTo("true");

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(completedTasksQuery, MainModel.class)
                        .build();

        adapter = new CompletedTasksAdapter(options);
        completedTasksRecyclerView.setAdapter(adapter);

        TextView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompletedHistory.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
