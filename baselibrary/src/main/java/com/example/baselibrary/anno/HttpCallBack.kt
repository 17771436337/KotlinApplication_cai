package com.example.baselibrary.anno

import kotlin.reflect.KClass
//类注解
//默认整个生命周期都不会消失
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class HttpCallBack(
    val url: String = "",
    val type: HttpType = HttpType.OK
)



