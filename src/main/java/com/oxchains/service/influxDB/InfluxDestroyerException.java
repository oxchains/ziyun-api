package com.oxchains.service.influxDB;

/**
 * Created by song on 2017/6/26.
 */
public class InfluxDestroyerException extends Exception {
    public InfluxDestroyerException() {
    }

    public InfluxDestroyerException(String message) {
        super(message);
    }

    public InfluxDestroyerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfluxDestroyerException(Throwable cause) {
        super(cause);
    }

    public InfluxDestroyerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}