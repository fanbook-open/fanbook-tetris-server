package com.minghui.controller;

import com.minghui.commons.dto.UserOauthDTO;
import com.minghui.commons.enums.ResponseEnum;
import com.minghui.commons.utils.Md5Util;
import com.minghui.commons.vo.ResponseResult;
import com.minghui.entity.UserOauth;
import com.minghui.service.IUserOauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author 明辉
 * @Date 2021-06-09
 */
@RestController
@RequestMapping("/oauth")
public class UserOauthController {

    @Autowired
    private IUserOauthService userOauthService;

    /**
     * 查询用户是否授权
     * @return ResponseResult
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/judgeAuthorization")
    public ResponseResult judgeAuthorization(@RequestBody UserOauthDTO userOauthDTO) {

        String md5key = "userId:"+userOauthDTO.getUserId()
                +",gameId:"+userOauthDTO.getGameId()
                +",nickName:"+userOauthDTO.getNickName()
                +",avatar:"+userOauthDTO.getAvatar()
                +",gender:"+userOauthDTO.getGender();
        // 前端请求参数校验
        if(!Md5Util.checkParameterMd5(md5key,userOauthDTO.getMd5())){
            return new ResponseResult(
                    ResponseEnum.USER_NO_OAUTH.getCode(),
                    ResponseEnum.USER_NO_OAUTH.getMsg(),
                    null);
        }

        UserOauth userOauth = userOauthService.judgeAuthorization(userOauthDTO);
        if (userOauth != null) {
            return new ResponseResult(
                    ResponseEnum.SUCCESS.getCode(),
                    ResponseEnum.SUCCESS.getMsg(),
                    userOauth);
        }
        return new ResponseResult(
                ResponseEnum.USER_NO_OAUTH.getCode(),
                ResponseEnum.USER_NO_OAUTH.getMsg(),
                null);
    }

    /**
     * 存储授权用户信息
     * @return ResponseResult
     */
    @PostMapping("/saveUserInfo")
    public ResponseResult saveUserInfo(@RequestBody UserOauthDTO userOauthDTO){
        Map<String,String> map = userOauthService.saveUserInfo(userOauthDTO);
        if (map.size() > 0) {
            return new ResponseResult(
                    ResponseEnum.SUCCESS.getCode(),
                    ResponseEnum.SUCCESS.getMsg(),
                    map);
        }
        return new ResponseResult(
                ResponseEnum.SAVE_USER_INFO_FAIL.getCode(),
                ResponseEnum.SAVE_USER_INFO_FAIL.getMsg(),
                null);
    }

}
