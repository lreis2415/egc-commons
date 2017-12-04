package org.egc.commons.test;

import java.math.BigDecimal;
import org.egc.commons.files.FileSizeCalculate;
import org.junit.Test;

public class fileSizeCalTest {

  @Test
  public void Test(){
    FileSizeCalculate fileSizeCalculate = new FileSizeCalculate();
    String a = fileSizeCalculate.gb2Other(new BigDecimal(0.02));
    System.out.println(a);
  }
}
