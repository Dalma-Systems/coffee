package com.dalma.coffee.api.configuration;

import com.dalma.coffee.base.EnableRequestLogFilter;
import com.dalma.coffee.base.config.ModelMapperConfiguration;
import com.dalma.coffee.base.controller.RestErrorController;
import com.dalma.coffee.base.controller.advice.ErrorHandlingControllerAdvice;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Import({ModelMapperConfiguration.class, ErrorHandlingControllerAdvice.class, RestErrorController.class,
        SentryConfiguration.class})
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableRequestLogFilter
public class ApplicationConfiguration {

}