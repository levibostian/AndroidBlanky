package com.levibostian.androidblanky.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.levibostian.androidblanky.R;
import com.levibostian.androidblanky.fragment.OpenSourceLicensesFragment;

import java.util.ArrayList;

public class OpenSourceLicensesAdapter extends RecyclerView.Adapter<OpenSourceLicensesAdapter.ViewHolder> {

    private ArrayList<OpenSourceLicensesFragment.OpenSourceLicenseVo> mData;
    private ItemClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mLicenseTitle;
        private TextView mLicenseLink;

        public ViewHolder(View view) {
            super(view);

            mLicenseTitle = (TextView) view.findViewById(R.id.open_source_license_title);
            mLicenseLink = (TextView) view.findViewById(R.id.open_source_license_link);
        }
    }

    public interface ItemClickListener {
        void onItemClick(OpenSourceLicensesFragment.OpenSourceLicenseVo data, int position);
    }

    public OpenSourceLicensesAdapter(ArrayList<OpenSourceLicensesFragment.OpenSourceLicenseVo> data, ItemClickListener listener) {
        mData = data;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_source_list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        OpenSourceLicensesFragment.OpenSourceLicenseVo data = mData.get(position);

        holder.mLicenseTitle.setText(data.title);
        holder.mLicenseLink.setText(data.link);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(mData.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}