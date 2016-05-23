package group03.itsmap.groupshare.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.activities.GroupActivity;
import group03.itsmap.groupshare.activities.ToDoActivity;
import group03.itsmap.groupshare.adapters.ToDoListFragmentAdapter;
import group03.itsmap.groupshare.models.Group;
import group03.itsmap.groupshare.models.ToDoItem;
import group03.itsmap.groupshare.services.ToDoService;
import group03.itsmap.groupshare.utils.FacebookUtil;

public class ToDoFragment extends Fragment {

    private ArrayList<ToDoItem> toDoItemList;
    private ToDoListFragmentAdapter toDoListFragmentAdapter;
    private ListView toDoListView;
    private View view;
    private ImageButton toDoFragmentButton;
    private Group group;
    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = FacebookUtil.getFacebookUserId(getActivity().getApplicationContext());
        toDoItemList = new ArrayList<>();
        toDoListFragmentAdapter = new ToDoListFragmentAdapter(getActivity(), R.layout.todo_list_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_todo, container, false);
        toDoListView = (ListView) view.findViewById(R.id.fragment_todo_list);
        toDoListView.setAdapter(toDoListFragmentAdapter);

        toDoFragmentButton = (ImageButton) view.findViewById(R.id.todo_fragment_button);
        toDoFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startToDoActivityIntent = new Intent(getActivity(), ToDoActivity.class);
                startToDoActivityIntent.putExtra("Group", group);
                startActivity(startToDoActivityIntent);
            }
        });

        if (getArguments() != null) {
            Group groupVar = getArguments().getParcelable(GroupActivity.GROUP_KEY);
            if (groupVar != null) {
                group = groupVar;
                IntentFilter toDoItemIntentFilter = new IntentFilter(
                        ToDoService.GET_TODO_ITEMS_BROADCAST_INTENT + group.getId() + userId);

                ToDoItemReceiver toDoItemReceiver = new ToDoItemReceiver();
                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                        toDoItemReceiver,
                        toDoItemIntentFilter);
            }
        }
        return view;
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
        ToDoService.startActionSaveToDoItems(getActivity(), toDoItemList, group.getId(), userId);
    }


    private void getToDoItemsFromService() {
        ToDoService.startActionGetToDoItems(getActivity(), group.getId(), userId);
    }

    private class ToDoItemReceiver extends BroadcastReceiver {
        private ToDoItemReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ArrayList<ToDoItem> items = intent.getParcelableArrayListExtra(ToDoService.EXTRA_TODO_ITEMS);
            if (items == null) return;
            if (toDoItemList.containsAll(items)) return;
            toDoItemList = items;
            if (toDoListFragmentAdapter == null) return;
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        toDoListFragmentAdapter.clear();
        toDoListFragmentAdapter.addAll(toDoItemList);
        toDoListFragmentAdapter.notifyDataSetChanged();
    }
}
