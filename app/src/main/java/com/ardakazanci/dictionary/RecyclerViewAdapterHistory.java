package com.ardakazanci.dictionary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.HistoryViewHolder> {

    private ArrayList<History> histories;
    private Context context;

    public RecyclerViewAdapterHistory(Context context, ArrayList<History> histories) {
        this.histories = histories;
        this.context = context;
    }

    public  class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView enWord;
        TextView enDef;


        public HistoryViewHolder(View v) {
            super(v);
            enWord = (TextView) v.findViewById(R.id.en_word);
            enDef = (TextView) v.findViewById(R.id.en_def);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String text = histories.get(position).get_en_word();

                    Intent intent = new Intent(context, WordMeaningActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word", text);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }


    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        holder.enWord.setText(histories.get(position).get_en_word());
        holder.enDef.setText(histories.get(position).get_def());

    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}
