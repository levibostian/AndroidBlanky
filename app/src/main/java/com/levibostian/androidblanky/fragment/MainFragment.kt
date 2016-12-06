package com.levibostian.androidblanky.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.curiosityio.andoidviews.fragment.BaseFragment
import com.curiosityio.androidboilerplate.util.LogUtil
import com.levibostian.androidblanky.MainApplication
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.manager.api.BaseApiManager
import com.levibostian.androidblanky.manager.api.ReposApiManager
import com.levibostian.androidblanky.vo.RepoVo
import javax.inject.Inject

class MainFragment : BaseFragment() {

    @Inject lateinit var reposApiManager: ReposApiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.component.inject(this)

        reposApiManager.getRepos("curiosityio", object : BaseApiManager.ApiResponseCallback<List<RepoVo>> {
            override fun success(repos: List<RepoVo>) {
                LogUtil.d("Success. Number repos: " + repos.size)
            }

            override fun apiError(message: String) {
                LogUtil.d("Fail getting repos")
            }

            override fun failure(message: String) {
                LogUtil.d("Fail getting repos")
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_main, container, false)

        return view
    }

    companion object {

        fun newInstance(): MainFragment {
            val fragment = MainFragment()

            val bundle = Bundle()

            fragment.arguments = bundle

            return fragment
        }
    }

}
