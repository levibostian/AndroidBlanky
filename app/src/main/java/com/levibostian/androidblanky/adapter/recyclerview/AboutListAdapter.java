package com.levibostian.androidblanky.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.levibostian.androidblanky.R;

import java.util.ArrayList;

public class AboutListAdapter extends RecyclerView.Adapter<AboutListAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private ItemClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mListTitle;

        public ViewHolder(View view) {
            super(view);

            mListTitle = (TextView) view.findViewById(R.id.about_list_title_textview);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public AboutListAdapter(ArrayList<String> data, ItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @Override
    public AboutListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mListTitle.setText(mData.get(position));
        holder.mListTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
