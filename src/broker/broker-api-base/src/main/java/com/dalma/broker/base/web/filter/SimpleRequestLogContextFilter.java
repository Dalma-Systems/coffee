package com.dalma.broker.base.web.filter;

import com.dalma.broker.base.service.context.RequestLogContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Component
@Order(OrderedFilter.REQUEST_WRAPPER_FILTER_MAX_ORDER - 104) // Load after spring's RequestContextFilter
public class SimpleRequestLogContextFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleRequestLogContextFilter.class);

    @Autowired
    private RequestLogContext requestLogContext;

    @Override
    public void destroy() {
        // Does nothing.
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        requestLogContext.setRequestMethod(request.getMethod());
        requestLogContext.setRequestURI(request.getRequestURI());
        requestLogContext.setRequestRemoteAddr(request.getRemoteAddr());
        requestLogContext.setRequestRemoteUser(request.getRemoteUser() != null ? request.getRemoteUser() : "-");
        requestLogContext.setRequestParameters(getRequestMap(request).toString());
        requestLogContext.setRequestHeaders(getHeadersMap(request).toString());

        chain.doFilter(request, response);

        int responseStatus = response.getStatus();
        String responseTime = String.valueOf(System.currentTimeMillis() - startTime);

        requestLogContext.setResponseTime(responseTime);
        requestLogContext.setResponseStatus(String.valueOf(responseStatus));

        String method = request.getMethod();
        if (RequestMethod.GET.name().equals(method) || RequestMethod.POST.name().equals(method)
                || RequestMethod.PUT.name().equals(method) || RequestMethod.DELETE.name().equals(method)) {

            String logMsg = "[REQUEST METHOD: " + request.getMethod() + "]" + " [REQUEST URI: "
                    + request.getRequestURI() + "]" + " [RESPONSE STATUS: " + responseStatus + "]" + " [RESPONSE TIME: "
                    + responseTime + "]";

            if (responseStatus < HttpStatus.BAD_REQUEST.value()) {
                LOG.info(logMsg);
            } else {
                LOG.error(logMsg);
            }
        }

    }

    private Map<String, String> getRequestMap(HttpServletRequest request) {
        Map<String, String> requestMap = new HashMap<>();
        if (GET.name().equals(request.getMethod())) {
            Enumeration<?> requestParamNames = request.getParameterNames();
            while (requestParamNames.hasMoreElements()) {
                String requestParamName = (String) requestParamNames.nextElement();
                String requestParamValue = request.getParameter(requestParamName);
                requestMap.put(requestParamName, requestParamValue);
            }
        }
        return requestMap;
    }

    private Map<String, String> getHeadersMap(HttpServletRequest request) {
        Map<String, String> requestMap = new HashMap<>();
        Enumeration<?> requestParamNames = request.getHeaderNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue = request.getHeader(requestParamName);
            requestMap.put(requestParamName, requestParamValue);
        }
        return requestMap;
    }

}
