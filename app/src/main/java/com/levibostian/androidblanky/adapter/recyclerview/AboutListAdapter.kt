package com.levibostian.androidblanky.adapter.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.levibostian.androidblanky.R
import java.util.*

class AboutListAdapter(private val mData: ArrayList<String>, private val mListener: ItemClickListener) : RecyclerView.Adapter<AboutListAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val mListTitle: TextView

        init {
            mListTitle = view.findViewById(R.id.about_list_title_textview) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.about_list_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder!!.mListTitle.text = mData[position]
        holder.mListTitle.setOnClickListener {
            mListener.onItemClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

}