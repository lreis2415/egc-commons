package org.egc.commons.files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author houzhiwei
 * @date 2020/11/27 16:04
 */
public class GeneralCsvReader {

    private String filepath;

    public Iterable<CSVRecord> getRecords() {
        return records;
    }

    private Iterable<CSVRecord> records;

    public GeneralCsvReader(String filepath) throws IOException {
        this.filepath = filepath;
        read();
    }

    private void read() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(filepath));
        //首行为标题
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .parse(reader);

        this.records = records;
        for (CSVRecord record : records) {
            System.out.println("Record #: " + record.getRecordNumber());
            System.out.println("ID: " + record.get(0));
            System.out.println("Name: " + record.get(1));
            System.out.println("Email: " + record.get(2));
            System.out.println("Country: " + record.get(3));
        }
    }

    public int numberOfDataRecords(boolean includesHeaderLine) {
        // same as Guava does
        if (records instanceof Collection) {
            return ((Collection<?>) records).size();
        } else {
            int count = 0;
            for (CSVRecord record : records) {
                count++;
            }
            if (includesHeaderLine) {
                return count;
            } else {
                return count - 1;
            }
        }
    }

    public List<String> getHeaders() {
        CSVRecord headerRecord = this.records.iterator().next();
        long recordNumber = headerRecord.getRecordNumber();
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < recordNumber; i++) {
            headers.add(headerRecord.get(i));
        }
        return headers;
    }

}
