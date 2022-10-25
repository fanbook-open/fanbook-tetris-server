package com.minghui.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.minghui.commons.utils.RestTemplateUtil;
import com.minghui.entity.Point;
import com.minghui.service.IntegralService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IntegralServiceImpl implements IntegralService {
    @Value("${ids.jfsc.client_id}")
    String clientId;
    @Value("${ids.jfsc.client_secret}")
    String secret;
    @Value("${ids.jfsc.api.token}")
    String tokenUrl;
    @Value("${ids.jfsc.api.point}")
    String pointUrl;

    @Override
    public String getToken() {
        JSONObject parmJson = new JSONObject();
        parmJson.put("client_id", clientId);
        parmJson.put("client_secret", secret);
        String rStr = RestTemplateUtil.builder().post(tokenUrl, parmJson);
        if (null != rStr) {
            return JSONObject.parseObject(rStr).getJSONObject("data").getString("token");
        } else {
            return "";
        }
    }

    @Override
    public String addPoints(Point point) {
        JSONObject parmJson = JSONObject.parseObject(JSON.toJSON(point).toString());
        String token = this.getToken();
        return RestTemplateUtil.builder().post(pointUrl, token, parmJson);
    }
}
