package com.levibostian.view.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_header.*

class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    companion object {
        @LayoutRes val layoutRes: Int = com.levibostian.R.layout.vh_header
    }

    override val containerView: View?
        get() = itemView

    fun populate(name: String) {
        title_textview.text = name
    }
}
