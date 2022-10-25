package com.minghui.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户授权DTO
 * @Author 明辉
 * @Date 2021-06-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOauthDTO implements Serializable {

    //是否授权（true-允许，false-拒绝）
    private Boolean allow;

    //用户ID
    private String userId;

    //昵称
    private String nickName;

    //头像
    private String avatar;

    //性别
    private String gender;

    //游戏ID
    private String gameId;

    //授权code
    private String oauthCode;

    //前端请求参数MD5
    private String md5;

}
