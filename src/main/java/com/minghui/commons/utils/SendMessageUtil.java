package com.minghui.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.minghui.commons.constant.FanbookConstant;
import com.minghui.commons.constant.FanbookPostConstant;
import com.minghui.commons.dto.GameScoreDTO;
import com.minghui.commons.dto.MessageCardDTO;
import com.minghui.entity.UserGameScore;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;

/**
 * 机器人发送卡片消息工具类
 *
 * @Author 明辉
 * @Date 2021-07-01
 */
public class SendMessageUtil {

    /**
     * 字体
     */
//    public static final String FONT_NAME = "苹方-简 常规体";
    public static final String FONT_NAME = "微软雅黑";
    public static final String FONT_NAME1 = "Menlo";
    //public static final String FONT_NAME1=null;
    public static final Integer FONT_SIZE_30 = 30;
    public static final Integer FONT_SIZE_35 = 35;
    public static final Integer FONT_SIZE_40 = 40;
    public static final Integer FONT_SIZE_45 = 45;
    public static final Integer FONT_SIZE_50 = 50;
    public static final Integer FONT_SIZE_55 = 55;
    public static final Integer FONT_SIZE_36 = 36;
    public static final Integer FONT_SIZE_60 = 60;

    public static final String TODAY = "today";
    public static final String HIS = "his";
    public static final String TODAY_AND_HIS = "todayAndHis";

    public static final String FORMAT_NAME_TYPE = "png";

    /**
     * 类型1：排行榜&进入游戏 json串 字符串替换 %s
     */
    public static final String CONTENT_JSON_STR_TYPE1 = "{\"type\":\"task\",\"content\":{\"type\":\"column\",\"children\":[{\"param\":{\"image\":\"%s\",\"type\":2},\"type\":\"image\"},{\"type\":\"button\",\"param\":{\"list\":[{\"type\":3,\"text\":\"排行榜\",\"event\":{\"method\":\"mini_program\",\"param\":{\"appId\":\"%s\"}}},{\"type\":3,\"text\":\"进入游戏\",\"event\":{\"method\":\"mini_program\",\"param\":{\"appId\":\"%s\"}}}]}}]}}";

    /**
     * 类型2：我要挑战 json串 字符串替换 %s
     */
    public static final String CONTENT_JSON_STR_TYPE2 = "{\"type\":\"task\",\"content\":{\"type\":\"column\",\"children\":[{\"param\":{\"image\":\"%s\",\"type\":2},\"type\":\"image\"},{\"type\":\"button\",\"param\":{\"list\":[{\"type\":3,\"text\":\"我要挑战\",\"event\":{\"method\":\"mini_program\",\"param\":{\"appId\":\"%s\"}}}]}}]}}";

    /**
     * 处理消息卡片请求json
     *
     * @param chatId
     * @param textJson
     * @return
     */
    public static JSONObject handleMsgJsonStr(String chatId, String textJson) {
        JSONObject json = new JSONObject();
        json.put("type", "task");
        json.put("chat_id", new BigInteger(chatId));
        json.put("text", textJson);
        json.put("parse_mode", FanbookConstant.PARSE_MODE);
        System.out.println("发送数据：" + json);
        return json;
    }

    /**
     * 炫耀一下卡片内容
     *
     * @param messageCardDTO
     * @param nickname
     * @return
     */
    public static String sendMessage1(MessageCardDTO messageCardDTO, String nickname) {
        // 处理图片
        String cardImgUrl = makeTextFirstScore(nickname, String.valueOf(messageCardDTO.getScore()));
        JSONObject json = handleMsgJsonStr(messageCardDTO.getChatId(), String.format(CONTENT_JSON_STR_TYPE2, cardImgUrl, FanbookPostConstant.REDIRECT_URL));
        return JSON.toJSONString(json);
    }

    /**
     * 保存得分 发送刷新历史最高得分卡片
     *
     * @param nickname
     * @param avatarUrl
     * @param gameScoreDTO
     * @return
     */
    public static String sendMessage2(String nickname, String avatarUrl, GameScoreDTO gameScoreDTO) {

        // 处理图片
        String cardImgUrl = makeFirstScore(nickname, avatarUrl, String.valueOf(gameScoreDTO.getScore()), HIS);
        JSONObject json = handleMsgJsonStr(gameScoreDTO.getChatId(), String.format(CONTENT_JSON_STR_TYPE2, cardImgUrl, FanbookPostConstant.REDIRECT_URL+"?a="+System.currentTimeMillis()));
        return JSON.toJSONString(json);

    }


    /**
     * 保存得分 发送刷新今日最高得分卡片
     *
     * @param nickname
     * @param avatarUrl
     * @param gameScoreDTO
     * @return
     */
    public static String sendMessage3(String nickname, String avatarUrl, GameScoreDTO gameScoreDTO) {

        String cardImgUrl = makeFirstScore(nickname, avatarUrl, String.valueOf(gameScoreDTO.getScore()), TODAY);
        //封装发送请求的json参数
        JSONObject json = handleMsgJsonStr(gameScoreDTO.getChatId(), String.format(CONTENT_JSON_STR_TYPE2, cardImgUrl, FanbookPostConstant.REDIRECT_URL+"?a="+System.currentTimeMillis()));
        return JSON.toJSONString(json);
    }


    /**
     * @param chatId
     * @param hisScore
     * @param todayScore
     * @return
     * @俄罗斯方块机器人 发送游戏卡片
     */
    public static String sendMessage4(String chatId, UserGameScore hisScore
            , UserGameScore todayScore) {
        //处理消息背景图片
        String cardImgUrl = FanbookPostConstant.CARD_IMG_URL;
        if (hisScore != null && todayScore != null) {
            cardImgUrl = makeTodayAndHisFirstScore(hisScore.getNickName(), hisScore.getAvatar(), String.valueOf(hisScore.getScore())
                    , todayScore.getNickName(), todayScore.getAvatar(), String.valueOf(todayScore.getScore()));
        } else if (hisScore != null) {
            cardImgUrl = makeFirstScore(hisScore.getNickName(), hisScore.getAvatar(), String.valueOf(hisScore.getScore()), TODAY_AND_HIS);
        }

        //封装发送请求的json参数
        JSONObject json = handleMsgJsonStr(chatId, String.format(CONTENT_JSON_STR_TYPE1, cardImgUrl, FanbookPostConstant.RANK_LIST_URL+"?a="+System.currentTimeMillis(), FanbookPostConstant.REDIRECT_URL+"?a="+System.currentTimeMillis()));
//        HttpClientUtil.sendMsgCardPost(FanbookPostConstant.SEND_MSG_CARD_URL,JSON.toJSONString(json));
        RestTemplateUtil.builder().post(FanbookPostConstant.SEND_MSG_CARD_URL, json);
        return JSON.toJSONString(json);
    }

    /**
     * 最高得分图片处理
     *
     * @param userName
     * @param avatarUrl
     * @param userScore
     * @param type      today-今日，his-历史
     * @return 图片保存路径
     */
    public static String makeFirstScore(String userName, String avatarUrl, String userScore, String type) {
        try {

            BufferedImage img = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_RGB);
            BufferedImage bg = ImageIO.read(new URL(FanbookPostConstant.MGS_CARD_BACKGROUND_PIC_URL));
            BufferedImage avatarImg = ImageIO.read(makeRoundedCorner(avatarUrl, FORMAT_NAME_TYPE)); // 头像1
            BufferedImage crownImg;

            String title = "";
            if (type.equals(TODAY)) {
                title = "今日最高纪录刷新啦~";
                crownImg = ImageIO.read(new URL(FanbookPostConstant.HIGHEST_TODAY_CROWN_PIC_URL)); // 皇冠
            } else if (type.equals(TODAY_AND_HIS)) {
                title = "历史最高";
                crownImg = ImageIO.read(new URL(FanbookPostConstant.HIGHEST_TODAY_CROWN_PIC_URL)); // 皇冠
            } else {
                title = "历史最高记录刷新啦~";
                crownImg = ImageIO.read(new URL(FanbookPostConstant.HIGHEST_HIS_CROWN_PIC_URL)); // 皇冠
            }

            Graphics2D g = img.createGraphics();
            g.drawImage(bg.getScaledInstance(995, 595, Image.SCALE_DEFAULT), 2, 0, null);
            g.drawImage(avatarImg.getScaledInstance(150, 150, Image.SCALE_DEFAULT), 430, 200, null);

            g.drawImage(crownImg.getScaledInstance(50, 50, Image.SCALE_DEFAULT), 530, 170, null);

            g.setColor(Color.WHITE);
            g.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE_45));


            int titleWidth = g.getFontMetrics().stringWidth(title);
            g.drawString(title, 500 - titleWidth / 2, 130);

            g.setFont(new Font(FONT_NAME1, Font.PLAIN, 40));

            int nameWidth = g.getFontMetrics().stringWidth(userName);
            g.drawString(userName, 500 - nameWidth / 2, 430);

            g.setFont(new Font(FONT_NAME, Font.BOLD, 64));
            String score = String.format("%s分", userScore);
            int scoreWidth = g.getFontMetrics().stringWidth(score);
            g.drawString(score, 500 - scoreWidth / 2, 520);
            g.dispose();

            //上传到cos
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(img, FORMAT_NAME_TYPE, stream);
            String s = Base64.getEncoder().encodeToString(stream.toByteArray());
            return TencentCFSUtils.uploadFileParseBase64(FanbookPostConstant.MSG_BG_FOLDER, s);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 今日和历史最高得分图片处理
     *
     * @param hisUserName
     * @param hisAvatarUrl
     * @param hisUserScore
     * @param todayUserName
     * @param todayAvatarUrl
     * @param todayUserScore
     * @return 图片保存路径
     */
    public static String makeTodayAndHisFirstScore(String hisUserName, String hisAvatarUrl, String hisUserScore
            , String todayUserName, String todayAvatarUrl, String todayUserScore) {
        try {
            //名字超长处理
            hisUserName = subString(hisUserName, 20);
            todayUserName = subString(todayUserName, 20);

            BufferedImage img = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_RGB);
            BufferedImage bg = ImageIO.read(new URL(FanbookPostConstant.MGS_CARD_BACKGROUND_PIC_URL));
            BufferedImage todayAvatarImg = ImageIO.read(makeRoundedCorner(todayAvatarUrl, FORMAT_NAME_TYPE)); // 头像1
            BufferedImage todayCrownImg = ImageIO.read(new URL(FanbookPostConstant.HIGHEST_TODAY_CROWN_PIC_URL)); // 今日最高皇冠
            BufferedImage hisAvatarImg = ImageIO.read(makeRoundedCorner(hisAvatarUrl, FORMAT_NAME_TYPE)); // 头像1
            BufferedImage hisCrownImg = ImageIO.read(new URL(FanbookPostConstant.HIGHEST_HIS_CROWN_PIC_URL)); // 今日最高皇冠

            Graphics2D g = img.createGraphics();
            // 背景
            g.drawImage(bg.getScaledInstance(995, 595, Image.SCALE_DEFAULT), 0, 0, null);
            //头像+皇冠+标志图片
            g.drawImage(todayAvatarImg.getScaledInstance(150, 150, Image.SCALE_DEFAULT), 220, 210, null);
            g.drawImage(todayCrownImg.getScaledInstance(60, 60, Image.SCALE_DEFAULT), 315, 173, null);

            g.drawImage(hisAvatarImg.getScaledInstance(150, 150, Image.SCALE_DEFAULT), 620, 210, null);
            g.drawImage(hisCrownImg.getScaledInstance(60, 60, Image.SCALE_DEFAULT), 715, 173, null);

            // 标题、名字
            g.setColor(Color.WHITE);
            g.setFont(new Font(FONT_NAME, Font.BOLD, 38));

            String todayTitle = "今日最高";
            int todayTitleWidth = g.getFontMetrics().stringWidth(todayTitle);
            g.drawString(todayTitle, 300 - todayTitleWidth / 2, 130);

            String hisTitle = "历史最高";
            int hisTitleWidth = g.getFontMetrics().stringWidth(hisTitle);
            g.drawString(hisTitle, 700 - hisTitleWidth / 2, 130);

            g.setFont(new Font(FONT_NAME1, Font.PLAIN, 40));
            int todayNameWidth = g.getFontMetrics().stringWidth(todayUserName);
            g.drawString(todayUserName, 300 - todayNameWidth / 2, 430);

            int hisNameWidth = g.getFontMetrics().stringWidth(hisUserName);
            g.drawString(hisUserName, 700 - hisNameWidth / 2, 430);

            // 得分
            // 今日最高得分
            g.setFont(new Font(FONT_NAME, Font.BOLD, 64));
            String todayScore = String.format("%s分", todayUserScore);
            int todayScoreWidth = g.getFontMetrics().stringWidth(todayScore);
            g.drawString(todayScore, 300 - todayScoreWidth / 2, 510);

            String hisScore = String.format("%s分", hisUserScore);
            int hisScoreWidth = g.getFontMetrics().stringWidth(hisScore);
            g.drawString(hisScore, 700 - hisScoreWidth / 2, 510);
            g.dispose();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(img, FORMAT_NAME_TYPE, stream);
            String s = Base64.getEncoder().encodeToString(stream.toByteArray());
            return TencentCFSUtils.uploadFileParseBase64(FanbookPostConstant.MSG_BG_FOLDER, s);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理文本消息
     *
     * @param userName
     * @param userScore
     * @return
     */
    public static String makeTextFirstScore(String userName, String userScore) {
        try {
            //名字超长处理
            userName = subString2(userName, 20);

            BufferedImage img = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_RGB);
            BufferedImage bg = ImageIO.read(new URL(FanbookPostConstant.MGS_CARD_BACKGROUND_PIC_URL));

            Graphics2D g = img.createGraphics();
            g.drawImage(bg.getScaledInstance(995, 595, Image.SCALE_DEFAULT), 0, 0, null);

//            String textStr= String.format("恭喜 @%s 在[俄罗斯方块]中获得",userName);
//            g.setColor(Color.WHITE);
//            g.setFont(new Font(FONT_NAME,Font.BOLD,42));
//            int textWidth = g.getFontMetrics().stringWidth(textStr);
//            g.drawString(textStr, 500 - textWidth/2,280);

            String textStr = String.format("恭喜 @%s", userName);
            g.setColor(Color.WHITE);
            g.setFont(new Font(FONT_NAME1, Font.PLAIN, 46));
            int textWidth = g.getFontMetrics().stringWidth(textStr);
            g.drawString(textStr, 500 - textWidth / 2, 210);

            String textStr1 = String.format("在[俄罗斯方块]中获得");
            g.setColor(Color.WHITE);
            g.setFont(new Font(FONT_NAME, Font.PLAIN, 46));
            int textWidth1 = g.getFontMetrics().stringWidth(textStr1);
            g.drawString(textStr1, 500 - textWidth1 / 2, 280);

            g.setColor(Color.WHITE);
            g.setFont(new Font(FONT_NAME, Font.BOLD, 70));
            String score = String.format("%s分", userScore);
            int scoreWidth = g.getFontMetrics().stringWidth(score);
            g.drawString(score, 500 - scoreWidth / 2, 400);
            g.dispose();

            //上传到cos
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(img, FORMAT_NAME_TYPE, stream);
            String s = Base64.getEncoder().encodeToString(stream.toByteArray());
            return TencentCFSUtils.uploadFileParseBase64(FanbookPostConstant.MSG_BG_FOLDER, s);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        makeTextFirstScore("我的名字长长长长长...", "10000");
        makeFirstScore("我的名字长长长长\uD83E\uDD8A",
                "https://fb-cdn.fanbook.mobi/fanbook/app/files/service/headImage/72c758046ed7f3acc0da11bce6edb54c",
                "10000", HIS);

        makeTodayAndHisFirstScore("我的名字长长长长\uD83E\uDD8A",
                "https://fb-cdn.fanbook.mobi/fanbook/app/files/service/headImage/72c758046ed7f3acc0da11bce6edb54c",
                "10000", "我的名字长长长长1111",
                "https://fb-cdn.fanbook.mobi/fanbook/app/files/service/headImage/72c758046ed7f3acc0da11bce6edb54c",
                "10000");
    }

    /*
     * 头像圆角处理
     * @param BufferedImage
     * @param cornerRadius
     * */
    public static InputStream makeRoundedCorner(String avatarUrl, String type) {
        try {
            URL url = new URL(avatarUrl);

            BufferedImage image = ImageIO.read(url);
            int w = image.getWidth();
            int h = image.getHeight();
            BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = output.createGraphics();
            output = g2.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
            g2.dispose();
            g2 = output.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, w, h, 1000, 1000);
            g2.setComposite(AlphaComposite.SrcIn);
            g2.drawImage(image, 0, 0, w, h, null);
            g2.dispose();

            //Base64
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(output, type, stream);
            String s = Base64.getEncoder().encodeToString(stream.toByteArray());

            BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(s);
            InputStream bais = new ByteArrayInputStream(bytes);

            return bais;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String subString(String strValue, int maxLength) {
        StringBuffer resultStr = new StringBuffer();

        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < strValue.length(); i++) {
            /* 获取一个字符 */
            String temp = strValue.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                maxLength -= 2;
            } else {
                /* 其他字符长度为1 */
                maxLength -= 1;
            }
            resultStr.append(temp);
            if (maxLength <= 0) {
                resultStr.append("...");
                return resultStr.toString();
            }

        }
        return resultStr.toString();
    }


    public static String subString2(String strValue, int maxLength) {
        StringBuffer resultStr = new StringBuffer();

        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < strValue.length(); i++) {
            /* 获取一个字符 */
            String temp = strValue.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                maxLength -= 2;
            } else {
                /* 其他字符长度为1 */
                maxLength -= 1;
            }
            resultStr.append(temp);
            if (maxLength <= 0) {
                resultStr.append("...");
                return resultStr.toString();
            }

        }
        return resultStr.toString();
    }
}
