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
 * 游戏信息表 实体类
 * @Author 明辉
 * @Date 2021-06-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "TM_GAME_INFO")
public class GameInfo implements Serializable {

    //游戏唯一ID
    @TableField(value = "GAME_ID")
    private String gameId;

    //游戏名称
    @TableField(value = "GAME_NAME")
    private String gameName;

    //机器人ID
    @TableField(value = "SERVER_ID")
    private String bootId;

    //更新时间
    @TableField(value = "UPDATE_DATETIME")
    private Date updateDatetime;
}
