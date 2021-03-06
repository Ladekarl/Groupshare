package dk.iha.itsmap.f16.grp03.groupshare.activities;

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
import android.widget.TextView;

import dk.iha.itsmap.f16.grp03.groupshare.R;
import dk.iha.itsmap.f16.grp03.groupshare.adapters.ToDoListActivityAdapter;
import dk.iha.itsmap.f16.grp03.groupshare.models.Group;
import dk.iha.itsmap.f16.grp03.groupshare.models.ToDoItem;
import dk.iha.itsmap.f16.grp03.groupshare.models.ToDoList;
import dk.iha.itsmap.f16.grp03.groupshare.services.ToDoService;
import dk.iha.itsmap.f16.grp03.groupshare.utils.FacebookUtil;

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
    public static final String GROUP_INTENT_KEY = "ToDoActivity.GroupIntentKey";
    public static final String TODOLIST_ID_INTENT_KEY = "ToDoActivity.ToDoListIntentKey";
    private ToDoListReceiver toDoItemReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        userId = FacebookUtil.getFacebookUserId(getApplicationContext());
        toDoList = new ToDoList();
        group = getIntent().getParcelableExtra(GROUP_INTENT_KEY);
        toDoListId = getIntent().getLongExtra(TODOLIST_ID_INTENT_KEY, 0);
        toDoListActivityAdapter = new ToDoListActivityAdapter(this, R.layout.todo_list_item);
        toDoListView = (ListView) findViewById(R.id.act_todo_list);
        if (toDoListView != null) {
            toDoListView.setItemsCanFocus(true);
            toDoListView.setAdapter(toDoListActivityAdapter);
            RelativeLayout footerLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.todo_list_footer, null);
            toDoListView.addFooterView(footerLayout);
        }

        TextView toolbarTitle = (TextView) findViewById(R.id.todo_toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText(group.getName());
        }

        Toolbar todoToolbar = (Toolbar) findViewById(R.id.todo_toolbar);
        setSupportActionBar(todoToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
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
                    ToDoService.GET_TODO_LIST_BROADCAST_INTENT + group.getId() + toDoListId + userId);

            toDoItemReceiver = new ToDoListReceiver();
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
        getToDoListFromService();
    }

    @Override
    public void onPause() {
        toDoList.setTitle(todoTitleText.getText().toString());
        saveToDoList();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(toDoItemReceiver);
        super.onDestroy();
    }

    private void saveToDoList() {
        ToDoService.startActionSaveToDoList(this, toDoList, group.getId(), toDoListId, userId);
    }

    private void getToDoListFromService() {
        ToDoService.startActionGetToDoList(this, group.getId(), toDoListId, userId);
    }

    private class ToDoListReceiver extends BroadcastReceiver {
        private ToDoListReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ToDoList list = intent.getParcelableExtra(ToDoService.EXTRA_TODO_LIST);
            if (list == null) return;
            toDoList = list;
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
