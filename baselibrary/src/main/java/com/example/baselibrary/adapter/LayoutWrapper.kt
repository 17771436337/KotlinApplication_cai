package com.example.baselibrary.adapter

class LayoutWrapper<T> {
    private var layoutId:Int //布局Id
            = 0
    private var spanSize :Int//布局所占列数
            = 0
    private var data //数据源
            : T? = null
    private var holder: Dataholder<T>? = null

    fun constructor(){}

    fun constructor(layoutId : Int,data: T,holder: Dataholder<T>){
        this.layoutId = layoutId;
        this.data = data;
        this.holder = holder;
    }


   fun constructor(layoutId : Int,spanSize:Int,data: T,holder: Dataholder<T>)  {
        this.spanSize = spanSize
       this.layoutId = layoutId;
       this.data = data;
       this.holder = holder;
    }



    fun getLayoutId():Int{
        return layoutId
    }

    fun getHolder():Dataholder<T>{
        return holder!!
    }

    fun getData():T{
        return data!!
    }



}