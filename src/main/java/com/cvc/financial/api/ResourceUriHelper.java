package com.cvc.financial.api;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

public class ResourceUriHelper {

    public static void addLocationInUriResponseHeader(Object resourceId) {
        HttpServletResponse response = getHttpServletResponse();

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                        .path("/{id}")
                        .buildAndExpand(resourceId).toUri();

        response.setHeader(HttpHeaders.LOCATION, uri.toString());
    }

    public static HttpServletResponse getHttpServletResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
    }
}