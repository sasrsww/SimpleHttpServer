package com.sww.mvc.annotation;

import java.lang.annotation.*;

/**
 * RequestBody
 *
 * @author shenwenwen
 * @date 2020/4/30 10:44
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {

    boolean required() default true;

}
