package com.example.baselibrary.adapter

import android.content.Context


interface Dataholder<T> {
    fun bind(context: Context, holder: SimpleRecyclerHolder, item: T, position: Int)
}