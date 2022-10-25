package com.minghui.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.commons.dto.GameScoreDTO;
import com.minghui.commons.dto.MessageCardDTO;
import com.minghui.dao.GameScoreMapper;
import com.minghui.entity.Point;
import com.minghui.entity.TServerConfig;
import com.minghui.dao.mapper.ServerConifgMapper;
import com.minghui.entity.TmScoreRecord;
import com.minghui.entity.UserGameScore;
import com.minghui.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ServerConfigServiceImpl extends ServiceImpl<ServerConifgMapper, TServerConfig> implements IServerConfigService {


    @Autowired
    GameScoreMapper gameScoreMapper;

    @Autowired
    IntegralService integralService;

    @Autowired
    TmScoreRecordService tmScoreRecordService;

    @Autowired
    IFanbookService fanbookService;

    @Autowired
    IMessageCardService messageCardService;

    @Override
    public void executeRank(TServerConfig tServerConfig, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONArray array = JSONArray.parseArray(tServerConfig.getDataJson());
        //判断规则是否有效
        if (array.size() > 0) {
            //获取最后一名
            JSONObject xx = JSONObject.parseObject(array.get(array.size() - 1).toString());
            Integer endNumber = xx.getInteger("end");
            List<UserGameScore> userGameScores = gameScoreMapper.queryTodayRankScore(tServerConfig.getGuildId().toString(), endNumber, time);
            userGameScores.forEach(userGameScore -> {
                array.forEach(obj -> {
                    JSONObject json = JSONObject.parseObject(obj.toString());

                    if (userGameScore.getRank() >= json.getInteger("begin") && userGameScore.getRank() <= json.getInteger("end")) {
                        Point point = new Point();
                        point.setGuildId(tServerConfig.getGuildId().toString());
                        point.setTimestamp(System.currentTimeMillis());
                        point.setUserId(userGameScore.getUserId());
                        point.setNonceStr(UUID.randomUUID().toString());
                        point.setRemark("俄罗斯积分规则奖励");
                        point.setPoint(json.getString("rewards"));
                        log.debug("添加积分请求参数：" + JSON.toJSONString(point));
                        String rString = integralService.addPoints(point);
                        //添加通知
                        String getPrivateChatStr = fanbookService.getPrivateChat(userGameScore.getUserId());
                        System.out.println("getPrivateChatStr:" + getPrivateChatStr);
                        String guildStr = fanbookService.guild(userGameScore.getUserId(), tServerConfig.getGuildId().toString());
                        System.out.println("guildStr:" + guildStr);

                        System.out.println("getPrivateChatStr:" + getPrivateChatStr);
                        Long privateChattId = null;
                        String guildname = null;
                        if (null != getPrivateChatStr) {
                            if (JSONObject.parseObject(getPrivateChatStr).getBoolean("ok")) {
                                privateChattId = JSONObject.parseObject(getPrivateChatStr).getJSONObject("result").getLong("id");
                            }
                        }
                        if (null != guildStr) {
                            if (JSONObject.parseObject(guildStr).getBoolean("ok")) {
                                guildname = JSONObject.parseObject(guildStr).getJSONObject("result").getString("name");
                            }
                        }
                        //发送私聊通知
                        if (null != privateChattId && null != guildname) {
                            MessageCardDTO messageCardDTO = new MessageCardDTO();
                            messageCardDTO.setScore(json.getLong("rewards"));
                            messageCardDTO.setChatId(privateChattId.toString());
                            messageCardDTO.setGuildname(guildname);
                            JSONObject s = messageCardService.sendMsg(messageCardDTO);
                            System.out.println("私聊通知返回:" + s);
                        }
                        log.debug("调用添加积分接口返回：" + rString);
                        System.out.println("guildStr:" + guildStr);
                        log.debug("添加接口积分返回：" + rString);
                        TmScoreRecord tmScoreRecord = new TmScoreRecord();
                        tmScoreRecord.setScore(userGameScore.getScore().intValue());
                        tmScoreRecord.setConfigId(tServerConfig.getId());
                        tmScoreRecord.setGuildId(tServerConfig.getGuildId());
                        tmScoreRecord.setUserId(Long.valueOf(userGameScore.getUserId()));
                        tmScoreRecord.setCreateTime(sdf.format(new Date()));
                        tmScoreRecord.setApiResult(rString);
                        tmScoreRecord.setRewards(json.getInteger("rewards"));
                        tmScoreRecordService.save(tmScoreRecord);
                    }
                });
            });
        }
    }

    @Override
    public void executeRankToDay(Integer day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("触发排行补发任务:" + sdf.format(new Date()));
        System.out.println("触发排行补发任务:" + sdf.format(new Date()));
        QueryWrapper<TServerConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("type", 2);
        //获取启用规则的服务器列表
        List<TServerConfig> configList = this.list(queryWrapper);
        configList.forEach(tServerConfig -> {
            JSONArray array = JSONArray.parseArray(tServerConfig.getDataJson());
            //判断规则是否有效
            if (array.size() > 0) {
                //获取最后一名
                JSONObject xx = JSONObject.parseObject(array.get(array.size() - 1).toString());
                Integer endNumber = xx.getInteger("end");
                List<UserGameScore> userGameScores = gameScoreMapper.queryTodayRankScoreToDay(tServerConfig.getGuildId().toString(), endNumber, day);

                userGameScores.forEach(userGameScore -> {
                    array.forEach(obj -> {
                        JSONObject json = JSONObject.parseObject(obj.toString());

                        if (userGameScore.getRank() >= json.getInteger("begin") && userGameScore.getRank() <= json.getInteger("end")) {
                            Point point = new Point();
                            point.setGuildId(tServerConfig.getGuildId().toString());
                            point.setTimestamp(System.currentTimeMillis());
                            point.setUserId(userGameScore.getUserId());
                            point.setNonceStr(UUID.randomUUID().toString());
                            point.setRemark("俄罗斯积分规则奖励");
                            point.setPoint(json.getString("rewards"));
                            log.debug("添加积分请求参数：" + JSON.toJSONString(point));
                            String rString = integralService.addPoints(point);
                            //添加通知
                            String getPrivateChatStr = fanbookService.getPrivateChat(userGameScore.getUserId());
                            System.out.println("getPrivateChatStr:" + getPrivateChatStr);
                            String guildStr = fanbookService.guild(userGameScore.getUserId(), tServerConfig.getGuildId().toString());
                            System.out.println("guildStr:" + guildStr);

                            System.out.println("getPrivateChatStr:" + getPrivateChatStr);
                            Long privateChattId = null;
                            String guildname = null;
                            if (null != getPrivateChatStr) {
                                if (JSONObject.parseObject(getPrivateChatStr).getBoolean("ok")) {
                                    privateChattId = JSONObject.parseObject(getPrivateChatStr).getJSONObject("result").getLong("id");
                                }
                            }
                            if (null != guildStr) {
                                if (JSONObject.parseObject(guildStr).getBoolean("ok")) {
                                    guildname = JSONObject.parseObject(guildStr).getJSONObject("result").getString("name");
                                }
                            }
                            //发送私聊通知
                            if (null != privateChattId && null != guildname) {
                                MessageCardDTO messageCardDTO = new MessageCardDTO();
                                messageCardDTO.setScore(json.getLong("rewards"));
                                messageCardDTO.setChatId(privateChattId.toString());
                                messageCardDTO.setGuildname(guildname);
                                JSONObject s = messageCardService.sendMsg(messageCardDTO);
                                System.out.println("私聊通知返回:" + s);
                            }
                            log.debug("调用添加积分接口返回：" + rString);
                        }
                    });
                });
            }
        });
    }

    @Async
    @Override
    public void executeScore(GameScoreDTO gameScoreDTO) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<TServerConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("guild_id", gameScoreDTO.getServerId());
        queryWrapper.eq("status", 1);
        queryWrapper.eq("type", 1);

        //获取启用规则的服务器列表
        TServerConfig tServerConfig = this.getOne(queryWrapper);
        if (null != tServerConfig) {
            JSONArray array = JSONArray.parseArray(tServerConfig.getDataJson());
            if (array.size() > 0) {
                array.forEach(obj -> {
                    JSONObject objJson = JSONObject.parseObject(obj.toString());
                    //验证是否已经获取过
                    QueryWrapper<TmScoreRecord> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("guild_id", tServerConfig.getGuildId());
                    queryWrapper1.eq("user_id", Long.valueOf(gameScoreDTO.getUserId()));
                    queryWrapper1.eq("score", objJson.getInteger("target"));
                    queryWrapper1.eq("create_time", sdf1.format(new Date()));
                    queryWrapper1.eq("config_id", tServerConfig.getId());
                    List<TmScoreRecord> tmScoreRecords = tmScoreRecordService.list(queryWrapper1);
                    if (tmScoreRecords.size() == 0) {
                        if (gameScoreDTO.getScore() >= objJson.getInteger("target")) {
                            //满足加分
                            Point point = new Point();
                            point.setGuildId(tServerConfig.getGuildId().toString());
                            point.setTimestamp(System.currentTimeMillis());
                            point.setUserId(gameScoreDTO.getUserId());
                            point.setNonceStr(UUID.randomUUID().toString());
                            point.setRemark("俄罗斯积分规则奖励");
                            point.setPoint(objJson.getString("rewards"));
                            log.debug("添加积分请求参数：" + JSON.toJSONString(point));
                            String rString = integralService.addPoints(point);
                            //添加通知
                            String getPrivateChatStr = fanbookService.getPrivateChat(gameScoreDTO.getUserId());
                            log.debug("getPrivateChatStr:" + getPrivateChatStr);
                            Long privateChattId = null;
                            String guildname = null;
                            if (null != getPrivateChatStr) {
                                if (JSONObject.parseObject(getPrivateChatStr).getBoolean("ok")) {
                                    privateChattId = JSONObject.parseObject(getPrivateChatStr).getJSONObject("result").getLong("id");
                                }
                            }
                            String guildStr = fanbookService.guild(gameScoreDTO.getUserId(), tServerConfig.getGuildId().toString());
                            if (null != guildStr) {
                                if (JSONObject.parseObject(guildStr).getBoolean("ok")) {
                                    guildname = JSONObject.parseObject(guildStr).getJSONObject("result").getString("name");
                                }
                            }
                            //发送私聊通知
                            if (null != privateChattId && null != guildname) {
                                MessageCardDTO messageCardDTO = new MessageCardDTO();
                                messageCardDTO.setScore(objJson.getLong("rewards"));
                                messageCardDTO.setChatId(privateChattId.toString());
                                messageCardDTO.setGuildname(guildname);
                                JSONObject s = messageCardService.sendMsg(messageCardDTO);
                                log.debug("私聊通知返回:" + s);
                            }
                            System.out.println("guildStr:" + guildStr);
                            log.debug("添加接口积分返回：" + rString);
                            TmScoreRecord tmScoreRecord = new TmScoreRecord();
                            tmScoreRecord.setScore(objJson.getInteger("target"));
                            tmScoreRecord.setConfigId(tServerConfig.getId());
                            tmScoreRecord.setGuildId(tServerConfig.getGuildId());
                            tmScoreRecord.setUserId(Long.valueOf(gameScoreDTO.getUserId()));
                            tmScoreRecord.setCreateTime(sdf1.format(new Date()));
                            tmScoreRecord.setApiResult(rString);
                            tmScoreRecord.setRewards(objJson.getInteger("rewards"));
                            tmScoreRecordService.save(tmScoreRecord);
                        }
                    }
                });

            }
        }
    }

}
