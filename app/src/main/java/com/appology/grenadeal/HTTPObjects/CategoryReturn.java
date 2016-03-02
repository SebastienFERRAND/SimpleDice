package com.appology.grenadeal.HTTPObjects;

import com.appology.grenadeal.Business.Category;

import java.util.ArrayList;

/**
 * Created by sebastienferrand on 2/23/16.
 * HTTP object return for categories
 */

public class CategoryReturn extends HTTPStandardReturn {

    private ArrayList<Category> listCategories;

    public ArrayList<Category> getListCategories() {
        return listCategories;
    }

    public void setListCategories(ArrayList<Category> listCategories) {
        this.listCategories = listCategories;
    }

}
