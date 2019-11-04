package com.digio.assignment.log.parser.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogParserUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:HH:mm" +
            ":ss Z");

    public static long convertToEpochTime(String dateTime) throws ParseException {
        Date dt = sdf.parse(dateTime);
        return dt.getTime();
    }

    public static long getCurrentEpochTime() throws ParseException {
        return convertToEpochTime(sdf.format(new Date()));
    }
}
