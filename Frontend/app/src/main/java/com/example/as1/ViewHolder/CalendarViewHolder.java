package com.example.as1.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as1.Adapters.CalendarAdapter;
import com.example.as1.R;

import java.time.LocalDate;
import java.util.ArrayList;


public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
{
    private final ArrayList<LocalDate> days;
    public final View parentView, coloredRectangleGold, coloredRectangleCardinal, coloredRectangleBlue;
    public final TextView dayOfMonth;
    private final CalendarAdapter.OnItemListener onItemListener;
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days)
    {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
        this.days = days;
        coloredRectangleGold = itemView.findViewById(R.id.coloredRectangleGold);
        coloredRectangleCardinal = itemView.findViewById(R.id.coloredRectangleCardinal);
        coloredRectangleBlue = itemView.findViewById(R.id.coloredRectangleBlue);

    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), days.get(getAdapterPosition()));
    }

    @Override
    public boolean onLongClick(View view) {
        onItemListener.onItemLongClick(getAdapterPosition(), days.get(getAdapterPosition()));
        return true;
    }
}