package group03.itsmap.groupshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import group03.itsmap.groupshare.adapter.ToDoListAdapter;
import group03.itsmap.groupshare.model.ToDoItem;

public class ToDoFragment extends Fragment {

    private ArrayList<ToDoItem> toDoItemList;
    private ToDoListAdapter toDoListAdapter;
    private ListView toDoListView;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toDoItemList = new ArrayList<>();
        toDoItemList.add(new ToDoItem("Example 1"));
        toDoItemList.add(new ToDoItem("Example 2"));
        toDoItemList.add(new ToDoItem("Example 3"));

        toDoListAdapter = new ToDoListAdapter(getActivity(), R.layout.todo_list_item, toDoItemList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_todo, container, false);
        toDoListView = (ListView) view.findViewById(R.id.fragment_todo_list);
        toDoListView.setAdapter(toDoListAdapter);
        return view;
    }


}
