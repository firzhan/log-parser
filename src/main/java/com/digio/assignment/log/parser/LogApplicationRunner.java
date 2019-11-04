package com.digio.assignment.log.parser;

import com.digio.assignment.log.parser.model.LogStat;
import com.digio.assignment.log.parser.repo.LogRepository;
import com.digio.assignment.log.parser.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
public class LogApplicationRunner implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(LogApplicationRunner.class);

    @Autowired
    LogRepository logRepository;

    @Autowired
    LogService logService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (args.containsOption("log")) {
            logService.setFileName(args.getOptionValues("log").get(0));
            List<LogStat> logStatList = logService.processFile();
            logStatList.forEach(logStat -> logRepository.save(logStat));
            logRepository.findAll().forEach(logStat -> {
                logger.debug("ip:" + logStat.getIp() + " url:" + logStat.getUrl());
            });
        } else {
            logger.error(logService.printMessage());
        }
    }
}