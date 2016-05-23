package group03.itsmap.groupshare.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import group03.itsmap.groupshare.models.ToDoItem;

public class ToDoService extends IntentService {
    private static final String ACTION_GET_TODO_ITEMS = "group03.itsmap.groupshare.services.ToDoService.action.getToDoItems";
    private static final String ACTION_SAVE_TODO_ITEMS = "group03.itsmap.groupshare.services.ToDoService.action.saveToDoItems";
    public static final String EXTRA_TODO_ITEMS = "group03.itsmap.groupshare.services.ToDoService.extra.ToDoItems";
    public static final String EXTRA_GROUP_ID = "group03.itsmap.groupshare.services.ToDoService.extra.GroupId";
    public static final String EXTRA_USER_ID = "group03.itsmap.groupshare.services.ToDoService.extra.userId";
    public static final String GET_TODO_ITEMS_BROADCAST_INTENT = "group03.itsmap.groupshare.services.ToDoService.intent.getToDoItems";
    private static final String TODO_ITEMS_SHARED_PREFERENCES = "group03.itsmap.groupshare.services.ToDoService.toDoItem.sharedPref";

    public ToDoService() {
        super("ToDoService");
    }

    public static void startActionGetToDoItems(Context context, Long groupId, String userId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_GET_TODO_ITEMS);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        context.startService(intent);
    }


    public static void startActionSaveToDoItems(Context context, ArrayList<ToDoItem> toDoItems, Long groupId, String userId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_SAVE_TODO_ITEMS);
        intent.putExtra(EXTRA_TODO_ITEMS, toDoItems);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_TODO_ITEMS.equals(action)) {
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                handleActionGetToDoItems(groupId, userId);
            } else if (ACTION_SAVE_TODO_ITEMS.equals(action)) {
                final ArrayList<ToDoItem> toDoItems = intent.getParcelableArrayListExtra(EXTRA_TODO_ITEMS);
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                handleActionSaveToDoItems(toDoItems, groupId, userId);
            }
        }
    }

    private void handleActionGetToDoItems(Long groupId, String userId) {
        ArrayList<ToDoItem> toDoItems;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(TODO_ITEMS_SHARED_PREFERENCES + groupId + userId, null);
        Gson gson = new Gson();
        toDoItems = gson.fromJson(jsonString, new TypeToken<ArrayList<ToDoItem>>() {
        }.getType());
        Intent toDoItemsBroadcast =
                new Intent(GET_TODO_ITEMS_BROADCAST_INTENT + groupId + userId)
                        .putExtra(EXTRA_TODO_ITEMS, toDoItems);
        LocalBroadcastManager.getInstance(this).sendBroadcast(toDoItemsBroadcast);
    }

    private void handleActionSaveToDoItems(ArrayList<ToDoItem> toDoItems, Long groupId, String userId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String toDoItemsJson = gson.toJson(toDoItems);
        editor.putString(TODO_ITEMS_SHARED_PREFERENCES + groupId + userId, toDoItemsJson);
        editor.apply();
    }
}
