package com.example.test3;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private Context mContext;
    private List<MainModel> dataList;

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options, Context context) {
        super(options);
        this.mContext = context;
        this.dataList = new ArrayList<>();
    }

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.dateCreated.setText(model.getCreationDate());
        holder.taskDesc.setText(model.getTaskDesc());
        holder.taskTitle.setText(model.getTaskTitle());

        // Set OnClickListener for the CardView
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTask createTask = new CreateTask();
                createTask.setData(model);
                createTask.setAdapter(MainAdapter.this);
                createTask.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "CreateTask");

                String lastOpenedDate = model.getLastOpenedDate();
                if (lastOpenedDate != null && !lastOpenedDate.isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    try {
                        Date date = dateFormat.parse(lastOpenedDate);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
                        String monthName = new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime());
                        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.getTime());

                        holder.dayTextView.setText(String.valueOf(dayOfMonth));
                        holder.dateTextView.setText(weekday);
                        holder.monthTextView.setText(month);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Set OnClickListener for the deleteImageView
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure you want to delete this task?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the data from Firebase
                        getRef(position).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView taskTitle, taskId, taskDesc, dateCreated, isComplete, alarm, dayTextView, dateTextView, monthTextView;
        ImageView deleteImageView;
        CardView cardView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle= (TextView) itemView.findViewById(R.id.titleTextView);
            taskDesc = (TextView) itemView.findViewById(R.id.contentTextView);
            dateCreated = (TextView) itemView.findViewById(R.id.dateInitTextView);
            dayTextView = (TextView) itemView.findViewById(R.id.dayTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            monthTextView = (TextView) itemView.findViewById(R.id.monthTextView);
            cardView = itemView.findViewById(R.id.cardView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
        }
    }
}
