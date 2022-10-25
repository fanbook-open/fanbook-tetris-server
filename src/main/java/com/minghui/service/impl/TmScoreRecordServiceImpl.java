package com.minghui.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minghui.dao.TmScoreRecordDao;
import com.minghui.entity.TmScoreRecord;
import com.minghui.service.TmScoreRecordService;
import org.springframework.stereotype.Service;

/**
 * (TmScoreRecord)表服务实现类
 *
 * @author makejava
 * @since 2021-12-02 15:30:47
 */
@Service("tmScoreRecordService")
public class TmScoreRecordServiceImpl extends ServiceImpl<TmScoreRecordDao, TmScoreRecord> implements TmScoreRecordService {

}
