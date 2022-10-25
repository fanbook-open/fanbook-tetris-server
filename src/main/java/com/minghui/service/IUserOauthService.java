package com.minghui.service;

import com.minghui.commons.dto.UserOauthDTO;
import com.minghui.entity.UserOauth;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IUserOauthService {
    UserOauth judgeAuthorization(UserOauthDTO userOauthDTO);

    Map<String, String> saveUserInfo(UserOauthDTO userOauthDTO);

    UserOauth queryUserInfo(String userId);
}
