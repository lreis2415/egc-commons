package org.egc.commons.test;

import org.egc.commons.files.FileSizeCalculate;
import org.egc.commons.util.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class fileSizeCalTest {

    @Test
    public void Test() {
        FileSizeCalculate fileSizeCalculate = new FileSizeCalculate();
        String a = fileSizeCalculate.gb2Other(new BigDecimal(0.02));
        System.out.println(a);
        System.out.println(FileSizeCalculate.byte2Mb(new BigDecimal(556666666)));
    }

    @Test
    public void md5() throws IOException {

        String md5 = FileUtil.fileMD5(new File("H:/GIS data/全国地图/全国土壤图/tiff/clay1.tif"));
        System.out.println(md5);//F5513385D0DC27BEEE98A8DE1B866237
    }
}
