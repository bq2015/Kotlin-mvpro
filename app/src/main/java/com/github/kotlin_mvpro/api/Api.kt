package com.github.kotlin_mvpro.api

import com.github.kotlin_mvpro.api.request.ImageRequest
import com.github.kotlin_mvpro.api.request.NewsRequest
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    companion object {
        val IMPL: Api = ApiUtils.api
        val pageSize = 10
    }

    @GET("福利/{size}/{page}")
    fun getImageList(@Path("size") size: Int, @Path("page") page: Int): Observable<ImageRequest.Res>

    @GET("http://news-at.zhihu.com/api/4/news/latest")
    fun getNewsList(): Observable<NewsRequest.ListRes>

    //the last day of the current date
    @GET("http://news-at.zhihu.com/api/4/news/before/{date}")
    fun getNewsListForDate(@Path("date") date: String): Observable<NewsRequest.ListRes>

    @GET("http://news-at.zhihu.com/api/4/news/{id}")
    fun getNewDetail(@Path("id") id: Int): Observable<NewsRequest.DetailRes>
}