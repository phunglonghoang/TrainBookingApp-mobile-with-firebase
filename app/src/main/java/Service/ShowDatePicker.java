package Service;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ShowDatePicker {
    private Context context;
    private Calendar selectedDate;
    private EditText etSelectedDate;

    public ShowDatePicker(Context context, Calendar selectedDate, EditText etSelectedDate) {
        this.context = context;
        this.selectedDate = selectedDate;
        this.etSelectedDate = etSelectedDate;
    }

    public void showDatePickerDialog() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {

                        selectedDate.set(year, month, dayOfMonth);


                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        String selectedDateString = sdf.format(selectedDate.getTime());
                        etSelectedDate.setText(selectedDateString);
                    }
                }, year, month, day);


        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    public void showDatePickerDialogForFuture(int dayOfFuture) {
        // Lấy ngày hiện tại
        Calendar currentDate = Calendar.getInstance();

        // Tính toán ngày tối thiểu cho DatePickerDialog (ngày hiện tại + 2 ngày)
        currentDate.add(Calendar.DAY_OF_MONTH, dayOfFuture);

        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        String selectedDateString = sdf.format(selectedDate.getTime());
                        etSelectedDate.setText(selectedDateString);
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis()); // Đặt ngày tối thiểu cho DatePickerDialog
        datePickerDialog.show();
    }

}
