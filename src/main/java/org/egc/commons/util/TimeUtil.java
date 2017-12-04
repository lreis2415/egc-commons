package org.egc.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import java.util.Date;

/**
 * 时间工具类
 *
 * @author houzhiwei & lp
 * @date 2016/12/27 22:34
 */
public class TimeUtil
{
    /**
     * Time-Distance in minutes.
     *
     * @param start the start
     * @param end   the end
     * @return the minutes (int)
     */
    public static int distanceInMinutes(Date start, Date end)
    {
        if (start.after(end)) {
            Date temp = start;
            start = end;
            end = temp;
        }
        Interval interval = parserToInterval(start, end);
        return Minutes.minutesIn(interval).getMinutes();//获得时间段分钟
    }

    public static int distanceInDays(Date start, Date end)
    {
        if (start.after(end)) {
            Date temp = start;
            start = end;
            end = temp;
        }
        Interval interval = parserToInterval(start, end);
        return Days.daysIn(interval).getDays();//获得时间段
    }

    /**
     * 时间段 (milliseconds)
     *
     * @param start
     * @param end
     * @return
     */
    private static Interval parserToInterval(Date start, Date end)
    {
        return new Interval(start.getTime(), end.getTime());
    }

    /**
     * @author lp
     */

    public static String DateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * @author lp
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
