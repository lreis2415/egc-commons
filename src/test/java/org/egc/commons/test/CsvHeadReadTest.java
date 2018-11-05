package org.egc.commons.test;

import org.egc.commons.files.CsvReader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lp on 2017/5/3.
 */
public class CsvHeadReadTest {


    @Test
    public void test(){
        String csvPath = "D:\\SampleBase\\xc.csv";
        CsvReader csvReader = new CsvReader();
        List<String> filds = csvReader.csvHeaderRead(csvPath);
        List<String> samples = csvReader.readSamples(csvPath);
        String[] tmp = samples.get(2).split(",");
        BigDecimal a = new BigDecimal(tmp[1]);
        int x_index = filds.indexOf("LON");
        System.out.println(a);

    }
    @Test
    public void csvT(){
        String path = "D:\\DataBase\\anhui.csv";
        CsvReader csvReader = new CsvReader();
        List<String> a = csvReader.csvHeaderRead(path);
        System.out.println(a);

    }
}
