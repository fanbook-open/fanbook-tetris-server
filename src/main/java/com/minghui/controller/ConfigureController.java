package com.minghui.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.minghui.entity.TServerConfig;
import com.minghui.commons.enums.ResponseEnum;
import com.minghui.commons.vo.ResponseResult;
import com.minghui.service.IServerConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigureController {
    @Autowired
    IServerConfigService serverConfigService;

    /**
     * 添加服务器规则
     *
     * @param serverConfigDTO
     * @return ResponseResult
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody TServerConfig serverConfigDTO) {
        QueryWrapper<TServerConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("guild_id", serverConfigDTO.getGuildId());
        queryWrapper.eq("type", serverConfigDTO.getType());
        List<TServerConfig> list = serverConfigService.list(queryWrapper);

        Boolean flg;
        if (null != serverConfigDTO.getId() && serverConfigDTO.getId() > 0 || list.size() > 0) {
//            UpdateWrapper<TServerConfig> updateWrapper = new UpdateWrapper();
//            updateWrapper.set("data_json", serverConfigDTO.getDataJson());
//            updateWrapper.eq("id", serverConfigDTO.getId());
//            updateWrapper.eq("guild_id", serverConfigDTO.getGuildId());
            flg = serverConfigService.update(Wrappers.<TServerConfig>lambdaUpdate()
                    .set(TServerConfig::getDataJson,serverConfigDTO.getDataJson())
                    .eq(TServerConfig::getId,serverConfigDTO.getId())
                    .eq(TServerConfig::getGuildId,serverConfigDTO.getGuildId())
            );
        } else {
            flg = serverConfigService.save(serverConfigDTO);
        }

        if (flg) {
            return new ResponseResult(
                    ResponseEnum.SUCCESS.getCode(),
                    ResponseEnum.SUCCESS.getMsg(),
                    flg);
        }
        return new ResponseResult(
                ResponseEnum.SAVE_CONFIG_FAIL.getCode(),
                ResponseEnum.SAVE_CONFIG_FAIL.getMsg(),
                null);
    }

    /**
     * 查询服务器规则
     *
     * @param serverConfigDTO
     * @return ResponseResult
     */
    @PostMapping("/query")
    public ResponseResult query(@RequestBody TServerConfig serverConfigDTO) {
        QueryWrapper<TServerConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("guild_id", serverConfigDTO.getGuildId());
        List<TServerConfig> list = serverConfigService.list(queryWrapper);
        return new ResponseResult(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMsg(),
                list);

    }

    /**
     * 修改状态服务器规则
     *
     * @param serverConfigDTO
     * @return ResponseResult
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody TServerConfig serverConfigDTO) {
        UpdateWrapper<TServerConfig> updateWrapper = new UpdateWrapper();
        updateWrapper.set("status", serverConfigDTO.getStatus());
        updateWrapper.eq("id", serverConfigDTO.getId());
        updateWrapper.eq("guild_id", serverConfigDTO.getGuildId());
        Boolean flg = serverConfigService.update(updateWrapper);
        return new ResponseResult(
                ResponseEnum.SUCCESS.getCode(),
                ResponseEnum.SUCCESS.getMsg(),
                flg);

    }


    /**
     * 测试控制器
     */
    @GetMapping("/test")
    public void test() {
    }

    /**
     * 测试控制器
     */
    @GetMapping("/reissue")
    public void reissue(Integer day) {
        if (day > 0) {
            serverConfigService.executeRankToDay(day);
        }
    }

}
