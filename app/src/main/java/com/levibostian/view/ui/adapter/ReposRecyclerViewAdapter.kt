package com.levibostian.view.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ReposRecyclerViewAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        REPO,
        CTA;

        companion object {
            fun from(value: Int) = values()[value]
        }
    }

    interface Listener: CTAView.Listener {
        fun programItemClicked(item: ProgramListItem)
    }

    lateinit var listener: Listener

    data class RowItem(val viewType: ViewType, val item: ProgramListItem)

    var items: List<ProgramListItem> = emptyList()
        set(newValue) {
            this.data = newValue.map { item ->
                when (item) {
                    is ProgramListItem.Repo -> RowItem(ViewType.REPO, item)
                    is ProgramListItem.Cta -> RowItem(ViewType.CTA, item)
                }
            }

            field = newValue
        }

    private var data: List<RowItem> = emptyList()
        set(newValue) {
            val oldValue = field

            DiffUtil.calculateDiff(DiffCallback(oldValue, newValue)).apply {
                field = newValue
                dispatchUpdatesTo(this@ReposRecyclerViewAdapter)
            }
        }

    companion object {
        class DiffCallback(private val oldList: List<RowItem>, private val newList: List<RowItem>) : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldList.size
            override fun getNewListSize(): Int = newList.size
            override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean = areContentsTheSame(oldPosition, newPosition)
            override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean = oldList[oldPosition] == newList[newPosition]
        }
    }

    override fun getItemViewType(position: Int): Int = this.data[position].viewType.ordinal
    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.from(viewType)) {
            ViewType.REPO -> RepoViewHolder(view(parent, RepoViewHolder.layoutRes))
            ViewType.CTA -> CtaViewHolder(view(parent, CtaViewHolder.layoutRes))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rowItem = this.data[position]

        var programViewHolder: RepoViewHolder? = null
        when (rowItem.viewType) {
            ViewType.REPO -> {
                programViewHolder = holder as RepoViewHolder

                val item = rowItem.item as RepoListItem.Prog
                programViewHolder.populate(item.program.name, item.program.image_url)
            }
            ViewType.CTA -> {
                holder as CtaViewHolder
                val item = rowItem.item as RepoListItem.Cta
                holder.listener = listener
                holder.populate(item.cta)
            }
        }

        programViewHolder?.itemView?.setOnClickListener {
            listener.programItemClicked(rowItem.item)
        }
    }

}