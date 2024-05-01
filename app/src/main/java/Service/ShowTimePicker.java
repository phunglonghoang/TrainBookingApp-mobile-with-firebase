package Service;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.TimePicker;

public class ShowTimePicker {
    private EditText etSelectedTime;
    private Context context;
    private TimePickerDialog timePickerDialog;
    public ShowTimePicker(Context context,
                          EditText etSelectedTime,
                          TimePickerDialog timePickerDialog){
        this.context = context;
        this.etSelectedTime = etSelectedTime;
        this.timePickerDialog = timePickerDialog;
    }


    public void showTimePickerDialog() {
        timePickerDialog = new TimePickerDialog(
                context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        etSelectedTime.setText(selectedTime);
                    }
                },
                12, 0, true);

        timePickerDialog.show();
    }
}
