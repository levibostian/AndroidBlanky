package com.levibostian.view.ui.adapter.viewholder

import android.net.Uri
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_header.*

class RepoViewHolder(val view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    companion object {
        @LayoutRes val layoutRes: Int = R.layout.vh_repo
    }

    override val containerView: View?
        get() = itemView

    fun populate(name: String) {
        name_textview.text = name
    }

}