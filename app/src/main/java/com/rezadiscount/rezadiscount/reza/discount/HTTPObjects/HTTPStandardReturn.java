package com.rezadiscount.rezadiscount.reza.discount.HTTPObjects;

/**
 * Created by sebastienferrand on 2/19/16.
 */
public class HTTPStandardReturn {
    private String code;
    private String message;
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
