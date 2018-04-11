package is.hi.hopur16.nyttapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * Created by atliharaldsson on 19/02/2018.
 */

public class DatePickerFragment extends android.support.v4.app.DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year,month,day);
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        TextView dateTxt = (TextView) getActivity().findViewById(R.id.dateTxt);
        dateTxt.setText(currentDate);
    }
}
