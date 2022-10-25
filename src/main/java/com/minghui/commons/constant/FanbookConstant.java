package com.minghui.commons.constant;

/**
 * Fanbook 数据常量类
 * @Author 明辉
 * @Date 2021-06-09
 */
public class FanbookConstant {

    /**
     * 机器人令牌
     */
    public static final String BOOT_ID = "";//正式token

    /**
     * 游戏ID
     */
    public static final String GAME_ID = "";//正式参数
    /**
     * @俄罗斯方块 机器人聊天消息ID
     */
    public static final String BOOT_CHAT_MSG_ID = "${@!"+GAME_ID+"}";


    /**
     * 发送消息post - 发送消息的格式
     */
    public static final String PARSE_MODE = "Fanbook";

    /**
     * clientId
     */
    public static final String CLIENT_ID = "";//正式参数
    /**
     * clientSecret
     */
    public static final String SECRET_KEY = "";//正式参数

    /**
     * 请求参数32位校验KEY，和前端md5key保持一致
     */
    public static final String MD5_KEY = "";//正式参数
    /**
     * 获取用户access_token post - clientId:clientSecret
     */
    public static final String CLIENT_ID_AND_SECRET_KEY = CLIENT_ID+":"+SECRET_KEY;

    /**
     * 获取用户access_token post - grant_type属性
     */
    public static final String GRANT_TYPE = "authorization_code";

}
