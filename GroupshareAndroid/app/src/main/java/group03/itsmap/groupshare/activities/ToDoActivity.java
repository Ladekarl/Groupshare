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

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.adapters.ToDoListActivityAdapter;
import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.models.ToDoItem;
import group03.itsmap.groupshare.models.ToDoList;
import group03.itsmap.groupshare.services.ToDoService;
import group03.itsmap.groupshare.utils.FacebookUtil;

public class ToDoActivity extends AppCompatActivity {

    private ToDoList toDoList;
    private ToDoListActivityAdapter toDoListActivityAdapter;
    private ListView toDoListView;
    private EditText addToDoItemDescription;
    private ImageButton addToDoItemButton;
    private String userId;
    private Group group;
    private long toDoListId;
    private EditText todoTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        userId = FacebookUtil.getFacebookUserId(getApplicationContext());
        toDoList = new ToDoList();
        group = getIntent().getParcelableExtra("Group");
        toDoListId = getIntent().getLongExtra("ToDoListId", 0);
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
                    toDoList.getItems().remove(item);
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
                toDoList.getItems().add(item);
                refreshAdapter();
            }
        });

        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.getId() == R.id.todo_item_delete) {
                    ToDoItem item = (ToDoItem) view.getTag();
                    toDoList.getItems().remove(item);
                    refreshAdapter();
                }
            }
        });

        if (group != null) {
            IntentFilter toDoItemIntentFilter = new IntentFilter(
                    ToDoService.GET_TODO_ITEMS_BROADCAST_INTENT + group.getId() + toDoListId + userId);

            ToDoItemReceiver toDoItemReceiver = new ToDoItemReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    toDoItemReceiver,
                    toDoItemIntentFilter);
        }

        todoTitleText = (EditText) findViewById(R.id.todo_act_text);
        todoTitleText.setText(toDoList.getTitle());
    }

    @Override
    public void onStart() {
        super.onStart();
        getToDoItemsFromService();
    }

    @Override
    public void onPause() {
        toDoList.setTitle(todoTitleText.getText().toString());
        saveToDoItems();
        super.onPause();
    }

    private void saveToDoItems() {
        ToDoService.startActionSaveToDoItems(this, toDoList, group.getId(), toDoListId, userId);
    }

    private void getToDoItemsFromService() {
        ToDoService.startActionGetToDoItems(this, group.getId(), toDoListId, userId);
    }

    private class ToDoItemReceiver extends BroadcastReceiver {
        private ToDoItemReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ToDoList items = intent.getParcelableExtra(ToDoService.EXTRA_TODO_ITEMS);
            if (items == null) return;
            toDoList = items;
            todoTitleText.setText(toDoList.getTitle());
            if (toDoListActivityAdapter == null) return;
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        toDoListActivityAdapter.clear();
        toDoListActivityAdapter.addAll(toDoList.getItems());
        toDoListActivityAdapter.notifyDataSetChanged();
    }


}
