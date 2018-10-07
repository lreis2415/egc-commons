package org.egc.commons.files;

/**
 * Description:
 * <pre>
 * enum of file types
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/22 16:12
 */
public enum FileTypeEnum {

    Raster("raster", "raster file"), Vector("vector", "vector file"), Sample("sample", "sample file");

    private String value;
    private String description;

    FileTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getValue() {
        return this.value;
    }
}
