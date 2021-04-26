package com.dalma.broker.base.service.context;

import com.dalma.broker.base.ContextSource;
import org.slf4j.MDC;

import java.util.UUID;

@ContextSource
public class RequestLogContext {
    private String requestId;

    private String requestMethod;
    private String requestURI;
    private String requestRemoteAddr;
    private String requestParameters;
    private String requestRemoteUser;
    private String requestHeaders;
    private String responseStatus;
    private String responseTime;

    public RequestLogContext() {
        this(UUID.randomUUID().toString());
    }

    public RequestLogContext(String requestId) {
        this.setRequestId(requestId);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        MDC.put("requestId", requestId);
        this.requestId = requestId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        MDC.put("requestMethod", requestMethod);
        this.requestMethod = requestMethod;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        MDC.put("requestURI", requestURI);
        this.requestURI = requestURI;
    }

    public String getRequestRemoteAddr() {
        return requestRemoteAddr;
    }

    public void setRequestRemoteAddr(String requestRemoteAddr) {
        MDC.put("requestRemoteAddr", requestRemoteAddr);
        this.requestRemoteAddr = requestRemoteAddr;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        MDC.put("requestParameters", requestParameters);
        this.requestParameters = requestParameters;
    }

    public String getRequestRemoteUser() {
        return requestRemoteUser;
    }

    public void setRequestRemoteUser(String requestRemoteUser) {
        MDC.put("requestRemoteUser", requestRemoteUser);
        this.requestRemoteUser = requestRemoteUser;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        MDC.put("requestHeaders", requestHeaders);
        this.requestHeaders = requestHeaders;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        MDC.put("responseStatus", responseStatus);
        this.responseStatus = responseStatus;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        MDC.put("responseTime", responseTime);
        this.responseTime = responseTime;
    }
}
