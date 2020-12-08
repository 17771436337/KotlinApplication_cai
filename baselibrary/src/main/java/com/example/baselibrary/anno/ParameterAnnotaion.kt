package com.example.baselibrary.anno


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

class ParameterAnnotaion {
    //获取类上面的注解
    fun getClassAnnotaion(clazz:Class<*>) {
        val annotation = clazz.getAnnotation(RequestParam::class.java)
        println("类注解的值：${annotation?.name},${annotation?.value}")
    }

    //获取属性变量上面的注解
    fun getFieldAnnotaion(clazz:Class<*>) {

        val annotations = clazz.declaredFields
        for (i in annotations) {
            val annotation = i.getAnnotation(RequestParam::class.java)
            println("属性变量注解的值：${annotation?.name},${annotation?.value}")
        }
    }

    //获取函数上面的注解
    fun getFunctionAnnotaion(clazz:Class<*>) {
        val annotations = clazz.declaredMethods
        for (i in annotations) {
            val annotation = i.getAnnotation(RequestParam::class.java)
            println("函数注解的值：${annotation?.name},${annotation?.value}")
        }
    }


    /**获取方法参数上注解的值,不指定具体方法**/
    fun getParameterAnnotaion(clazz:Class<*>) {
        for (method in clazz.declaredMethods) {
            val parameterAnnotations = method.parameterAnnotations
            for (iss in parameterAnnotations) {
                for (i in iss) {
                    print("方法名：${method.name}")
                    if (i is RequestParam) {
                        println("参数注解的值：${i?.name},${i?.value}")
                    }
                }
            }
        }

    }

}