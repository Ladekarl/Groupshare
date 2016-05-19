package group03.itsmap.groupshare;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import group03.itsmap.groupshare.adapter.ToDoListActivityAdapter;
import group03.itsmap.groupshare.model.ToDoItem;

public class ToDoActivity extends AppCompatActivity {

    private ArrayList<ToDoItem> toDoList;
    private ToDoListActivityAdapter toDoListActivityAdapter;
    private ListView toDoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        toDoList = getIntent().getParcelableArrayListExtra("ToDoList");
        toDoListActivityAdapter = new ToDoListActivityAdapter(this, R.layout.todo_list_item, toDoList);
        toDoListView = (ListView) findViewById(R.id.act_todo_list);
        if (toDoListView != null) {
            toDoListView.setItemsCanFocus(true);
            toDoListView.setAdapter(toDoListActivityAdapter);
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
                    Intent resultIntent = new Intent();
                    resultIntent.putParcelableArrayListExtra("ToDoList", toDoList);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            });
        }
    }
}
