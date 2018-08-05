package com.revesoft.grs.util;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ObjectRequest<T> extends Request<T> {

    private static final String TAG = ObjectRequest.class.getSimpleName();
    private Map<String, String> headers;
    private Map<String, String> params;
    private byte[] body;
    private String dateFormat = null;
    private Response.Listener<T> listener;
    private Class<T> classType;

    public ObjectRequest(String url, Response.ErrorListener listener, Class<T> classType) {
        this(Method.GET, url, listener, classType);
    }


    public ObjectRequest(int method, String url, Response.ErrorListener errorListener, Class<T> classType) {
        this(method, url, null, errorListener, classType);
    }

    public ObjectRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener, Class<T> classType) {
        this(method, url, null, null, null, listener, errorListener, classType);
    }

    public ObjectRequest(int method, String url, Map<String, String> params, Response.Listener<T> listener, Response.ErrorListener errorListener, Class<T> classType) {
        this(method, url, null, params, null, listener, errorListener, classType);
    }

    public ObjectRequest(int method, String url, Map<String, String> headers, Map<String, String> params, byte[] body, Response.Listener<T> listener, Response.ErrorListener errorListener, Class<T> classType) {
        super(method, url, errorListener);
        this.headers = headers;
        this.params = params;
        this.body = body;
        this.listener = listener;
        this.classType = classType;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        Log.d(TAG, "parse network");
        JSONObject jsonObject;
        if (networkResponse != null) {
            Log.d(TAG, "response code-" + networkResponse.statusCode + " ");
        }
        try {
            jsonObject = new JSONObject(new String(networkResponse.data, "UTF-8"));
            Log.d(TAG, jsonObject.toString(1));
        } catch (JSONException | UnsupportedEncodingException e) {
            VolleyLog.e(e.getMessage());
            return Response.error(new VolleyError(e));
        }
        Gson objectParser;
        if (dateFormat != null) {
            objectParser = new GsonBuilder().setDateFormat(dateFormat).create();
        } else {
            objectParser = new GsonBuilder().create();
        }
        T t;
        try {
            Log.d(TAG, jsonObject.toString());
            t = objectParser.fromJson(jsonObject.toString(), classType);
        } catch (Exception e) {
            VolleyLog.e(e.getMessage());
            return Response.error(new VolleyError(e));
        }
        return Response.success(t, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected void deliverResponse(T t) {
        if (listener != null) {
            listener.onResponse(t);
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s","guest","33615sua");
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return (params != null) ? this.params : super.getParams();
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        return body != null ? this.body : super.getBody();
    }
}
