package com.minghui.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.minghui.commons.dto.MessageCardDTO;

public interface IMessageCardService {
    JSONObject sendMsgCard(MessageCardDTO messageCardDTO);

    void getBootMsgCard();

    JSONObject sendMsg(MessageCardDTO messageCardDTO);
}
