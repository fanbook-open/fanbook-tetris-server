package com.minghui.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class Point {
    @JSONField(name = "guild_id")
    String guildId;
    @JSONField(name = "user_id")
    String userId;
    @JSONField(name = "nonce_str")
    String nonceStr;
    Long timestamp;
    String point;
    String remark;
}
