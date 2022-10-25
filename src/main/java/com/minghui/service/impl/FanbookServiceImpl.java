package com.minghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.minghui.commons.constant.FanbookPostConstant;
import com.minghui.commons.utils.RestTemplateUtil;
import com.minghui.service.IFanbookService;
import org.springframework.stereotype.Service;

@Service
public class FanbookServiceImpl implements IFanbookService {
    @Override
    public String guild(String userId, String guildId) {

        JSONObject json = new JSONObject();
        json.put("user_id", Long.valueOf(userId));
        json.put("guild_id", Long.valueOf(guildId));
        return RestTemplateUtil.builder().get(
                FanbookPostConstant.getGuild, json);
    }

    @Override
    public String getPrivateChat(String userId) {
        JSONObject json = new JSONObject();
        json.put("user_id", Long.valueOf(userId));
        return RestTemplateUtil.builder().get(
                FanbookPostConstant.getPrivateChat, json);
    }
}
