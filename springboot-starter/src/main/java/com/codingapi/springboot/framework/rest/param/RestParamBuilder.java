package com.codingapi.springboot.framework.rest.param;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RestParamBuilder {

    private final JSONObject jsonBody;
    private final MultiValueMap<String, String> mapBody;

    private RestParamBuilder() {
        this.jsonBody = new JSONObject();
        this.mapBody = new LinkedMultiValueMap<>();
    }

    public static RestParamBuilder create() {
        return new RestParamBuilder();
    }

    @SneakyThrows
    public static RestParamBuilder parser(Object obj) {
        RestParamBuilder builder = create();
        JSONObject object = (JSONObject) JSONObject.toJSON(obj);
        for (String key : object.keySet()) {
            Object value = object.getJSONObject(key);
            if(value!=null){
                builder.add(key, value);
            }else {
                value = object.getJSONArray(key);
                if (value != null) {
                    builder.add(key, value);
                }
            }
        }
        return builder;
    }

    public JSONObject toJsonRequest() {
        return jsonBody;
    }

    public MultiValueMap<String, String> toFormRequest() {
        return mapBody;
    }

    public RestParamBuilder add(String key, Object value) {
        return add(key, value, true);
    }

    public RestParamBuilder add(String key, Object value, boolean encode) {
        String stringValue = value.toString();
        String encodeValue = encode ? URLEncoder.encode(stringValue, StandardCharsets.UTF_8) : value.toString();
        jsonBody.put(key, value);
        mapBody.add(key, encodeValue);
        return this;
    }

}
