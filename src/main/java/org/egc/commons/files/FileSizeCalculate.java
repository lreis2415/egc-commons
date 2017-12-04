package org.egc.commons.files;

import java.math.BigDecimal;

public class FileSizeCalculate {

  public String gb2Other(BigDecimal size){
    BigDecimal b1024 = new BigDecimal(1024);
    BigDecimal b1048576 = new BigDecimal(1048576);
    BigDecimal b1073741824 = new BigDecimal(1073741824);

    BigDecimal size1024 = size.multiply(b1024);
    BigDecimal size1048576 = size.multiply(b1048576);
    BigDecimal size1073741824 = size.multiply(b1073741824);

    if (size.doubleValue() > 0.1) {
      return Float.toString(size.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "GB";
    } else if (size1024.doubleValue() > 0.1) {
      return Float.toString(size1024.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "MB";
    } else if (size1048576.doubleValue() > 0.1) {
      return Float.toString(size1048576.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "KB";
    } else {
      return Float.toString(size1073741824.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue())
          + "B";
    }
  }

  public String byte2Other(BigDecimal size){
    BigDecimal b1024 = new BigDecimal(1024);
    BigDecimal b1048576 = new BigDecimal(1048576);
    BigDecimal b1073741824 = new BigDecimal(1073741824);

    if (size.doubleValue()>=0 && size.doubleValue()<1024){
      return Float.toString(size.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "B";
    }else if (size.doubleValue()>=1024 && size.doubleValue()<=1048576){
      BigDecimal kb = size.divide(b1024);
      return Float.toString(kb.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "KB";
    }else if (size.doubleValue()>=1048576 && size.doubleValue()<1073741824){
      BigDecimal mb = size.divide(b1048576);
      return Float.toString(mb.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "MB";
    }else{
      BigDecimal gb = size.divide(b1073741824);
      return Float.toString(gb.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()) + "GB";
    }
  }
}
