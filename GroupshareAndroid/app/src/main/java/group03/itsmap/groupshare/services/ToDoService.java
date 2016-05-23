package group03.itsmap.groupshare.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import group03.itsmap.groupshare.models.ToDoList;

public class ToDoService extends IntentService {
    private static final String ACTION_GET_TODO_ITEMS = "group03.itsmap.groupshare.services.ToDoService.action.getToDoItems";
    private static final String ACTION_SAVE_TODO_ITEMS = "group03.itsmap.groupshare.services.ToDoService.action.saveToDoItems";
    public static final String EXTRA_TODO_ITEMS = "group03.itsmap.groupshare.services.ToDoService.extra.ToDoItems";
    public static final String EXTRA_GROUP_ID = "group03.itsmap.groupshare.services.ToDoService.extra.GroupId";
    public static final String EXTRA_USER_ID = "group03.itsmap.groupshare.services.ToDoService.extra.userId";
    public static final String EXTRA_TODO_LIST_ID = "group03.itsmap.groupshare.services.ToDoService.extra.toDoListId";
    public static final String GET_TODO_ITEMS_BROADCAST_INTENT = "group03.itsmap.groupshare.services.ToDoService.intent.getToDoItems";
    private static final String TODO_ITEMS_SHARED_PREFERENCES = "group03.itsmap.groupshare.services.ToDoService.toDoItem.sharedPref";

    public ToDoService() {
        super("ToDoService");
    }

    public static void startActionGetToDoItems(Context context, Long groupId, Long toDoListId, String userId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_GET_TODO_ITEMS);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_TODO_LIST_ID, toDoListId);
        context.startService(intent);
    }


    public static void startActionSaveToDoItems(Context context, ToDoList toDoList, Long groupId, Long toDoListId, String userId) {
        Intent intent = new Intent(context, ToDoService.class);
        intent.setAction(ACTION_SAVE_TODO_ITEMS);
        intent.putExtra(EXTRA_TODO_ITEMS, toDoList);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_TODO_LIST_ID, toDoListId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_TODO_ITEMS.equals(action)) {
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long toDoListId = intent.getLongExtra(EXTRA_TODO_LIST_ID, 0);
                handleActionGetToDoItems(groupId, toDoListId, userId);
            } else if (ACTION_SAVE_TODO_ITEMS.equals(action)) {
                final ToDoList toDoItems = intent.getParcelableExtra(EXTRA_TODO_ITEMS);
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0);
                final String userId = intent.getStringExtra(EXTRA_USER_ID);
                final Long toDoListId = intent.getLongExtra(EXTRA_TODO_LIST_ID, 0);
                handleActionSaveToDoItems(toDoItems, groupId, toDoListId, userId);
            }
        }
    }

    private void handleActionGetToDoItems(Long groupId, Long toDoListId, String userId) {
        ToDoList toDoItems;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String jsonString = sharedPref.getString(TODO_ITEMS_SHARED_PREFERENCES + groupId + toDoListId + userId, null);
        Gson gson = new Gson();
        toDoItems = gson.fromJson(jsonString, new TypeToken<ToDoList>() {
        }.getType());
        Intent toDoItemsBroadcast =
                new Intent(GET_TODO_ITEMS_BROADCAST_INTENT + groupId + toDoListId + userId)
                        .putExtra(EXTRA_TODO_ITEMS, toDoItems);
        LocalBroadcastManager.getInstance(this).sendBroadcast(toDoItemsBroadcast);
    }

    private void handleActionSaveToDoItems(ToDoList toDoList, Long groupId, Long toDoListId, String userId) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String toDoItemsJson = gson.toJson(toDoList);
        editor.putString(TODO_ITEMS_SHARED_PREFERENCES + groupId + toDoListId + userId, toDoItemsJson);
        editor.apply();
    }
}
