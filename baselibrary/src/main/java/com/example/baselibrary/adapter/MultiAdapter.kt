package com.example.baselibrary.adapter

import android.content.Context
import android.util.Log

open abstract class MultiAdapter<T> constructor(context:Context,layoutIds:IntArray): BaseRecylerAdapter<T>(context) ,MultiAdapterImp<T>{


    protected var layoutMap: HashMap<Int, Int> = HashMap()



    init {
        for ((i : Int,e: Int) in layoutIds?.withIndex()) {
            layoutMap.put(i, e)
        }
    }


    override fun getCurrentLayout(viewType: Int): Int {

        return getLayoutType(viewType);
    }

    override fun onBindViewHolder(holder: SimpleRecyclerHolder, position: Int) {
        bindData(mContext,holder,items.get(position),bindLayout(items.get(position),position),position);
    }


    override fun getItemViewType(position: Int): Int {
        val layoutId = bindLayout(items[position], position)
        return getViewType(layoutId)
    }


    /**根据ViewType获取对应的布局 */
    fun getLayoutType(viewType: Int): Int {
        return layoutMap[viewType]!!
    }

    /**根据layoutId获取对应的ViewType */
    fun getViewType(layoutId: Int): Int {
        val iter: Iterator<*> = layoutMap.entries.iterator()
        while (iter.hasNext()) {
            val entry = iter.next() as Map.Entry<*, *>
            val key = entry.key as Int
            val `val` = entry.value as Int
            if (`val` == layoutId) {
                return key
            }
        }
        return 0
    }

}