package com.sww.servlet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sww.http.SimpleHttpRequest;
import com.sww.http.SimpleHttpResponse;
import com.sww.mvc.HandlerMethod;
import com.sww.mvc.annotation.RequestMapping;
import com.sww.util.HttpHandlerUtil;
import com.sww.util.HttpUrlUtil;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DispatcherServlet
 *
 * @author shenwenwen
 * @date 2020/4/29 21:05
 */
public class DispatcherServlet {

    private Set<Object> controllers = Sets.newHashSet();

    private Reflections reflections = new Reflections();

    private Map<String, HandlerMethod> mappingHandlers = Maps.newHashMap();

    public void init() {
        loadAllController();
        buildMappingHandler();
    }

    /**
     * 处理http请求，1.根据uri找到HandlerMethod, 2.从请求中获取数据绑定到方法参数 3.反射执行方法
     * @param request http请求
     * @return http响应
     */
    public SimpleHttpResponse doService(SimpleHttpRequest request) {
        //1.根据uri找到HandlerMethod
        HandlerMethod handlerMethod = mappingHandlers.get(request.getReqPath());
        if (handlerMethod == null) {
            return notFoundResponse();
        }
        try {
            //2.从请求中获取数据绑定到方法参数 3.反射执行方法
            Object returnValue = handlerMethod.invokeForRequest(request);
            if (returnValue == null) {
                throw new RuntimeException("returnValue is null");
            }

            return HttpHandlerUtil.buildJson(returnValue);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> data = new HashMap<>();
            data.put("error", e.getMessage() != null ? e.getMessage() : "服务异常");
            SimpleHttpResponse httpResponse = HttpHandlerUtil.buildJson(data);
            httpResponse.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            return httpResponse;
        }
    }

    /**
     * 加载所有的@RequestMappin注解类
     */
    private void loadAllController() {

        Set<Class<?>> components = reflections.getTypesAnnotatedWith(RequestMapping.class);
        for (Class<?> component : components) {
            try {
                controllers.add(component.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建能够处理uri的handler的map, 即：uri -> HandlerMethod
     */
    private void buildMappingHandler() {
        if (controllers.isEmpty()) {
            return;
        }

        for (Object bean : controllers) {
            RequestMapping mapping = bean.getClass().getAnnotation(RequestMapping.class);
            if (mapping == null) {
                continue;
            }
            String mappingUri = HttpUrlUtil.fixUri(mapping.value()[0]);
            for (Method actionMethod : bean.getClass().getMethods()) {
                RequestMapping subMapping = actionMethod.getAnnotation(RequestMapping.class);
                if (subMapping == null) {
                    continue;
                }
                String subMappingUri = HttpUrlUtil.fixUri(subMapping.value()[0]);

                String reqPath = mappingUri + subMappingUri;
                HandlerMethod handlerMethod = new HandlerMethod(bean,actionMethod);
                mappingHandlers.put(reqPath, handlerMethod);
            }
        }
    }

    public SimpleHttpResponse notFoundResponse() {
        SimpleHttpResponse httpResponse = new SimpleHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.NOT_FOUND);
        httpResponse.getHeaders().set(HttpHeaderNames.CONTENT_TYPE.toString(), "text/html; charset=utf-8");
        return httpResponse;
    }
}
