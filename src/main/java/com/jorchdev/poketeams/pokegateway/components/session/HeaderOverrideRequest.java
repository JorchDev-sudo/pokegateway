package com.jorchdev.poketeams.pokegateway.components.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;

public class HeaderOverrideRequest extends HttpServletRequestWrapper {
    private final String headerName;
    private final String headerValue;

    public HeaderOverrideRequest(HttpServletRequest request, String headerName, String headerValue) {
        super(request);
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public String getHeader(String name) {
        return headerName.equalsIgnoreCase(name) ? headerValue : super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return headerName.equalsIgnoreCase(name)
                ? Collections.enumeration(List.of(headerValue))
                : super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new LinkedHashSet<>(Collections.list(super.getHeaderNames()));
        names.removeIf(headerName::equalsIgnoreCase);
        names.add(headerName);
        return Collections.enumeration(names);
    }
}