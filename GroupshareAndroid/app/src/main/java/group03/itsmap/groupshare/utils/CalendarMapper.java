package group03.itsmap.groupshare.utils;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import group03.itsmap.groupshare.models.CalendarEvent;

public class CalendarMapper {
    public static List<WeekViewEvent> calendarEventsToWeekViewEvents(List<CalendarEvent> calendarEvents) {
        List<WeekViewEvent> weekViewEvents = new ArrayList<>();

        for (CalendarEvent event : calendarEvents) {
            weekViewEvents.add(calendarEventToWeekViewEvent(event));
        }
        return weekViewEvents;
    }

    public static WeekViewEvent calendarEventToWeekViewEvent(CalendarEvent event) {
        WeekViewEvent weekViewEvent = new WeekViewEvent(event.getId(), event.getName(), event.getStartYear(), event.getStartMonth(),
                event.getStartDay(), event.getStartHour(), event.getStartMinute(), event.getEndYear(), event.getEndMonth(),
                event.getEndDay(), event.getEndHour(), event.getEndMinute());
        weekViewEvent.setLocation(event.getLocation());
        weekViewEvent.setColor(event.getColor());
        return weekViewEvent;
    }

    // From https://gist.github.com/alamkanak/eb67870f0b7a6001e188
    public static ArrayList<WeekViewEvent> getNewEvents(int year, int month, List<WeekViewEvent> weekViewEvents) {

        // Get the starting point and ending point of the given month. We need this to find the
        // events of the given month.
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.YEAR, year);
        startOfMonth.set(Calendar.MONTH, month - 1);
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);
        startOfMonth.set(Calendar.MILLISECOND, 0);
        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);
        endOfMonth.set(Calendar.SECOND, 59);

        // Find the events that were added by tapping on empty view and that occurs in the given
        // time frame.
        ArrayList<WeekViewEvent> events = new ArrayList<>();
        for (WeekViewEvent event : weekViewEvents) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                events.add(event);
            }
        }
        return events;
    }
}
