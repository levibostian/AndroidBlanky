package com.levibostian.view.widget


import android.widget.LinearLayout
import android.content.Context
import android.util.AttributeSet
import android.annotation.TargetApi
import android.os.Build.VERSION_CODES
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import com.levibostian.R
import com.levibostian.extensions.textOrHide
import com.levibostian.view.type.CTALink
import kotlinx.android.synthetic.main.view_cta.view.*

class CTAView: LinearLayout, ButtonsBag.Listener {

    interface Listener {
        fun ctaLinkPressed(link: CTALink)
    }

    private var links: List<CTALink>? = null

    lateinit var listener: Listener

    fun populate(header: String?, subheader: String?,  links: List<CTALink>?, notice: String?) {
        buttons_bag.removeAllButtons() // reset view during recycling.

        header_textview.textOrHide = header
        subheader_textview.textOrHide = subheader

        this.links = links

        links?.forEachIndexed { index, link ->
            buttons_bag.addButton(index, R.layout.button_bag_cta_button, R.id.buttons_bag_button, link.title)
        }

        buttons_bag.listener = this

        notice_textview.textOrHide= notice
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs, 0)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }
    @TargetApi(VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int): super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context, attrs, defStyleAttr)
    }

    private fun initialize(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        LayoutInflater.from(context).inflate(R.layout.view_cta, this, true)

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun buttonClicked(id: ButtonsBagButtonIdentifier) {
        listener.ctaLinkPressed(links!![id])
    }

}