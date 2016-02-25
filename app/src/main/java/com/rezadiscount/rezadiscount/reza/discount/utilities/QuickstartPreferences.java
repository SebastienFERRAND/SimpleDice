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

import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

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
    public static final String TAG_ISFB = "from_facebook";
    public static final String TAG_SOURCE = "source";
    public static final String TAG_PASSWORD_RECOVER = "password-recover";
    public static final String TAG_REGISTER = "register";
    public static final String TAG_ERROR_CODE = "code";
    public static final String TAG_DEVICE_ID = "deviceid";
    public static final String TAG_CONTENT_TYPE = "Content-Type";
    public static final String TAG_ACCEPT = "Accept";



    //////// METHODS ///////
    public static final String TAG_GET = "GET";
    public static final String TAG_POST = "POST";

    ////// TIME CONSTANTS //////
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String URL_SERV_DEV = "http://api.booking.touratier.fr/app_dev.php/";
    public static final String URL_SERV_PREPROD = "http://api.preprod.appology.fr/";
    public static final String URL_CAT = "category";
    public static final String URL_MERC = "merchant";
    public static final String URL_AUTH = "auth";
    public static final String URL_REG = "register";
    public static final String URL_FORPSSWD = "password-recover";
    ///// HTTP CODES /////
    public static final String TAG_HTTP_SUCCESS = "200";
    public static final String TAG_HTTP_FAIL = "40";


    ///// ERROR CODES /////
    public static final String TAG_NO_ERROR = "0";

    //Auth
    public static final String TAG_ERROR_CODE_AUTH_1 = "AUTH_1";
    public static final String TAG_ERROR_CODE_AUTH_2 = "AUTH_2";
    public static final String TAG_ERROR_CODE_AUTH_3 = "AUTH_3";
    public static final String TAG_ERROR_CODE_AUTH_4 = "AUTH_4";
    //Register
    public static final String TAG_ERROR_CODE_REG_1 = "REG_1";
    public static final String TAG_ERROR_CODE_REG_2 = "REG_2";
    public static final String TAG_ERROR_CODE_REG_3 = "REG_3";
    public static final String TAG_ERROR_CODE_REG_4 = "REG_4";

    /////// URL ///////
    public static String URL_SERV = "";
    ////// CONST ///////
    public static String normalConnexion = "Normal Connexion";
    public static String facebookConnexion = "Facebook Connexion";

    /**
     * @param dateP         the String date
     * @param inputPattern  the pattern of the input String date
     * @param outputPattern the pattern of the output desired date
     * @return outputPattern date. if there's no date, return ""
     */
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
            return "";
        }

    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
