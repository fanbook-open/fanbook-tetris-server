package com.minghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.minghui.commons.dto.MessageCardDTO;
import com.minghui.commons.enums.ResponseEnum;
import com.minghui.commons.vo.ResponseResult;
import com.minghui.service.IMessageCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 明辉
 * @Date 2021-06-15
 */
@RestController
@RequestMapping("/msgcard")
public class MessageCardController {

    @Autowired
    private IMessageCardService messageCardService;


    /**
     * 往频道发送消息卡片
     * @param messageCardDTO
     * @return ResponseResult
     */
    @PostMapping("/sendMsgCard")
    public ResponseResult sendMsgCard(@RequestBody MessageCardDTO messageCardDTO){
        JSONObject jsonObject = messageCardService.sendMsgCard(messageCardDTO);
        if (jsonObject != null) {
            return new ResponseResult(
                    ResponseEnum.SUCCESS.getCode(),
                    ResponseEnum.SUCCESS.getMsg(),
                    jsonObject);
        }
        return new ResponseResult(
                ResponseEnum.CARD_MESSAGE_SEND_FAIL.getCode(),
                ResponseEnum.CARD_MESSAGE_SEND_FAIL.getMsg(),
                null);
    }

}
