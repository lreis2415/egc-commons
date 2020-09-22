package org.egc.commons.exception;


/**
 * Created by lp on 2017/4/24.
 */
public class RasterReadException extends NullPointerException {
    public RasterReadException() {
        super();
    }

    public RasterReadException(String msg) {
        super(msg);
    }

}
