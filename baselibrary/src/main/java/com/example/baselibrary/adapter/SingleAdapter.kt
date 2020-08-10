package com.example.baselibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes


 abstract class SingleAdapter<T> constructor(context:Context,  @LayoutRes layoutId : Int): BaseRecylerAdapter<T>(context) {
     @LayoutRes
    private var layoutId : Int = layoutId



     override fun getCurrentLayout(viewType: Int): Int {
        return layoutId;
     }

    override fun onBindViewHolder(holder: SimpleRecyclerHolder, position: Int) {
         bindData(holder,items.get(position))
    }


    abstract fun bindData(holder: SimpleRecyclerHolder, item: T);
 }