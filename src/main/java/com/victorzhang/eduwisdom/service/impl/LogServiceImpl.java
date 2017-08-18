package com.victorzhang.eduwisdom.service.impl;

import com.victorzhang.eduwisdom.domain.Log;
import com.victorzhang.eduwisdom.mapper.BaseMapper;
import com.victorzhang.eduwisdom.mapper.LogMapper;
import com.victorzhang.eduwisdom.service.LogService;
import com.victorzhang.eduwisdom.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.victorzhang.eduwisdom.util.Constants.ADMIN_ROLE_ID;
import static com.victorzhang.eduwisdom.util.Constants.ROLE_ID;
import static com.victorzhang.eduwisdom.util.Constants.USER_ID;

@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<Log, String> implements LogService {

    @Autowired
    @Qualifier("logMapper")
    private LogMapper logMapper;

    @Override
    protected BaseMapper<Log, String> getMapper() {
        return logMapper;
    }

    @Override
    public boolean saveLogByLogTypeAndLogContent(String logType, String logContent, String userId) throws Exception {
        Log log = new Log();

        log.setId(CommonUtils.newUuid());
        log.setLogType(logType);
        log.setLogContent(logContent);
        log.setUserId(userId);
        log.setUserDate(CommonUtils.getDateTime());
        log.setUserIp(CommonUtils.getIpAddr());

        return super.save(log);
    }

    @Override
    public boolean saveLogByLogTypeAndLogContent(String logType, String logContent) throws Exception {
        String userId = (String) CommonUtils.getSession(false).getAttribute(USER_ID);
        return saveLogByLogTypeAndLogContent(logType, logContent, userId);
    }

    @Override
    public List<Map<String, Object>> listLogType(HttpServletRequest request) throws Exception {
        String userId = null;
        //if user's role is admin, search all log type
        if (!StringUtils.equals(CommonUtils.sesAttr(request, ROLE_ID), ADMIN_ROLE_ID)) {
            userId = CommonUtils.sesAttr(request, USER_ID);
        }
        return logMapper.listLogType(userId);
    }

}
