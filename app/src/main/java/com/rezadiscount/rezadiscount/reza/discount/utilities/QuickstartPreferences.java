/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rezadiscount.rezadiscount.reza.discount.utilities;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuickstartPreferences {


    ///////// TOKEN ////////
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    /////// headers/body attributes name //////
    public static final String TAG_RESULT = "results";
    public static final String TAG_ID = "id";
    public static final String TAG_FBUID = "fbuid";
    public static final String TAG_NAME = "name";
    public static final String TAG_LABEL = "label";
    public static final String TAG_LATITUDE = "latitude";
    public static final String TAG_LONGITUDE = "longitude";
    public static final String TAG_DISTANCE = "distance";
    public static final String TAG_PICTURE = "picture";
    public static final String TAG_ADRESS = "address";
    public static final String TAG_STATUS = "status";
    public static final String TAG_DETAIL = "detail";
    public static final String TAG_CATEGORIES = "categories";
    public static final String TAG_TOKEN = "token";
    public static final String TAG_TOKENG = "tokenG";
    public static final String TAG_TOKENFB = "tokenFb";
    public static final String TAG_DEVICEMODEL = "devicemodel";
    public static final String TAG_LOGIN = "login";
    public static final String TAG_PASSWD = "password";
    public static final String TAG_LASTNAME = "lastname";
    public static final String TAG_FIRSTNAME = "firstname";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_BIRTHDAY = "birthday";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_HTTPCODE = "httpCode";
    public static final String TAG_MESSAGE = "message";
    public static final String URL_CAT = "category";
    public static final String URL_MERC = "merchant";
    public static final String URL_AUTH = "auth";
    public static final String URL_REG = "register";
    public static final String TAG_ISFB = "from_facebook";
    public static final String TAG_SOURCE = "source";


    //////// METHODS ///////
    public static final String TAG_GET = "GET";
    public static final String TAG_POST = "POST";
    ////// TIME CONSTANTS //////
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String URL_SERV_DEV = "http://api.booking.touratier.fr/app_dev.php/";
    public static final String URL_SERV_PREPROD = "http://api.preprod.appology.fr/";
    /////// URL ///////
    public static String URL_SERV = "";
    ////// CONST ///////
    public static String normalConnexion = "Normal Connexion";
    public static String facebookConnexion = "Facebook Connexion";
    public static String facebookSubscription = "Facebook Subscription";

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }


    public static String convertToDateFormat(String dateP, String inputPattern, String outputPattern) {

        if (dateP!=null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date;
            String str = null;

            try {
                date = inputFormat.parse(dateP);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        } else {
            return null;
        }

    }




}
