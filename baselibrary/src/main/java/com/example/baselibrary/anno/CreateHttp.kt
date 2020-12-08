package com.example.baselibrary.anno

import kotlin.reflect.KClass
//类注解
//默认整个生命周期都不会消失
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class CreateHttp(
    val url: String = "",
    val type: HttpType = HttpType.OK
)

@Target(
    //类注解
    AnnotationTarget.CLASS,
    //属性变量注解
    AnnotationTarget.FIELD,
    //函数方法注解
    AnnotationTarget.FUNCTION,
    //方法参数注解
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestParam(val name: String, val value: String)

