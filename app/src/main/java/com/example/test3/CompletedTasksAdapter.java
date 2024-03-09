package com.example.test3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CompletedTasksAdapter extends FirebaseRecyclerAdapter<MainModel, CompletedTasksAdapter.CompletedTasksViewHolder> {

    public CompletedTasksAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CompletedTasksViewHolder holder, int position, @NonNull MainModel model) {
        holder.titleTextView.setText(model.getTaskTitle());
        holder.contentTextView.setText(model.getTaskDesc());
        holder.dateInitTextView.setText(model.getCreationDate());
        holder.completedTextView.setText(model.getCompletedDate());

        String completedDate = model.getCompletedDate();
        if (completedDate != null && !completedDate.isEmpty()) {
            try {
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = inputDateFormat.parse(completedDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                String formattedDate = new SimpleDateFormat("EEE, MMMM d, yyyy", Locale.getDefault()).format(calendar.getTime());
                holder.completedTextView.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.completedTextView.setText("");
        }
    }

    @NonNull
    @Override
    public CompletedTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_item, parent, false);
        return new CompletedTasksViewHolder(view);
    }

    public static class CompletedTasksViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView, dateInitTextView, completedTextView;

        public CompletedTasksViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.C_titleTextView);
            contentTextView = itemView.findViewById(R.id.C_contentTextView);
            dateInitTextView = itemView.findViewById(R.id.C_dateInitTextView);
            completedTextView = itemView.findViewById(R.id.completedTextView);
        }
    }
}