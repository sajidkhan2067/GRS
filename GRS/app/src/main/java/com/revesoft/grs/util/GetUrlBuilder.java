package com.revesoft.grs.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by sajid on 11/7/2015.
 */
public class GetUrlBuilder {
    private static final String TAG = GetUrlBuilder.class.getSimpleName();
    private String url;
    private Map<String, String> parameters;

    private String queryUrl;

    public GetUrlBuilder() {

    }

    public GetUrlBuilder(String url, Map<String, String> parameters) {
        this.url = url;
        this.parameters = parameters;
        buildQueryUrl();
    }

    private void buildQueryUrl() {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (parameters != null && parameters.size() != 0) {
            urlBuilder.append("?");
            int i = 0;
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                Log.d(TAG, "key= " + key + " & " + "value=" + value);
                try {
                    urlBuilder.append(String.format("%s=%s", key, URLEncoder.encode(value, "UTf-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
                if (i != parameters.size()) {
                    urlBuilder.append("&");
                }
            }
        }
        queryUrl = urlBuilder.toString();
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetUrlBuilder)) return false;

        GetUrlBuilder that = (GetUrlBuilder) o;

        return this.getUrl().equals(that.getUrl())
                && this.getParameters().equals(that.getParameters())
                && this.getQueryUrl().equals(that.getQueryUrl());
    }

    @Override
    public int hashCode() {
        int result = getUrl().hashCode();
        result = 31 * result + getParameters().hashCode();
        result = 31 * result + getQueryUrl().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GetUrlBuilder{" +
                "url='" + url + '\'' +
                ", parameters=" + parameters +
                ", queryUrl='" + queryUrl + '\'' +
                '}';
    }
}
