package dk.iha.itsmap.f16.grp03.groupshare.fragments;

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
import android.widget.TextView;

import dk.iha.itsmap.f16.grp03.groupshare.R;
import dk.iha.itsmap.f16.grp03.groupshare.models.ToDoList;
import dk.iha.itsmap.f16.grp03.groupshare.activities.GroupActivity;
import dk.iha.itsmap.f16.grp03.groupshare.activities.ToDoActivity;
import dk.iha.itsmap.f16.grp03.groupshare.adapters.ToDoListFragmentAdapter;
import dk.iha.itsmap.f16.grp03.groupshare.models.Group;
import dk.iha.itsmap.f16.grp03.groupshare.services.ToDoService;
import dk.iha.itsmap.f16.grp03.groupshare.utils.FacebookUtil;

public class ToDoFragment extends Fragment {

    private ToDoList toDoList;
    private ToDoListFragmentAdapter toDoListFragmentAdapter;
    private Group group;
    private String userId;
    private Long toDoListId;
    private TextView toDoListTitleText;
    private ToDoListReceiver toDoItemReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = FacebookUtil.getFacebookUserId(getActivity().getApplicationContext());
        toDoList = new ToDoList();
        toDoListFragmentAdapter = new ToDoListFragmentAdapter(getActivity(), R.layout.todo_list_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        ListView toDoListView = (ListView) view.findViewById(R.id.fragment_todo_list);
        toDoListView.setAdapter(toDoListFragmentAdapter);

        ImageButton toDoFragmentButton = (ImageButton) view.findViewById(R.id.todo_fragment_button);
        toDoFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startToDoActivityIntent = new Intent(getActivity(), ToDoActivity.class);
                startToDoActivityIntent.putExtra(ToDoActivity.GROUP_INTENT_KEY, group);
                startToDoActivityIntent.putExtra(ToDoActivity.TODOLIST_ID_INTENT_KEY, toDoListId);
                startActivity(startToDoActivityIntent);
            }
        });

        toDoListTitleText = (TextView) view.findViewById(R.id.todo_list_text);
        toDoListTitleText.setText(toDoList.getTitle());

        if (getArguments() != null) {
            Group groupVar = getArguments().getParcelable(GroupActivity.GROUP_KEY);
            toDoListId = getArguments().getLong(GroupActivity.TODOLIST_ID_KEY);
            if (groupVar != null) {
                group = groupVar;
                IntentFilter toDoItemIntentFilter = new IntentFilter(
                        ToDoService.GET_TODO_LIST_BROADCAST_INTENT + group.getId() + toDoListId + userId);

                toDoItemReceiver = new ToDoListReceiver();
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
        getToDoListFromService();
    }

    @Override
    public void onPause() {
        saveToDoList();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(toDoItemReceiver);
        super.onDestroy();
    }

    private void saveToDoList() {
        ToDoService.startActionSaveToDoList(getActivity(), toDoList, group.getId(), toDoListId, userId);
    }


    private void getToDoListFromService() {
        ToDoService.startActionGetToDoList(getActivity(), group.getId(), toDoListId, userId);
    }

    private class ToDoListReceiver extends BroadcastReceiver {
        private ToDoListReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ToDoList list = intent.getParcelableExtra(ToDoService.EXTRA_TODO_LIST);
            if (list == null) return;
            toDoList = list;
            toDoListTitleText.setText(toDoList.getTitle());
            if (toDoListFragmentAdapter == null) return;
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        toDoListFragmentAdapter.clear();
        toDoListFragmentAdapter.addAll(toDoList.getItems());
        toDoListFragmentAdapter.notifyDataSetChanged();
    }
}
