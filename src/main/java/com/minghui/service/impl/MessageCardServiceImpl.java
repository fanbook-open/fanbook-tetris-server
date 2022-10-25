package com.minghui.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.minghui.commons.constant.FanbookConstant;
import com.minghui.commons.constant.FanbookPostConstant;
import com.minghui.commons.dto.GameScoreDTO;
import com.minghui.commons.dto.MessageCardDTO;
import com.minghui.commons.utils.HttpClientUtil;
import com.minghui.commons.utils.RestTemplateUtil;
import com.minghui.commons.utils.SendMessageUtil;
import com.minghui.entity.UserGameScore;
import com.minghui.entity.UserOauth;
import com.minghui.service.IGameScoreService;
import com.minghui.service.IMessageCardService;
import com.minghui.service.IUserOauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author 明辉
 * @Date 2021-06-15
 */
@Service
@Slf4j
public class MessageCardServiceImpl implements IMessageCardService {

    @Autowired
    private IUserOauthService userOauthService;

    @Autowired
    private IGameScoreService gameScoreService;

    /**
     * 调用机器人 发送消息卡片
     *
     * @param messageCardDTO
     * @return JsonObject
     */
    @Override
    public JSONObject sendMsgCard(MessageCardDTO messageCardDTO) {
        //查询用户信息,并设置text属性的值
        UserOauth userOauth = userOauthService.queryUserInfo(messageCardDTO.getUserId());
        String json = "";
        if (userOauth != null) {
            json = SendMessageUtil.sendMessage1(messageCardDTO, userOauth.getNickName());
        } else {
            json = SendMessageUtil.sendMessage1(messageCardDTO, "玩家");
        }
        return HttpClientUtil.sendMsgCardPost(
                FanbookPostConstant.SEND_MSG_CARD_URL, json);
    }

    /**
     * @俄罗斯机器人 获取卡片
     */
    @Override
    public void getBootMsgCard() {
        String str = "";
        try {
//            str = OkHttpUtils.builder()
//                    .url(FanbookPostConstant.GET_BOOT_UPDATES_URL)
//                    .addHeader("content-type", "application/json; charset=utf-8")
//                    .post(false)
//                    .sync();
            JSONObject pJson = new JSONObject();
            pJson.put("limit", 1000);
            pJson.put("timeout", 2);
            String url=FanbookPostConstant.GET_BOOT_UPDATES_URL;
            str = RestTemplateUtil.builder().post(url, pJson);
            //System.out.println(str);
            JSONObject jsonObject = JSON.parseObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            //System.out.println(jsonArray);
            if (jsonArray != null) {
                Map<Long, String> map = new HashMap<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject obj1 = jsonArray.getJSONObject(i);
                    JSONObject obj2 = obj1.getJSONObject("channel_post");
                    JSONObject obj3 = obj2.getJSONObject("chat");
                    Long id = (Long) obj3.get("id");
                    Long guildId = (Long) obj3.get("guild_id");
                    String botChatId = obj2.getString("text");
                    // 只监听@俄罗斯方块包含的消息
                    if (!botChatId.contains(FanbookConstant.BOOT_CHAT_MSG_ID)) {
                        continue;
                    }
                    if (!map.containsKey(id)) {
                        map.put(id, "1");
                        //查询今日和历史最高得分
                        GameScoreDTO gameScoreDTO = new GameScoreDTO()
                                .setServerId(String.valueOf(guildId))
                                .setGameId(FanbookConstant.GAME_ID);
                        Map<String, UserGameScore> scoreMap = gameScoreService.queryTotalAndTodayFirstScore(gameScoreDTO);
                        UserGameScore his = scoreMap.get("total");
                        UserGameScore today = scoreMap.get("today");
                        SendMessageUtil.sendMessage4(String.valueOf(id), his, today);
                    }
                }
            }
        } catch (JSONException e) {
            log.info("轮询机器人消息为null");
        } catch (Exception e) {
            log.warn("轮询机器人消息异常，e:{}", e);
        }
    }

    @Override
    public JSONObject sendMsg(MessageCardDTO messageCardDTO) {
        JSONObject msgJson=new JSONObject();
        msgJson.put("chat_id",Long.valueOf(messageCardDTO.getChatId()));
        String string="恭喜您在【俄罗斯方块积分活动】中获得【x】积分，请在《server》的积分商城中查看";
        string=string.replace("x",messageCardDTO.getScore().toString()).replace("server",messageCardDTO.getGuildname());
        msgJson.put("text",string);
        String str=msgJson.toJSONString();
        return HttpClientUtil.sendMsgCardPost(
                FanbookPostConstant.SEND_MSG_CARD_URL,str);
    }
}
