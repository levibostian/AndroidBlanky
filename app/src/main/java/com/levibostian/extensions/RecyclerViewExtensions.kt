package com.levibostian.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.Adapter<*>.view(parent: ViewGroup, @LayoutRes layout: Int): View {
    return LayoutInflater.from(parent.context).inflate(layout, parent, false)
}