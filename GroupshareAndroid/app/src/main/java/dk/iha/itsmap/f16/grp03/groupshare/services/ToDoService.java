package dk.iha.itsmap.f16.grp03.groupshare.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dk.iha.itsmap.f16.grp03.groupshare.models.ToDoList;

public class ToDoService extends IntentService {
    private static final String ACTION_GET_TODO_LIST = "ToDoService.action.getToDoList";
    private static final String ACTION_SAVE_TODO_LIST = "ToDoService.action.saveToDoList";
    private static final String ACTION_DELETE_TODO_LIST = "ToDoService.action.deleteToDoList";
    public static final String EXTRA_TODO_LIST = "ToDoService.extra.ToDoList";
    public static final String EXTRA_GROUP_ID = "ToDoService.extra.GroupId";
    public static final String EXTRA_USER_ID = "ToDoService.extra.userId";
    public static final String EXTRA_TODO_LIST_ID = "ToDoService.extra.toDoListId";
    public static final String GET_TODO_LIST_BROADCAST_INTENT = "ToDoService.intent.getToDoList";
    private static final String TODO_LIST_SHARED_PREFERENCES = "ToDoService.toDoItem.sharedPref";

    public ToDoService() {
        super("ToDoService");
    }

    public static void startActionGetToDoList(Context context, Long groupId, Long toDoListId, String userId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_GET_TODO_LIST);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_TODO_LIST_ID, toDoListId);
        context.startService(intent);
    }


    public static void startActionSaveToDoList(Context context, ToDoList toDoList, Long groupId, Long toDoListId, String userId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_SAVE_TODO_LIST);
        intent.putExtra(EXTRA_TODO_LIST, toDoList);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_TODO_LIST_ID, toDoListId);
        context.startService(intent);
    }

    public static void startActionDeleteToDoList(Context context, Long groupId, Long toDoListId, String userId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_DELETE_TODO_LIST);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_TODO_LIST_ID, toDoListId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_TODO_LIST.equals(action)) {
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long toDoListId = intent.getLongExtra(EXTRA_TODO_LIST_ID, 0);
                handleActionGetToDoList(groupId, toDoListId, userId);
            } else if (ACTION_SAVE_TODO_LIST.equals(action)) {
                final ToDoList toDoList = intent.getParcelableExtra(EXTRA_TODO_LIST);
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long toDoListId = intent.getLongExtra(EXTRA_TODO_LIST_ID, 0);
                handleActionSaveToDoList(toDoList, groupId, toDoListId, userId);
            } else if (ACTION_DELETE_TODO_LIST.equals(action)) {
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long toDoListId = intent.getLongExtra(EXTRA_TODO_LIST_ID, 0);
                handleActionDeleteToDoLists(groupId, toDoListId, userId);
            }
        }
    }

    private void handleActionDeleteToDoLists(Long groupId, Long toDoListId, String userId) {
        ToDoList toDoList;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(TODO_LIST_SHARED_PREFERENCES + groupId + toDoListId + userId, null);
        Gson gson = new Gson();
        toDoList = gson.fromJson(jsonString, new TypeToken<ToDoList>() {
        }.getType());
        if (toDoList != null) {
            sharedPref.edit().remove(TODO_LIST_SHARED_PREFERENCES + groupId + toDoListId + userId).apply();
        }
    }

    private void handleActionGetToDoList(Long groupId, Long toDoListId, String userId) {
        ToDoList toDoList;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(TODO_LIST_SHARED_PREFERENCES + groupId + toDoListId + userId, null);
        Gson gson = new Gson();
        toDoList = gson.fromJson(jsonString, new TypeToken<ToDoList>() {
        }.getType());
        Intent toDoListBroadcaster =
                new Intent(GET_TODO_LIST_BROADCAST_INTENT + groupId + toDoListId + userId)
                        .putExtra(EXTRA_TODO_LIST, toDoList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(toDoListBroadcaster);
    }

    private void handleActionSaveToDoList(ToDoList toDoList, Long groupId, Long toDoListId, String userId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String toDoListJson = gson.toJson(toDoList);
        editor.putString(TODO_LIST_SHARED_PREFERENCES + groupId + toDoListId + userId, toDoListJson);
        editor.apply();
    }
}
