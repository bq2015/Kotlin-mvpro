package com.github.kotlin_mvpro.ui.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.kotlin_mvpro.R

abstract class CommonListAdapter<B>(layoutResId: Int) : BaseQuickAdapter<B, CommonListAdapter.BindHolder>(layoutResId) {

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(mLayoutInflater, layoutResId, parent, false) ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }

    class BindHolder(val view: View) : BaseViewHolder(view) {
        fun getBinding(): ViewDataBinding {
            return itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
        }
    }
}