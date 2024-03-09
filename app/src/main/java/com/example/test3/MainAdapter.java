package com.example.test3;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

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
    private FirebaseRecyclerOptions<MainModel> mSnapshots;

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

        holder.monthTextView.setText(model.getLastOpenedDate());
        holder.weekdayTextView.setText(model.getLastOpenedDate());
        holder.dayTextView.setText(model.getLastOpenedDate());

        String taskDescription = model.getTaskDesc();
        if (taskDescription.length() > 20) {
            taskDescription = taskDescription.substring(0, 20) + "...";
        }
        holder.taskDesc.setText(taskDescription);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTask createTask = new CreateTask();
                createTask.setData(model);
                createTask.setAdapter(MainAdapter.this);
                createTask.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "CreateTask");

                DatabaseReference taskRef = getRef(position);
                taskRef.child("lastOpenedDate").setValue(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });


        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.options);
                popupMenu.inflate(R.menu.options); // Assuming you have a menu XML file named options_menu
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.menuDelete) {
                            // Delete action
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("Delete Task");
                            builder.setMessage("Are you sure you want to delete this task?");
                            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getRef(position).removeValue();
                                    Toast.makeText(mContext, "Task '" + model.getTaskTitle() + "' deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                            return true;
                        } else if (itemId == R.id.menuComplete) {
                            // Complete action
                            DatabaseReference taskRef = getRef(position);
                            taskRef.child("isComplete").setValue("true")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(mContext, "Task '" + model.getTaskTitle() + "' completed", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(mContext, "Failed to mark task '" + model.getTaskTitle() + "' as completed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        String lastOpenedDate = model.getLastOpenedDate();
        if (lastOpenedDate != null && !lastOpenedDate.isEmpty()) {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date date = inputDateFormat.parse(lastOpenedDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                String monthName = new SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.getTime());
                String dayOfWeek = new SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.getTime());
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                holder.monthTextView.setText(monthName);
                holder.weekdayTextView.setText(dayOfWeek);
                holder.dayTextView.setText(String.valueOf(dayOfMonth));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.monthTextView.setText("");
            holder.weekdayTextView.setText("");
            holder.dayTextView.setText("");
        }
    }
    


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView taskTitle, taskId, taskDesc, dateCreated, isComplete, alarm, weekdayTextView, dayTextView, monthTextView;
        ImageView options;
        CardView cardView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle= (TextView) itemView.findViewById(R.id.titleTextView);
            taskDesc = (TextView) itemView.findViewById(R.id.contentTextView);
            dateCreated = (TextView) itemView.findViewById(R.id.dateInitTextView);
            weekdayTextView = (TextView) itemView.findViewById(R.id.weekdayTextView);
            dayTextView = (TextView) itemView.findViewById(R.id.dayTextView);
            monthTextView = (TextView) itemView.findViewById(R.id.monthTextView);
            cardView = itemView.findViewById(R.id.cardView);
            options = itemView.findViewById(R.id.options);
        }
    }
}
