package com.sww.mvc.annotation;

import java.lang.annotation.*;

/**
 * RequestMapping
 *
 * @author shenwenwen
 * @date 2020/4/28 21:42
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RequestMapping {

    String[] value() default {};
}
