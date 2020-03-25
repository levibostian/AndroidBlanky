package com.levibostian.view.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.levibostian.R
import com.levibostian.service.model.RepoModel

class ReposRecyclerViewAdapter(private val repos: List<RepoModel>): RecyclerView.Adapter<ReposRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int = repos.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.repo_name_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_repo_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repos[position]

        holder.nameTextView.text = repo.name
    }

}