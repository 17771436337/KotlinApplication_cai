package com.example.baselibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList


abstract class BaseRecylerAdapter<T> constructor(context : Context) :RecyclerView.Adapter<SimpleRecyclerHolder>() {

    protected var mContext: Context = context
    protected var inflater: LayoutInflater = LayoutInflater.from(context)
    protected var items: List<T> = ArrayList()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleRecyclerHolder {
        return  SimpleRecyclerHolder.createViewHolder(parent.getContext(),inflater.inflate(getCurrentLayout(viewType),parent,false))
    }


    override fun getItemCount(): Int {
        return items.size
    }


    open fun setData(list: List<T>) {
        var list: List<T> = list
        if (list == null) {
            list = Collections.emptyList()
        }
        items = list
        notifyDataSetChanged()
    }

    open abstract fun getCurrentLayout(viewType: Int) : Int

}