package group03.itsmap.groupshare.services;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import group03.itsmap.groupshare.models.CalendarEvent;
import group03.itsmap.groupshare.models.GroupShareCalendar;

public class CalendarService extends IntentService {
    private static final String ACTION_GET_CALENDAR = "group03.itsmap.groupshare.services.CalendarService.action.getCalendar";
    private static final String ACTION_SAVE_CALENDAR = "group03.itsmap.groupshare.services.CalendarService.action.saveCalendar";
    private static final String ACTION_DELETE_CALENDAR = "group03.itsmap.groupshare.services.CalendarService.action.deleteCalendar";
    private static final String ACTION_DELETE_CALENDAR_EVENT = "group03.itsmap.groupshare.services.CalendarService.action.deleteCalendarEvent";
    public static final String EXTRA_CALENDAR = "group03.itsmap.groupshare.services.CalendarService.extra.GroupShareCalendar";
    public static final String EXTRA_GROUP_ID = "group03.itsmap.groupshare.services.CalendarService.extra.GroupId";
    public static final String EXTRA_USER_ID = "group03.itsmap.groupshare.services.CalendarService.extra.userId";
    public static final String EXTRA_CALENDAR_ID = "group03.itsmap.groupshare.services.CalendarService.extra.calendarId";
    public static final String GET_CALENDAR_BROADCAST_INTENT = "group03.itsmap.groupshare.services.CalendarService.intent.getCalendar";
    public static final String DELETE_CALENDAR_EVENT_BROADCAST_INTENT = "group03.itsmap.groupshare.services.CalendarService.intent.deleteCalendarEvent";
    private static final String CALENDAR_SHARED_PREFERENCES = "group03.itsmap.groupshare.services.CalendarService.sharedPref";
    private static final String EXTRA_CALENDAR_EVENT_ID = "group03.itsmap.groupshare.services.CalendarService.extra.calendarEventId";

    public CalendarService() {
        super ("CalendarService");
    }

    public static void startActionGetCalendar(Context context, Long groupId, Long calendarId, String userId) {
        Intent intent = new Intent(context, CalendarService.class);
        intent.setAction(ACTION_GET_CALENDAR);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_CALENDAR_ID, calendarId);
        context.startService(intent);
    }


    public static void startActionSaveCalendar(Context context, GroupShareCalendar groupShareCalendar, Long groupId, Long calendarId, String userId) {
        Intent intent = new Intent(context, CalendarService.class);
        intent.setAction(ACTION_SAVE_CALENDAR);
        intent.putExtra(EXTRA_CALENDAR, groupShareCalendar);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_CALENDAR_ID, calendarId);
        context.startService(intent);
    }

    public static void startActionDeleteCalendar(Context context, Long groupId, Long calendarId, String userId) {
        Intent intent = new Intent(context, CalendarService.class);
        intent.setAction(ACTION_DELETE_CALENDAR);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_CALENDAR_ID, calendarId);
        context.startService(intent);
    }

    public static void startActionDeleteCalendarEvent(Context context, Long groupId, Long calendarId, String userId, Long eventId) {
        Intent intent = new Intent(context, CalendarService.class);
        intent.setAction(ACTION_DELETE_CALENDAR_EVENT);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_CALENDAR_ID, calendarId);
        intent.putExtra(EXTRA_CALENDAR_EVENT_ID, eventId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_CALENDAR.equals(action)) {
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long calendarId = intent.getLongExtra(EXTRA_CALENDAR_ID, 0);
                handleActionGetCalendar(groupId, calendarId, userId);
            } else if (ACTION_SAVE_CALENDAR.equals(action)) {
                final GroupShareCalendar groupShareCalendar = intent.getParcelableExtra(EXTRA_CALENDAR);
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long calendarId = intent.getLongExtra(EXTRA_CALENDAR_ID, 0);
                handleActionSaveCalendar(groupShareCalendar, groupId, calendarId, userId);
            } else if (ACTION_DELETE_CALENDAR.equals(action)) {
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long calendarId = intent.getLongExtra(EXTRA_CALENDAR_ID, 0);
                handleActionDeleteCalendars(groupId, calendarId, userId);
            } else if (ACTION_DELETE_CALENDAR_EVENT.equals(action)) {
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long calendarId = intent.getLongExtra(EXTRA_CALENDAR_ID, 0);
                final Long calendarEventId = intent.getLongExtra(EXTRA_CALENDAR_EVENT_ID, 0);
                handleActionDeleteCalendarEvent(groupId, calendarId, userId, calendarEventId);
            }
        }
    }

    private void handleActionDeleteCalendarEvent(Long groupId, Long calendarId, String userId, Long calendarEventId) {
        GroupShareCalendar groupShareCalendar;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(CALENDAR_SHARED_PREFERENCES + groupId + calendarId + userId, null);
        Gson gson = new Gson();
        groupShareCalendar = gson.fromJson(jsonString, new TypeToken<GroupShareCalendar>() {
        }.getType());
        CalendarEvent eventToRemove = null;
        if(groupShareCalendar != null) {
            for (CalendarEvent event : groupShareCalendar.getCalendarEvents()) {
                if (event.getId().equals(calendarEventId)) {
                    eventToRemove = event;
                }
            }
            if (eventToRemove != null) {
                groupShareCalendar.getCalendarEvents().remove(eventToRemove);
            }
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        String groupsJson = gson.toJson(groupShareCalendar);
        editor.putString(CALENDAR_SHARED_PREFERENCES + userId, groupsJson);
        Intent calendarBroadcaster =
                new Intent(DELETE_CALENDAR_EVENT_BROADCAST_INTENT)
                        .putExtra(EXTRA_CALENDAR, groupShareCalendar);
        LocalBroadcastManager.getInstance(this).sendBroadcast(calendarBroadcaster);
        editor.apply();
    }

    private void handleActionDeleteCalendars(Long groupId, Long calendarId, String userId) {
        GroupShareCalendar groupShareCalendar;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(CALENDAR_SHARED_PREFERENCES + groupId + calendarId + userId, null);
        Gson gson = new Gson();
        groupShareCalendar = gson.fromJson(jsonString, new TypeToken<GroupShareCalendar>() {
        }.getType());
        if (groupShareCalendar != null) {
            sharedPref.edit().remove(CALENDAR_SHARED_PREFERENCES + groupId + calendarId + userId).apply();
        }
    }

    private void handleActionGetCalendar(Long groupId, Long calendarId, String userId) {
        GroupShareCalendar groupShareCalendar;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(CALENDAR_SHARED_PREFERENCES + groupId + calendarId + userId, null);
        Gson gson = new Gson();
        groupShareCalendar = gson.fromJson(jsonString, new TypeToken<GroupShareCalendar>() {
        }.getType());
        Intent calendarBroadcaster =
                new Intent(GET_CALENDAR_BROADCAST_INTENT + groupId + calendarId + userId)
                        .putExtra(EXTRA_CALENDAR, groupShareCalendar);
        LocalBroadcastManager.getInstance(this).sendBroadcast(calendarBroadcaster);
    }

    private void handleActionSaveCalendar(GroupShareCalendar groupShareCalendar, Long groupId, Long calendarId, String userId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String calendarJson = gson.toJson(groupShareCalendar);
        editor.putString(CALENDAR_SHARED_PREFERENCES + groupId + calendarId + userId, calendarJson);
        editor.apply();
    }
}
