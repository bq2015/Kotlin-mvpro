package com.github.kotlin_mvpro.ui

import android.app.Fragment
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v13.app.FragmentPagerAdapter
import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.SnackbarUtils
import com.github.kotlin_mvpro.R
import com.github.kotlin_mvpro.databinding.ActivityMainBinding
import com.github.kotlin_mvpro.ui.base.BaseActivity
import com.github.kotlin_mvpro.ui.fragment.ImageFragment
import com.github.kotlin_mvpro.ui.fragment.NewsFragment
import com.github.kotlin_mvpro.utils.Cons
import com.github.library.base.defaults.EmptyPresenter
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.paperdb.Paper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast

class MainActivity : BaseActivity<EmptyPresenter, ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //rxNetState
        ReactiveNetwork.observeNetworkConnectivity(applicationContext)
                .subscribeOn(Schedulers.io())
                .bindToLifecycle(this)
                .flatMap { Observable.just((it.isAvailable && it.state == NetworkInfo.State.CONNECTED)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it) {
                        SnackbarUtils.with(findViewById(android.R.id.content))
                                .setMessage("网络未连接")
                                .setDuration(0)
                                .showWarning()
                    }
                    Paper.book().write(Cons.NET_STATE, it)
                })

        val title = arrayOf("美图", "知乎日报")
        val items = arrayOf(ImageFragment(), NewsFragment())
        mBinding.pager.adapter = object : FragmentPagerAdapter(fragmentManager) {

            override fun getItem(position: Int): Fragment = items[position] as Fragment
            override fun getCount(): Int = title.size
            override fun getPageTitle(position: Int): CharSequence = title[position]
        }
        mBinding.tab.setViewPager(mBinding.pager, title)
    }
}
