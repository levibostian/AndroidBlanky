package com.levibostian.androidblanky.adapter.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.fragment.OpenSourceLicensesFragment
import org.w3c.dom.Text

import java.util.ArrayList

class OpenSourceLicensesAdapter(private val mData: ArrayList<OpenSourceLicensesFragment.OpenSourceLicenseVo>, private val mListener: ItemClickListener) : RecyclerView.Adapter<OpenSourceLicensesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var mLicenseTitle: TextView
        lateinit var mLicenseLink: TextView

        init {
            mLicenseTitle = view.findViewById(R.id.open_source_license_title) as TextView
            mLicenseLink = view.findViewById(R.id.open_source_license_link) as TextView
        }
    }

    interface ItemClickListener {
        fun onItemClick(data: OpenSourceLicensesFragment.OpenSourceLicenseVo, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.open_source_list_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val data: OpenSourceLicensesFragment.OpenSourceLicenseVo = mData[position]

        holder!!.mLicenseTitle.text = data.title
        holder.mLicenseLink.text = data.link
    }

    override fun getItemCount(): Int {
        return mData.size
    }

}