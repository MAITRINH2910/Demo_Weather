package com.example.demo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used to format Date
 */
public class CommonUtil {
    /**
     * Format Date Type DATE --> Date Type STRING
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    /**
     * Format Date Type STRING --> Date Type DATE
     *
     * @param dateTypeString
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateTypeString) throws ParseException {
        Date dateTypeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTypeString);
        return dateTypeDate;
    }
}
