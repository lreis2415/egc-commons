package org.egc.commons.xml;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lp on 2017/5/3.
 */
public class CsvReader {

    public List<String> csvHeaderRead(String csvPath) {
        BufferedReader buffer;
        List<String> filds = new ArrayList<>();
        try {
//            buffer = new BufferedReader(new FileReader(csvPath));
//            String line = buffer.readLine();
//            String[] colums = line.split(",");
//            for(String colum : colums){
//                filds.add(colum);
//            }
//            buffer.close();
            Reader in = new FileReader(csvPath);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            CSVRecord record = records.iterator().next();
            for (int i = 0; i < record.size(); i++) {
                filds.add(record.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filds;
    }

    //TODO:添加只读的模糊匹配
    public String getGeneralName() {
        return "";
    }

    //读取样点，第一行表头也包括在内
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
            e.printStackTrace();
        }
        return samples;
    }

    public List<CsvSampleInfo> readCsvSamples(String csvPath) {
        List<String> headers = csvHeaderRead(csvPath);
        String[] knownHeaders = {"ID", "X", "Y", "DepthUp", "DepthDown", "srid"};
        List<CsvSampleInfo> csvSampleInfos = new ArrayList<>();
        try {
            Reader in = new FileReader(csvPath);
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
                            csvSampleInfo.setSemanticValue(Double.parseDouble(semanticValue));
                        }
                        csvSampleInfos.add(csvSampleInfo);

                    }
                }
                if (flag) {
                    CsvSampleInfo csvSampleInfo = getRecordItem(headers, record);
                    csvSampleInfos.add(csvSampleInfo);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
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
        if (headers.contains("ID")) {
            String id = record.get("ID");
            csvSampleInfo.setId(id);
        }
        if (headers.contains("X")) {
            String x = record.get("X");
            if (x != null) {
                csvSampleInfo.setX(Double.parseDouble(record.get("X")));
            }
        }
        if (headers.contains("Y")) {
            String y = record.get("Y");
            if (y != null) {
                csvSampleInfo.setY(Double.parseDouble(y));
            }
        }
        if (headers.contains("DepthUp")) {
            String depthUp = record.get("DepthUp");
            if (depthUp != null) {
                csvSampleInfo.setDepthUp(Double.parseDouble(depthUp));
            }
        }
        if (headers.contains("DepthDown")) {
            String depthDown = record.get("DepthDown");
            if (depthDown != null) {
                csvSampleInfo.setDepthDown(Double.parseDouble(depthDown));
            }
        }
        if (headers.contains("srid")) {
            String srid = record.get("srid");
            if (srid != null) {
                csvSampleInfo.setSrid(Integer.parseInt(srid));
            }
        }
        return csvSampleInfo;
    }


}
