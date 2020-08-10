package com.example.baselibrary.adapter

import android.content.Context

interface MultiAdapterImp<T> {

    /**获取每个item的viewType */
    open   fun bindLayout(item: T, viewType: Int): Int

    /**绑定数据源 */
    open  fun bindData(
        context: Context?,
        holder: SimpleRecyclerHolder,
        item: T,
        layoutId: Int,
        position: Int
    )
}