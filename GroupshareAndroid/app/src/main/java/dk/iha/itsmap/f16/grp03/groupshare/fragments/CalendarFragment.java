package dk.iha.itsmap.f16.grp03.groupshare.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dk.iha.itsmap.f16.grp03.groupshare.R;
import dk.iha.itsmap.f16.grp03.groupshare.models.GroupShareCalendar;
import dk.iha.itsmap.f16.grp03.groupshare.services.CalendarService;
import dk.iha.itsmap.f16.grp03.groupshare.activities.CalendarActivity;
import dk.iha.itsmap.f16.grp03.groupshare.activities.GroupActivity;
import dk.iha.itsmap.f16.grp03.groupshare.models.Group;
import dk.iha.itsmap.f16.grp03.groupshare.utils.CalendarMapper;
import dk.iha.itsmap.f16.grp03.groupshare.utils.FacebookUtil;

public class CalendarFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    private WeekView weekView;
    private GroupShareCalendar groupShareCalendar;
    private Group group;
    private String userId;
    private long calendarId;
    private List<WeekViewEvent> weekViewEvents;
    private CalendarReceiver calendarReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = FacebookUtil.getFacebookUserId(getActivity().getApplicationContext());
        if (getArguments() != null) {
            group = getArguments().getParcelable(GroupActivity.GROUP_KEY);
            calendarId = getArguments().getLong(GroupActivity.CALENDAR_ID_KEY);
            if (group != null) {
                IntentFilter calendarIntentFilter = new IntentFilter(
                        CalendarService.GET_CALENDAR_BROADCAST_INTENT + group.getId() + calendarId + userId);

                calendarReceiver = new CalendarReceiver();
                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                        calendarReceiver,
                        calendarIntentFilter);
            }
        }
        weekViewEvents = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        weekView = (WeekView) view.findViewById(R.id.fragment_week_view);

        weekView.setOnEventClickListener(this);

        weekView.setMonthChangeListener(this);

        weekView.setEventLongPressListener(this);

        weekView.setEmptyViewLongPressListener(this);

        weekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        setupDateTimeInterpreter(false);

        ImageButton calendarFragmentButton = (ImageButton) view.findViewById(R.id.calender_fragment_button);
        calendarFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCalendarActivityIntent = new Intent(getActivity(), CalendarActivity.class);
                startCalendarActivityIntent.putExtra(CalendarActivity.GROUP_INTENT_KEY, group);
                startCalendarActivityIntent.putExtra(CalendarActivity.CALENDAR_ID_INTENT_KEY, calendarId);
                startActivity(startCalendarActivityIntent);
            }
        });

        return view;
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
        CalendarService.startActionSaveCalendar(getActivity(), groupShareCalendar, group.getId(), calendarId, userId);
    }


    private void getCalendarFromService() {
        CalendarService.startActionGetCalendar(getActivity(), group.getId(), calendarId, userId);
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();
        ArrayList<WeekViewEvent> newEvents = CalendarMapper.getNewEvents(newYear, newMonth, weekViewEvents);
        events.addAll(newEvents);
        return events;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(calendarReceiver);
        super.onDestroy();
    }

    private class CalendarReceiver extends BroadcastReceiver {
        private CalendarReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            GroupShareCalendar calendar = intent.getParcelableExtra(CalendarService.EXTRA_CALENDAR);
            if (calendar == null) return;
            groupShareCalendar = calendar;
            weekViewEvents = CalendarMapper.calendarEventsToWeekViewEvents(calendar.getCalendarEvents());
            weekView.notifyDatasetChanged();
        }
    }
}
