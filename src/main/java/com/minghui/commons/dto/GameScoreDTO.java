package com.minghui.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 游戏得分DTO
 * @Author 明辉
 * @Date 2021-06-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GameScoreDTO implements Serializable {

    //当前页
    private Integer currentPage;

    //每页条数
    private Integer pageSize;

    //用户ID
    private String userId;

    //游戏ID
    private String gameId;

    //游戏得分
    private Long score;

    //所在服务器ID
    private String serverId;

    private String chatId;

    //前端请求参数md5
    private String md5;
}
