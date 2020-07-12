package com.levibostian.view.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.ViewInfo
import com.levibostian.extensions.view
import com.levibostian.service.type.list_item.RepoListItem
import com.levibostian.view.ui.adapter.viewholder.CtaViewHolder
import com.levibostian.view.ui.adapter.viewholder.RepoViewHolder
import com.levibostian.view.widget.CTAView

/**
 * This is an example adapter that can:
 * 1. Contain different sections
 * 2. Diffable
 * 3. Uses external ViewHolders so they can be shared between adapters. Adapters sometimes are unique to a screen as that screen needs something done in a certain way.
 *
 * Use MergeAdapter or ConcatAdapter if you want to combine multiple adapters together: https://developer.android.com/reference/androidx/recyclerview/widget/MergeAdapter, https://medium.com/androiddevelopers/merge-adapters-sequentially-with-mergeadapter-294d2942127a
 *
 * ConcatAdapter is good if you want to add a header or footer to your RV. Not headers/footers for each section - but headers/footers for the entire RV. That's why it's good for paging to show a loading view to the end.
 */
class ReposRecyclerViewAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        REPO,
        CTA,
        FAVORITE;

        companion object {
            fun from(value: Int) = values()[value]
        }
    }

    interface Listener: CTAView.Listener {
        fun repoItemClicked(item: RepoListItem)
    }

    lateinit var listener: Listener

    data class RowItem(val viewType: ViewType, val item: RepoListItem)

    var items: List<RepoListItem> = emptyList()
        set(newValue) {
            this.data = newValue.map { item ->
                when (item) {
                    is RepoListItem.Repo -> RowItem(ViewType.REPO, item)
                    is RepoListItem.Cta -> RowItem(ViewType.CTA, item)
                    is RepoListItem.Favorite -> RowItem(ViewType.FAVORITE, item)
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
            ViewType.REPO, ViewType.FAVORITE -> RepoViewHolder(view(parent, RepoViewHolder.layoutRes))
            ViewType.CTA -> CtaViewHolder(view(parent, CtaViewHolder.layoutRes))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rowItem = this.data[position]

        var programViewHolder: RepoViewHolder? = null
        when (rowItem.viewType) {
            ViewType.REPO, ViewType.FAVORITE -> {
                programViewHolder = holder as RepoViewHolder

                val item = rowItem.item as RepoListItem.Repo
                programViewHolder.populate(item.repo.name)
            }
            ViewType.CTA -> {
                holder as CtaViewHolder
                val item = rowItem.item as RepoListItem.Cta
                holder.listener = listener
                holder.populate(item.cta)
            }
        }

        programViewHolder?.itemView?.setOnClickListener {
            listener.repoItemClicked(rowItem.item)
        }
    }

}