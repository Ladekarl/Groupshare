package group03.itsmap.groupshare.activities;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.UUID;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.fragments.DatePickerFragment;
import group03.itsmap.groupshare.fragments.TimePickerFragment;
import group03.itsmap.groupshare.models.CalendarEvent;
import group03.itsmap.groupshare.utils.IntentKey;

import static group03.itsmap.groupshare.R.drawable.abc_ic_clear_mtrl_alpha;

public class AddEventActivity extends AppCompatActivity {

    private Toolbar addEventToolbar;
    private EditText eventName;
    private TextView eventStartDate;
    private TextView eventStartTime;
    private TextView eventEndDate;
    private TextView eventEndTime;
    private EditText eventLocation;
    private ImageView eventColor;
    private TextView eventColorText;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
    @ColorInt private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        eventName = (EditText) findViewById(R.id.event_name);
        eventStartDate = (TextView) findViewById(R.id.event_start_date);
        eventStartTime = (TextView) findViewById(R.id.event_start_time);
        eventEndDate = (TextView) findViewById(R.id.event_end_date);
        eventEndTime = (TextView) findViewById(R.id.event_end_time);
        eventLocation = (EditText) findViewById(R.id.event_location_text);
        eventColor = (ImageView) findViewById(R.id.event_color_image);
        eventColorText = (TextView) findViewById(R.id.event_color_text);
        color = ContextCompat.getColor(this, R.color.colorPrimary);

        if (eventStartDate != null) {
            eventStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker(v);
                }
            });
        }

        if (eventEndDate != null) {
            eventEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker(v);
                }
            });
        }

        if (eventStartTime != null) {
            eventStartTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePicker(v);
                }
            });
        }

        if (eventEndTime != null) {
            eventEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePicker(v);
                }
            });
        }

        if (eventColorText != null) {
            eventColorText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showColorPicker();
                }
            });
        }

        addEventToolbar = (Toolbar) findViewById(R.id.add_event_toolbar);
        setSupportActionBar(addEventToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setHomeAsUpIndicator(abc_ic_clear_mtrl_alpha);
        }

        if (addEventToolbar != null) {
            addEventToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private void showColorPicker() {
        new SpectrumDialog.Builder(new ContextThemeWrapper(getApplicationContext(), R.style.GroupshareTheme_AlertDialog))
                .setColors(R.array.color_palette)
                .setDismissOnColorSelected(true)
                //Work around for not showing selected color - Picking a color not available
                .setSelectedColor(getResources().getColor(R.color.md_yellow_900))
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int newColor) {
                        if (positiveResult) {
                            LayerDrawable layer = (LayerDrawable) eventColor.getBackground();
                            GradientDrawable shape = (GradientDrawable) layer.findDrawableByLayerId(R.id.color_viewer_shape);
                            shape.setColor(newColor);
                            color = newColor;
                            eventColorText.setText(R.string.event_color_selected);
                        }
                    }
                }).build().show(getSupportFragmentManager(), "ColorPicker");
    }

    private void showDatePicker(View v) {
        DatePickerFragment datePicker = new DatePickerFragment();

        if (v == eventStartDate) {
            datePicker.setCallback(onStartDateSetListener);
        } else if (v == eventEndDate) {
            datePicker.setCallback(onEndDateSetListener);
        } else {
            return;
        }

        datePicker.show(getSupportFragmentManager(), "DatePicker");
    }

    OnDateSetListener onStartDateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month+1;
            startYear = year;
            startMonth = month;
            startDay = day;
            eventStartDate.setText(dateValuesToString(year, month, day));
        }
    };

    OnDateSetListener onEndDateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month+1;
            endYear = year;
            endMonth = month;
            endDay = day;
            eventEndDate.setText(dateValuesToString(year, month, day));
        }
    };

    private String dateValuesToString(int year, int month, int day) {
        return String.valueOf(day + "/" + month + " - " + year);
    }

    private void showTimePicker(View v) {
        TimePickerFragment timePicker = new TimePickerFragment();

        if (v == eventStartTime) {
            timePicker.setCallback(onStartTimeSetListener);
        } else if (v == eventEndTime) {
            timePicker.setCallback(onEndTimeSetListener);
        } else {
            return;
        }

        timePicker.show(getSupportFragmentManager(), "TimePicker");
    }

    OnTimeSetListener onStartTimeSetListener = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            startHour = hour;
            startMinute = minute;
            eventStartTime.setText(timeValuesToString(hour, minute));
        }
    };

    OnTimeSetListener onEndTimeSetListener = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            endHour = hour;
            endMinute = minute;
            eventEndTime.setText(timeValuesToString(hour, minute));
        }
    };

    private String timeValuesToString(int hour, int minute) {
        String displayHour = hour < 10 ? "0" + hour : String.valueOf(hour);
        String displayMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
        return displayHour + ":" + displayMinute;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_event_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_item:
                saveEventAndCloseActivity();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveEventAndCloseActivity() {
        String name = eventName.getText().toString();
        String location = eventLocation.getText().toString();

        if (!name.equals("") && startYear != 0 && startHour != 0 && endYear != 0 && endHour != 0) {
            CalendarEvent event = new CalendarEvent(System.currentTimeMillis(), name, location, startYear, startMonth,
                    startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute, color);
            Intent intent = new Intent();
            intent.putExtra(IntentKey.AddEventIntent, event);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(AddEventActivity.this, R.string.add_event_fill_fields, Toast.LENGTH_SHORT).show();
        }
    }
}
