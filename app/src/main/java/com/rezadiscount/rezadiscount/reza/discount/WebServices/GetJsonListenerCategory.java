package com.rezadiscount.rezadiscount.reza.discount.WebServices;

/**
 * Created by sebastienferrand on 12/2/15.
 * All classes implementing it will get the JSON result from the server
 */
public interface GetJsonListenerCategory {
    /**
     * HTTPCategoryReturn is ready for usage
     * Do a HTTPCategoryReturn return =  GetJsonResultCategory.getReturnCategories(); to get it
     */
    void getReturnCategory();
}
