package com.sww.mvc.annotation;

import java.lang.annotation.*;

/**
 * RequestParam
 *
 * @author shenwenwen
 * @date 2020/4/30 10:43
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value() default "";

}
