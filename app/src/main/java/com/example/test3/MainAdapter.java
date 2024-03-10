package com.example.test3;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                taskRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            MainModel model = snapshot.getValue(MainModel.class);
                            if (model != null) {
                                String storedPassword = model.getTaskPassword();

                                if (TextUtils.isEmpty(storedPassword)) {
                                    // No password is set, open the CreateTask dialog directly
                                    openCreateTaskDialog(model);
                                } else {
                                    // Display a password input dialog
                                    showPasswordInputDialog(taskRef, storedPassword, position, model);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            private void showPasswordInputDialog(DatabaseReference taskRef, String storedPassword, int position, MainModel model) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.inputdialog_layout, null);

                EditText passEditText = view.findViewById(R.id.passEditText);
                AlertDialog alertDialog = new MaterialAlertDialogBuilder(mContext, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                        .setTitle("Lock")
                        .setView(view)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                alertDialog.show();

                // Override the positive button to handle custom logic
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String enteredPassword = passEditText.getText().toString();

                        if (enteredPassword.equals(storedPassword)) {
                            openCreateTaskDialog(model);

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
                                            // Handle onFailure
                                        }
                                    });

                            alertDialog.dismiss(); // Dismiss the dialog after successful password entry
                        } else {
                            // Incorrect password, show a message
                            Toast.makeText(mContext, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private void openCreateTaskDialog(MainModel model) {
                CreateTask createTask = new CreateTask();
                createTask.setData(model);
                createTask.setAdapter(MainAdapter.this);

                createTask.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "CreateTask");
            }
        });


        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.options);
                popupMenu.inflate(R.menu.options);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.menuDelete) {
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext,R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                                    .setTitle("Delete Task")
                                    .setMessage("Are you sure you want to delete this task?")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getRef(position).removeValue();
                                        Toast.makeText(mContext, "Task '" + model.getTaskTitle() + "' deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
                            String completionDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            taskRef.child("isComplete").setValue("true");
                            taskRef.child("completedDate").setValue(completionDate)
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

        if (TextUtils.isEmpty(model.getTaskPassword())) {
            String taskDescription = model.getTaskDesc();
            if (taskDescription.length() > 20) {
                taskDescription = taskDescription.substring(0, 20) + "...";
            }
            holder.taskDesc.setText(taskDescription);
        } else {
            holder.taskDesc.setText("Locked");
        }

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

    public void addToTop(MainModel newTask) {
        dataList.add(0, newTask);
        notifyItemInserted(0);
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
