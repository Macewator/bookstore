package com.bookstore.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UriBuilder {

    private String url;
    private Map<String, String> params = new HashMap<>();

    public UriBuilder(String url) {
        this.url = url;
    }

    public UriBuilder param(String name, String value) {
        if (value != null) {
            params.put(name, value);
        }

        return this;
    }

    public UriBuilder param(String name, Long value) {
        if (value != null) {
            params.put(name, value.toString());
        }

        return this;
    }

    public UriBuilder param(String name, Boolean value) {
        if (value != null) {
            params.put(name, value.toString());
        }

        return this;
    }

    public UriBuilder param(String name, Integer value) {
        if (value != null) {
            params.put(name, value.toString());
        }

        return this;
    }

    public String build() {
        String paramsPart = paramsToQueryPart();
        String params = paramsPart.isEmpty() ? "" : "?" + paramsPart;
        return url + params;
    }

    private String paramsToQueryPart() {
        return params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }
}
