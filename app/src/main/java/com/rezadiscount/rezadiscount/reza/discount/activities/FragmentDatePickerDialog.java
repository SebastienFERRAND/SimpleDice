package com.rezadiscount.rezadiscount.reza.discount.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.rezadiscount.rezadiscount.reza.discount.utilities.GetDateSpinnerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sebastienferrand on 12/21/15.
 */
public class FragmentDatePickerDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {


    private int year;
    private int month;
    private int date;
    private List<GetDateSpinnerListener> listeners = new ArrayList<GetDateSpinnerListener>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int yearP, int monthP, int dayP) {
        // Do something with the date chosen by the user
        Log.d("Date0", "date results " + year + month + dayP);
        year = yearP;
        month = monthP;
        date = dayP;

        for (GetDateSpinnerListener hl : listeners)
            hl.getDateSpinner();


    }


    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }



    public void addListener(GetDateSpinnerListener toAdd) {
        listeners.add(toAdd);
    }

}