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
    private static final String ACTION_GET_TODO_ITEMS = "group03.itsmap.groupshare.services.action.getToDoItems";
    private static final String ACTION_SAVE_TODO_ITEMS = "group03.itsmap.groupshare.services.action.saveToDoItems";
    public static final String EXTRA_TODO_ITEMS = "group03.itsmap.groupshare.services.extra.ToDoItems";
    public static final String EXTRA_GROUP_ID = "group03.itsmap.groupshare.services.extra.GroupId";
    public static final String GET_TODO_ITEMS_BROADCAST_INTENT = "group03.itsmap.groupshare.services.intent.getToDoItems";
    private static final String TODO_ITEMS_SHARED_PREFERENCES = "group03.itsmap.groupshare.services.toDoItem.sharedPref";

    public ToDoService() {
        super("ToDoService");
    }

    public static void startActionGetToDoItems(Context context, String groupId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_GET_TODO_ITEMS);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        context.startService(intent);
    }


    public static void startActionSaveToDoItems(Context context, ArrayList<ToDoItem> toDoItems, String groupId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_SAVE_TODO_ITEMS);
        intent.putExtra(EXTRA_TODO_ITEMS, toDoItems);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_TODO_ITEMS.equals(action)) {
                final String groupId = intent.getStringExtra(EXTRA_GROUP_ID);
                handleActionGetToDoItems(groupId);
            } else if (ACTION_SAVE_TODO_ITEMS.equals(action)) {
                final ArrayList<ToDoItem> toDoItems = intent.getParcelableArrayListExtra(EXTRA_TODO_ITEMS);
                final String groupId = intent.getStringExtra(EXTRA_GROUP_ID);
                handleActionSaveToDoItems(toDoItems, groupId);
            }
        }
    }

    private void handleActionGetToDoItems(String groupId) {
        ArrayList<ToDoItem> toDoItems;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(TODO_ITEMS_SHARED_PREFERENCES + groupId, null);
        Gson gson = new Gson();
        toDoItems = gson.fromJson(jsonString, new TypeToken<ArrayList<ToDoItem>>() {
        }.getType());
        Intent toDoItemsBroadcast =
                new Intent(GET_TODO_ITEMS_BROADCAST_INTENT + groupId)
                        .putExtra(EXTRA_TODO_ITEMS, toDoItems);
        LocalBroadcastManager.getInstance(this).sendBroadcast(toDoItemsBroadcast);
    }

    private void handleActionSaveToDoItems(ArrayList<ToDoItem> toDoItems, String groupId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String toDoItemsJson = gson.toJson(toDoItems);
        editor.putString(TODO_ITEMS_SHARED_PREFERENCES + groupId, toDoItemsJson);
        editor.apply();
    }
}
