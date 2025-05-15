package de.yagub.deliverysystem.msprocessmanager.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;



@Component
public class LoggingFilterConfig extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilterConfig.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logRequest(wrappedRequest);
            logResponse(wrappedResponse);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        if (logger.isTraceEnabled()) {
            Map<String, String> headers = getHeaders(request);
            String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

            logger.trace("CLIENT_REQUEST: method={}, uri={}, headers={}, body={}",
                    request.getMethod(), request.getRequestURI(), headers, body);
        }
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        if (logger.isTraceEnabled()) {
            Map<String, String> headers = getHeaders(response);
            String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

            logger.trace("CLIENT_RESPONSE: status={}, headers={}, body={}",
                    response.getStatus(), headers, body);
        }
    }

    // Helper methods to get headers
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }
        return headers;
    }

    private Map<String, String> getHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            headers.put(header, response.getHeader(header));
        }
        return headers;
    }
}