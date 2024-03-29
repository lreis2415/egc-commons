package org.egc.commons.files;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.egc.commons.exception.BusinessException;
import org.egc.commons.gis.CoordinateTransformUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lp on 2017/5/3.
 */
@Slf4j
public class CsvReader {

    public static final String X = "X";
    public static final String Y = "Y";
    public static final String ID = "ID";
    public static final String DEPTH_UP = "DepthUp";
    public static final String DEPTH_DOWN = "DepthDown";
    public static final String SRID = "srid";

    public List<String> csvHeaderRead(String csvPath) {
        List<String> fields = new ArrayList<>();
        try {
            Reader in = new BufferedReader(new FileReader(csvPath));
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            CSVRecord record = records.iterator().next();
            for (int i = 0; i < record.size(); i++) {
                fields.add(record.get(i));
            }
            in.close();
        } catch (IOException e) {
            throw new BusinessException(e, "CSV file not found or invalid");
        }
        return fields;
    }

    //TODO:添加只读的模糊匹配
    public String getGeneralName() {
        return "";
    }

    /**
     * 读取样点，第一行表头也包括在内
     */
    public List<String> readSamples(String csvPath) {
        BufferedReader bufferedReader;
        List<String> samples = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(csvPath));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                samples.add(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            throw new BusinessException(e, "CSV file not found or invalid");
        }
        return samples;
    }

    /**
     * 如果csv表不是UTF-8编码，则会抛出IllegalArgumentException，调用此方法时请务必考虑这一点。
     * @param csvPath csv文件的路径
     * @return 样点
     */
    public List<CsvSampleInfo> readCsvSamples(String csvPath){
        List<String> headers = csvHeaderRead(csvPath);
        String[] knownHeaders = {ID, X, Y, DEPTH_UP, DEPTH_DOWN, SRID};
        List<CsvSampleInfo> csvSampleInfos = new ArrayList<>();
        try {
            Reader in = new InputStreamReader(new FileInputStream(csvPath), "UTF-8");
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            for (CSVRecord record : records) {
                boolean flag = true;
                for (String head : headers) {
                    if (Arrays.binarySearch(knownHeaders, head) < 0) {
                        flag = false;
                        CsvSampleInfo csvSampleInfo = getRecordItem(headers, record);
                        csvSampleInfo.setSemanticName(head);
                        String semanticValue = record.get(head);
                        if (semanticValue != null) {
                            csvSampleInfo.setSemanticValue(semanticValue);
                        }
                        csvSampleInfos.add(csvSampleInfo);
                    }
                }
                if (flag) {
                    CsvSampleInfo csvSampleInfo = getRecordItem(headers, record);
                    csvSampleInfos.add(csvSampleInfo);
                }
            }
            in.close();
        } catch (IOException e) {
            throw new BusinessException(e, "CSV file not found or invalid");
        }
        return csvSampleInfos;

    }
    public double getMax(List<Double> arrayData) {
        double max = -9999999999999999.0;
        for (int i = 0; i < arrayData.size(); i++) {
            if (arrayData.get(i) > max) {
                max = arrayData.get(i);
            }
        }
        return max;
    }

    public double getMin(List<Double> arrayData) {
        double min = 99999999999999999.0;
        for (int i = 0; i < arrayData.size(); i++) {
            if (arrayData.get(i) < min) {
                min = arrayData.get(i);
            }
        }
        return min;
    }

    public CsvSampleInfo getRecordItem(List<String> headers, CSVRecord record) {
        CsvSampleInfo csvSampleInfo = new CsvSampleInfo();
        if (headers.contains(ID)) {
            String id = record.get(ID);
            csvSampleInfo.setId(id);
        }
        if (headers.contains(X)) {
            String x = record.get(X);
            if (x != null) {
                csvSampleInfo.setX(getConvertedCoord(x));
            }
        }
        if (headers.contains(Y)) {
            String y = record.get(Y);
            if (y != null) {
                csvSampleInfo.setY(getConvertedCoord(y));
            }
        }
        if (headers.contains(DEPTH_UP)) {
            String depthUp = record.get(DEPTH_UP);
            if (depthUp != null) {
                csvSampleInfo.setDepthUp(Double.parseDouble(depthUp));
            }
        }
        if (headers.contains(DEPTH_DOWN)) {
            String depthDown = record.get(DEPTH_DOWN);
            if (depthDown != null) {
                csvSampleInfo.setDepthDown(Double.parseDouble(depthDown));
            }
        }
        if (headers.contains(SRID)) {
            String srid = record.get(SRID);
            if (srid != null) {
                csvSampleInfo.setSrid(Integer.parseInt(srid));
            }
        }
        return csvSampleInfo;
    }
    public double getConvertedCoord(String x) {
        double result=0.0;
        if (x.contains("°")) {
            String[] dms = x.split("[°'′\"″]");
            if (dms.length >= 3) {
                int dir = 1;
                if (dms.length==4 && (dms[3].equals("W") || dms[3].equals("S"))) {
                    dir = -1;
                }
                result= dir * (Double.parseDouble(dms[0]) + Double.parseDouble(dms[1]) / 60 + Double.parseDouble(dms[2]) / (60 * 60));
            }
        }else{
            result= Double.parseDouble(x);
        }
        return result;
    }

}
