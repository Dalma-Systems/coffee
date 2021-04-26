package com.dalma.coffee.base;

import com.dalma.coffee.base.service.context.RequestLogContext;
import com.dalma.coffee.base.web.filter.SimpleRequestLogContextFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RequestLogContext.class, SimpleRequestLogContextFilter.class})
public @interface EnableRequestLogFilter {
}
