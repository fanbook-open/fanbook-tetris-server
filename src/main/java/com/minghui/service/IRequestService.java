package com.minghui.service;

import com.alibaba.fastjson.JSONObject;
import com.minghui.commons.dto.GameScoreDTO;

public interface IRequestService {
    JSONObject requestApi(GameScoreDTO gameScoreDTO);

}
