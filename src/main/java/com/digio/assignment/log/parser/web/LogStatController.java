package com.digio.assignment.log.parser.web;

import com.digio.assignment.log.parser.exception.DateTimeFormatException;
import com.digio.assignment.log.parser.model.LogStat;
import com.digio.assignment.log.parser.model.RequestObj;
import com.digio.assignment.log.parser.repo.LogRepository;
import com.digio.assignment.log.parser.utils.LogParserConstants;
import com.digio.assignment.log.parser.utils.LogParserUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/log")
public class LogStatController {

    @Autowired
    private LogRepository logRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogStat create(@RequestBody LogStat logStat) {

        return logRepository.save(logStat);
    }

    @PostMapping("/ip/unique")
    public ResponseEntity<Object> getUniqueIPsCount(@RequestBody RequestObj requestObj) {

        JSONObject jsonObject = new JSONObject();
        try {
            long startTime = requestObj.getStartTime();
            long endTime = requestObj.getEndTime() == 0L ?
                    LogParserUtils.getCurrentEpochTime() :
                    requestObj.getEndTime();
            jsonObject.put(LogParserConstants.COUNT_CONSTANT,
                    logRepository.findUniqueIPsCount(startTime, endTime));
        } catch (ParseException e) {
            throw new DateTimeFormatException("TimeStamp conversation failed"
                    , e.getCause());
        }

        return ResponseEntity.ok(jsonObject);
    }

    @PostMapping("/url/unique")
    public ResponseEntity<Object> getTopVisitedURL(@RequestBody RequestObj requestObj) {

        JSONObject jsonObject = new JSONObject();

        try {
            long startTime = requestObj.getStartTime();
            long endTime = requestObj.getEndTime() == 0L ?
                    LogParserUtils.getCurrentEpochTime() :
                    requestObj.getEndTime();
            int count = requestObj.getCount() == -1 ? LogParserConstants.DEFAULT_MAX_LIMIT_COUNT :
                    requestObj.getCount();
            List<Object[]> urlsList =
                    logRepository.findMostVisitedURLs(startTime, endTime,
                            count);
            JSONArray jsonArray = new JSONArray();

            for (Object[] ipObj : urlsList) {
                JSONObject obj = new JSONObject();
                obj.put(LogParserConstants.URL_CONSTANT, ipObj[0]);
                obj.put(LogParserConstants.COUNT_CONSTANT, ipObj[1]);
                jsonArray.add(obj);
            }
            jsonObject.put(LogParserConstants.URLS_CONSTANT, jsonArray);
        } catch (ParseException e) {
            throw new DateTimeFormatException("TimeStamp conversation failed"
                    , e.getCause());
        }
        return ResponseEntity.ok(jsonObject);

    }

    @PostMapping("/ip/active")
    public ResponseEntity<Object> getActiveIPs(@RequestBody RequestObj requestObj) {

        JSONObject jsonObject = new JSONObject();

        try {
            long startTime = requestObj.getStartTime();
            long endTime = requestObj.getEndTime() == 0L ?
                    LogParserUtils.getCurrentEpochTime() :
                    requestObj.getEndTime();
            int count = requestObj.getCount() == -1 ? LogParserConstants.DEFAULT_MAX_LIMIT_COUNT :
                    requestObj.getCount();

            List<Object[]> ipsList =
                    logRepository.findMostActiveIPs(startTime, endTime, count);
            JSONArray jsonArray = new JSONArray();

            for (Object[] ipObj : ipsList) {
                JSONObject obj = new JSONObject();
                obj.put(LogParserConstants.IP_CONSTANT, ipObj[0]);
                obj.put(LogParserConstants.COUNT_CONSTANT, ipObj[1]);
                jsonArray.add(obj);
            }
            jsonObject.put(LogParserConstants.IPS_CONSTANT, jsonArray);

        } catch (ParseException e) {
            throw new DateTimeFormatException("TimeStamp conversation failed"
                    , e.getCause());
        }
        return ResponseEntity.ok(jsonObject);
    }

}
