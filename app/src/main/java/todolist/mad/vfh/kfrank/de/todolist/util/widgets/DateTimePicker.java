package todolist.mad.vfh.kfrank.de.todolist.util.widgets;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Kevin Frank on 29.06.2017.
 */

public class DateTimePicker extends android.support.v7.widget.AppCompatEditText {

    private SimpleDateFormat dateTimeFormat;

    private Date dateTime;

    public DateTimePicker(Context context) {
        super(context);
        initDateTimePicker();
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDateTimePicker();
    }

    public DateTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDateTimePicker();
    }

    private void initDateTimePicker() {
        setFocusable(false);
        setEnabled(true);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date dueDate = dateTimeFormat.parse(getText().toString());
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(dueDate);
                    showDatePickerDialog(calendar);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDatePickerDialog(final Calendar calendar) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTime(calendar);
                showTimePickerDialog(calendar);
            }
        };
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getContext(), listener, year, month, dayOfMonth);
        dialog.show();
    }

    private void showTimePickerDialog(final Calendar calendar) {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateDateTime(calendar);
            }
        };
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(getContext(), listener, hourOfDay, minute, true);
        dialog.show();
    }

    private void updateDateTime(Calendar calendar) {
        dateTime = calendar.getTime();
        updateDateTimeText();
    }

    private void updateDateTimeText() {
        setText(dateTime == null ? null : dateTimeFormat.format(dateTime));
    }

    public void setDateTimeFormat(SimpleDateFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
        updateDateTimeText();
    }
}
