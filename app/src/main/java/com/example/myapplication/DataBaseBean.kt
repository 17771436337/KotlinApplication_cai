package com.example.myapplication
data class DataBase(
    var error_code: Int,
    var reason: String,
    var result: Result
)

data class Result(
    var `data`: List<Data>,
    var stat: String
)

data class Data(
    var author_name: String,
    var category: String,
    var date: String,
    var thumbnail_pic_s: String,
    var thumbnail_pic_s02: String,
    var thumbnail_pic_s03: String,
    var title: String,
    var uniquekey: String,
    var url: String
)
