package com.levibostian.androidblanky.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.curiosityio.andoidviews.fragment.BaseFragment
import com.curiosityio.androidboilerplate.util.LogUtil
import com.levibostian.androidblanky.MainApplication
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.vo.RepoVo
import javax.inject.Inject

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val bundle = Bundle()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //MainApplication.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_main, container, false)

        return view
    }

}
