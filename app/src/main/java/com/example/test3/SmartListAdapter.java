package com.example.test3;

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

    public SmartListAdapter(List<SmartListData> items) {
        this.items = items;
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
        holder.numberTextView.setText(String.valueOf(dataList.getTextCount()));
        holder.textTextView.setText(dataList.getTextName());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(dataList.getCircleColor());
        holder.circle.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView numberTextView;
        public TextView textTextView;

        public View circle; // Add circle view

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            textTextView = itemView.findViewById(R.id.textTextView);
            circle = itemView.findViewById(R.id.circle);
        }
    }
}
