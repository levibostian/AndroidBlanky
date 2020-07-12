package com.levibostian.view.widget


import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 * When a ViewGroup becomes a buttons bag, this is the Api you can expect from it.
 */
interface ButtonsBag {
    interface Listener {
        fun buttonClicked(id: ButtonsBagButtonIdentifier)
    }

    var listener: Listener
    val isEmpty: Boolean
    val count: Int
    fun addButton(id: ButtonsBagButtonIdentifier, buttonLayout: Int, buttonViewId: Int, text: String)
    fun removeButton(id: ButtonsBagButtonIdentifier)
    fun removeAllButtons()
}

typealias ButtonsBagButtonIdentifier = Int