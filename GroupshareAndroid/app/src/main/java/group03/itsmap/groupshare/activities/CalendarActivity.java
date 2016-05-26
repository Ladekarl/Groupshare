package group03.itsmap.groupshare.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.models.CalendarEvent;
import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.models.GroupShareCalendar;
import group03.itsmap.groupshare.models.ToDoList;
import group03.itsmap.groupshare.services.CalendarService;
import group03.itsmap.groupshare.services.GroupService;
import group03.itsmap.groupshare.services.ToDoService;
import group03.itsmap.groupshare.utils.CalendarMapper;
import group03.itsmap.groupshare.utils.FacebookUtil;
import group03.itsmap.groupshare.utils.IntentKey;

public class CalendarActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    private static final int ADD_EVENT_REQUEST = 1000;
    public static final String GROUP_INTENT_KEY = "group03.itsmap.groupshare.activities.CalendarActivity.GroupIntentKey";
    public static final String CALENDAR_ID_INTENT_KEY = "group03.itsmap.groupshare.activities.CalendarActivity.CalendarIdIntentKey";
    private WeekView weekView;
    private Toolbar calendarToolbar;
    private GroupShareCalendar groupShareCalendar;
    private Group group;
    private long calendarId;
    private String userId;
    private List<WeekViewEvent> weekViewEvents;
    private CalendarReceiver calendarReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = FacebookUtil.getFacebookUserId(getApplicationContext());
        group = getIntent().getParcelableExtra(GROUP_INTENT_KEY);
        calendarId = getIntent().getLongExtra(CALENDAR_ID_INTENT_KEY, 0);
        setContentView(R.layout.activity_calendar);
        weekView = (WeekView) findViewById(R.id.calendar_week_view);
        weekViewEvents = new ArrayList<>();

        if (weekView != null) {
            weekView.setOnEventClickListener(this);

            weekView.setMonthChangeListener(this);

            weekView.setEventLongPressListener(this);

            weekView.setEmptyViewLongPressListener(this);

            weekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

            setupDateTimeInterpreter(false);
        }

        calendarToolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        setSupportActionBar(calendarToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        if (calendarToolbar != null) {
            calendarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (group != null) {
            IntentFilter getCalendarIntentFilter = new IntentFilter(
                    CalendarService.GET_CALENDAR_BROADCAST_INTENT + group.getId() + calendarId + userId);

            IntentFilter deleteCalendarEventIntentFilter = new IntentFilter(
                    CalendarService.DELETE_CALENDAR_EVENT_BROADCAST_INTENT);

            calendarReceiver = new CalendarReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    calendarReceiver,
                    getCalendarIntentFilter);
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    calendarReceiver,
                    deleteCalendarEventIntentFilter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getCalendarFromService();
    }

    @Override
    public void onPause() {
        saveCalendar();
        super.onPause();
    }

    private void saveCalendar() {
        CalendarService.startActionSaveCalendar(this, groupShareCalendar, group.getId(), calendarId, userId);
    }

    private void getCalendarFromService() {
        CalendarService.startActionGetCalendar(this, group.getId(), calendarId, userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calendar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_event_menu_item:
                addEvent();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(calendarReceiver);
        super.onDestroy();
    }

    private void addEvent() {
        Intent startAddEventActivityIntent = new Intent(getApplicationContext(), AddEventActivity.class);
        startActivityForResult(startAddEventActivityIntent, ADD_EVENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EVENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                CalendarEvent event = data.getExtras().getParcelable(IntentKey.AddEventIntent);
                groupShareCalendar.getCalendarEvents().add(event);
                weekViewEvents.add(CalendarMapper.calendarEventToWeekViewEvent(event));
                saveCalendar();
            }
        }

    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(final WeekViewEvent event, RectF eventRect) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        groupShareCalendar.getCalendarEvents();
                        CalendarService.startActionDeleteCalendarEvent(CalendarActivity.this, group.getId(), calendarId, userId, event.getId());
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.GroupshareTheme_AlertDialog);
        builder.setTitle(getString(R.string.delete_event_dialog)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        weekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat("d/M", Locale.getDefault());

                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + " " + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour < 10 ? "0" + hour + ":00" : hour + ":00";
            }
        });
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();
        ArrayList<WeekViewEvent> newEvents = CalendarMapper.getNewEvents(newYear, newMonth, weekViewEvents);
        events.addAll(newEvents);
        return events;
    }

    private class CalendarReceiver extends BroadcastReceiver {
        private CalendarReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            GroupShareCalendar calendar = intent.getParcelableExtra(CalendarService.EXTRA_CALENDAR);
            if (calendar == null) return;
            groupShareCalendar = calendar;
            weekViewEvents = CalendarMapper.calendarEventsToWeekViewEvents(calendar.getCalendarEvents());
            WeekView weekView = (WeekView) findViewById(R.id.calendar_week_view);
            if (weekView != null) {
                weekView.notifyDatasetChanged();
            }
        }
    }
}
