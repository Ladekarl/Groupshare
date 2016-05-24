package group03.itsmap.groupshare.activities;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.fragments.DatePickerFragment;
import group03.itsmap.groupshare.fragments.TimePickerFragment;

import static group03.itsmap.groupshare.R.drawable.abc_ic_clear_mtrl_alpha;

public class AddEventActivity extends AppCompatActivity {

    private Toolbar addEventToolbar;
    private EditText eventName;
    private TextView eventStartDate;
    private TextView eventStartTime;
    private TextView eventEndDate;
    private TextView eventEndTime;
    private EditText eventLocation;
    private TextView eventColor;

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
        eventColor = (TextView) findViewById(R.id.event_color_text);

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

        if (eventStartTime != null)  {
            eventStartTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePicker(v);
                }
            });
        }

        if (eventEndTime != null)  {
            eventEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePicker(v);
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
            eventStartDate.setText(dateValuesToString(year, month, day));
        }
    };

    OnDateSetListener onEndDateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
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
            eventStartTime.setText(timeValuesToString(hour, minute));
        }
    };

    OnTimeSetListener onEndTimeSetListener = new OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            eventEndTime.setText(timeValuesToString(hour, minute));
        }
    };

    private String timeValuesToString(int hour, int minute) {
        return String.valueOf(hour + ":" + minute);
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
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
