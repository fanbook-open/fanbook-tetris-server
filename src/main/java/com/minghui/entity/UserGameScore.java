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
 * 用户游戏得分表 实体类
 * @Author 明辉
 * @Date 2021-06-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "TM_USER_GAME_SCORE")
public class UserGameScore implements Serializable {

    //唯一ID
    @TableField(value = "SCORE_ID")
    private Integer scoreId;

    //用户全局唯一ID
    @TableField(value = "USER_ID")
    private String userId;

    //游戏唯一ID
    @TableField(value = "GAME_ID")
    private String gameId;

    //游戏得分
    @TableField(value = "SCORE")
    private Long score;

    //服务器ID
    @TableField(value = "SERVER_ID")
    private String serverId;

    //更新时间
    @TableField(value = "UPDATE_DATETIME")
    private Date updateDatetime;

    //是否是最高分（0-否，1-是）
    @TableField(exist = false)
    private String gender;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String avatar;

    @TableField(value = "rank")
    private Integer rank;
}
