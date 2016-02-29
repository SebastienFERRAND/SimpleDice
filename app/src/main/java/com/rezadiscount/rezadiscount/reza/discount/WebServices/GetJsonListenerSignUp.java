package com.rezadiscount.rezadiscount.reza.discount.WebServices;

/**
 * Created by sebastienferrand on 12/2/15.
 * All classes implementing it will get the JSON result from the server
 */
public interface GetJsonListenerSignUp {
    /**
     * HTTPSignUpReturn is ready for usage
     * Do a HTTPSignUpReturn return =  GetJsonResultSignUp.getReturnSignUp(); to get it
     */
    void getReturnSignUp();
}
