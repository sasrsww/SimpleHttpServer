package com.sww.mvc;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.sww.http.SimpleHttpRequest;
import com.sww.mvc.annotation.RequestBody;
import com.sww.mvc.annotation.RequestParam;
import io.netty.util.internal.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * HandlerMethod
 *
 * @author shenwenwen
 * @date 2020/4/30 10:36
 */
public class HandlerMethod {

    private final Object bean;

    private final Method method;

    private final MethodParameter[] parameters;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
        this.parameters = initMethodParameters();
    }

    /**
     * 2.从请求中获取数据绑定到方法参数 3.反射执行方法
     * @param request   http请求
     * @return controller方法执行的返回值
     * @throws Exception    执行异常时抛出
     */
    public Object invokeForRequest(SimpleHttpRequest request) throws Exception {
        //2.从请求中获取数据绑定到方法参数
        Object[] args = getMethodArgumentValues(request);
        //3.反射执行方法
        return method.invoke(bean, args);
    }

    /**
     * 2.从请求中获取数据绑定到方法参数
     * @param request   http请求
     * @return  controller方法参数值
     */
    public Object[] getMethodArgumentValues(SimpleHttpRequest request) {
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter methodParameter = parameters[i];
            RequestParam requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
            if (requestParam != null) {
                //从请求参数获取值
                String value = request.getReqParams().get(methodParameter.getParameterName());
                Preconditions.checkNotNull(value);
                args[i] = strToConvert(value, methodParameter.getParameterType());
                continue;
            }

            RequestBody requestBody = methodParameter.getParameterAnnotation(RequestBody.class);
            if (requestBody != null) {
                //从请求体获取值
                args[i] = JSON.parseObject(request.content(), methodParameter.getParameterType());
                continue;
            }
        }
        return args;
    }

    private Object strToConvert(String value, Class<?> c) {
        if (c.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(value);
        }
        return value;
    }

    /**
     * 初始化controller方法的参数,关注点1.参数类型 2.参数名称 3.参数的注解
     * @return
     */
    private MethodParameter[] initMethodParameters() {
        Class<?>[] parameterTypes = this.method.getParameterTypes();
        Annotation[][] parameterAnnos = this.method.getParameterAnnotations();
        Parameter[] parameters = this.method.getParameters();

        MethodParameter[] result = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            Annotation[] annos = parameterAnnos[i];
            Parameter param = parameters[i];
            MethodParameter parameter = new MethodParameter(this.method, type, annos, param.getName());
            result[i] = parameter;
        }
        return result;
    }

}
