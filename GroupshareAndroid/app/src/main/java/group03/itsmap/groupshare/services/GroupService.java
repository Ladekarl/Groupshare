package group03.itsmap.groupshare.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.models.ToDoItem;

public class GroupService extends IntentService {
    private static final String ACTION_GET_GROUPS = "group03.itsmap.groupshare.services.GroupService.action.getGroups";
    private static final String ACTION_SAVE_GROUPS = "group03.itsmap.groupshare.services.GroupService.action.saveGroups";
    public static final String EXTRA_GROUPS = "group03.itsmap.groupshare.services.GroupService.extra.Groups";
    public static final String EXTRA_USER_ID = "group03.itsmap.groupshare.services.GroupService.extra.UserId";
    public static final String GET_GROUPS_BROADCAST_INTENT = "group03.itsmap.groupshare.services.GroupService.intent.getGroups";
    private static final String GROUPS_SHARED_PREFERENCES = "group03.itsmap.groupshare.services.GroupService.groups.sharedPref";

    public GroupService() {
        super("GroupService");
    }

    public static void startActionGetGroups(Context context, String userId) {
        Intent intent = new Intent(context, GroupService.class);
        intent.setAction(ACTION_GET_GROUPS);
        intent.putExtra(EXTRA_USER_ID, userId);
        context.startService(intent);
    }


    public static void startActionSaveGroups(Context context, ArrayList<Group> groups, String userId) {
        Intent intent = new Intent(context, GroupService.class);
        intent.setAction(ACTION_SAVE_GROUPS);
        intent.putExtra(EXTRA_GROUPS, groups);
        intent.putExtra(EXTRA_USER_ID, userId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_GROUPS.equals(action)) {
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                handleActionGetGroups(userId);
            } else if (ACTION_SAVE_GROUPS.equals(action)) {
                final ArrayList<Group> groups = intent.getParcelableArrayListExtra(EXTRA_GROUPS);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                handleActionSaveToDoItems(groups, userId);
            }
        }
    }

    private void handleActionGetGroups(String userId) {
        ArrayList<Group> groups;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(GROUPS_SHARED_PREFERENCES + userId, null);
        Gson gson = new Gson();
        groups = gson.fromJson(jsonString, new TypeToken<ArrayList<Group>>() {
        }.getType());
        Intent groupsBroadcast =
                new Intent(GET_GROUPS_BROADCAST_INTENT + userId)
                        .putExtra(EXTRA_GROUPS, groups);
        LocalBroadcastManager.getInstance(this).sendBroadcast(groupsBroadcast);
    }

    private void handleActionSaveToDoItems(ArrayList<Group> groups, String userId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String groupsJson = gson.toJson(groups);
        editor.putString(GROUPS_SHARED_PREFERENCES + userId, groupsJson);
        editor.apply();
    }
}
