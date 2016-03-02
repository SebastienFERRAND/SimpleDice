package com.appology.grenadeal.HTTPObjects;

/**
 * Created by sebastienferrand on 2/21/16.
 * HTTP object return for signing in
 */
public class SignInReturn extends HTTPStandardReturn {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
