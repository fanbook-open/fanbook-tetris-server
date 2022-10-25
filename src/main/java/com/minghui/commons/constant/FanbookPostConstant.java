package com.minghui.commons.constant;

/**
 * @Author 明辉
 * @Date 2021-06-25
 */
public class FanbookPostConstant {


    /**
     * Oauth2.0 API 正式环境
     */
    public static final String FANBOOK_PROD_URL = "https://a1.fanbook.mobi/open/";

    /**
     * Oauth2.0 API 测试环境
     */
    public static final String FANBOOK_DEV_URL = "http://s2.tensafe.net/open/";

    /**
     * 游戏主页面即授权后跳转地址
     */
    public static final String REDIRECT_URL = "https://open.fanbook.mobi/mp/138519745866498048/273383374205222914/";

    /**
     * 游戏排行榜链接
     */

    public static final String RANK_LIST_URL = "https://open.fanbook.mobi/mp/138519745866498048/273383374205222914/list";

    /**
     * 空卡片图片URL
     */
    public static final String CARD_IMG_URL = "";

    /**
     * 今日最高皇冠图片
     */
    public static final String HIGHEST_TODAY_CROWN_PIC_URL = "";

    /**
     * 历史最高皇冠图片
     */
    public static final String HIGHEST_HIS_CROWN_PIC_URL = "";


    /**
     * 消息卡片背景图片
     */
    public static final String MGS_CARD_BACKGROUND_PIC_URL = "";

    /**
     * 消息背景图片存放目录
     */
    public static final String MSG_BG_FOLDER ="tetris-game";


    /**
     * 获取机器人消息更新url
     */
    public static final String GET_BOOT_UPDATES_URL =
            "https://a1.fanbook.mobi/api/bot/bot" + FanbookConstant.BOOT_ID + "/getUpdates";


    /**
     * Fanbook发送消息卡片URL
     */
    public static final String SEND_MSG_CARD_URL =
            "https://a1.fanbook.mobi/api/bot/bot" + FanbookConstant.BOOT_ID + "/sendMessage";
    /**
     * Fanbook通过这个方法可创建与某个用户的私聊会话
     */
    public static final String getPrivateChat=
            "https://a1.fanbook.mobi/api/bot/bot" + FanbookConstant.BOOT_ID + "/getPrivateChat";
    /**
     * Fanbook获取服务器的基本信息
     */
    public static final String getGuild=
            "https://a1.fanbook.mobi/api/bot/bot" + FanbookConstant.BOOT_ID + "/guild";
    /**
     * FanBook获取access_token  URL
     */
    public static final String FANBOOK_GET_ACCESS_TOKEN_URL = FanbookPostConstant.FANBOOK_PROD_URL + "oauth2/token";

    /**
     * Fanbook获取用户信息URL
     */
    public static final String FANBOOK_GET_USER_URL = FanbookPostConstant.FANBOOK_PROD_URL + "api/user/getMe";
}
