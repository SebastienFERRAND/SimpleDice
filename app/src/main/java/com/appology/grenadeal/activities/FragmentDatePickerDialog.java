package com.appology.grenadeal.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.appology.grenadeal.utilities.GetDateSpinnerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sebastienferrand on 12/21/15.
 */
public class FragmentDatePickerDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private List<GetDateSpinnerListener> listeners = new ArrayList<GetDateSpinnerListener>();

    private Calendar c;

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
        Log.d("Date0", "date results " + yearP + monthP + dayP);

        c = Calendar.getInstance();
        c.set(yearP, monthP, dayP);

        String time = String.valueOf(c.get(Calendar.MONTH));

        for (GetDateSpinnerListener hl : listeners)
            hl.getDateSpinner();
    }


    public String getYear() {
        return String.valueOf(c.get(Calendar.YEAR));
    }

    public String getMonth() {
        return String.format("%02d", c.get(Calendar.MONTH));
    }

    public String getDate() {
        return String.valueOf(c.get(Calendar.DATE));
    }

    public void addListener(GetDateSpinnerListener toAdd) {
        listeners.add(toAdd);
    }

}