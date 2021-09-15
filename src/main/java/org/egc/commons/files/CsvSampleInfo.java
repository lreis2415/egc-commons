package org.egc.commons.files;

import lombok.Data;

@Data
public class CsvSampleInfo {
    private String id;
    private int srid;
    private double x;
    private double y;
    private double depthUp;
    private double depthDown;
    private String semanticName;
    private String semanticValue;
}
