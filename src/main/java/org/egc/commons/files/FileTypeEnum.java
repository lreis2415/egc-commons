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

    Raster("raster file"), Vector("vector file"), Sample("sample file");

    private String description;

    FileTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
