package com.victorzhang.eduwisdom.controller;

import com.victorzhang.eduwisdom.domain.ScoreRecord;
import com.victorzhang.eduwisdom.service.ScoreRecordService;
import com.victorzhang.eduwisdom.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.sql.SQLException;

import static com.victorzhang.eduwisdom.util.Constants.INSERT_ERROR;
import static com.victorzhang.eduwisdom.util.Constants.SCORE_FLAG;
import static com.victorzhang.eduwisdom.util.Constants.USER_ID;

@Controller
@RequestMapping("scoreRecord")
public class ScoreRecordController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    @Qualifier("scoreRecordService")
    private ScoreRecordService scoreRecordService;

    @RequestMapping("/getRateScore.do")
    @ResponseBody
    public ScoreRecord getRateScore(String resourceId) throws Exception {
        ScoreRecord scoreRecord = new ScoreRecord(resourceId, CommonUtils.sesAttr(request, USER_ID));
        ScoreRecord scoreRecordDB = scoreRecordService.get(scoreRecord);
        if (scoreRecordDB != null && StringUtils.equals(scoreRecordDB.getScoreFlag(),SCORE_FLAG)) {
            return scoreRecordDB;
        }
        return null;
    }

    @RequestMapping("/saveRate.do")
    @ResponseBody
    public void saveRate(String score, String resourceId) throws Exception {
        String userId = CommonUtils.sesAttr(request, USER_ID);
        String ratingTime = CommonUtils.getDateTime();
        ScoreRecord scoreRecord = new ScoreRecord(userId, resourceId, score, ratingTime, SCORE_FLAG);
        if (!scoreRecordService.update(scoreRecord)) {
            throw new SQLException(INSERT_ERROR);
        }
    }
}