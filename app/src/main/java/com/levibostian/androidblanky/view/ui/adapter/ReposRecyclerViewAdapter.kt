package com.levibostian.androidblanky.view.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.model.RepoModel
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.adapter_repos.view.*

class ReposRecyclerViewAdapter(val data: RealmResults<RepoModel>) : RealmRecyclerViewAdapter<RepoModel, ReposRecyclerViewAdapter.ViewHolder>(data, true) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var reposNameTextView: TextView = view.adapter_repos_repo_name
    }

    override fun onBindViewHolder(holder: ReposRecyclerViewAdapter.ViewHolder?, position: Int) {
        val adapterItem: RepoModel = data[position]!!

        holder!!.reposNameTextView.text = adapterItem.full_name
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ReposRecyclerViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_repos, parent, false))
    }

}