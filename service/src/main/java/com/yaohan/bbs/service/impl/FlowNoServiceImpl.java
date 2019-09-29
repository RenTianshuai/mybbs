package com.yaohan.bbs.service.impl;

import com.yaohan.bbs.service.FlowNoService;
import com.yaohan.bbs.utils.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FlowNoServiceImpl implements FlowNoService {

    @Value("${id.worker}")
    private Long workerId;

    @Value("${id.datacenter}")
    private Long datacenterId;

    private static SnowflakeIdUtil snowflakeIdUtil = null;

    @Override
    public synchronized String  generateFlowNo() {
        if(snowflakeIdUtil == null){
            snowflakeIdUtil = new SnowflakeIdUtil(workerId, datacenterId);
        }
        return String.valueOf(snowflakeIdUtil.nextId());
    }
}
