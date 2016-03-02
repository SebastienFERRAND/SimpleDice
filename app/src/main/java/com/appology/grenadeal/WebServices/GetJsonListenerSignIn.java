package com.appology.grenadeal.WebServices;

/**
 * Created by sebastienferrand on 12/2/15.
 * All classes implementing it will get the JSON result from the server
 */
public interface GetJsonListenerSignIn {
    /**
     * HTTPSignInReturn is ready for usage
     * Do a HTTPSignInReturn return =  GetJsonResultSignIn.getReturnSignIn(); to get it
     */
    void getReturnSignIn();
}
