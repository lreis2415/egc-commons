package org.egc.commons.utils;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.Minutes;

import java.util.Date;

/**
 * 时间工具类
 *
 * @author houzhiwei
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

}
