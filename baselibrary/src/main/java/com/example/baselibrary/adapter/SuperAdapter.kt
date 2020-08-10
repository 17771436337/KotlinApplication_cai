package com.example.baselibrary.adapter

import android.content.Context


/**多实体多布局*/
class SuperAdapter constructor(context: Context,layoutIds:IntArray) : MultiAdapter<LayoutWrapper<*>>(context,layoutIds) {
   open override fun bindData(
        context: Context?,
        holder: SimpleRecyclerHolder,
        item: LayoutWrapper<*>,
        layoutId: Int,
        position: Int) {
        val wrapper = items[position]
        wrapper.getHolder().bind(mContext, holder, wrapper.getData() as Nothing, position)
    }

    open override fun bindLayout(item: LayoutWrapper<*>, viewType: Int): Int {
        return item.getLayoutId()
    }


}