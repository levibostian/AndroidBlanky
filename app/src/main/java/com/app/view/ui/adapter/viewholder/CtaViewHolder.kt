package com.app.view.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.app.view.type.CTA
import com.app.view.widget.CTAView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.vh_cta.*

class CtaViewHolder(val view: View) : RecyclerView.ViewHolder(view), LayoutContainer {

    companion object {
        @LayoutRes val layoutRes: Int = com.app.R.layout.vh_cta
    }

    override val containerView: View?
        get() = itemView

    lateinit var listener: CTAView.Listener

    fun populate(cta: CTA) {
        cta_view.populate(header = null, subheader = cta.title, links = cta.links, notice = cta.notice)
        cta_view.listener = listener
    }
}
