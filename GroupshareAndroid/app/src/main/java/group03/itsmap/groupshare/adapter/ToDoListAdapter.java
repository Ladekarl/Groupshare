package group03.itsmap.groupshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import group03.itsmap.groupshare.R;
import group03.itsmap.groupshare.model.ToDoItem;

public class ToDoListAdapter extends ArrayAdapter<ToDoItem> {

    public ToDoListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ToDoListAdapter(Context context, int resource, List<ToDoItem> items) {
        super(context, resource, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.todo_list_item, null);
        }

        final ToDoItem item = getItem(position);

        if (item != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), item.getDescription() + " Clicked", Toast.LENGTH_LONG).show();
                }
            });

            TextView descriptionTextView = (TextView) v.findViewById(R.id.todo_item_description);
            if (descriptionTextView != null) {
                descriptionTextView.setText(item.getDescription());
            }
        }

        return v;
    }
}