package group03.itsmap.groupshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import group03.itsmap.groupshare.adapter.ToDoListActivityAdapter;
import group03.itsmap.groupshare.adapter.ToDoListFragmentAdapter;
import group03.itsmap.groupshare.model.ToDoItem;

public class ToDoFragment extends Fragment {

    private ArrayList<ToDoItem> toDoItemList;
    private ToDoListFragmentAdapter toDoListFragmentAdapter;
    private ListView toDoListView;
    private View view;
    private ImageButton todoFragmentButton;

    private static final int TODO_ACTIVITY_KEY = 0x01;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toDoItemList = new ArrayList<>();
        toDoItemList.add(new ToDoItem("Example 1", false));
        toDoItemList.add(new ToDoItem("Example 2", false));
        toDoItemList.add(new ToDoItem("Example 3", false));

        toDoListFragmentAdapter = new ToDoListFragmentAdapter(getActivity(), R.layout.todo_list_item, toDoItemList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_todo, container, false);
        toDoListView = (ListView) view.findViewById(R.id.fragment_todo_list);
        toDoListView.setAdapter(toDoListFragmentAdapter);

        todoFragmentButton = (ImageButton) view.findViewById(R.id.todo_fragment_button);
        todoFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startToDoActivityIntent = new Intent(getActivity(), ToDoActivity.class);
                startToDoActivityIntent.putExtra("ToDoList", toDoItemList);
                startActivityForResult(startToDoActivityIntent, TODO_ACTIVITY_KEY);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TODO_ACTIVITY_KEY) {
            if (resultCode == Activity.RESULT_OK) {
                toDoListFragmentAdapter.clear();
                toDoItemList = data.getParcelableArrayListExtra("ToDoList");
                toDoListFragmentAdapter.addAll(toDoItemList);
                toDoListFragmentAdapter.notifyDataSetChanged();
            }
        }
    }
}
