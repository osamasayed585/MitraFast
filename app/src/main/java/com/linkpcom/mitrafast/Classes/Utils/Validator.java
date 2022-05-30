package com.linkpcom.mitrafast.Classes.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linkpcom.mitrafast.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;


public class Validator {

    private final Context context;
    private final String emailPattern = Patterns.EMAIL_ADDRESS.pattern();
    private final String phonePattern = Patterns.PHONE.pattern();
    private final int passwordLength = 5;
    private final int creditNumber = 16;

    @Inject
    public Validator(@ActivityContext Context context) {
        this.context = context;
    }

    public boolean isNotEmpty(EditText view) {
        String value = view.getText().toString().trim();
        if (value == null || value.equals("")) {
            view.setError(getResources().getString(R.string.errorRequired));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isGreaterThan(EditText view) {
        String value = view.getText().toString().trim();
        if (value.length() != creditNumber) {
            view.setError(getResources().getString(R.string.errorNumber));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isDateValid(String m, String y) {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        int my_month = Integer.parseInt(m); // 01
        int my_year = Integer.parseInt(20 + y); // 2025

        if (my_year > year) return true;
        else if (my_year == year) {
            return my_month >= month;
        }
        return false;
    }

    public boolean isMonthLengthValid(EditText view) {
        if (view.length() != 2) {
            view.setError(getResources().getString(R.string.vaildMonth));
            return false;
        }
        return true;
    }

    public boolean isYearLengthValid(EditText view) {
        if (view.length() != 2) {
            view.setError(getResources().getString(R.string.vaildYear));
            return false;
        }
        return true;
    }

    public boolean isNotEmpty(String value, String message) {
        if (value == null || value.equals("")) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isIban(String value, TextView view, int count) {
        if (value == null || value.length() < count) {
            Toast.makeText(context, context.getString(R.string.iban_number_should_be_22_digit), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checked(boolean checked, TextView view) {
        if (!checked) {
            view.setError(getResources().getString(R.string.errorRequired));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean password(EditText view) {
        String value = view.getText().toString().trim();
        if (value == null || value.length() < passwordLength) {
            view.setError(getResources().getString(R.string.errorRequired));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validationCode(EditText view) {
        String value = view.getText().toString().trim();
        int codeLength = 4;
        if (value == null || value.length() < codeLength) {
            view.setError(getResources().getString(R.string.error_validation_code_message));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean passwordsMatches(String value, String value2, TextView view2) {
        if (value == null || !value.equals(value2)) {
            view2.setError(getResources().getString(R.string.errorPasswordMatchingMessage));
            view2.requestFocus();
            return false;
        }
        return true;
    }

    public boolean passwordsMatches(String value, String value2) {
        if (value == null || !value.equals(value2)) {
            Toast.makeText(context, getResources().getString(R.string.errorPasswordMatchingMessage), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean phone(EditText view) {
        String value = view.getText().toString().trim();
        int phoneLength = 10;
        if (value == null || value.length() < phoneLength) {
            view.setError(getResources().getString(R.string.errorWrongPhoneNumberMessage));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean number(int value, TextView view) {
        if (value == 0) {
            view.setError(getResources().getString(R.string.errorRequired));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean number(int value) {
        return value != 0;
    }

    public boolean password(String value) {
        return value != null && value.length() >= passwordLength;
    }

    public boolean email(EditText view) {
        String value = view.getText().toString().trim();
        if (!value.matches(emailPattern)) {
            view.setError(getResources().getString(R.string.errorInvalidEmail));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean emailNullable(EditText view) {
        String value = view.getText().toString().trim();
        if (value == null || value.equals(""))
            return true;
        if (!value.matches(emailPattern)) {
            view.setError(getResources().getString(R.string.errorInvalidEmail));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean phone(String value, TextView view, int count) {
        if (value == null || value.length() != count) {
            view.setError(getResources().getString(R.string.errorWrongPhoneNumberMessage) + " " + count);
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean startWith(String value, TextView view, String startWith) {
        if (value == null || startWith == null || !value.startsWith(startWith)) {
            view.setError(getResources().getString(R.string.errorWrongStartPhoneNumberMessage) + " " + startWith);
            view.requestFocus();
            return false;
        }
        return true;
    }

    //    public boolean startWith(String value, TextView view, List<StartWith> startWith) {
//        if (value.equals(""))
//            return true;
//        if (startWith == null)
//            return true;
//        StringBuilder message = new StringBuilder();
//        for (StartWith startWithBean :
//                startWith) {
//            if (value.startsWith(startWithBean.getStart_with())) {
//                return true;
//            }
//            message.append(startWithBean.getStart_with()).append(" ");
//        }
//        view.setError(getResources().getString(R.string.errorWrongStartPhoneNumberMessage) + " " + message);
//        view.requestFocus();
//        return false;
//    }
    public boolean isNotNull(Object value, TextView view) {
        if (value == null) {
            view.setError(getResources().getString(R.string.errorRequired));
            view.requestFocus();
            return false;
        }
        view.setError(null);
        return true;
    }

    public boolean isNotNull(Object value, String Message) {
        if (value == null) {
            Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isListEmpty(List list, TextView view) {
        if (list.isEmpty()) {
            view.setError(getResources().getString(R.string.errorRequired));
            view.requestFocus();
            return false;
        }
        view.setError(null);
        return true;
    }

    public boolean isListEmpty(List list, String Message) {
        if (list.isEmpty()) {
            Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean spinner(Object value, TextView view) {
        if (value == null) {
            view.setError(getResources().getString(R.string.errorRequired));
            view.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isNotEmpty(String value) {
        return value != null && !value.equals("");
    }

    public boolean email(String value) {
        return value.matches(emailPattern);
    }

    private Resources getResources() {
        return context.getResources();
    }

    public boolean isChecked(CheckBox cbTerms, String message) {
        if (!cbTerms.isChecked()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    public boolean isSmallerThan(float totalCost, float max_value) {
        if (totalCost < max_value)
            return true;
        else {
            Toast.makeText(context, (String.format("%s%s", context.getString(R.string.total_cost_should_be_smaller_than), max_value)), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean isGraterThan(float totalCost, float min_value) {
        if (totalCost > min_value)
            return true;
        else {
            Toast.makeText(context, (String.format("%s%s", context.getString(R.string.total_cost_should_be_grater_than), min_value)), Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
