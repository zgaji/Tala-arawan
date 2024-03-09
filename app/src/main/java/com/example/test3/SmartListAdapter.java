package com.example.test3;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SmartListAdapter extends RecyclerView.Adapter<SmartListAdapter.ViewHolder> {

    private List<SmartListData> items;
    private Context context;

    public SmartListAdapter(List<SmartListData> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_smartlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SmartListData dataList = items.get(position);

        holder.imageView.setImageResource(dataList.getImage());
        holder.textTextView.setText(dataList.getTextName());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(dataList.getCircleColor());
        holder.circle.setBackground(drawable);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.getTextName().equals("Completed")) {
                    Intent intent = new Intent(context, CompletedHistory.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView numberTextView;
        public TextView textTextView;
        public View circle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textTextView = itemView.findViewById(R.id.textTextView);
            circle = itemView.findViewById(R.id.circle);
        }
    }
}
