package com.example.averma1212.teleprompter.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.averma1212.teleprompter.R;
import com.example.averma1212.teleprompter.data.Script;
import com.example.averma1212.teleprompter.data.ScriptContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP on 07-01-2018.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.viewHolder> {
    private ArrayList<Script> scripts;
    private OnClickListener listener;

    public MainAdapter(ArrayList<Script> mScripts, OnClickListener mListener) {
        listener = mListener;
        scripts = mScripts;
    }

    public interface OnClickListener{
        void onClickListener(int position);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_script;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
            holder.bind(scripts.get(position).title,scripts.get(position).desc);
            holder.itemView.setContentDescription(scripts.get(position).title);
    }

    @Override
    public int getItemCount() {
        if(scripts==null)
            return 0;
        return scripts.size();
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.script_title)
        TextView title;
        @BindView(R.id.script_desc)
        TextView desc;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(String mTitle,String mDesc){
            title.setText(mTitle.substring(1));
            desc.setText(mDesc);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onClickListener(position);
        }
    }
}
