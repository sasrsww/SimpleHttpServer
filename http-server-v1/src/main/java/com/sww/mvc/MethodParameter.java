package com.sww.mvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * MethodParameter
 *
 * @author shenwenwen
 * @date 2020/4/30 10:45
 */
public class MethodParameter {
    private final Method method;

    /**
     * 参数类型,用于http数据转换的类型
     */
    private final Class<?> parameterType;

    /**
     * 参数注解：@RequestBody 从http请求体取值 @RequestParam 从http请求uri请求路径取值
     */
    private final Annotation[] parameterAnnotations;

    /**
     * 参数名称：获取uri请求路径对应的parameterName的值
     */
    private final String parameterName;

    public MethodParameter(Method method, Class<?> parameterType, Annotation[] parameterAnnotations, String parameterName) {
        this.method = method;
        this.parameterType = parameterType;
        this.parameterAnnotations = parameterAnnotations;
        this.parameterName = parameterName;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public Annotation[] getParameterAnnotations() {
        return parameterAnnotations;
    }

    public String getParameterName() {
        return parameterName;
    }

    public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
        Annotation[] anns = getParameterAnnotations();
        for (Annotation ann : anns) {
            if (annotationType.isInstance(ann)) {
                return (A) ann;
            }
        }
        return null;
    }
}
