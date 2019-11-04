package com.digio.assignment.log.parser.service;

import com.digio.assignment.log.parser.exception.LogFileNotFoundException;
import com.digio.assignment.log.parser.model.LogStat;
import com.digio.assignment.log.parser.utils.LogParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogService {

    Logger logger = LoggerFactory.getLogger(LogService.class);

    //@Value("${spring.file.name}")
    private String fileName;

    @Value("${spring.log.pattern.regexp}")
    String logPatternRedExp;

    public List<LogStat> processFile(){
        Path path = Paths.get(fileName);
        Pattern pattern = Pattern.compile(logPatternRedExp);
        List<LogStat> logStatList = new ArrayList<>();
        try {
            Files.lines(path).forEach(line -> {
                try {
                    processLog(line, pattern, logStatList);
                } catch (ParseException e) {
                    logger.error("Parsing of the log line failed due to " +
                            "malformed date for the line : " + line);
                }
            });
        } catch (IOException e){
           throw new LogFileNotFoundException("Log file reading operation " +
                   "failed", e.getCause());
        }
        return logStatList;
    }

    private void processLog(String line, Pattern pattern, List<LogStat> logStatList)
            throws ParseException {

        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches() ||
                9 != matcher.groupCount()) {
            logger.error("Bad log entry " + line);
        } else {
            LogStat logStat = new LogStat();
            logStat.setIp(matcher.group(1));

            int responseCode = Integer.parseInt(matcher.group(6));
            if(responseCode < 400){
                String request = matcher.group(5);
                String[] requestArray = request.split(" ");
                logStat.setUrl(requestArray[1]);
                logStat.setEventTime(LogParserUtils.convertToEpochTime(matcher.group(4)));
                logStatList.add(logStat);
            } else {
                logger.warn("URL not added as the request was not successful " +
                        "with the code: " + responseCode);
            }
        }
    }

    public void setFileName(String fileName) {

        this.fileName = fileName;
    }

    public String printMessage() {
        return "Usage: java -jar target/log-parser-1.0.0.jar --log=access.log" +
                ". Can't continue the operations";
    }

}
