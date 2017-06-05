package org.egc.commons.test;

import org.egc.commons.util.DateUtil;
import org.egc.commons.util.TimeUtil;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO
 *
 * @author houzhiwei
 * @date 2016/12/27 22:50
 */
public class DateTimeTest
{
    @Test
    public void timeTest () {
//        Date ed = new DateTime(new Date()).plusMinutes(25).toDate();
        Date sd = DateUtil.parseDate("2016-12-27 22:34:00");
        System.out.println( TimeUtil.distanceInMinutes(sd, new Date()));
    }

    @Test
    public void timestamp(){
        DateFormat df = new SimpleDateFormat("");

        Timestamp s=new Timestamp(new Date().getTime());
        System.out.println(s);
    }
}
