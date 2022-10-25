package com.minghui.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 已授权用户表 实体类
 * @Author 明辉
 * @Date 2021-06-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)//链式编程
@TableName(value = "TM_USER_OAUTH")
public class UserOauth implements Serializable {

    //用户全局唯一ID
    @TableField(value = "USER_ID")
    private String userId;

    //用户昵称
    @TableField(value = "NICK_NAME")
    private String nickName;

    //用户可读标识ID
    @TableField(value = "USER_NAME")
    private String userName;

    //头像
    @TableField(value = "AVATAR")
    private String avatar;

    //性别
    @TableField(value = "GENDER")
    private String gender;

    //游戏ID
    @TableField(value = "GAME_ID")
    private String gameId;

    //更新时间
    @TableField(value = "UPDATE_DATETIME")
    private Date updateDatetime;
}
