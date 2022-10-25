package com.minghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.minghui.commons.dto.GameScoreDTO;
import com.minghui.commons.utils.RestTemplateUtil;
import com.minghui.service.IRequestService;

import java.util.Map;

public class RequestServiceImpl implements IRequestService {
    @Override
    public JSONObject requestApi(GameScoreDTO gameScoreDTO) {
        String url = "";
        String rJson = RestTemplateUtil.builder().post(url, new JSONObject((Map<String, Object>) gameScoreDTO));
        return null;
    }
}
