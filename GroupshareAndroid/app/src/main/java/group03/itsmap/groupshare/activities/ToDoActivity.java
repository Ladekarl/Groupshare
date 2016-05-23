package group03.itsmap.groupshare.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.adapters.ToDoListActivityAdapter;
import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.models.ToDoItem;
import group03.itsmap.groupshare.services.ToDoService;
import group03.itsmap.groupshare.utils.FacebookUtil;

public class ToDoActivity extends AppCompatActivity {

    private ArrayList<ToDoItem> toDoList;
    private ToDoListActivityAdapter toDoListActivityAdapter;
    private ListView toDoListView;
    private EditText addToDoItemDescription;
    private ImageButton addToDoItemButton;
    private String userId;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        userId = FacebookUtil.getFacebookUserId(getApplicationContext());
        toDoList = new ArrayList<>();
        group = getIntent().getParcelableExtra("Group");
        toDoListActivityAdapter = new ToDoListActivityAdapter(this, R.layout.todo_list_item);
        toDoListView = (ListView) findViewById(R.id.act_todo_list);
        if (toDoListView != null) {
            toDoListView.setItemsCanFocus(true);
            toDoListView.setAdapter(toDoListActivityAdapter);
            RelativeLayout footerLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.todo_list_footer, null);
            toDoListView.addFooterView(footerLayout);
        }

        Toolbar todoToolbar = (Toolbar) findViewById(R.id.todo_toolbar);
        setSupportActionBar(todoToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        if (todoToolbar != null) {
            todoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        ImageButton removeButton = (ImageButton) toDoListView.findViewById(R.id.todo_item_delete);
        if (removeButton != null) {
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToDoItem item = (ToDoItem) v.getTag();
                    toDoList.remove(item);
                    refreshAdapter();
                }
            });
        }

        addToDoItemDescription = (EditText) findViewById(R.id.add_todo_item_description);
        addToDoItemButton = (ImageButton) findViewById(R.id.add_todo_item_button);

        addToDoItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                ToDoItem item = new ToDoItem(addToDoItemDescription.getText().toString(), false);
                addToDoItemDescription.setText("");
                toDoList.add(item);
                refreshAdapter();
            }
        });

        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.getId() == R.id.todo_item_delete) {
                    ToDoItem item = (ToDoItem) view.getTag();
                    toDoList.remove(item);
                    refreshAdapter();
                }
            }
        });

        if (group != null) {
            IntentFilter toDoItemIntentFilter = new IntentFilter(
                    ToDoService.GET_TODO_ITEMS_BROADCAST_INTENT + group.getId() + userId);

            ToDoItemReceiver toDoItemReceiver = new ToDoItemReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    toDoItemReceiver,
                    toDoItemIntentFilter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getToDoItemsFromService();
    }

    @Override
    public void onPause() {
        saveToDoItems();
        super.onPause();
    }

    private void saveToDoItems() {
        ToDoService.startActionSaveToDoItems(this, toDoList, group.getId(), userId);
    }

    private void getToDoItemsFromService() {
        ToDoService.startActionGetToDoItems(this, group.getId(), userId);
    }

    private class ToDoItemReceiver extends BroadcastReceiver {
        private ToDoItemReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ArrayList<ToDoItem> items = intent.getParcelableArrayListExtra(ToDoService.EXTRA_TODO_ITEMS);
            if (items == null) return;
            if (toDoList.containsAll(items)) return;
            toDoList = items;
            if (toDoListActivityAdapter == null) return;
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        toDoListActivityAdapter.clear();
        toDoListActivityAdapter.addAll(toDoList);
        toDoListActivityAdapter.notifyDataSetChanged();
    }


}
