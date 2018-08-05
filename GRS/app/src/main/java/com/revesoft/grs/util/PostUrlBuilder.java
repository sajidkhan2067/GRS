package com.revesoft.grs.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by sajid on 11/7/2015.
 */
public class PostUrlBuilder {
    private static final String TAG = PostUrlBuilder.class.getSimpleName();
    private String url;
    private Map<String, String> parameters;
    private Map<String, List<String>> arrayParameters;

    private String queryUrl;

    public PostUrlBuilder() {

    }

    public PostUrlBuilder(String url, Map<String, String> parameters, Map<String, List<String>> arrayParameters) {
        this.url = url;
        this.parameters = parameters;
        this.arrayParameters = arrayParameters;
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
        if (arrayParameters != null && arrayParameters.size() != 0) {
            if (parameters != null && parameters.size() != 0) {
                urlBuilder.append("&");
            } else {
                urlBuilder.append("?");
            }
            int i = 0;
            for (String key : arrayParameters.keySet()) {
                List<String> values = arrayParameters.get(key);
                int j = 0;
                for (String value : values) {
                    Log.d(TAG, "size = " + values.size() + " j = " + j + " key= " + key + " & " + "value=" + value);
                    try {
                        urlBuilder.append(String.format("%s[]=%s", key, URLEncoder.encode(value, "UTf-8")));
                        j++;
                        if (j != values.size()) {
                            urlBuilder.append("&");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                i++;
                if (i != arrayParameters.size()) {
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

    public Map<String, List<String>> getArrayParameters() {
        return arrayParameters;
    }

    public void setArrayParameters(Map<String, List<String>> arrayParameters) {
        this.arrayParameters = arrayParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostUrlBuilder)) return false;

        PostUrlBuilder that = (PostUrlBuilder) o;

        return this.getUrl().equals(that.getUrl())
                && this.getParameters().equals(that.getParameters())
                && this.getArrayParameters().equals(that.getArrayParameters())
                && this.getQueryUrl().equals(that.getQueryUrl());
    }

    @Override
    public int hashCode() {
        int result = getUrl().hashCode();
        result = 31 * result + getParameters().hashCode();
        result = 31 * result + getArrayParameters().hashCode();
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
