package de.yagub.deliverysystem.msprocessmanager.error;

import feign.Request;
import feign.Response;
import feign.error.FeignExceptionConstructor;

import java.util.Collection;
import java.util.Map;

import static feign.Util.caseInsensitiveCopyOf;


public class CustomFeignException extends RuntimeException{

    private static final String EXCEPTION_MESSAGE_TEMPLATE_NULL_REQUEST =
            "request should not be null";
    private static final long serialVersionUID = 0;
    private final int status;
    private byte[] responseBody;
    private Map<String, Collection<String>> responseHeaders;
    private final Request request;




    public CustomFeignException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.request = null;
    }



    public CustomFeignException(
            int status,
            String message,
            Throwable cause,
            byte[] responseBody,
            Map<String, Collection<String>> responseHeaders) {
        super(message, cause);
        this.status = status;
        this.responseBody = responseBody;
        this.responseHeaders = caseInsensitiveCopyOf(responseHeaders);
        this.request = null;
    }

    public CustomFeignException(int status, String message) {
        super(message);
        this.status = status;
        this.request = null;
    }

    public CustomFeignException(
            int status,
            String message,
            byte[] responseBody,
            Map<String, Collection<String>> responseHeaders) {
        super(message);
        this.status = status;
        this.responseBody = responseBody;
        this.responseHeaders = caseInsensitiveCopyOf(responseHeaders);
        this.request = null;
    }

    public CustomFeignException(int status, String message, Request request, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.request = request;
    }

   public CustomFeignException(
            int status,
            String message,
            Request request,
            Throwable cause,
            byte[] responseBody,
            Map<String, Collection<String>> responseHeaders) {
        super(message, cause);
        this.status = status;
        this.responseBody = responseBody;
        this.responseHeaders = caseInsensitiveCopyOf(responseHeaders);
        this.request = request;
    }

    public CustomFeignException(int status, String message, Request request) {
        super(message);
        this.status = status;
        this.request = request;
    }

    public CustomFeignException(
            int status,
            String message,
            Request request,
            byte[] responseBody,
            Map<String, Collection<String>> responseHeaders) {
        super(message);
        this.status = status;
        this.responseBody = responseBody;
        this.responseHeaders = caseInsensitiveCopyOf(responseHeaders);
        this.request = request;
    }
    @FeignExceptionConstructor
    public CustomFeignException(Response response){
        this.status = 0;
        this.request = null;
        System.out.println("Check");
    }
}
