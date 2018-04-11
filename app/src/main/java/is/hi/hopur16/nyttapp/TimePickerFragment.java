package is.hi.hopur16.nyttapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

public class TimePickerFragment extends android.support.v4.app.DialogFragment implements TimePickerDialog.OnTimeSetListener{
    String hourString = "";
    String minuteString = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Get a Calendar instance
        final Calendar calendar = Calendar.getInstance();
        // Get the current hour and minute
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog with current time
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),this,hour,minute,true);
        // Return the TimePickerDialog
        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        if (minute < 10)
            minuteString = "0" + String.valueOf(minute);
        if (hourOfDay < 10)
            hourString = "0" + String.valueOf(hourOfDay);
        TextView timeTxt = (TextView) getActivity().findViewById(R.id.timeTxt);
        timeTxt.setText(hourString + ":" + minuteString);
    }
}