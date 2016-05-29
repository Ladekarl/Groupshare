package dk.iha.itsmap.f16.grp03.groupshare.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import dk.iha.itsmap.f16.grp03.groupshare.models.Group;

public class GroupService extends IntentService {
    private static final String ACTION_GET_GROUPS = "GroupService.action.getGroups";
    private static final String ACTION_GET_SINGLE_GROUP = "GroupService.action.getGroup";
    private static final String ACTION_SAVE_GROUPS = "GroupService.action.saveGroups";
    private static final String ACTION_SAVE_SINGLE_GROUP = "GroupService.action.saveGroup";
    private static final String ACTION_DELETE_SINGLE_GROUP = "GroupService.action.deleteGroup";
    public static final String EXTRA_GROUPS = "GroupService.extra.Groups";
    public static final String EXTRA_SINGLE_GROUP = "GroupService.extra.Group";
    public static final String EXTRA_USER_ID = "GroupService.extra.UserId";
    public static final String EXTRA_GROUP_ID = "GroupService.extra.GroupId";
    public static final String GET_GROUPS_BROADCAST_INTENT = "GroupService.intent.getGroups";
    public static final String GET_SINGLE_GROUP_BROADCAST_INTENT = "GroupService.intent.getGroup";
    private static final String GROUPS_SHARED_PREFERENCES = "GroupService.groups.sharedPref";

    public GroupService() {
        super("GroupService");
    }

    public static void startActionGetGroups(Context context, String userId) {
        Intent intent = new Intent(context, GroupService.class);
        intent.setAction(ACTION_GET_GROUPS);
        intent.putExtra(EXTRA_USER_ID, userId);
        context.startService(intent);
    }

    public static void startActionGetSingleGroup(Context context, Long groupId, String userId) {
        Intent intent = new Intent(context, GroupService.class);
        intent.setAction(ACTION_GET_SINGLE_GROUP);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        context.startService(intent);
    }


    public static void startActionSaveGroups(Context context, ArrayList<Group> groups, String userId) {
        Intent intent = new Intent(context, GroupService.class);
        intent.setAction(ACTION_SAVE_GROUPS);
        intent.putExtra(EXTRA_GROUPS, groups);
        intent.putExtra(EXTRA_USER_ID, userId);
        context.startService(intent);
    }

    public static void startActionSaveSingleGroup(Context context, Group group, Long groupId, String userId) {
        Intent intent = new Intent(context, GroupService.class);
        intent.setAction(ACTION_SAVE_SINGLE_GROUP);
        intent.putExtra(EXTRA_SINGLE_GROUP, group);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        context.startService(intent);
    }

    public static void startActionDeleteSingleGroup(Context context, Long groupId, String userId) {
        Intent intent = new Intent(context, GroupService.class);
        intent.setAction(ACTION_DELETE_SINGLE_GROUP);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
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
                handleActionSaveGroups(groups, userId);
            } else if (ACTION_GET_SINGLE_GROUP.equals(action)) {
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                handleActionGetSingleGroup(groupId, userId);
            } else if (ACTION_SAVE_SINGLE_GROUP.equals(action)) {
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final Group group = intent.getParcelableExtra(EXTRA_SINGLE_GROUP);
                handleActionSaveSingleGroup(group, groupId, userId);
            } else if (ACTION_DELETE_SINGLE_GROUP.equals(action)) {
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                handleActionDeleteSingleGroup(groupId, userId);
            }
        }
    }

    private void handleActionGetSingleGroup(Long groupId, String userId) {
        ArrayList<Group> groups;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(GROUPS_SHARED_PREFERENCES + userId, null);
        Gson gson = new Gson();
        groups = gson.fromJson(jsonString, new TypeToken<ArrayList<Group>>() {
        }.getType());
        Group theGroup = new Group();
        if (groups != null) {
            for (Group group : groups) {
                if (group.getId() == groupId) {
                    theGroup = group;
                }
            }
        }
        Intent groupBroadcast =
                new Intent(GET_SINGLE_GROUP_BROADCAST_INTENT + groupId + userId)
                        .putExtra(EXTRA_SINGLE_GROUP, theGroup);
        LocalBroadcastManager.getInstance(this).sendBroadcast(groupBroadcast);
    }


    private void handleActionSaveSingleGroup(Group group, Long groupId, String userId) {
        ArrayList<Group> groups;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(GROUPS_SHARED_PREFERENCES + userId, null);
        Gson gson = new Gson();
        groups = gson.fromJson(jsonString, new TypeToken<ArrayList<Group>>() {
        }.getType());
        if (groups != null) {
            Group groupToRemove = null;
            for (Group retrievedGroup : groups) {
                if (retrievedGroup.getId() == groupId) {
                    groupToRemove = retrievedGroup;
                }
            }
            if (groupToRemove != null) {
                groups.remove(groupToRemove);
            }
            groups.add(group);
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        String groupsJson = gson.toJson(groups);
        editor.putString(GROUPS_SHARED_PREFERENCES + userId, groupsJson);
        editor.apply();
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

    private void handleActionSaveGroups(ArrayList<Group> groups, String userId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String groupsJson = gson.toJson(groups);
        editor.putString(GROUPS_SHARED_PREFERENCES + userId, groupsJson);
        editor.apply();
    }

    private void handleActionDeleteSingleGroup(Long groupId, String userId) {
        ArrayList<Group> groups;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(GROUPS_SHARED_PREFERENCES + userId, null);
        Gson gson = new Gson();
        groups = gson.fromJson(jsonString, new TypeToken<ArrayList<Group>>() {
        }.getType());
        if (groups != null) {
            Group groupToRemove = null;
            for (Group retrievedGroup : groups) {
                if (retrievedGroup.getId() == groupId) {
                    groupToRemove = retrievedGroup;
                }
            }
            if (groupToRemove != null) {
                groups.remove(groupToRemove);
            }
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        String groupsJson = gson.toJson(groups);
        editor.putString(GROUPS_SHARED_PREFERENCES + userId, groupsJson);
        editor.apply();
    }
}
