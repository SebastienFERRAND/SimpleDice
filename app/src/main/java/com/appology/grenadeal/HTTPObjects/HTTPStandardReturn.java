package com.appology.grenadeal.HTTPObjects;

/**
 * Created by sebastienferrand on 2/19/16.
 */
public class HTTPStandardReturn {

    private String errorCode;
    private String HTTPcode;
    private String message;
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHTTPCode() {
        return HTTPcode;
    }

    public void setHTTPCode(String code) {
        this.HTTPcode = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


}
