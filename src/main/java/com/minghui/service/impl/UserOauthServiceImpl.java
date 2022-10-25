package com.minghui.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minghui.commons.constant.FanbookPostConstant;
import com.minghui.commons.dto.UserOauthDTO;
import com.minghui.commons.utils.HttpClientUtil;
import com.minghui.dao.UserOauthMapper;
import com.minghui.entity.UserOauth;
import com.minghui.service.IUserOauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 明辉
 * @Date 2021-06-09
 */
@Service
@Slf4j
public class UserOauthServiceImpl implements IUserOauthService {

    @Autowired
    private UserOauthMapper userOauthMapper;

    /**
     * 查询用户是否授权
     * @param userOauthDTO
     * @return Map<String, String>
     */
    @Override
    public UserOauth judgeAuthorization(UserOauthDTO userOauthDTO) {
        //查询数据库是否存在此userId
        System.out.println("***judgeAuthorization:"+JSONObject.toJSON(userOauthDTO));
        QueryWrapper<UserOauth> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_ID",userOauthDTO.getUserId())
                .eq("GAME_ID",userOauthDTO.getGameId());
        UserOauth userOauth = userOauthMapper.selectOne(wrapper);
        if (userOauth != null) {
            //此用户存在，已经授权，更新用户信息
            UserOauth userInfo = new UserOauth()
                    .setNickName(userOauthDTO.getNickName())
                    .setAvatar(userOauthDTO.getAvatar())
                    .setGender(userOauthDTO.getGender())
                    .setUpdateDatetime(new Date());
            userOauthMapper.update(userInfo,wrapper);
            return userOauth;
        }
        return null;
    }

    /**
     * 存储授权用户信息
     * @param userOauthDTO
     * @return Map<String, String>
     */
    @Override
    public Map<String, String> saveUserInfo(UserOauthDTO userOauthDTO) {
        log.info("授权登录code：{}",userOauthDTO.getOauthCode());
        //根据授权成功code获取access_token
        JSONObject result = HttpClientUtil.getTokenPost(userOauthDTO.getOauthCode());
        log.info("换发token获取信息：{}",result);
        String access_token = result.getString("access_token");
        log.info("access_token:{}",access_token);

        //根据access_token获取用户信息
        JSONObject userInfoPost = HttpClientUtil.getUserInfoPost(FanbookPostConstant.FANBOOK_GET_USER_URL,
                "Bearer " + access_token);
        JSONObject userJsonObject = null;
        if (userInfoPost != null) {
            userJsonObject = JSON.parseObject(userInfoPost.getString("data"));
        }

        System.out.println(userJsonObject);
        String userId = userJsonObject.getString("user_id");
        String nickname = userJsonObject.getString("nickname");
        String avatar = userJsonObject.getString("avatar");
        String username = userJsonObject.getString("username");
        String gender = userJsonObject.getString("gender");
        //判断用户是否存在
        QueryWrapper<UserOauth> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_ID",userId)
                .eq("GAME_ID",userOauthDTO.getGameId());
        UserOauth userOauth1 = userOauthMapper.selectOne(wrapper);
        if (userOauth1 == null) {
            //存储用户信息
            UserOauth userOauth = new UserOauth()
                    .setUserId(userId)
                    .setNickName(nickname)
                    .setAvatar(avatar)
                    .setUserName(username)
                    .setGender(gender)
                    .setGameId(userOauthDTO.getGameId())
                    .setUpdateDatetime(new Date());
            userOauthMapper.insert(userOauth);
        }
        Map<String,String> map = new HashMap<>();
        //封装用户信息
        map.put("userId",userId);
        map.put("nickName",nickname);
        map.put("avatar",avatar);
        map.put("gender",gender);
        return map;
    }

    /**
     * 查询用户信息
     * @param userId 用户ID
     * @return UserOauth 用户对象
     */
    @Override
    public UserOauth queryUserInfo(String userId) {
        QueryWrapper<UserOauth> wrapper = new QueryWrapper<>();
        wrapper.eq("USER_ID",userId);
        return userOauthMapper.selectOne(wrapper);
    }
}
