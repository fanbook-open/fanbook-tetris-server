package com.minghui.gamesservice;


import com.alibaba.fastjson.JSONObject;
import com.minghui.dao.GameScoreMapper;
import com.minghui.entity.UserGameScore;
import com.minghui.service.IMessageCardService;
import com.minghui.service.impl.GameScoreServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GamesServiceApplicationTests {

    @Autowired
    private IMessageCardService messageCardService;
    @Autowired
    GameScoreMapper gameScoreMapper;

    @Test
    void contextLoads() {
        UserGameScore u = gameScoreMapper.queryTotalFirstScore("221110739232882688", "273383374205222914");
        System.out.println(JSONObject.toJSON(u));
    }

}
