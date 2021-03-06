package com.github.kotlin_mvpro.ui.presenter

import android.os.Bundle
import com.github.kotlin_mvpro.api.Api
import com.github.kotlin_mvpro.api.ApiCacheProvider
import com.github.kotlin_mvpro.ui.view.INewsDetailActivity
import com.github.kotlin_mvpro.utils.Cons
import com.github.library.base.BasePresenter
import com.github.library.utils.defThread
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import github.library.parser.ExceptionParseMgr
import io.paperdb.Paper
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictDynamicKey

class NewsDetailActivityPresenter : BasePresenter<INewsDetailActivity>() {
    //isLocalCss:是否使用本地Css显示
    var onLoadCallback: ((title: String, data: String) -> Unit)? = null

    override fun onViewCreated(view: INewsDetailActivity, arguments: Bundle?, savedInstanceState: Bundle?) {
    }

    fun getNewsDetail(id: Int, resetCache: Boolean = true) {
        val api = Api.IMPL.getNewDetail(id)
        ApiCacheProvider.IMPL.getNewDetail(api, DynamicKey(id), EvictDynamicKey(Paper.book().read(Cons.NET_STATE, resetCache)))
                .defThread()
                .bindToLifecycle(this)
                .doOnSubscribe { view()!!.showLoading() }
                .subscribe({
                    onLoadCallback?.invoke(it.data.title,  it.data.share_url)
                },
                        {
                            ExceptionParseMgr.Instance.parseException(it, { error, message -> view()!!.showMessageFromNet(error, message) })
                        }, { view()!!.hideLoading() })
    }

    fun convertBody(preResult: String): String {
        var preResult = preResult

        preResult = preResult.replace("<div class=\"img-place-holder\">", "")
        preResult = preResult.replace("<div class=\"headline\">", "")

        val css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">"

        val theme = "<body yclassName=\"\" onload=\"onLoaded()\">"

        return StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preResult)
                .append("</body></html>").toString()
    }
}
