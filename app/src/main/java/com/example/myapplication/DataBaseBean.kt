package com.example.myapplication

data class DataBaseBean(
    var data: List<Any>,
    var descript: Descript
)

data class Descript(
    var end_time: String,
    var remark: String,
    var start_time: String,
    var title: String,
    var vote_cut: String,
    var vote_num: Int,
    var vote_type: String,
    var web_url: String
)